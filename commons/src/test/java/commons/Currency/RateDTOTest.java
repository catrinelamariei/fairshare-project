package commons.Currency;

import org.junit.jupiter.api.Test;

import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;

class RateDTOTest {

    @Test
    void testEquals() {
        RateDTO rateDTO1 = new RateDTO("EUR", "USD", 1.1795, new Date());
        RateDTO rateDTO2 = new RateDTO("EUR", "USD", 1.1795, new Date());
        assertEquals(rateDTO1, rateDTO2);
    }

    @Test
    void testHashCode() {
        RateDTO rateDTO1 = new RateDTO("EUR", "USD", 1.1795, new Date());
        RateDTO rateDTO2 = new RateDTO("EUR", "USD", 1.1795, new Date());
        assertEquals(rateDTO1.hashCode(), rateDTO2.hashCode());
    }

    @Test
    void testNotEquals() {
        RateDTO rateDTO1 = new RateDTO();
        RateDTO rateDTO2 = new RateDTO("EUR", "USD", 1.1796, new Date());
        assertNotEquals(rateDTO1, rateDTO2);
    }
}