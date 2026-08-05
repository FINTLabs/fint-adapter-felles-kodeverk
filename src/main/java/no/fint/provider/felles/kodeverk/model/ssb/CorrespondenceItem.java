package no.fint.provider.felles.kodeverk.model.ssb;

import lombok.Data;

@Data
public class CorrespondenceItem {
    String sourceCode;
    String sourceName;
    String sourceShortName;
    String targetCode;
    String targetName;
    String targetShortName;
}
