package no.fint.provider.felles.kodeverk;

import javax.annotation.PostConstruct;

import org.springframework.stereotype.Component;

import no.fint.model.felles.kodeverk.KodeverkActions;
import no.fint.model.felles.kodeverk.iso.IsoActions;
import no.fint.provider.adapter.AbstractSupportedActions;

@Component
public class SupportedActions extends AbstractSupportedActions {

    @PostConstruct
    public void addSupportedActions() {
    	add(KodeverkActions.GET_ALL_FYLKE);
    	add(KodeverkActions.GET_ALL_KOMMUNE);
    	add(IsoActions.GET_ALL_KJONN);
    	add(IsoActions.GET_ALL_LANDKODE);
    	add(IsoActions.GET_ALL_SPRAK);
    }

}
