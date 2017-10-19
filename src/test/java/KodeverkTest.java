import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

import java.io.IOException;
import java.util.List;

import org.junit.Test;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;

import no.fint.provider.felles.kodeverk.client.Kodeverk;
import no.fint.provider.felles.kodeverk.model.Kode;

public class KodeverkTest {

	@Test
	public void kjonn() throws JsonParseException, JsonMappingException, IOException {
		List<Kode> kjonn = new Kodeverk().getKjonn();
		assertThat(kjonn.size(), is(4));
	}

	@Test
	public void land() throws Exception {
		List<Kode> land = new Kodeverk().getLand();
		assertThat(land.size(), is(249));
		assertThat(land.stream().filter(k -> k.getKode().equals("NO")).map(Kode::getNavn).findAny().get(), is("Norge"));
	}

	@Test
	public void spraak() throws Exception {
		List<Kode> sprak = new Kodeverk().getSprak();
		assertThat(sprak.size(), is(186));
		assertThat(sprak.stream().filter(k -> k.getKode().equals("no")).map(Kode::getNavn).findAny().get(), is("Norsk"));
	}

	@Test
	public void stillingskoder() throws Exception {
		List<Kode> stillingskoder = new Kodeverk().getStillingskoder();
		assertThat(stillingskoder.size(), is(91));
		assertThat(stillingskoder.stream().filter(k -> k.getKode().equals("6014")).map(Kode::getNavn).findAny().get(), is("ARBEIDER"));
	}
}
