package no.fint.provider.ssb.klass;

import javax.annotation.PostConstruct;

import org.springframework.stereotype.Component;

import no.fint.model.felles.kodeverk.KodeverkActions;
import no.fint.provider.adapter.AbstractSupportedActions;

@Component
public class SupportedActions extends AbstractSupportedActions {

    @PostConstruct
    public void addSupportedActions() {
        addAll(KodeverkActions.class);
    }

}
