package no.fint.provider.felles.kodeverk.service;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import no.fint.model.resource.felles.kodeverk.FylkeResource;
import no.fint.model.resource.felles.kodeverk.KommuneResource;
import no.fint.provider.felles.kodeverk.client.KlassClient;
import no.fint.provider.felles.kodeverk.model.Mapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class KlassDataService {

    @Value("${fint.adapter.ssb-klass.kommune:131}")
    String kommuneKode;

    @Value("${fint.adapter.ssb-klass.fylke:104}")
    String fylkeKode;

    @Value("${fint.adapter.ssb-klass.valid-from:1900-01-01}")
    String validFrom;

    @Value("${fint.adapter.ssb-klass.valid-to:2199-12-31}")
    String validTo;

    @Autowired
    KlassClient client;

    @Getter
    private volatile List<KommuneResource> kommuner;

    @Getter
    private volatile List<FylkeResource> fylker;

    @Scheduled(initialDelay = 6000, fixedDelayString = "${fint.adapter.ssb-klass.interval:3600000}")
    public void update() {
        log.info("Fetching classifications from SSB...");
        kommuner = client.getCodes(kommuneKode, validFrom, validTo).getCodes().stream().map(Mapper::toKommune).collect(Collectors.toList());
        fylker = client.getCodes(fylkeKode, validFrom, validTo).getCodes().stream().map(Mapper::toFylke).collect(Collectors.toList());
    }

    public boolean isHealthy() {
        return kommuner != null && !kommuner.isEmpty() && fylker != null && !fylker.isEmpty();
    }
}
