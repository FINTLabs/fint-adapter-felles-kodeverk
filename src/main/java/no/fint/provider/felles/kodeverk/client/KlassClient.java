package no.fint.provider.felles.kodeverk.client;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import lombok.extern.slf4j.Slf4j;
import no.fint.provider.felles.kodeverk.model.KlassCodes;

@Component
@Slf4j
public class KlassClient {
	
	@Autowired
	RestTemplate restTemplate;
	
	@Value("${fint.adapter.ssb-klass.root-url:http://data.ssb.no/api/klass/v1/classifications}")
	String rootURL;

	public KlassCodes getCodes(String classification, String fromDate, String toDate) {
		log.info("Fetching {} for {}...{}", classification, fromDate, toDate);
		KlassCodes codes = restTemplate.getForObject(rootURL + "/{classification}/codes?from={fromDate}&to={toDate}", KlassCodes.class, classification, fromDate, toDate);
		return codes;
	}
}
