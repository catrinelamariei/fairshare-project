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

import commons.DTOs.EventDTO;
import commons.DTOs.TransactionDTO;
import commons.Transaction;
import jakarta.ws.rs.WebApplicationException;
import org.glassfish.jersey.client.ClientConfig;

import jakarta.ws.rs.client.ClientBuilder;
import jakarta.ws.rs.client.Entity;

import java.util.Collection;
import java.util.UUID;

public class ServerUtils {
    private static final String SERVER = "http://localhost:8080/";

    /**
     * Adds and event to the database.
     * @param event the event to be added
     * @return the event that was added
     */
    public EventDTO addEvent(EventDTO event) {
        return ClientBuilder.newClient(new ClientConfig()) //
                .target(SERVER).path("api/event") //
                .request(APPLICATION_JSON) //
                .accept(APPLICATION_JSON) //
                .post(Entity.entity(event, APPLICATION_JSON), EventDTO.class);
    }

    //I think there is a problem with this method
    public EventDTO getEvent(UUID id) throws WebApplicationException {
        return ClientBuilder.newClient()
                .target(SERVER).path("api/event/" + id)
                .request(APPLICATION_JSON)
                .accept(APPLICATION_JSON)
                .get(EventDTO.class);
    }

    @SuppressWarnings("unchecked")
    public Collection<EventDTO> getAllEvents(String token) {
        return ClientBuilder.newClient()
            .target(SERVER).path("event")
            .request(APPLICATION_JSON)
            .header("Authorization", "Bearer " + token)
            .get(Collection.class);
    }

    /**
     * Adds a transaction to the database.
     * @param transaction the transaction to be added
     * @return the transaction that was added
     */
    public Transaction addTransaction(Transaction transaction) {
        return ClientBuilder.newClient(new ClientConfig()) //
                .target(SERVER).path("api/transaction") //
                .request(APPLICATION_JSON) //
                .accept(APPLICATION_JSON) //
                .post(Entity.entity(transaction, APPLICATION_JSON), Transaction.class);
    }

    public TransactionDTO getTransaction(UUID id) throws WebApplicationException {
        return ClientBuilder.newClient()
                .target(SERVER).path("api/transaction/" + id)
                .request(APPLICATION_JSON)
                .get(TransactionDTO.class);
    }

    public void deleteTransactionById(UUID id) throws WebApplicationException {
        ClientBuilder.newClient()
                .target(SERVER).path("api/transaction/" + id)
                .request()
                .delete(); // Send DELETE request
    }

    public void updateEvent(EventDTO eventDTO) throws WebApplicationException {
        System.out.println("am intrat");
        ClientBuilder.newClient(new ClientConfig())
                .target(SERVER).path("/api/event/"+eventDTO.getId())
                .request(APPLICATION_JSON)
                .accept(APPLICATION_JSON)
                .put(Entity.entity(eventDTO, APPLICATION_JSON), EventDTO.class);
    }
}