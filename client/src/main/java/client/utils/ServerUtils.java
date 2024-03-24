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

import static jakarta.ws.rs.core.MediaType.APPLICATION_JSON;

import client.UserData;
import commons.DTOs.EventDTO;
import commons.DTOs.ParticipantDTO;
import commons.DTOs.TagDTO;
import commons.DTOs.TransactionDTO;
import jakarta.ws.rs.WebApplicationException;

import jakarta.ws.rs.client.ClientBuilder;
import jakarta.ws.rs.client.Entity;

import java.util.Collection;
import java.util.UUID;

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
        System.out.println("am intrat");
        return ClientBuilder.newClient()
            .target(UserData.getInstance().getServerURL()).path("/api/event/"+eventDTO.getId())
            .request(APPLICATION_JSON)
            .put(Entity.entity(eventDTO, APPLICATION_JSON), EventDTO.class);
    }

    public boolean deleteEvent(UUID id) throws WebApplicationException {
        return false;
    }

    @SuppressWarnings("unchecked")
    public Collection<EventDTO> getAllEvents() throws WebApplicationException {
        return ClientBuilder.newClient()
            .target(UserData.getInstance().getServerURL()).path("event")
            .request(APPLICATION_JSON)
            .header("Authorization", "Bearer " + UserData.getInstance().getToken())
            .get(Collection.class);
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
        return null;
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
        return null;
    }

    //create a gatParticipants method
    // //- returns a list of all participants from the event

    public ParticipantDTO postParticipant(ParticipantDTO p) throws WebApplicationException {
        return ClientBuilder.newClient()
            .target(UserData.getInstance().getServerURL()).path("api/participants/")
            .request(APPLICATION_JSON)
            .post(Entity.entity(p, APPLICATION_JSON), ParticipantDTO.class);
    }

    public ParticipantDTO putParticipant(ParticipantDTO p) throws WebApplicationException {
        return null;
    }

    public void deleteParticipant(UUID id) throws WebApplicationException {

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


}