package no.fint.provider.ssb.klass.model;

import lombok.Data;

@Data
public class Code {

	String code;
	String parentCode;
	String level;
	String name;
	String shortName;
	String presentationName;
	String validFromInRequestedRange;
	String validToInRequestedRange;
	
}
