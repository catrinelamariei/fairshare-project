package server.Services;

import org.junit.jupiter.api.Test;
import org.mockito.Mock;

import java.io.IOException;
import java.util.Calendar;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class CurrencyExchangeTest {

    @Test
    void getRate() {
        String eur = "EUR";
        String usd = "USD";
        Date date = new Date(2020-1900, Calendar.OCTOBER, 10, 17, 0, 0);
        Date date1 = new Date(2020-1900, Calendar.OCTOBER, 10, 0, 0, 0);
        FrankfurterAPI api = mock(FrankfurterAPI.class);
        when(api.getURL("EUR","USD","20201010")).thenReturn("url");
        when(api.getRate("url")).thenReturn(2.0);
        CurrencyExchange currencyExchange = new CurrencyExchange(api);
        assertNotNull(currencyExchange.getRate(eur, usd, new Date()));
        assertEquals(currencyExchange.getRate(eur, usd, date).rate,
                currencyExchange.getRate(eur, usd, date1).rate);

    }
}