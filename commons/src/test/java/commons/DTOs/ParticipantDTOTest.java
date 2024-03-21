package commons.DTOs;

import commons.Event;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ParticipantDTOTest {

    @Test
    void constructorTest() {
        commons.Participant participant = new commons.Participant(new Event("event"), "name", "surname", "mail", "iban", "bic");
        participant.id = new java.util.UUID(0, 1);
        ParticipantDTO participantDTO = new ParticipantDTO(participant);
        assertEquals(participant.getId(), participantDTO.id);
        assertEquals(participant.getFirstName(), participantDTO.firstName);
        assertEquals(participant.getLastName(), participantDTO.lastName);
        assertEquals(participant.getEmail(), participantDTO.email);
        assertEquals(participant.getIban(), participantDTO.iban);
        assertEquals(participant.getEvent().getId(), participantDTO.eventId);
    }

}