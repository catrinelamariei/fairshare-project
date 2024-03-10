package commons.DTOs;

import commons.Event;
import commons.Participant;
import org.junit.jupiter.api.Test;
import commons.Transaction;

import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;

class TransactionDTOTest {
    @Test
    void testConstructor() {
        Event event = new Event("event");
        Transaction t = new Transaction(event, new java.util.Date(), "usd",
                new java.math.BigDecimal("0.00"), new Participant(event, "Bo", "Echt", "mail", "FR4321"), "Chicken-nuggets");
        TransactionDTO tDTO = new TransactionDTO(t);
        assertEquals(t.getId(), tDTO.id);
        assertEquals(t.getDate(), tDTO.date);
        assertEquals(t.getCurrencyCode(), tDTO.currencyCode);
        assertEquals(t.getAmount(), tDTO.amount);
        assertEquals(new ParticipantDTO(t.getAuthor()), tDTO.author);
        assertEquals(t.getParticipants().stream().map(ParticipantDTO::new).collect(Collectors.toSet()),
                tDTO.participants);
        assertEquals(t.getTags().stream().map(TagDTO::new).collect(Collectors.toSet()), tDTO.tags);
        assertEquals(t.getEvent().getId(), tDTO.eventId);

    }

}