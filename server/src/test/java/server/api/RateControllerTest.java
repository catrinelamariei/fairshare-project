package server.api;

import commons.Currency.*;
import org.junit.jupiter.api.Test;
import server.Services.CurrencyExchange;

import java.text.*;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;
import static org.springframework.http.HttpStatus.BAD_REQUEST;

class RateControllerTest {

    @Test
    void getRate() throws ParseException {

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");
        Date parsedDate = dateFormat.parse("20201010");
        RateDTO rateDTO = new RateDTO("EUR", "USD", 2.0, parsedDate);
        CurrencyExchange currencyExchange = mock(CurrencyExchange.class);
        when(currencyExchange.getRate("EUR", "USD", parsedDate)).thenReturn(
                new Rate("EUR", "USD", 2.0, parsedDate)
        );

        RateController rateController = new RateController(currencyExchange);
        RateDTO result = rateController.getRate(rateDTO).getBody();
        assertEquals(rateDTO, result);


    }

    @Test
    void getRateFromFile() throws ParseException {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");
        Date parsedDate = dateFormat.parse("20201010");
        RateDTO rateDTO = new RateDTO("EUR", "USD", 2.0, parsedDate);
        CurrencyExchange currencyExchange = mock(CurrencyExchange.class);
        when(currencyExchange.getRate("EUR", "USD", parsedDate))
                .thenThrow(new RuntimeException());
        when(currencyExchange.getRateFromFile("EUR", "USD", "20201010")).thenReturn(
                new Rate("EUR", "USD", 2.0, parsedDate)
        );
        RateController rateController = new RateController(currencyExchange);
        RateDTO result = rateController.getRate(rateDTO).getBody();
        assertEquals(rateDTO, result);
    }

    @Test
    void getRateFromFileException() throws ParseException {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");
        Date parsedDate = dateFormat.parse("20201010");
        RateDTO rateDTO = new RateDTO("EUR", "USD", 2.0, parsedDate);
        CurrencyExchange currencyExchange = mock(CurrencyExchange.class);
        when(currencyExchange.getRate("EUR", "USD", parsedDate))
                .thenThrow(new RuntimeException());
        when(currencyExchange.getRateFromFile("EUR", "USD", "20201010"))
                .thenThrow(new ParseException("Error", 0));
        RateController rateController = new RateController(currencyExchange);
        assertEquals(BAD_REQUEST, rateController.getRate(rateDTO).getStatusCode());
    }
}