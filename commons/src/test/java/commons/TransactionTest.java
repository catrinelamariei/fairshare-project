package commons;

import org.junit.jupiter.api.Test;
import java.math.BigDecimal;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class TransactionTest {

    @Test
    public void checkConstructor() {
        Date date = new Date();
        BigDecimal amount = new BigDecimal("100.00");
        String currencyCode = "USD";

        Transaction transaction = new Transaction(date, currencyCode, amount);

        assertEquals(date, transaction.date);
        assertEquals(currencyCode, transaction.currencyCode);
        assertEquals(amount, transaction.amount);
    }

    @Test
    public void testEqualsMethod() {
        // Creating two transactions with the same attributes
        Date date = new Date();
        BigDecimal amount = new BigDecimal("100.00");
        String currencyCode = "USD";
        Transaction transaction1 = new Transaction(date, currencyCode, amount);
        Transaction transaction2 = new Transaction(date, currencyCode, amount);

        // Asserting that the two transactions are equal
        assertEquals(transaction1, transaction2);

        // Creating a third transaction with different attributes
        Date differentDate = new Date(date.getTime() + 1000); // Adding one second difference
        BigDecimal differentAmount = new BigDecimal("200.00");
        String differentCurrencyCode = "EUR";
        Transaction differentTransaction = new Transaction(differentDate, differentCurrencyCode, differentAmount);

        // Asserting that the first transaction is not equal to the third transaction
        assertNotEquals(transaction1, differentTransaction);
    }


    @Test
    public void equalsHashCode() {
        Date date = new Date();
        BigDecimal amount = new BigDecimal("100.00");
        String currencyCode = "USD";

        Transaction a = new Transaction(date, currencyCode, amount);
        Transaction b = new Transaction(date, currencyCode, amount);

        assertEquals(a, b);
        assertEquals(a.hashCode(), b.hashCode());
    }

    @Test
    public void hasToString() {
        Date date = new Date();
        BigDecimal amount = new BigDecimal("100.00");
        String currencyCode = "USD";

        Transaction transaction = new Transaction(date, currencyCode, amount);

        String actual = transaction.toString();

        assertTrue(actual.contains("Transaction"));
        assertTrue(actual.contains("date=" + date));
        assertTrue(actual.contains("currencyCode=" + currencyCode));
        assertTrue(actual.contains("amount=" + amount));
    }
}
