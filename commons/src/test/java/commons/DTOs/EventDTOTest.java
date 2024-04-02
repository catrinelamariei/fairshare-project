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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

class EventDTOTest {
    @Test
    void constructorTest() {
        EventDTO eventDTO;
        Event event = new Event("event");
        event.id = new UUID(0, 1);
        Tag tag = new Tag(event, "tag", Tag.Color.BLUE);
        Participant participant = new Participant(event,"name", "surname", "mail", "iban", "bic");
        Transaction transaction = new Transaction(event, new Date(), "usd",
                new BigDecimal("0.00"), participant, "big-mac");
        event.addParticipant(participant);
        event.addTransaction(transaction);

        eventDTO = new EventDTO(event);
        assertEquals(event.getId(), eventDTO.id);
        assertEquals(event.getName(), eventDTO.name);
        assertEquals(event.getCreationDate(), eventDTO.date);
        assertEquals(event.getTags().size(), eventDTO.tags.size());
        assertEquals(event.getParticipants().size(), eventDTO.participants.size());
        assertEquals(event.getTransactions().size(), eventDTO.transactions.size());
    }

    @Test
    void constructorTest2() {
        EventDTO eventDTO;
        UUID id = new UUID(0, 1);
        String name = "name";
        eventDTO = new EventDTO(id, name);
        assertEquals(id, eventDTO.id);
        assertEquals(name, eventDTO.name);
        assertEquals(0, eventDTO.tags.size());
        assertEquals(0, eventDTO.participants.size());
        assertEquals(0, eventDTO.transactions.size());
    }

    @Test
    void getters(){
        EventDTO eventDTO = new EventDTO();
        UUID id = new UUID(0, 1);
        String name = "name";
        Set<TagDTO> tags = new HashSet<>();
        Date date = new Date();

        eventDTO.id = id;
        eventDTO.name = name;
        eventDTO.tags = tags;

        assertEquals(id, eventDTO.getId());
        assertEquals(name, eventDTO.getName());
        assertEquals(date, eventDTO.getDate());

    }

    @Test
    void equals(){
        EventDTO eventDTO = new EventDTO();
        EventDTO eventDTO2 = new EventDTO();
        EventDTO eventDTO3 = new EventDTO(new UUID(0, 1), "name");
        EventDTO eventDTO4 = new EventDTO(new UUID(0, 1), "name");
        EventDTO eventDTO5 = new EventDTO(new UUID(0, 1), "name");
        eventDTO5.tags.add(new TagDTO());
        EventDTO eventDTO6 = new EventDTO(new UUID(0, 1), "name");
        eventDTO6.participants.add(new ParticipantDTO());
        EventDTO eventDTO7 = new EventDTO(new UUID(0, 1), "name");
        eventDTO7.transactions.add(new TransactionDTO());

        assertEquals(eventDTO, eventDTO);
        assertEquals(eventDTO, eventDTO2);
        assertNotEquals(eventDTO, null);
        assertNotEquals(eventDTO, new TagDTO());
        assertNotEquals(eventDTO, eventDTO3);
        assertEquals(eventDTO3, eventDTO4);
        assertNotEquals(eventDTO4, eventDTO5);
        assertNotEquals(eventDTO5, eventDTO6);
        assertNotEquals(eventDTO6, eventDTO7);
    }

    @Test
    void hash(){
        EventDTO eventDTO3 = new EventDTO(new UUID(0, 1), "name");
        EventDTO eventDTO4 = new EventDTO(new UUID(0, 1), "name");

        assertEquals(eventDTO3.hashCode(), eventDTO4.hashCode());
    }


}