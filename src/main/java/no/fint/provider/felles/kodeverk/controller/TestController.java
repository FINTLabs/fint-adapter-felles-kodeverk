package no.fint.provider.felles.kodeverk.controller;

import no.fint.model.resource.felles.kodeverk.FylkeResources;
import no.fint.model.resource.felles.kodeverk.KommuneResources;
import no.fint.provider.felles.kodeverk.service.KlassDataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "/", produces = MediaType.APPLICATION_JSON_VALUE)
public class TestController {
    @Autowired
    KlassDataService klassDataService;

    @GetMapping(path = "/fylke")
    public FylkeResources getFylkeResources() {
        return new FylkeResources(klassDataService.getFylker());
    }

    @GetMapping(path = "/kommune")
    public KommuneResources getKommuneResources() {
        return new KommuneResources(klassDataService.getKommuner());
    }
}
