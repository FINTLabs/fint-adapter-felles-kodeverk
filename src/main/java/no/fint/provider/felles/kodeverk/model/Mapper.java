package no.fint.provider.felles.kodeverk.model;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;

import no.fint.model.felles.basisklasser.Begrep;
import no.fint.model.felles.kodeverk.Fylke;
import no.fint.model.felles.kodeverk.Kommune;
import no.fint.model.felles.kompleksedatatyper.Periode;

public class Mapper {

	public static Kommune toKommune(KlassCode code) {
		Kommune k = new Kommune();
		copy(code, k);
		return k;
	}
	
	public static Fylke toFylke(KlassCode code) {
		Fylke f = new Fylke();
		copy(code, f);
		return f;
	}
	
	private static void copy(KlassCode code, Begrep begrep) {
		begrep.setKode(code.getCode());
		begrep.setNavn(code.getName());
		Periode periode = new Periode();
		periode.setStart(parseDate(code.getValidFromInRequestedRange()));
		periode.setSlutt(parseDate(code.getValidToInRequestedRange()));
		begrep.setGyldighetsperiode(periode);
	}
	
	private static Date parseDate(String input) {
		LocalDate date = LocalDate.parse(input, DateTimeFormatter.ISO_LOCAL_DATE);
		ZonedDateTime zdt = date.atStartOfDay(ZoneId.systemDefault());
		return Date.from(zdt.toInstant());
	}
}
