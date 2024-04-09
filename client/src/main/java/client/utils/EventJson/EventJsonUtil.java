package client.utils.EventJson;

import client.utils.ServerUtils;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.google.inject.Inject;
import commons.DTOs.EventDTO;

import java.util.UUID;

/**
 * service for creation and delivery of JSON to/from server/filesystem
 */
public class EventJsonUtil {
    private final ServerUtils server;
    private final ObjectMapper objectMapper = new ObjectMapper()
        .enable(SerializationFeature.INDENT_OUTPUT);

    @Inject
    public EventJsonUtil(ServerUtils serverUtils) {
        this.server = serverUtils;
    }

    /**
     * gets an eventDTO and turns it into JSON
     * @param id UUID of event to be fetched
     * @return string JSON representation of eventDTO
     */
    public String getJson(UUID id) {
        try {
            EventDTO event = server.getEvent(id);
            return objectMapper.writeValueAsString(event);
        } catch (JsonProcessingException e) {
            System.err.println(e);
            return null;
        }
    }

    /**
     * creates event, sends to server
     * @param json string of eventDTO json
     * @return eventDTO as returned by server
     */
    public EventDTO postJson(String json) {
        System.out.println("Post JSON");
        return null;
        //TODO
    }
}
