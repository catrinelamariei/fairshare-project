package client.utils.EventJson;

import client.utils.ServerUtils;
import com.google.inject.Inject;
import commons.DTOs.EventDTO;

import java.util.UUID;

public class EventJsonUtil {
    private final ServerUtils server;

    @Inject
    private EventJsonUtil(ServerUtils serverUtils) {
        this.server = serverUtils;
    }

    public String getJson(UUID id) {
        System.out.println("Gen JSON");
        return null;
        //TODO
    }

    public EventDTO postJson(String json) {
        System.out.println("Post JSON");
        return null;
        //TODO
    }
}
