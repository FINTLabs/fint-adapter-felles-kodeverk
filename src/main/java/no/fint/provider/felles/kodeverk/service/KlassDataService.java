package no.fint.provider.felles.kodeverk.service;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import no.fint.model.resource.Link;
import no.fint.model.resource.felles.kodeverk.FylkeResource;
import no.fint.model.resource.felles.kodeverk.KommuneResource;
import no.fint.provider.felles.kodeverk.client.KlassClient;
import no.fint.provider.felles.kodeverk.model.Mapper;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class KlassDataService {

    @Value("${fint.adapter.ssb-klass.kommune:131}")
    String kommuneKode;

    @Value("${fint.adapter.ssb-klass.fylke:104}")
    String fylkeKode;

    @Value("${fint.adapter.ssb-klass.date:}")
    String date;

    @Autowired
    KlassClient client;

    @Getter
    private volatile List<KommuneResource> kommuner;

    @Getter
    private volatile List<FylkeResource> fylker;

    @Scheduled(initialDelay = 6000, fixedDelayString = "${fint.adapter.ssb-klass.interval:3600000}")
    public void update() {
        log.info("Fetching classifications from SSB...");
        String currentDate = date;
        if (StringUtils.isBlank(currentDate)) {
            currentDate = LocalDate.now().format(DateTimeFormatter.ISO_LOCAL_DATE);
        }
        kommuner = client.getCodes(kommuneKode, currentDate).getCodes().stream().map(Mapper::toKommune).collect(Collectors.toList());
        fylker = client.getCodes(fylkeKode, currentDate).getCodes().stream().map(Mapper::toFylke).collect(Collectors.toList());

        fylker.forEach(fylke ->
                kommuner
                        .stream()
                        .map(KommuneResource::getKode)
                        .filter(s -> s.startsWith(fylke.getKode()))
                        .map(Link.apply(KommuneResource.class, "systemid"))
                        .forEach(fylke::addKommune));

        kommuner.forEach(kommune ->
                fylker
                        .stream()
                        .map(FylkeResource::getKode)
                        .filter(s -> kommune.getKode().startsWith(s))
                        .map(Link.apply(FylkeResource.class, "systemid"))
                        .forEach(kommune::addFylke));
    }

    public boolean isHealthy() {
        return kommuner != null && !kommuner.isEmpty() && fylker != null && !fylker.isEmpty();
    }
}
