package server.Services;

import commons.*;
import commons.DTOs.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import server.database.*;

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
}