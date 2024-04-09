/*
 * Copyright 2021 Delft University of Technology
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package client.utils;

import client.UserData;
import commons.DTOs.*;
import jakarta.ws.rs.WebApplicationException;
import jakarta.ws.rs.client.*;
import jakarta.ws.rs.core.*;
import org.springframework.messaging.converter.MappingJackson2MessageConverter;
import org.springframework.messaging.simp.stomp.*;
import org.springframework.web.socket.client.standard.StandardWebSocketClient;
import org.springframework.web.socket.messaging.WebSocketStompClient;

import java.lang.reflect.Type;
import java.util.*;
import java.util.concurrent.ExecutionException;
import java.util.function.Consumer;

import static jakarta.ws.rs.core.MediaType.APPLICATION_JSON;

public class ServerUtils {
    //events
    //I think there is a problem with this method
    public EventDTO getEvent(UUID id) throws WebApplicationException {
        return ClientBuilder.newClient()
            .target(UserData.getInstance().getServerURL()).path("api/event/" + id)
            .request(APPLICATION_JSON)
            .get(EventDTO.class);
    }

    /**
     * Adds and event to the database.
     * @param event the event to be added
     * @return the event that was added
     */
    public EventDTO postEvent(EventDTO event) throws WebApplicationException {
        return ClientBuilder.newClient() //
                .target(UserData.getInstance().getServerURL()).path("api/event") //
                .request(APPLICATION_JSON) //
                .post(Entity.entity(event, APPLICATION_JSON), EventDTO.class);
    }

    public EventDTO putEvent(EventDTO eventDTO) throws WebApplicationException {
        return ClientBuilder.newClient()
            .target(UserData.getInstance().getServerURL())
                .path("/api/event/"+eventDTO.getId())
            .request(APPLICATION_JSON)
            .put(Entity.entity(eventDTO, APPLICATION_JSON), EventDTO.class);
    }

    public void deleteEvent(UUID id) throws WebApplicationException {
        ClientBuilder.newClient()
                .target(UserData.getInstance().getServerURL())
                .path("api/event/" + id)
                .request()
                .delete();

    }

    @SuppressWarnings("unchecked")
    public Collection<EventDTO> getAllEvents() throws WebApplicationException {
        return ClientBuilder.newClient()
                .target(UserData.getInstance().getServerURL()).path("api/event")
                .request(APPLICATION_JSON)
                .header("Authorization", "Bearer " + UserData.getInstance().getToken())
                .get(new GenericType<Collection<EventDTO>>() {});
    }


    //transactions
    public TransactionDTO getTransaction(UUID id) throws WebApplicationException {
        return ClientBuilder.newClient()
            .target(UserData.getInstance().getServerURL()).path("api/transaction/" + id)
            .request(APPLICATION_JSON)
            .get(TransactionDTO.class);
    }

    /**
     * Adds a transaction to the database.
     * @param ts the transaction to be added
     * @return the transaction that was added
     */
    public TransactionDTO postTransaction(TransactionDTO ts) throws WebApplicationException {
        return ClientBuilder.newClient() //
                .target(UserData.getInstance().getServerURL()).path("api/transaction") //
                .request(APPLICATION_JSON) //
                .post(Entity.entity(ts, APPLICATION_JSON), TransactionDTO.class);
    }

    public TransactionDTO putTransaction(TransactionDTO ts) throws WebApplicationException {
        return ClientBuilder.newClient()
                .target(UserData.getInstance().getServerURL()).path("api/transaction")
                .request(APPLICATION_JSON)
                .put(Entity.entity(ts, APPLICATION_JSON), TransactionDTO.class);
    }

    /**
     * deletes a transaction from the DB
     * @param id target for deletion
     * @throws WebApplicationException HTTP error respose (e.g. 404 - not found)
     */
    public void deleteTransaction(UUID id) throws WebApplicationException {
        ClientBuilder.newClient()
                .target(UserData.getInstance().getServerURL()).path("api/transaction/" + id)
                .request()
                .delete(); // Send DELETE request
    }

    //participants
    public ParticipantDTO getParticipant(UUID id) throws WebApplicationException {
        return ClientBuilder.newClient()
                .target(UserData.getInstance().getServerURL()).path("api/participants/" + id)
                .request(APPLICATION_JSON)
                .get(ParticipantDTO.class);
    }

    //create a gatParticipants method
    // //- returns a list of all participants from the event

    public ParticipantDTO postParticipant(ParticipantDTO p) throws WebApplicationException {
        return ClientBuilder.newClient()
                .target(UserData.getInstance().getServerURL()).path("api/participants")
                .request(APPLICATION_JSON)
                .post(Entity.entity(p, APPLICATION_JSON), ParticipantDTO.class);
    }

    public ParticipantDTO putParticipant(ParticipantDTO p) throws WebApplicationException {
        return ClientBuilder.newClient()
                .target(UserData.getInstance().getServerURL()).path("api/participants/")
                .request(APPLICATION_JSON)
                .put(Entity.entity(p, APPLICATION_JSON), ParticipantDTO.class);
    }

    public void deleteParticipant(UUID id) throws WebApplicationException {
        ClientBuilder.newClient()
            .target(UserData.getInstance().getServerURL()).path("api/participants/" + id)
            .request()
            .delete();
    }

    //tags
    public TagDTO getTag(UUID id) throws WebApplicationException {
        return null;
    }

    public TagDTO postTag(TagDTO t) throws WebApplicationException {
        return ClientBuilder.newClient()
            .target(UserData.getInstance().getServerURL()).path("api/tags/")
            .request(APPLICATION_JSON)
            .post(Entity.entity(t, APPLICATION_JSON), TagDTO.class);
    }

    public TagDTO putTag(TagDTO t) throws WebApplicationException {
        return null;
    }

    public void deleteTag(UUID id) throws WebApplicationException {
        ClientBuilder.newClient()
                .target(UserData.getInstance().getServerURL()).path("api/tags/" + id)
                .request()
                .delete();
    }

    //JSON
    public String getJSON() throws WebApplicationException {
        return ClientBuilder.newClient()
            .target(UserData.getInstance().getServerURL()).path("data/JSON")
            .request(APPLICATION_JSON)
            .header("Authorization", "Bearer " + UserData.getInstance().getToken())
            .get(String.class);
    }

    public String getJSON(UUID id) throws  WebApplicationException {
        return null;
    }

    public void putJSON(String json) throws WebApplicationException {

    }

    public void putJSON(String json, UUID id) throws WebApplicationException {

    }

    //Websockets
    private String getWebSocketURL() {
        String url = UserData.getInstance().getServerURL();
        url = url.replaceFirst("http", "ws");
        url = url + "/websocket";
        return url;
    }
    private StompSession session = connect(getWebSocketURL()) ;
    private StompSession connect (String url) {
        var client = new StandardWebSocketClient();
        var stomp = new WebSocketStompClient(client);
        stomp.setMessageConverter(new MappingJackson2MessageConverter());
        try {
            return stomp.connect(url, new StompSessionHandlerAdapter() {}).get();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        } catch (ExecutionException e){
            System.out.println("Error connecting to websocket");
            System.out.println(e.getMessage());
        }
        return null;
    }
    public void register(String dest, Consumer<EventDTO> consumer) {
        session.subscribe(dest, new StompFrameHandler() {
            @Override
            public Type getPayloadType(StompHeaders headers) {
                return EventDTO.class;
            }
            @Override
            public void handleFrame(StompHeaders headers, Object payload) {
                consumer.accept((EventDTO) payload) ;
            }
        });
    }

    public void register(String dest, Consumer<UUID> consumer, UUID id) {
        session.subscribe(dest, new StompFrameHandler() {
            @Override
            public Type getPayloadType(StompHeaders headers) {
                return UUID.class;
            }
            @Override
            public void handleFrame(StompHeaders headers, Object payload) {
                consumer.accept((UUID) payload) ;
            }
        });
    }

    //Testing
    public Response.StatusType reach(String url) {
        return ClientBuilder.newClient()
                .target(url).path("api/test/reach/")
                .request()
                .get().getStatusInfo();
    }
}