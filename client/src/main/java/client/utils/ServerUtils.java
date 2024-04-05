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
import commons.DTOs.EventDTO;
import commons.DTOs.ParticipantDTO;
import commons.DTOs.TagDTO;
import commons.DTOs.TransactionDTO;
import jakarta.ws.rs.WebApplicationException;
import jakarta.ws.rs.client.ClientBuilder;
import jakarta.ws.rs.client.Entity;
import jakarta.ws.rs.core.GenericType;
import jakarta.ws.rs.core.Response;
import org.springframework.http.HttpStatus;

import java.util.Collection;
import java.util.UUID;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
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
//////////////////////////////////////
    private static final ExecutorService EXEC = Executors.newSingleThreadExecutor();
    public void registerForUpdatesTransaction(Consumer<TransactionDTO> consumer){
        EXEC.submit(()->{
            while(!Thread.interrupted()) {
                var res = ClientBuilder.newClient()
                        .target(UserData.getInstance().getServerURL())
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

    private static ExecutorService exec;
    public void registerForUpdatesParticipant(Consumer<ParticipantDTO> consumer) {
        exec = Executors.newSingleThreadExecutor();
        exec.submit(() -> {
            while (!Thread.interrupted()) {
                var res = ClientBuilder.newClient()
                        .target(UserData.getInstance().getServerURL())
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

    public void stop(){
        EXEC.shutdownNow();
    }

    public void registerForDeletionUpdates(Runnable action) {
        EXEC.submit(() -> {
            while (!Thread.interrupted()) {
                var response = ClientBuilder.newClient()
                        .target(UserData.getInstance().getServerURL())
                        .path("/api/transactions/deletion/updates")
                        .request(APPLICATION_JSON)
                        .get(Response.class);

                if (response.getStatus() == HttpStatus.NO_CONTENT.value()) {
                    continue;
                }

                if (response.getStatus() == HttpStatus.OK.value()) {
                    String deletedTransactionId = response.readEntity(String.class);
                    action.run();
                }
            }
        });
    }


}