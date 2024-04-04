package server.api;

import commons.DTOs.EventDTO;
import commons.DTOs.ParticipantDTO;
import commons.Event;
import commons.Participant;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import server.Services.DTOtoEntity;
import server.database.ParticipantRepository;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.NOT_FOUND;

import static org.mockito.Mockito.*;

public class ParticipantControllerTest {
    private ParticipantController controller;
    private ParticipantRepository repo = mock(ParticipantRepository.class);
    private DTOtoEntity d2e = mock(DTOtoEntity.class);
    private ParticipantDTO participantDTO;
    private Participant participant;

    @BeforeEach
    public void setUp() {
        controller = new ParticipantController(repo,d2e,null);
        Event event = new Event("event");
        event.id = UUID.randomUUID();
        participant = new Participant(event, "participant","last name", "email", "iban","bic");
        participant.id = UUID.randomUUID();
        participantDTO = new ParticipantDTO(participant);
        when(d2e.get(any(EventDTO.class))).thenReturn(new Event("event"));
    }

    @Test
    public void getParticipant() {
        when(repo.existsById(participant.id)).thenReturn(true);
        when(repo.getReferenceById(participant.id)).thenReturn(participant);
        ResponseEntity<ParticipantDTO> response = controller.getParticipant(participant.id);
        assertEquals(participantDTO, response.getBody());
    }

    @Test
    public void getParticipantNotFound() {
        when(repo.existsById(participant.id)).thenReturn(false);
        ResponseEntity<ParticipantDTO> response = controller.getParticipant(participant.id);
        assertEquals(NOT_FOUND, response.getStatusCode());
    }

    @Test
    public void createParticipant() {
        participantDTO.id = null;
        when(d2e.create(participantDTO)).thenReturn(participant);
        ResponseEntity<ParticipantDTO> response = controller.createParticipant(participantDTO);
        participantDTO.id = participant.id;
        assertEquals(participantDTO, response.getBody());
    }

    @Test
    public void createParticipantBadRequest() {
        participantDTO.id = null;
        participantDTO.firstName = null;
        ResponseEntity<ParticipantDTO> response = controller.createParticipant(participantDTO);
        assertEquals(BAD_REQUEST, response.getStatusCode());

        participantDTO.firstName = "";
        ResponseEntity<ParticipantDTO> response2 = controller.createParticipant(participantDTO);
        assertEquals(BAD_REQUEST, response2.getStatusCode());

        participantDTO.firstName = "first name";
        participantDTO.lastName = null;
        ResponseEntity<ParticipantDTO> response3 = controller.createParticipant(participantDTO);
        assertEquals(BAD_REQUEST, response3.getStatusCode());

        participantDTO.lastName = "";
        ResponseEntity<ParticipantDTO> response4 = controller.createParticipant(participantDTO);
        assertEquals(BAD_REQUEST, response4.getStatusCode());

        participantDTO.lastName = "last name";
        participantDTO.email = null;
        ResponseEntity<ParticipantDTO> response5 = controller.createParticipant(participantDTO);
        assertEquals(BAD_REQUEST, response5.getStatusCode());

        participantDTO.email = "";
        ResponseEntity<ParticipantDTO> response6 = controller.createParticipant(participantDTO);
        assertEquals(BAD_REQUEST, response6.getStatusCode());

        participantDTO.email = "email";
        participantDTO.iban = null;
        ResponseEntity<ParticipantDTO> response7 = controller.createParticipant(participantDTO);
        assertEquals(BAD_REQUEST, response7.getStatusCode());

        participantDTO.iban = "iban";
        participantDTO.bic = null;
        ResponseEntity<ParticipantDTO> response8 = controller.createParticipant(participantDTO);
        assertEquals(BAD_REQUEST, response8.getStatusCode());

        participantDTO.bic = "bic";
        participantDTO.eventId = null;
        ResponseEntity<ParticipantDTO> response9 = controller.createParticipant(participantDTO);
        assertEquals(BAD_REQUEST, response9.getStatusCode());
    }

    @Test
    public void updateParticipant() {
        when(repo.existsById(participant.id)).thenReturn(true);
        when(d2e.update(participantDTO)).thenReturn(participant);
        ResponseEntity<ParticipantDTO> response = controller.updateParticipant(participant.id, participantDTO);
        assertEquals(participantDTO, response.getBody());
    }

    @Test
    public void updateParticipantBadRequest() {
        when(repo.existsById(participant.id)).thenReturn(true);
        participantDTO.firstName = null;
        ResponseEntity<ParticipantDTO> response = controller.updateParticipant(participant.id, participantDTO);
        assertEquals(BAD_REQUEST, response.getStatusCode());

        participantDTO.firstName = "";
        ResponseEntity<ParticipantDTO> response2 = controller.updateParticipant(participant.id, participantDTO);
        assertEquals(BAD_REQUEST, response2.getStatusCode());

        participantDTO.firstName = "first name";
        participantDTO.lastName = null;
        ResponseEntity<ParticipantDTO> response3 = controller.updateParticipant(participant.id, participantDTO);
        assertEquals(BAD_REQUEST, response3.getStatusCode());

        participantDTO.lastName = "";
        ResponseEntity<ParticipantDTO> response4 = controller.updateParticipant(participant.id, participantDTO);
        assertEquals(BAD_REQUEST, response4.getStatusCode());

        participantDTO.lastName = "last name";
        participantDTO.email = null;
        ResponseEntity<ParticipantDTO> response5 = controller.updateParticipant(participant.id, participantDTO);
        assertEquals(BAD_REQUEST, response5.getStatusCode());

        participantDTO.email = "";
        ResponseEntity<ParticipantDTO> response6 = controller.updateParticipant(participant.id, participantDTO);
        assertEquals(BAD_REQUEST, response6.getStatusCode());

        participantDTO.email = "email";
        participantDTO.iban = null;
        ResponseEntity<ParticipantDTO> response7 = controller.updateParticipant(participant.id, participantDTO);
        assertEquals(BAD_REQUEST, response7.getStatusCode());

        participantDTO.iban = "iban";
        participantDTO.bic = null;
        ResponseEntity<ParticipantDTO> response9 = controller.updateParticipant(participant.id, participantDTO);
        assertEquals(BAD_REQUEST, response9.getStatusCode());


        participantDTO.bic = "bic";
        participantDTO.eventId = null;
        ResponseEntity<ParticipantDTO> response11 = controller.updateParticipant(participant.id, participantDTO);
        assertEquals(BAD_REQUEST, response11.getStatusCode());

    }

    @Test
    public void deleteParticipant() {
        when(repo.existsById(participant.id)).thenReturn(true);
        when(repo.findById(participant.id)).thenReturn(java.util.Optional.of(participant));
        ResponseEntity response = controller.deleteParticipant(participant.id);
        assertEquals(response.getStatusCode(), HttpStatus.OK);
    }

    @Test
    public void deleteParticipantNotFound() {
        when(repo.existsById(participant.id)).thenReturn(false);
        ResponseEntity response = controller.deleteParticipant(participant.id);
        assertEquals(response.getStatusCode(), NOT_FOUND);
    }

    @Test
    public void deleteParticipantBadRequest() {
        ResponseEntity response = controller.deleteParticipant(null);
        assertEquals(response.getStatusCode(), BAD_REQUEST);
    }

}