import no.fint.provider.felles.kodeverk.client.Kodeverk;
import no.fint.provider.felles.kodeverk.model.Kode;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class KodeverkTest {

    @Test
    public void kjonn() {
        List<Kode> kjonn = new Kodeverk().getKjonn();
        assertEquals(kjonn.size(), 4);
    }

    @Test
    public void land() {
        List<Kode> land = new Kodeverk().getLand();
        assertEquals(land.size(), 249);
        assertEquals(land.stream().filter(k -> k.getKode().equals("NO")).map(Kode::getNavn).findAny().get(), "Norge");
    }

    @Test
    public void spraak() {
        List<Kode> sprak = new Kodeverk().getSprak();
        assertEquals(sprak.size(), 186);
        assertEquals(sprak.stream().filter(k -> k.getKode().equals("no")).map(Kode::getNavn).findAny().get(), "Norsk");
    }

    @Test
    public void stillingskoder() {
        List<Kode> stillingskoder = new Kodeverk().getStillingskoder();
        assertEquals(stillingskoder.size(), 91);
        assertEquals(stillingskoder.stream().filter(k -> k.getKode().equals("6014")).map(Kode::getNavn).findAny().get(), "ARBEIDER");
    }
}
