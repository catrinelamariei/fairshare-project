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
import commons.Event;
import commons.Transaction;
import jakarta.ws.rs.WebApplicationException;
import org.glassfish.jersey.client.ClientConfig;

import jakarta.ws.rs.client.ClientBuilder;
import jakarta.ws.rs.client.Entity;

import java.util.UUID;

public class ServerUtils {
    private static final String SERVER = "http://localhost:8080/";

    /**
     * Adds and event to the database.
     * @param event the event to be added
     * @return the event that was added
     */
    public Event addEvent(Event event) {
        return ClientBuilder.newClient(new ClientConfig()) //
                .target(SERVER).path("api/event") //
                .request(APPLICATION_JSON) //
                .accept(APPLICATION_JSON) //
                .post(Entity.entity(event, APPLICATION_JSON), Event.class);
    }

    public EventDTO getEvent(UUID id) throws WebApplicationException {
        return ClientBuilder.newClient()
                .target(SERVER).path("api/event/" + id)
                .request(APPLICATION_JSON)
                .get(EventDTO.class);
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
}