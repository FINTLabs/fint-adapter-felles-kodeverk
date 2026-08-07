package no.fint.provider.felles.kodeverk.client;

import lombok.extern.slf4j.Slf4j;
import no.fint.provider.felles.kodeverk.model.ssb.CorrespondenceItemList;
import no.fint.provider.felles.kodeverk.model.ssb.KlassCodeList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
@Slf4j
public class KlassClient {
	
	@Autowired
	RestTemplate restTemplate;
	
	@Value("${fint.adapter.ssb-klass.root-url:https://data.ssb.no/api/klass/v1/classifications}")
	String rootURL;

	public KlassCodeList getCodes(String classification, String date) {
		log.info("Fetching {} for {}", classification, date);
		KlassCodeList codes = restTemplate.getForObject(rootURL + "/{classification}/codesAt?date={date}", KlassCodeList.class, classification, date);
		return codes;
	}

	public CorrespondenceItemList getCorrespondences(String sourceClassification, String targetClassification, String date) {
		log.info("Fetching correspondences from {} to {} for {}", sourceClassification, targetClassification, date);
		return restTemplate.getForObject(rootURL + "/{sourceClassification}/correspondsAt?date={date}&targetClassificationId={targetClassification}", CorrespondenceItemList.class, sourceClassification, date, targetClassification);
	}
}
