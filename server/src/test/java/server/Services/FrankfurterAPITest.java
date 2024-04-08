package server.Services;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class FrankfurterAPITest {

    @Test
    void getURL() {
        String expected = "https://api.frankfurter.app/2021-01-01?from=USD&to=EUR";
        FrankfurterAPI frankfurterAPI = new FrankfurterAPI();
        String actual = frankfurterAPI.getURL("USD", "EUR", "2021-01-01");
        assertEquals(expected, actual);
    }
}