package commons.DTOs;

import commons.Event;
import commons.Participant;
import commons.Tag;
import commons.Transaction;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class EventDTOTest {
    @Test
    void constructorTest() {
        EventDTO eventDTO;
        Event event = new Event("event");
        event.id = new UUID(0, 1);
        Tag tag = new Tag(event, "tag", Tag.Color.BLUE);
        Participant participant = new Participant(event,"name", "surname", "mail", "iban");
        Transaction transaction = new Transaction(event, new Date(), "usd", new BigDecimal("0.00"), participant);


        eventDTO = new EventDTO(event);
        assertEquals(event.getId(), eventDTO.id);
        assertEquals(event.getName(), eventDTO.name);
        assertEquals(event.getCreationDate(), eventDTO.date);
        assertEquals(event.getTags().size(), eventDTO.tags.size());
        assertEquals(event.getParticipants().size(), eventDTO.participants.size());
        assertEquals(event.getTransactions().size(), eventDTO.transactions.size());
    }


}