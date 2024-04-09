package client.utils;

import commons.Currency.RateDTO;
import org.junit.jupiter.api.Test;

import java.util.Date;

import static org.junit.jupiter.api.Assertions.assertEquals;

class RateUtilsTest {

    @Test
    void getRateSameCurrency() {
        RateDTO rate = RateUtils.getRate("EUR", "EUR",new Date());
        assertEquals(1.0, rate.rate);
    }
}