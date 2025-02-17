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
import com.google.inject.Inject;
import commons.DTOs.*;
import jakarta.ws.rs.*;
import jakarta.ws.rs.client.*;
import jakarta.ws.rs.core.*;
import org.springframework.http.HttpStatus;
import org.springframework.messaging.converter.MappingJackson2MessageConverter;
import org.springframework.messaging.simp.stomp.*;
import org.springframework.web.socket.client.standard.StandardWebSocketClient;
import org.springframework.web.socket.messaging.WebSocketStompClient;

import java.lang.reflect.Type;
import java.util.*;
import java.util.concurrent.*;
import java.util.function.Consumer;

import static jakarta.ws.rs.core.MediaType.APPLICATION_JSON;

public class ServerUtils {
    private final UserData userData;
    private StompSession session;

    @Inject
    private ServerUtils(UserData userData) {
        this.userData = userData;
        this.session =  connect(getWebSocketURL());
    }

    //events
    //I think there is a problem with this method
    public EventDTO getEvent(UUID id) throws WebApplicationException {
        return ClientBuilder.newClient()
            .target(userData.getServerURL()).path("api/event/" + id)
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
                .target(userData.getServerURL()).path("api/event") //
                .request(APPLICATION_JSON) //
                .post(Entity.entity(event, APPLICATION_JSON), EventDTO.class);
    }

    public EventDTO patchEvent(EventDTO eventDTO) throws WebApplicationException {
        return ClientBuilder.newClient()
            .target(userData.getServerURL())
                .path("/api/event/"+eventDTO.getId())
            .request(APPLICATION_JSON)
            .put(Entity.entity(eventDTO, APPLICATION_JSON), EventDTO.class);
    }

    public EventDTO putEvent(EventDTO event) throws WebApplicationException {
        return ClientBuilder.newClient() //
            .target(userData.getServerURL()).path("api/event") //
            .request(APPLICATION_JSON) //
            .put(Entity.entity(event, APPLICATION_JSON), EventDTO.class);
    }

    public void deleteEvent(UUID id) throws WebApplicationException {
        ClientBuilder.newClient()
                .target(userData.getServerURL())
                .path("api/event/" + id)
                .request()
                .delete();

    }

    @SuppressWarnings("unchecked")
    public Collection<EventDTO> getAllEvents() throws WebApplicationException {
        return ClientBuilder.newClient()
                .target(userData.getServerURL()).path("api/event")
                .request(APPLICATION_JSON)
                .header("Authorization", "Bearer " + userData.getToken())
                .get(new GenericType<Collection<EventDTO>>() {});
    }


    //transactions
    public TransactionDTO getTransaction(UUID id) throws WebApplicationException {
        return ClientBuilder.newClient()
            .target(userData.getServerURL()).path("api/transaction/" + id)
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
                .target(userData.getServerURL()).path("api/transaction") //
                .request(APPLICATION_JSON) //
                .post(Entity.entity(ts, APPLICATION_JSON), TransactionDTO.class);
    }

    public TransactionDTO putTransaction(TransactionDTO ts) throws WebApplicationException {
        return ClientBuilder.newClient()
                .target(userData.getServerURL()).path("api/transaction")
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
                .target(userData.getServerURL()).path("api/transaction/" + id)
                .request()
                .delete(); // Send DELETE request
    }

    //participants
    public ParticipantDTO getParticipant(UUID id) throws WebApplicationException {
        return ClientBuilder.newClient()
                .target(userData.getServerURL()).path("api/participants/" + id)
                .request(APPLICATION_JSON)
                .get(ParticipantDTO.class);
    }

    //create a gatParticipants method
    // //- returns a list of all participants from the event

    public ParticipantDTO postParticipant(ParticipantDTO p) throws WebApplicationException {
        return ClientBuilder.newClient()
                .target(userData.getServerURL()).path("api/participants")
                .request(APPLICATION_JSON)
                .post(Entity.entity(p, APPLICATION_JSON), ParticipantDTO.class);
    }

    public ParticipantDTO putParticipant(ParticipantDTO p) throws WebApplicationException {
        return ClientBuilder.newClient()
                .target(userData.getServerURL()).path("api/participants/")
                .request(APPLICATION_JSON)
                .put(Entity.entity(p, APPLICATION_JSON), ParticipantDTO.class);
    }

    public void deleteParticipant(UUID id) throws WebApplicationException {
        ClientBuilder.newClient()
            .target(userData.getServerURL()).path("api/participants/" + id)
            .request()
            .delete();
    }

    //tags
    public TagDTO getTag(UUID id) throws WebApplicationException {
        return null;
    }

    public TagDTO postTag(TagDTO t) throws WebApplicationException {
        return ClientBuilder.newClient()
            .target(userData.getServerURL()).path("api/tags/")
            .request(APPLICATION_JSON)
            .post(Entity.entity(t, APPLICATION_JSON), TagDTO.class);
    }

    public TagDTO putTag(TagDTO t) throws WebApplicationException {
        return null;
    }

    public void deleteTag(UUID id) throws WebApplicationException {
        ClientBuilder.newClient()
                .target(userData.getServerURL()).path("api/tags/" + id)
                .request()
                .delete();
    }

    //JSON
    public String getJSON() throws WebApplicationException {
        return ClientBuilder.newClient()
            .target(userData.getServerURL()).path("data/JSON")
            .request(APPLICATION_JSON)
            .header("Authorization", "Bearer " + userData.getToken())
            .get(String.class);
    }

    public String getJSON(UUID id) throws  WebApplicationException {
        return null;
    }

    public void putJSON(String json) throws WebApplicationException {

    }

    public void putJSON(String json, UUID id) throws WebApplicationException {

    }

    //ADMIN
    public Response.StatusType adminReqCode() {
        return ClientBuilder.newClient()
                .target(userData.getServerURL()).path("/admin")
                .request().get().getStatusInfo();
    }

    public Response adminReqToken(String code) {
        return ClientBuilder.newClient()
                .target(userData.getServerURL()).path("/admin")
                .request(APPLICATION_JSON)
                .post(Entity.entity(code, APPLICATION_JSON));
    }

//////////////////////////////////////
    public static ExecutorService transactionEXEC;
    public void registerForUpdatesTransaction(Consumer<TransactionDTO> consumer){
        transactionEXEC = Executors.newSingleThreadExecutor();
        transactionEXEC.submit(()->{
            while(!Thread.interrupted()) {
                var res = ClientBuilder.newClient()
                        .target(userData.getServerURL())
                        .path("api/transaction/updates")
                        .request(APPLICATION_JSON)
                        .get(Response.class);
                if(res.getStatus()==204){
                    continue;
                }
                var t = res.readEntity(TransactionDTO.class);
                consumer.accept(t);
            }
        });
    }


    private static ExecutorService execUpdateParticipant;
    public void registerForUpdatesParticipant(Consumer<ParticipantDTO> consumer) {
        execUpdateParticipant = Executors.newSingleThreadExecutor();
        execUpdateParticipant.submit(() -> {
            while (!Thread.interrupted()) {
                var res = ClientBuilder.newClient()
                        .target(userData.getServerURL())
                        .path("/api/participants/updates")
                        .request(APPLICATION_JSON)
                        .get(Response.class);
                if (res.getStatus() == 204) {
                    continue;
                }
                var participant = res.readEntity(ParticipantDTO.class);
                consumer.accept(participant);
            }
        });
    }

    private static ExecutorService execDeleteParticipant;


    public void registerForParticipantDeletionUpdates(Consumer<UUID> listener) {
        execDeleteParticipant = Executors.newSingleThreadExecutor();
        execDeleteParticipant.submit(() -> {
            while (!Thread.interrupted()) {
                var response = ClientBuilder.newClient()
                        .target(userData.getServerURL())
                        .path("/api/participants/deletes")
                        .request(APPLICATION_JSON)
                        .get(Response.class);

                if (response.getStatus() == HttpStatus.NO_CONTENT.value()) {
                    continue;
                }

                if (response.getStatus() == HttpStatus.OK.value()) {
                    UUID deletedParticipantId = response.readEntity(UUID.class);
                    listener.accept(deletedParticipantId);
                }
            }
        });
    }

    private static ExecutorService execDeleteTransaction;

    public void registerForTransactionDeletionUpdates(Consumer<UUID> listener) {
        execDeleteTransaction = Executors.newSingleThreadExecutor();
        execDeleteTransaction.submit(() -> {
            while (!Thread.interrupted()) {
                var response = ClientBuilder.newClient()
                        .target(userData.getServerURL())
                        .path("/api/transaction/deletion/updates")
                        .request(APPLICATION_JSON)
                        .get(Response.class);
                if (response.getStatus() == HttpStatus.NO_CONTENT.value()) {
                    continue;
                }
                if (response.getStatus() == HttpStatus.OK.value()) {
                    UUID deletedTransactionId = response.readEntity(UUID.class);
                    listener.accept(deletedTransactionId);
                }
            }
        });
    }

    public void stop(){
        if(transactionEXEC !=null && execDeleteTransaction!=null){
            transactionEXEC.shutdownNow();
            execDeleteTransaction.shutdownNow();
        }
        if(eventNameExecutor!=null){
            eventNameExecutor.shutdownNow();
        }

        if(execUpdateParticipant != null && execDeleteParticipant != null ){
            execUpdateParticipant.shutdownNow();
            execDeleteParticipant.shutdownNow();
        }
    }

    private String getWebSocketURL() {
        String url = userData.getServerURL();
        url = url.replaceFirst("http", "ws");
        url = url + "/websocket";
        return url;
    }

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
        if(session != null && session.isConnected()) {
            session.subscribe(dest, new StompFrameHandler() {
                @Override
                public Type getPayloadType(StompHeaders headers) {
                    return EventDTO.class;
                }

                @Override
                public void handleFrame(StompHeaders headers, Object payload) {
                    consumer.accept((EventDTO) payload);
                }
            });
        }
    }

    public void register(String dest, Consumer<UUID> consumer, UUID id) {
        if(session != null && session.isConnected()) {
            session.subscribe(dest, new StompFrameHandler() {
                @Override
                public Type getPayloadType(StompHeaders headers) {
                    return UUID.class;
                }

                @Override
                public void handleFrame(StompHeaders headers, Object payload) {
                    consumer.accept((UUID) payload);
                }
            });
        }
    }

    public void webSocketReconnect() {
        if(session != null && session.isConnected()) {
            session.disconnect();
        }
        session = connect(getWebSocketURL());
    }

    //Testing
    public Response.StatusType reach(String url) throws ProcessingException {
        return ClientBuilder.newClient()
                .target(url).path("api/test/reach/")
                .request()
                .get().getStatusInfo();
    }

    ExecutorService eventNameExecutor;
    public void registerForEventNameUpdates(Consumer<String> consumer) {
        eventNameExecutor= Executors.newSingleThreadExecutor();
        eventNameExecutor.submit(() -> {
            while (!Thread.interrupted()) {

                var response = ClientBuilder.newClient()
                        .target(userData.getServerURL())
                        .path("api/event/name/updates")
                        .request(APPLICATION_JSON)
                        .get(Response.class);

                if (response.getStatus() == HttpStatus.OK.value()) {
                    String eventName = response.readEntity(String.class);
                    consumer.accept(eventName);
                }


            }
        });
    }
}