package no.fint.provider.felles.kodeverk.service

import no.fint.model.resource.Link
import no.fint.model.resource.felles.kodeverk.FylkeResource
import no.fint.model.resource.felles.kodeverk.KommuneResource
import no.fint.provider.felles.kodeverk.client.KlassClient
import no.fint.provider.felles.kodeverk.model.ssb.CorrespondenceItem
import no.fint.provider.felles.kodeverk.model.ssb.CorrespondenceItemList
import no.fint.provider.felles.kodeverk.model.ssb.KlassCodeItem
import no.fint.provider.felles.kodeverk.model.ssb.KlassCodeList
import spock.lang.Specification

class KlassDataServiceSpec extends Specification {
    private KlassDataService dataService
    private KlassClient client

    void setup() {
        client = Mock(KlassClient)
        dataService = new KlassDataService(client: client)
        dataService.fylkeKode = '104'
        dataService.kommuneKode = '131'
        dataService.date = '2024-01-01'
    }

    def "FylkeResource and KommuneResource are mapped with links from SSB Klass correspondences"() {
        given:
        def fylke = new KlassCodeItem(code: '03', level: '1', name: 'Oslo')
        def kommune = new KlassCodeItem(code: '0301', level: '1', name: 'Oslo')
        def fylker = new KlassCodeList(codes: [fylke])

        def kommuner = new KlassCodeList(codes: [kommune])
        def correspondence = new CorrespondenceItem(
                sourceCode: '03', sourceName: 'Oslo - Oslove',
                targetCode: '0301', targetName: 'Oslo - Oslove')
        def correspondences = new CorrespondenceItemList(correspondenceItems: [correspondence])

        when:
        client.getCodes('104', '2024-01-01') >> fylker
        client.getCodes('131', '2024-01-01') >> kommuner
        client.getCorrespondences('104', '131', '2024-01-01') >> correspondences
        dataService.update()

        then:
        dataService.fylker.size() == 1
        def fylkeResource = dataService.fylker[0]
        fylkeResource instanceof FylkeResource
        fylkeResource.kode == '03'
        fylkeResource.navn == 'Oslo'
        fylkeResource.kommune != null
        fylkeResource.kommune.size() == 1
        fylkeResource.kommune[0] == Link.with(KommuneResource, '0301')

        dataService.kommuner.size() == 1
        def kommuneResource = dataService.kommuner[0]
        kommuneResource instanceof KommuneResource
        kommuneResource.kode == '0301'
        kommuneResource.navn == 'Oslo'
        kommuneResource.fylke != null
        kommuneResource.fylke.size() == 1
        kommuneResource.fylke[0] == Link.with(FylkeResource, '03')
    }

    def "Agder FylkeResource contains links to both Grimstad and Lillesand"() {
        given:
        def agder = new KlassCodeItem(code: '42', level: '1', name: 'Agder',)
        def grimstad = new KlassCodeItem(code: '4202', level: '1', name: 'Grimstad',)
        def lillesand = new KlassCodeItem(code: '4215', level: '1', name: 'Lillesand',)

        def fylker = new KlassCodeList(codes: [agder])
        def kommuner = new KlassCodeList(codes: [grimstad, lillesand])
        def correspondences = new CorrespondenceItemList(correspondenceItems: [new CorrespondenceItem(sourceCode: '42', sourceName: 'Agder', targetCode: '4202', targetName: 'Grimstad'),
                                                                               new CorrespondenceItem(sourceCode: '42', sourceName: 'Agder', targetCode: '4215', targetName: 'Lillesand')])

        when:
        client.getCodes('104', '2024-01-01') >> fylker
        client.getCodes('131', '2024-01-01') >> kommuner
        client.getCorrespondences('104', '131', '2024-01-01') >> correspondences
        dataService.update()

        then:
        dataService.fylker.size() == 1
        def fylkeResource = dataService.fylker[0]
        fylkeResource.kode == '42'
        fylkeResource.navn == 'Agder'
        fylkeResource.kommune != null
        fylkeResource.kommune.size() == 2
        fylkeResource.kommune.containsAll([Link.with(KommuneResource, '4202'),
                                           Link.with(KommuneResource, '4215')])
        dataService.kommuner.size() == 2
        dataService.kommuner.every { it.fylke[0] == Link.with(FylkeResource, '42') }
    }
}
