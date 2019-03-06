package no.fint.provider.felles.kodeverk.service

import no.fint.event.model.DefaultActions
import no.fint.event.model.Event
import no.fint.event.model.Status
import no.fint.model.felles.kodeverk.KodeverkActions
import no.fint.model.felles.kodeverk.iso.IsoActions
import no.fint.model.resource.felles.kodeverk.FylkeResource
import no.fint.model.resource.felles.kodeverk.KommuneResource
import no.fint.provider.adapter.event.EventResponseService
import no.fint.provider.adapter.event.EventStatusService
import no.fint.provider.felles.kodeverk.client.Kodeverk
import no.fint.provider.felles.kodeverk.model.Kode
import spock.lang.Specification

class EventHandlerServiceSpec extends Specification {
    private EventHandlerService eventHandlerService
    private EventStatusService eventStatusService
    private EventResponseService eventResponseService
    private Kodeverk kodeverk
    private KlassDataService dataService

    void setup() {
        eventStatusService = Mock(EventStatusService)
        eventResponseService = Mock(EventResponseService)
        kodeverk = Mock(Kodeverk)
        dataService = Mock(KlassDataService)
        eventHandlerService = new EventHandlerService(
                eventStatusService: eventStatusService,
                eventResponseService: eventResponseService,
                kodeverk: kodeverk,
                dataService: dataService
        )
    }

    def "Post response on health check"() {
        given:
        def event = new Event('rogfk.no', 'test', DefaultActions.HEALTH, 'test')

        when:
        eventHandlerService.handleEvent(event)

        then:
        1 * eventResponseService.postResponse(_ as Event)
        1 * dataService.isHealthy() >> true
    }

    def "Get Kjonn"() {
        given:
        def event = new Event('fintlabs.no', 'test', IsoActions.GET_ALL_KJONN, 'test')

        when:
        eventHandlerService.handleEvent(event)

        then:
        1 * eventResponseService.postResponse(_ as Event)
        1 * kodeverk.getKjonn() >> [new Kode(kode: '99', navn: 'Test')]
        1 * eventStatusService.verifyEvent(_ as Event) >> { Event e -> e.status = Status.ADAPTER_ACCEPTED; e }
    }

    def "Get Landkode"() {
        given:
        def event = new Event('fintlabs.no', 'test', IsoActions.GET_ALL_LANDKODE, 'test')

        when:
        eventHandlerService.handleEvent(event)

        then:
        1 * eventResponseService.postResponse(_ as Event)
        1 * kodeverk.getLand() >> [new Kode(kode: '99', navn: 'Test')]
        1 * eventStatusService.verifyEvent(_ as Event) >> { Event e -> e.status = Status.ADAPTER_ACCEPTED; e }
    }

    def "Get Sprak"() {
        given:
        def event = new Event('fintlabs.no', 'test', IsoActions.GET_ALL_SPRAK, 'test')

        when:
        eventHandlerService.handleEvent(event)

        then:
        1 * eventResponseService.postResponse(_ as Event)
        1 * kodeverk.getSprak() >> [new Kode(kode: '99', navn: 'Test')]
        1 * eventStatusService.verifyEvent(_ as Event) >> { Event e -> e.status = Status.ADAPTER_ACCEPTED; e }
    }

    def "Get Kommune"() {
        given:
        def event = new Event('fintlabs.no', 'test', KodeverkActions.GET_ALL_KOMMUNE, 'test')

        when:
        eventHandlerService.handleEvent(event)

        then:
        1 * eventResponseService.postResponse(_ as Event)
        1 * dataService.getKommuner() >> [new KommuneResource(kode: '99', navn: 'Test')]
        1 * eventStatusService.verifyEvent(_ as Event) >> { Event e -> e.status = Status.ADAPTER_ACCEPTED; e }
    }

    def "Get Fylke"() {
        given:
        def event = new Event('fintlabs.no', 'test', KodeverkActions.GET_ALL_FYLKE, 'test')

        when:
        eventHandlerService.handleEvent(event)

        then:
        1 * eventResponseService.postResponse(_ as Event)
        1 * dataService.getFylker() >> [new FylkeResource(kode: '99', navn: 'Test')]
        1 * eventStatusService.verifyEvent(_ as Event) >> { Event e -> e.status = Status.ADAPTER_ACCEPTED; e }
    }

}
