package no.fint.provider.felles.kodeverk.client;

import lombok.extern.slf4j.Slf4j;
import no.fint.provider.felles.kodeverk.model.KlassCodes;
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

	public KlassCodes getCodes(String classification, String date) {
		log.info("Fetching {} for {}", classification, date);
		KlassCodes codes = restTemplate.getForObject(rootURL + "/{classification}/codesAt?date={date}", KlassCodes.class, classification, date);
		return codes;
	}
}
