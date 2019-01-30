package no.fint.provider.felles.kodeverk.service;

import java.security.acl.Owner;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.annotation.PostConstruct;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;
import no.fint.event.model.Event;
import no.fint.event.model.Status;
import no.fint.event.model.health.Health;
import no.fint.event.model.health.HealthStatus;
import no.fint.model.felles.kodeverk.KodeverkActions;
import no.fint.model.felles.kodeverk.iso.IsoActions;
import no.fint.model.relation.FintResource;
import no.fint.model.relation.Relation;
import no.fint.provider.adapter.event.EventResponseService;
import no.fint.provider.adapter.event.EventStatusService;
import no.fint.provider.felles.kodeverk.client.KlassClient;
import no.fint.provider.felles.kodeverk.client.Kodeverk;
import no.fint.provider.felles.kodeverk.model.Kode;

/**
 * The EventHandlerService receives the <code>event</code> from SSE endpoint
 * (provider) in the {@link #handleEvent(Event)} method.
 */
@Slf4j
@Service
public class EventHandlerService {

    @Autowired
    private EventResponseService eventResponseService;

    @Autowired
    private EventStatusService eventStatusService;

    @Autowired
    private KlassDataService dataService;

    @Autowired
    private Kodeverk kodeverk;

    /**
     * <p>
     * HandleEvent is responsible of handling the <code>event</code>. This is what
     * should be done:
     * </p>
     * <ol>
     * <li>Verify that the adapter can handle the <code>event</code>. This is done
     * in the {@link EventStatusService#verifyEvent(Event)} method</li>
     * <li>Call the code to handle the action</li>
     * <li>Posting back the handled <code>event</code>. This done in the
     * {@link EventResponseService#postResponse(Event)} method</li>
     * </ol>
     * <p>
     * This is where you implement your code for handling the <code>event</code>. It
     * is typically done by making a onEvent method:
     * </p>
     *
     * <pre>
     *     {@code
     *     public void onGetAllDogs(Event<FintResource> dogAllEvent) {
     *
     *         // Call a service to get all dogs from the application and add the result to the event data
     *         // dogAllEvent.addData(dogResource);
     *
     *     }
     *     }
     * </pre>
     *
     * @param event The <code>event</code> received from the provider
     */
    public void handleEvent(Event event) {
        if (event.isHealthCheck()) {
            postHealthCheckResponse(event);
        } else {
            if (eventStatusService.verifyEvent(event).getStatus() == Status.ADAPTER_ACCEPTED) {
                Event<FintResource> responseEvent = new Event<>(event);
                responseEvent.setStatus(Status.ADAPTER_RESPONSE);

                try {
                    if (KodeverkActions.getActions().contains(event.getAction())) {
                        KodeverkActions action = KodeverkActions.valueOf(event.getAction());

                        switch (action) {
                            case GET_ALL_FYLKE:
                                onGetAllFylke(responseEvent);
                                break;
                            case GET_ALL_KOMMUNE:
                                onGetAllKommune(responseEvent);
                                break;
                            default:
                                responseEvent.setStatus(Status.ADAPTER_REJECTED);
                                responseEvent.setMessage("Unsupported action " + action);
                        }
                    } else if (IsoActions.getActions().contains(event.getAction())) {
                        IsoActions action = IsoActions.valueOf(event.getAction());

                        switch (action) {
                            case GET_ALL_KJONN:
                                onGetKodeverk(kodeverk.getKjonn(), responseEvent);
                                break;
                            case GET_ALL_LANDKODE:
                                onGetKodeverk(kodeverk.getLand(), responseEvent);
                                break;
                            case GET_ALL_SPRAK:
                                onGetKodeverk(kodeverk.getSprak(), responseEvent);
                                break;
                            default:
                                responseEvent.setStatus(Status.ADAPTER_REJECTED);
                                responseEvent.setMessage("Unsupported action " + action);
                        }
                    }

                } catch (Exception e) {
                    responseEvent.setStatus(Status.ERROR);
                    responseEvent.setMessage(ExceptionUtils.getStackTrace(e));
                }
                eventResponseService.postResponse(responseEvent);
            }
        }
    }


    private void onGetKodeverk(List<Kode> data, Event<FintResource> responseEvent) {
        responseEvent.setData(data.stream().map(FintResource::with).collect(Collectors.toList()));
    }


    private void onGetAllFylke(Event<FintResource> responseEvent) {
        responseEvent.setData(dataService.getFylker().stream().map(FintResource::with).collect(Collectors.toList()));
    }

    private void onGetAllKommune(Event<FintResource> responseEvent) {
        responseEvent.setData(dataService.getKommuner().stream().map(FintResource::with).collect(Collectors.toList()));
    }

    /**
     * Checks if the application is healthy and updates the event object.
     *
     * @param event The event object
     */
    public void postHealthCheckResponse(Event event) {
        Event<Health> healthCheckEvent = new Event<>(event);
        healthCheckEvent.setStatus(Status.TEMP_UPSTREAM_QUEUE);

        if (healthCheck()) {
            healthCheckEvent.addData(new Health("adapter", HealthStatus.APPLICATION_HEALTHY.name()));
        } else {
            healthCheckEvent.addData(new Health("adapter", HealthStatus.APPLICATION_UNHEALTHY));
            healthCheckEvent.setMessage("The adapter is unable to communicate with the application.");
        }

        eventResponseService.postResponse(healthCheckEvent);
    }

    /**
     * This is where we implement the health check code
     *
     * @return {@code true} if health is ok, else {@code false}
     */
    private boolean healthCheck() {
        /*
         * Check application connectivity etc.
         */
        return true;
    }

}
