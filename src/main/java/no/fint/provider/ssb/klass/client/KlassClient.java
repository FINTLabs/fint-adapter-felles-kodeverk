package no.fint.provider.ssb.klass.client;

import org.springframework.web.client.RestTemplate;

import no.fint.provider.ssb.klass.model.Codes;

public class KlassClient {

	public Codes getCodes() {
		RestTemplate restTemplate = new RestTemplate();
		Codes codes = restTemplate.getForObject("http://data.ssb.no/api/klass/v1/classifications/131/codes?from=1900-01-01&to=2199-12-31", Codes.class);
		return codes;
	}
}
