package commons;

import org.junit.jupiter.api.Test;
import java.math.BigDecimal;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class TransactionTest {

    @Test
    public void addTags(){
        Date date = new Date();
        BigDecimal amount = new BigDecimal("100.00");
        String currencyCode = "USD";
        Event event = new Event("event");
        Transaction transaction = new Transaction(event,date, currencyCode, amount, new Participant(event,"name", "email", "q", "iban", "bic"), "French-Fries");
        Tag tag = new Tag(null, "name", Tag.Color.RED);
        transaction.tags.add(tag);
        assertTrue(transaction.tags.contains(tag));

    }

    @Test
    public void setAuthor (){

    }





    @Test
    public void checkConstructor() {
        Date date = new Date();
        BigDecimal amount = new BigDecimal("100.00");
        String currencyCode = "USD";

        Event event = new Event("event");
        Transaction transaction = new Transaction(event,new Date(), currencyCode, amount, new Participant(event,"name", "email", "q", "iban", "bic"), "Cheese-Burger");

        assertEquals(event, transaction.event);
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

        Event event1 = new Event("event1");
        Transaction transaction1 = new Transaction(event1, date, currencyCode, amount, new Participant(event1, "name", "email", "q", "iban", "bic"), "Chicken-Burger");
        Transaction transaction2 = new Transaction(event1, date, currencyCode, amount, new Participant(event1,"name", "email", "q", "iban", "bic"), "Chicken-Burger");
        transaction2.id = transaction1.id;

        // Asserting that the two transactions are equal
        assertEquals(transaction1, transaction2);

        // Creating a third transaction with different attributes
        Date differentDate = new Date(date.getTime() + 1000); // Adding one second difference
        BigDecimal differentAmount = new BigDecimal("200.00");
        String differentCurrencyCode = "EUR";
        Event event2 = new Event("event2");
        Transaction differentTransaction = new Transaction(event2, differentDate, differentCurrencyCode, differentAmount, new Participant(event2,"diff name", "email", "q", "iban", "bic"), "Chicken-Burger");

        // Asserting that the first transaction is not equal to the third transaction
        assertNotEquals(transaction1, differentTransaction);
    }


    @Test
    public void equalsHashCode() {
        Date date = new Date();
        BigDecimal amount = new BigDecimal("100.00");
        String currencyCode = "USD";

        Event event = new Event("event");
        Transaction a = new Transaction(event, date, currencyCode, amount, new Participant(event,"name", "email", "q", "iban", "bic"), "Chicken-Burger");
        Transaction b = new Transaction(event, date, currencyCode, amount, new Participant(event,"name", "email", "q", "iban", "bic"), "Chicken-Burger");
        b.id = a.id;
        
        assertEquals(a, b);
        assertEquals(a.hashCode(), b.hashCode());
    }

    @Test
    public void hasToString() {
        Date date = new Date();
        BigDecimal amount = new BigDecimal("100.00");
        String currencyCode = "USD";
        Event event = new Event("event");
        Transaction transaction = new Transaction(event, date, currencyCode, amount, new Participant(event,"name", "email", "q", "iban", "bic"), "Chicken-Burger");

        String actual = transaction.toString();

        assertTrue(actual.contains("Transaction"));
        assertTrue(actual.contains("date=" + date));
        assertTrue(actual.contains("currencyCode=" + currencyCode));
        assertTrue(actual.contains("amount=" + amount));
    }

}
