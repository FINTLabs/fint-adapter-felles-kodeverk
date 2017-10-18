package no.fint.provider;

import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

import org.junit.Test;

import no.fint.provider.ssb.klass.client.KlassClient;
import no.fint.provider.ssb.klass.model.Codes;

public class KlassClientTest {

	@Test
	public void test() {
		
		KlassClient client = new KlassClient();
		Codes codes = client.getCodes();
		assertThat(codes.getCodes().size(), greaterThan(100));
		assertThat(codes.getCodes().stream().anyMatch(c -> c.getName().equalsIgnoreCase("oslo")), is(true));
	}

}
