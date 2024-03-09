package commons.DTOs;

import commons.Event;
import org.junit.jupiter.api.Test;
import commons.Transaction;

import static org.junit.jupiter.api.Assertions.*;

class TransactionDTOTest {
    @Test
    void testConstructor() {
        Transaction t = new Transaction(new Event("event"), new java.util.Date(), "usd",
                new java.math.BigDecimal("0.00"), null, "Chicken-nuggets");
        TransactionDTO tDTO = new TransactionDTO(t);
        assertEquals(t.getId(), tDTO.id);
        assertEquals(t.getDate(), tDTO.date);
        assertEquals(t.getCurrencyCode(), tDTO.currencyCode);
        assertEquals(t.getAmount(), tDTO.amount);
        assertEquals(t.getAuthor(), tDTO.author);
        assertEquals(t.getParticipants(), tDTO.participants);
        assertEquals(t.getTags(), tDTO.tags);
        assertEquals(t.getEvent().getId(), tDTO.eventId);

    }

}