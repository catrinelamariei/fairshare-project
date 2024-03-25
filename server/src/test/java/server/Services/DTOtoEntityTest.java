package server.Services;

import commons.*;
import commons.DTOs.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import server.database.*;

import java.math.BigDecimal;
import java.util.Date;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class DTOtoEntityTest {

    DTOtoEntity d2e;
    TagRepository tagRepository;
    EventRepository eventRepository;
    TransactionRepository transactionRepository;
    ParticipantRepository participantRepository;



    @BeforeEach
    void setUp() {
        tagRepository = mock(TagRepository.class);
        eventRepository = mock(EventRepository.class);
        transactionRepository = mock(TransactionRepository.class);
        participantRepository = mock(ParticipantRepository.class);
        d2e = new DTOtoEntity(eventRepository, transactionRepository, tagRepository, participantRepository);
    }


    @Test
    void getEvent() {
        Event event = new Event("event");
        event.id = new UUID(0, 1);
        EventDTO eventDTO = new EventDTO(event);
        when(eventRepository.getReferenceById(eventDTO.id)).thenReturn(event);
        assertEquals(event, d2e.get(eventDTO));
    }

    @Test
    void createEvent() {
        EventDTO eventDTO = new EventDTO(UUID.randomUUID(), "event");
        Event event = new Event(eventDTO.getName());
        when(tagRepository.save(any(Tag.class))).thenReturn(new Tag(event, "tag", Tag.Color.BLUE));
        when(eventRepository.save(event)).thenReturn(event);
        assertEquals(event, d2e.create(eventDTO));
    }

    @Test
    void updateEvent() {
        Event event = new Event("event");
        event.id = new UUID(0, 1);
        EventDTO eventDTO = new EventDTO(event);
        eventDTO.name = "new name";
        event.setName("new name");
        when(eventRepository.getReferenceById(eventDTO.id)).thenReturn(event);
        when(eventRepository.save(event)).thenReturn(event);
        assertEquals(event, d2e.update(eventDTO));
    }

    @Test
    void deleteEvent() {
        Event event = new Event("event");
        event.id = new UUID(0, 1);
        EventDTO eventDTO = new EventDTO(event);
        when(eventRepository.existsById(eventDTO.id)).thenReturn(true);
        assertTrue(d2e.delete(eventDTO));
    }

    @Test
    void deleteEventNotFound() {
        Event event = new Event("event");
        event.id = new UUID(0, 1);
        EventDTO eventDTO = new EventDTO(event);
        when(eventRepository.existsById(eventDTO.id)).thenReturn(false);
        assertFalse(d2e.delete(eventDTO));
    }

    @Test
    void getTransaction() {
        Event event = new Event("event");
        event.id = new UUID(0, 1);
        Participant participant = new Participant(event, "name", "surname", "mail", "iban", "bic");
        participant.id = new UUID(0, 2);
        Transaction transaction = new Transaction(event, new Date(), "usd", new BigDecimal(100), participant, "Subject");
        transaction.id = new UUID(0, 3);
        TransactionDTO transactionDTO = new TransactionDTO(transaction);
        when(transactionRepository.getReferenceById(transaction.id)).thenReturn(transaction);
        assertEquals(transaction, d2e.get(transactionDTO));
    }

    @Test
    void createTransaction() {
        Event event = new Event("event");
        event.id = new UUID(0, 1);
        Participant participant = new Participant(event, "name", "surname", "mail", "iban", "bic");
        participant.id = new UUID(0, 2);
        Transaction transaction = new Transaction(event, new Date(), "usd", new BigDecimal(100), participant, "Subject");
        transaction.id = new UUID(0, 2);
        TransactionDTO transactionDTO = new TransactionDTO(transaction);
        when(eventRepository.getReferenceById(transactionDTO.eventId)).thenReturn(event);
        when(participantRepository.getReferenceById(transaction.author.id)).thenReturn(participant);
        when(transactionRepository.save(transaction)).thenReturn(transaction);
        assertEquals(transaction, d2e.create(transactionDTO));
    }

    @Test
    void updateTransaction() {
Event event = new Event("event");
        event.id = new UUID(0, 1);
        Participant participant = new Participant(event, "name", "surname", "mail", "iban", "bic");
        participant.id = new UUID(0, 2);
        Transaction transaction = new Transaction(event, new Date(), "usd", new BigDecimal(100), participant, "Subject");
        transaction.id = new UUID(0, 2);
        TransactionDTO transactionDTO = new TransactionDTO(transaction);
        transactionDTO.subject = "new subject";
        transaction.subject = "new subject";
        when(transactionRepository.getReferenceById(transactionDTO.id)).thenReturn(transaction);
        when(transactionRepository.save(transaction)).thenReturn(transaction);
        assertEquals(transaction, d2e.update(transactionDTO));

        transaction.participants.add(participant);
        transactionDTO.participants.add(new ParticipantDTO(participant));
        when(participantRepository.getReferenceById(participant.id)).thenReturn(participant);
        assertEquals(transaction, d2e.update(transactionDTO));
    }

    @Test
    void getParticipant() {
        Event event = new Event("event");
        event.id = new UUID(0, 1);
        Participant participant = new Participant(event, "name", "surname", "mail", "iban", "bic");
        participant.id = new UUID(0, 2);
        ParticipantDTO participantDTO = new ParticipantDTO(participant);
        when(participantRepository.getReferenceById(participantDTO.id)).thenReturn(participant);
        assertEquals(participant, d2e.get(participantDTO));
    }

    @Test
    void createParticipant() {
        Event event = new Event("event");
        event.id = new UUID(0, 1);
        Participant participant = new Participant(event, "name", "surname", "mail", "iban", "bic");
        participant.id = new UUID(0, 2);
        ParticipantDTO participantDTO = new ParticipantDTO(participant);
        when(eventRepository.getReferenceById(participantDTO.eventId)).thenReturn(event);
        when(participantRepository.save(participant)).thenReturn(participant);
        assertEquals(participant, d2e.create(participantDTO));
    }

    @Test
    void updateParticipant() {
        Event event = new Event("event");
        event.id = new UUID(0, 1);
        Participant participant = new Participant(event, "name", "surname", "mail", "iban", "bic");
        participant.id = new UUID(0, 2);
        ParticipantDTO participantDTO = new ParticipantDTO(participant);
        participantDTO.firstName = "new name";
        participant.firstName = "new name";
        when(participantRepository.getReferenceById(participantDTO.getId())).thenReturn(participant);
        when(participantRepository.save(participant)).thenReturn(participant);
        assertEquals(participant, d2e.update(participantDTO));
    }

    @Test
    void getTag() {
        Event event = new Event("event");
        event.id = new UUID(0, 1);
        Tag tag = new Tag(event, "tag", Tag.Color.BLUE);
        tag.id = new UUID(0, 2);
        TagDTO tagDTO = new TagDTO(tag);
        when(tagRepository.getReferenceById(tagDTO.id)).thenReturn(tag);
        assertEquals(tag, d2e.get(tagDTO));
    }

    @Test
    void createTag() {
        Event event = new Event("event");
        event.id = new UUID(0, 1);
        TagDTO tagDTO = new TagDTO();
        tagDTO.eventId = event.id;
        tagDTO.name = "tag";
        tagDTO.color = Tag.Color.BLUE;
        Event expectedEvent = new Event("event");
        expectedEvent.id = new UUID(0, 1);
        Tag expected = new Tag(expectedEvent, "tag", Tag.Color.BLUE);
        expected.id = new UUID(0, 2);

        when(eventRepository.getReferenceById(tagDTO.eventId)).thenReturn(event);
        when(tagRepository.save(any())).thenReturn(expected);
        assertEquals(expected, d2e.create(tagDTO));
    }

    @Test
    void updateTag() {
        Event event = new Event("event");
        event.id = new UUID(0, 1);
        Tag tag = new Tag(event, "tag", Tag.Color.BLUE);
        tag.id = new UUID(0, 2);
        TagDTO tagDTO = new TagDTO(tag);
        tagDTO.name = "new name";
        tag.name = "new name";
        when(tagRepository.getReferenceById(tagDTO.id)).thenReturn(tag);
        when(tagRepository.save(tag)).thenReturn(tag);
        assertEquals(tag, d2e.update(tagDTO));
    }
}