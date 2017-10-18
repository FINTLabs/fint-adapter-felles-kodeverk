package no.fint.provider.ssb.klass.service;

import java.security.acl.Owner;
import java.util.ArrayList;
import java.util.Optional;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
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

/**
 * The EventHandlerService receives the <code>event</code> from SSE endpoint (provider) in the {@link #handleEvent(Event)} method.
 */
@Slf4j
@Service
public class EventHandlerService {

    @Autowired
    private EventResponseService eventResponseService;

    @Autowired
    private EventStatusService eventStatusService;


    /**
     * <p>
     * HandleEvent is responsible of handling the <code>event</code>. This is what should be done:
     * </p>
     * <ol>
     * <li>Verify that the adapter can handle the <code>event</code>. This is done in the {@link EventStatusService#verifyEvent(Event)} method</li>
     * <li>Call the code to handle the action</li>
     * <li>Posting back the handled <code>event</code>. This done in the {@link EventResponseService#postResponse(Event)} method</li>
     * </ol>
     * <p>
     * This is where you implement your code for handling the <code>event</code>. It is typically done by making a onEvent method:
     * </p>
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
            if (event != null && eventStatusService.verifyEvent(event).getStatus() == Status.ADAPTER_ACCEPTED) {
            	KodeverkActions action = KodeverkActions.valueOf(event.getAction());
                Event<FintResource> responseEvent = new Event<>(event);

                switch (action) {
                case GET_ALL_FYLKE:
                	onGetAllFylke(responseEvent);
                case GET_KOMMUNE:
                	onGetKommune(responseEvent);
                }

                responseEvent.setStatus(Status.ADAPTER_RESPONSE);
                eventResponseService.postResponse(responseEvent);
            }
        }
    }

    private void onGetAllFylke(Event<FintResource> responseEvent) {
    	/*
        Optional<Owner> owner = owners.stream().filter(o -> o.getId().equals(responseEvent.getQuery())).findFirst();

        if (owner.isPresent()) {
            responseEvent.addData(FintResource.with(owner.get()).addRelations(
                    new Relation.Builder().with(Owner.Relasjonsnavn.DOG).forType(Dog.class).value(owner.get().getId().substring(0, 1)).build())
            );
        }
        */
    }

    private void onGetKommune(Event<FintResource> responseEvent) {
    	/*
        Optional<Dog> dog = dogs.stream().filter(d -> d.getId().equals(responseEvent.getQuery())).findFirst();

        if (dog.isPresent()) {
            responseEvent.addData(FintResource.with(dog.get()).addRelations(
                    new Relation.Builder().with(Dog.Relasjonsnavn.OWNER).forType(Owner.class).value(dog.get().getId() + "0").build())
            );

        }
        */
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

    /**
     * Data used in examples
     */
    @PostConstruct
    void init() {
    }
}
