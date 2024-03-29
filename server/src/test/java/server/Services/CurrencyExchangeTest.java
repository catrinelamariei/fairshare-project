package server.Services;

import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.net.MalformedURLException;
import java.time.LocalDate;
import java.util.Calendar;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;

class CurrencyExchangeTest {

    @Test
    void getRate() throws IOException, InterruptedException {
        String eur = "EUR";
        String usd = "USD";
        Date date = new Date(2020-1900, Calendar.OCTOBER, 10, 17, 0, 0);
        Date date1 = new Date(2020-1900, Calendar.OCTOBER, 10, 0, 0, 0);
        CurrencyExchange currencyExchange = new CurrencyExchange();
        assertNotNull(currencyExchange.getRate(eur, usd, new Date()));
        assertEquals(currencyExchange.getRate(eur, usd, date).rate,
                currencyExchange.getRate(eur, usd, date1).rate);

        assertEquals(1.1795, currencyExchange.getRate(eur, usd, date).rate);

    }
}