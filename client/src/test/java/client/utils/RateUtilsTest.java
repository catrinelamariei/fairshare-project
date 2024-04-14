package client.utils;

import client.UserData;
import commons.Currency.RateDTO;
import org.junit.jupiter.api.Test;

import java.util.Date;

import static org.junit.jupiter.api.Assertions.assertEquals;

class RateUtilsTest {
    private final UserData userData = UserData.load();

    @Test
    void getRateSameCurrency() {
        RateDTO rate = RateUtils.getRate("EUR", "EUR",new Date(), userData);
        assertEquals(1.0, rate.rate);
    }
}