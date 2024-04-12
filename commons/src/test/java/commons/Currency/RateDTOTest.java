package commons.Currency;

import org.junit.jupiter.api.Test;

import java.text.SimpleDateFormat;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;

class RateDTOTest {

    @Test
    void testEquals() {
        Date date = new Date();
        RateDTO rateDTO1 = new RateDTO("EUR", "USD", 1.1795, date);
        RateDTO rateDTO2 = new RateDTO("EUR", "USD", 1.1795, date);
        assertEquals(rateDTO1, rateDTO2);
    }

    @Test
    void testHashCode() {
        Date date = new Date();
        RateDTO rateDTO1 = new RateDTO("EUR", "USD", 1.1795, date);
        RateDTO rateDTO2 = new RateDTO("EUR", "USD", 1.1795, date);
        assertEquals(rateDTO1.hashCode(), rateDTO2.hashCode());
    }

    @Test
    void testNotEquals() {
        RateDTO rateDTO1 = new RateDTO();
        RateDTO rateDTO2 = new RateDTO("EUR", "USD", 1.1796, new Date());
        assertNotEquals(rateDTO1, rateDTO2);
    }

    @Test
    void testConstructor() {
        Rate rate = new Rate("EUR", "USD", 1.1795, new Date());
        RateDTO rateDTO = new RateDTO(rate);
        assertEquals(rate.currencyFrom, rateDTO.currencyFrom);
        assertEquals(rate.currencyTo, rateDTO.currencyTo);
        assertEquals(rate.rate, rateDTO.rate);
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");
        assertEquals(dateFormat.format(rate.date), rateDTO.date);
    }
}