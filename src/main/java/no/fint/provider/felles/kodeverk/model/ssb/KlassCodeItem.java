package no.fint.provider.felles.kodeverk.model.ssb;

import lombok.Data;

@Data
public class KlassCodeItem {
	String code;
	String parentCode;
	String level;
	String name;
	String shortName;
	String presentationName;
	String validFromInRequestedRange;
	String validToInRequestedRange;
}
