package commons.DTOs;

import commons.Event;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public class EventDTO {
    public UUID id;
    public String name;
    public Set<TagDTO> tags;
    public final Date date;
    public Set<ParticipantDTO> participants;
    public Set<TransactionDTO> transactions;

    public EventDTO() {
        this.date = new Date();
    }

    public EventDTO(Event event) {
        this.id = event.getId();
        this.name = event.getName();
        this.tags = new HashSet<>();
        event.getTags().forEach(tag -> tags.add(new TagDTO(tag)));
        this.date = event.getCreationDate();
        this.participants = new HashSet<>();
        event.getParticipants().forEach(
                participant -> participants.add(new ParticipantDTO(participant)));
        this.transactions = new HashSet<>();
        event.getTransactions().forEach(
                transaction -> transactions.add(new TransactionDTO(transaction)));
    }
}
