package commons.Currency;

import org.junit.jupiter.api.Test;

import java.util.Date;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

class RateTest {

    @Test
    void testEquals() {
        Rate rate1 = new Rate("EUR", "USD", 1.1795, new Date());
        Rate rate2 = new Rate("EUR", "USD", 1.1795, new Date());
        assertEquals(rate1, rate2);

        Rate rate3 = new Rate("EUR", "CHF", 1.1795, new Date());
        assertNotEquals(rate1, rate3);

        Rate rate4 = new Rate("EUR", "USD", 1.1796, new Date());
        assertNotEquals(rate1, rate4);

        Rate rate5 = new Rate("EUR", "USD", 1.1795, new Date(2020-1900, 10, 10, 17, 0, 0));
        assertNotEquals(rate1, rate5);

        Rate rate6 = new Rate("CHF", "USD", 1.1795, new Date());
        assertNotEquals(rate1, rate6);
    }

    @Test
    void testHashCode() {
        Rate rate1 = new Rate("EUR", "USD", 1.1795, new Date(
                2020-1900, 10, 10, 10, 0, 0
        ));
        Rate rate2 = new Rate("EUR", "USD", 1.1795, new Date(
                2020-1900, 10, 10, 17, 0, 0
        ));
        assertEquals(rate1.hashCode(), rate2.hashCode());
    }

    @Test
    void getDate() {
        Rate rate = new Rate("EUR", "USD", 1.1795, new Date());
        assertEquals(rate.date, rate.getDate());
    }

    @Test
    void getRate() {
        Rate rate = new Rate("EUR", "USD", 1.1795, new Date());
        assertEquals(rate.rate, rate.getRate());
    }
}