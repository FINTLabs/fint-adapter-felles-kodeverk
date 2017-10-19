package no.fint.provider.felles.kodeverk.model;

import lombok.Data;

@Data
public class KlassCode {

	String code;
	String parentCode;
	String level;
	String name;
	String shortName;
	String presentationName;
	String validFromInRequestedRange;
	String validToInRequestedRange;
	
}
