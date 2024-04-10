package client.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import commons.DTOs.EventDTO;
import commons.DTOs.ParticipantDTO;
import commons.DTOs.TagDTO;
import commons.DTOs.TransactionDTO;
import commons.Tag;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

public class EventJsonUtilTest {
    private ServerUtils serverUtils;
    private EventJsonUtil jsonUtil;
    private EventDTO eventDTO;

    @BeforeEach
    public void setup() {
        serverUtils = mock(ServerUtils.class);
        jsonUtil = new EventJsonUtil(serverUtils);
        eventDTO = new EventDTO(UUID.randomUUID(), "event");

        when(serverUtils.getEvent(any(UUID.class))).thenReturn(null);
        when(serverUtils.getEvent(eventDTO.id)).thenReturn(eventDTO);
        when(serverUtils.putEvent(any(EventDTO.class))).thenAnswer(inv -> inv.getArguments()[0]);
    }

    @Test
    public void simpleEventSuccessTest() throws Exception {
        EventDTO result = jsonUtil.putJSON(jsonUtil.getJson(eventDTO.id));
        assertEquals(eventDTO, result);
    }

    @Test
    public void simpleEventFailTest() {
        assertThrows(JsonProcessingException.class, () -> jsonUtil.putJSON("blabla"));
    }

    @Test
    public void complexEventSuccessTest() throws Exception {
        TagDTO tag = new TagDTO(UUID.randomUUID(), eventDTO.id, "food", Tag.Color.BLUE);
        eventDTO.tags.add(tag);

        ParticipantDTO participant = new ParticipantDTO(UUID.randomUUID(),
            eventDTO.id, "Max", "Well", "mw@me.com", "-", "-");
        eventDTO.participants.add(participant);

        eventDTO.transactions.add(new TransactionDTO(UUID.randomUUID(), eventDTO.id, new Date(),
            "EUR", BigDecimal.valueOf(14.99d), participant, new HashSet<>(List.of(participant)),
            new HashSet<>(List.of(tag)), "Burgers"));

        EventDTO result = jsonUtil.putJSON(jsonUtil.getJson(eventDTO.id));
        assertEquals(eventDTO, result);
    }
}
