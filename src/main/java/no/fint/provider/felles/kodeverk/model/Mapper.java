package no.fint.provider.felles.kodeverk.model;

import no.fint.model.felles.basisklasser.Begrep;
import no.fint.model.felles.kompleksedatatyper.Identifikator;
import no.fint.model.felles.kompleksedatatyper.Periode;
import no.fint.model.resource.felles.kodeverk.FylkeResource;
import no.fint.model.resource.felles.kodeverk.KommuneResource;
import no.fint.model.resource.felles.kodeverk.iso.KjonnResource;
import no.fint.model.resource.felles.kodeverk.iso.LandkodeResource;
import no.fint.model.resource.felles.kodeverk.iso.SprakResource;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;

public class Mapper {

	public static KommuneResource toKommune(KlassCode code) {
		KommuneResource k = new KommuneResource();
		copy(code, k);
		return k;
	}
	
	public static FylkeResource toFylke(KlassCode code) {
		FylkeResource f = new FylkeResource();
		copy(code, f);
		return f;
	}
	
	private static void copy(KlassCode code, Begrep begrep) {
        Identifikator systemId = new Identifikator();
        systemId.setIdentifikatorverdi(code.getCode());
        begrep.setSystemId(systemId);
		begrep.setKode(code.getCode());
		begrep.setNavn(code.getName());
		Periode periode = new Periode();
		periode.setStart(parseDate(code.getValidFromInRequestedRange(), false));
		periode.setSlutt(parseDate(code.getValidToInRequestedRange(), true));
		begrep.setGyldighetsperiode(periode);
		systemId.setGyldighetsperiode(periode);
	}
	
	private static Date parseDate(String input, boolean end) {
		LocalDate date = LocalDate.parse(input, DateTimeFormatter.ISO_LOCAL_DATE);
		LocalDateTime ldt = end ? date.atTime(23, 59) : date.atStartOfDay();
		ZonedDateTime zdt = ldt.atZone(ZoneId.systemDefault());
		return Date.from(zdt.toInstant());
	}

	public static KjonnResource toKjonn(Kode kode) {
		KjonnResource r = new KjonnResource();
		copy(kode, r);
		return r;
	}

	public static LandkodeResource toLandkode(Kode kode) {
		LandkodeResource r = new LandkodeResource();
		copy(kode, r);
		return r;
	}

	public static SprakResource toSprak(Kode kode) {
		SprakResource r = new SprakResource();
		copy(kode, r);
		return r;
	}

	private static void copy(Kode code, Begrep begrep) {
		Identifikator systemId = new Identifikator();
		systemId.setIdentifikatorverdi(code.getKode());
		begrep.setSystemId(systemId);
		begrep.setKode(code.getKode());
		begrep.setNavn(code.getNavn());
	}
}
