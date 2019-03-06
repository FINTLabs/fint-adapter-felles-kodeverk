package no.fint.provider.felles.kodeverk.service;

import lombok.extern.slf4j.Slf4j;
import no.fint.event.model.Event;
import no.fint.event.model.ResponseStatus;
import no.fint.event.model.Status;
import no.fint.event.model.health.Health;
import no.fint.event.model.health.HealthStatus;
import no.fint.model.felles.kodeverk.KodeverkActions;
import no.fint.model.felles.kodeverk.iso.IsoActions;
import no.fint.model.resource.FintLinks;
import no.fint.provider.adapter.event.EventResponseService;
import no.fint.provider.adapter.event.EventStatusService;
import no.fint.provider.felles.kodeverk.client.Kodeverk;
import no.fint.provider.felles.kodeverk.model.Kode;
import no.fint.provider.felles.kodeverk.model.Mapper;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.function.Function;

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
                Event<FintLinks> responseEvent = new Event<>(event);
                responseEvent.setStatus(Status.ADAPTER_RESPONSE);

                try {
                    if (KodeverkActions.getActions().contains(event.getAction())) {

                        switch (KodeverkActions.valueOf(event.getAction())) {
                            case GET_ALL_FYLKE:
                                onGetAllFylke(responseEvent);
                                break;
                            case GET_ALL_KOMMUNE:
                                onGetAllKommune(responseEvent);
                                break;
                            default:
                                responseEvent.setStatus(Status.ADAPTER_REJECTED);
                                responseEvent.setMessage("Unsupported action " + event.getAction());
                        }
                    } else if (IsoActions.getActions().contains(event.getAction())) {

                        switch (IsoActions.valueOf(event.getAction())) {
                            case GET_ALL_KJONN:
                                onGetKodeverk(kodeverk.getKjonn(), responseEvent, Mapper::toKjonn);
                                break;
                            case GET_ALL_LANDKODE:
                                onGetKodeverk(kodeverk.getLand(), responseEvent, Mapper::toLandkode);
                                break;
                            case GET_ALL_SPRAK:
                                onGetKodeverk(kodeverk.getSprak(), responseEvent, Mapper::toSprak);
                                break;
                            default:
                                responseEvent.setStatus(Status.ADAPTER_REJECTED);
                                responseEvent.setMessage("Unsupported action " + event.getAction());
                        }

                    } else {
                        responseEvent.setStatus(Status.ADAPTER_REJECTED);
                        responseEvent.setMessage("Unsupported action " + event.getAction());
                    }

                } catch (Exception e) {
                    responseEvent.setResponseStatus(ResponseStatus.ERROR);
                    responseEvent.setMessage(ExceptionUtils.getStackTrace(e));
                } finally {
                    if (responseEvent.getData() != null) {
                        log.info("Response for {}: {}, {} items", responseEvent.getAction(), responseEvent.getResponseStatus(), responseEvent.getData().size());
                    } else {
                        log.info("Response for {}: {}", responseEvent.getAction(), responseEvent.getResponseStatus());
                    }
                    eventResponseService.postResponse(event);
                }
            }
        }
    }


    private void onGetKodeverk(List<Kode> data, Event<FintLinks> responseEvent, Function<Kode, FintLinks> mapper) {
        data.stream().map(mapper).forEach(responseEvent::addData);
    }


    private void onGetAllFylke(Event<FintLinks> responseEvent) {
        dataService.getFylker().forEach(responseEvent::addData);
    }

    private void onGetAllKommune(Event<FintLinks> responseEvent) {
        dataService.getKommuner().forEach(responseEvent::addData);
    }

    /**
     * Checks if the application is healthy and updates the event object.
     *
     * @param event The event object
     */
    private void postHealthCheckResponse(Event event) {
        Event<Health> healthCheckEvent = new Event<>(event);
        healthCheckEvent.setStatus(Status.TEMP_UPSTREAM_QUEUE);

        if (healthCheck()) {
            healthCheckEvent.addData(new Health("adapter", HealthStatus.APPLICATION_HEALTHY));
        } else {
            healthCheckEvent.addData(new Health("adapter", HealthStatus.APPLICATION_UNHEALTHY));
            healthCheckEvent.setMessage("Unable to fetch data from SSB.");
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
        return !dataService.getKommuner().isEmpty() && !dataService.getFylker().isEmpty();
    }

}
