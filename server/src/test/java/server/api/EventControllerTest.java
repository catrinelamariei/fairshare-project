package server.api;

import commons.DTOs.*;
import commons.Tag;
import commons.*;
import org.junit.jupiter.api.*;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import server.Services.DTOtoEntity;
import server.database.EventRepository;

import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

public class EventControllerTest {
    EventController controller;
    EventRepository repo = mock(EventRepository.class);
    DTOtoEntity d2e = mock(DTOtoEntity.class);
    SimpMessagingTemplate smtMock = mock(SimpMessagingTemplate.class);
    Event event;
    EventDTO eventDTO;

    @BeforeEach
    public void setUp() {
        controller = new EventController(repo, d2e, smtMock);
        event = new Event("event");
        event.id = java.util.UUID.randomUUID();
        eventDTO = new EventDTO(event);
    }

    @Test
    public void getEvent() {
        when(repo.existsById(event.id)).thenReturn(true);
        when(repo.getReferenceById(event.id)).thenReturn(event);
        ResponseEntity<EventDTO> response = controller.getEvent(event.id);
        assertEquals(eventDTO, response.getBody());
    }

    @Test
    public void getEventNotFound() {
        when(repo.existsById(event.id)).thenReturn(false);
        ResponseEntity<EventDTO> response = controller.getEvent(event.id);
        assertEquals(ResponseEntity.notFound().build(), response);
    }

    @Test
    public void createEvent() {
        eventDTO.id = null;
        when(d2e.create(eventDTO)).thenReturn(event);
        doNothing().when(smtMock).convertAndSend("/topic/events", eventDTO);
        ResponseEntity<EventDTO> response = controller.createEvent(eventDTO);
        eventDTO.id = event.id;
        assertEquals(eventDTO, response.getBody());
        verify(smtMock).convertAndSend("/topic/events", eventDTO);
    }

    @Test
    public void updateEvent() {
        when(repo.existsById(event.id)).thenReturn(true);
        when(d2e.update(eventDTO)).thenReturn(event);
        doNothing().when(smtMock).convertAndSend("/topic/events", eventDTO);
        ResponseEntity<EventDTO> response = controller.renameEventName(event.id, eventDTO);
        verify(d2e).update(eventDTO);
        assertEquals(eventDTO, response.getBody());
        verify(smtMock).convertAndSend("/topic/events", eventDTO);
    }

    @Test
    public void updateEventNotFound() {
        when(repo.existsById(event.id)).thenReturn(false);
        ResponseEntity<EventDTO> response = controller.renameEventName(event.id, eventDTO);
        assertEquals(ResponseEntity.notFound().build(), response);
    }

    @Test
    public void updateEventBadRequest() {
        ResponseEntity<EventDTO> response = controller.renameEventName(null, eventDTO);
        assertEquals(ResponseEntity.badRequest().build(), response);

        response = controller.renameEventName(event.id, null);
        assertEquals(ResponseEntity.badRequest().build(), response);

        response = controller.renameEventName(null, null);
        assertEquals(ResponseEntity.badRequest().build(), response);

        eventDTO.name = null;
        response = controller.renameEventName(event.id, eventDTO);
        assertEquals(ResponseEntity.badRequest().build(), response);

        eventDTO.name = "";
        response = controller.renameEventName(event.id, eventDTO);
        assertEquals(ResponseEntity.badRequest().build(), response);
    }

    @Test
    public void deleteEvent() {
        when(repo.existsById(event.id)).thenReturn(true);
        when(repo.getReferenceById(event.id)).thenReturn(event);
        doNothing().when(smtMock).convertAndSend("/topic/deletedEvent", eventDTO);
        ResponseEntity response = controller.deleteEvent(event.id);
        assertEquals(ResponseEntity.ok().build(), response);
        verify(smtMock).convertAndSend("/topic/deletedEvent", eventDTO);
    }

    @Test
    public void deleteEventBadRequest() {
        ResponseEntity response = controller.deleteEvent(null);
        assertEquals(ResponseEntity.badRequest().build(), response);
    }

    @Test
    public void deleteEventNotFound() {
        when(repo.existsById(event.id)).thenReturn(false);
        ResponseEntity response = controller.deleteEvent(event.id);
        assertEquals(ResponseEntity.notFound().build(), response);
    }

    @Test
    public void getEventTagsById() {
        when(repo.existsById(event.id)).thenReturn(true);
        when(repo.getReferenceById(event.id)).thenReturn(event);
        ResponseEntity<Set<TagDTO>> response = controller.getEventTagsById(event.id);
        assertEquals(Collections.emptySet(), response.getBody());

        commons.Tag tag = new commons.Tag(event, "tag", Tag.Color.GREEN);
        event.addTag(tag);
        eventDTO.tags.add(new TagDTO(tag));
        response = controller.getEventTagsById(event.id);
        assertEquals(java.util.Collections.singleton(new TagDTO(tag)), response.getBody());
    }

    @Test
    public void getEventTagsByIdNotFound() {
        when(repo.existsById(event.id)).thenReturn(false);
        ResponseEntity<Set<TagDTO>> response = controller.getEventTagsById(event.id);
        assertEquals(ResponseEntity.notFound().build(), response);
    }

}
