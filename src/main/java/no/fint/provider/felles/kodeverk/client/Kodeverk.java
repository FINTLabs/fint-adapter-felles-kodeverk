package no.fint.provider.felles.kodeverk.client;

import java.io.InputStream;
import java.util.List;

import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.TypeFactory;

import lombok.SneakyThrows;
import no.fint.provider.felles.kodeverk.model.Kode;

@Component
public class Kodeverk {
	
	@SneakyThrows
	public List<Kode> getKodeList(InputStream stream) {
		ObjectMapper mapper = new ObjectMapper();
		TypeFactory tf = mapper.getTypeFactory();
		return mapper.readValue(stream, tf.constructCollectionType(List.class, Kode.class));
	}

	public List<Kode> getKjonn() {
		return getKodeList(getClass().getResourceAsStream("/iso_5218.json"));
	}

	public List<Kode> getLand() {
		return getKodeList(getClass().getResourceAsStream("/iso_3166_1_alfa_2.json"));
	}

	public List<Kode> getSprak() {
		return getKodeList(getClass().getResourceAsStream("/iso_639_1_alfa_2.json"));
	}

	public List<Kode> getStillingskoder() {
		return getKodeList(getClass().getResourceAsStream("/stillingskoder.json"));
	}

}
