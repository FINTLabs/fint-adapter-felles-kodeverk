package no.fint.provider.felles.kodeverk.model;

import lombok.Data;
import no.fint.model.felles.kompleksedatatyper.Identifikator;

@Data
public class Kode {
	Identifikator systemId;
	String kode;
	String navn;
}
