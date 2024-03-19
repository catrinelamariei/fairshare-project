package server.Services;

import commons.DTOs.EventDTO;
import commons.DTOs.ParticipantDTO;
import commons.DTOs.TagDTO;
import commons.DTOs.TransactionDTO;
import commons.Event;
import commons.Participant;
import commons.Tag;
import commons.Transaction;
import org.springframework.stereotype.Service;
import server.database.EventRepository;
import server.database.ParticipantRepository;
import server.database.TagRepository;
import server.database.TransactionRepository;

@Service //singleton bean managed by Spring
public class DTOtoEntity {
    private final EventRepository eventRepository;
    private final TransactionRepository transactionRepository;
    private final TagRepository tagRepository;
    private final ParticipantRepository participantRepository;

    public DTOtoEntity(EventRepository eventRepository,
                       TransactionRepository transactionRepository,
                       TagRepository tagRepository,
                       ParticipantRepository participantRepository){
        this.eventRepository = eventRepository;
        this.transactionRepository = transactionRepository;
        this.tagRepository = tagRepository;
        this.participantRepository = participantRepository;
    }

    // TODO: add  [404 - NOT FOUND EXCEPTION] support

    //EVENT
    public Event get(EventDTO e){
        return eventRepository.findById(e.id).get();
    }
    public Event create(EventDTO e){
        Event event = new Event(e.getName());
        event.addTag(tagRepository.save(new Tag(event, "food", Tag.Color.GREEN)));
        event.addTag(tagRepository.save(new Tag(event, "entrance fees", Tag.Color.BLUE)));
        event.addTag(tagRepository.save(new Tag(event, "travel", Tag.Color.RED)));
        eventRepository.save(event);
        return event;
    }

    public Event update(EventDTO e){
        Event event = eventRepository.findById(e.id).get();
        event.setName(e.getName());
        eventRepository.save(event);
        return event;
    }
    public boolean delete(EventDTO e){
        eventRepository.deleteById(e.id);
        return true;
    }

    //TRANSACTION
    public Transaction get(TransactionDTO t){
        return null;
    }
    public Transaction create(TransactionDTO t){
        //create & save transactionEntity
        Transaction transaction = new Transaction(t);
        transaction.event = eventRepository.getReferenceById(t.eventId);
        transaction.author = get(t.author);
        transaction.participants.addAll(t.participants.stream().map(this::get).toList());
        transaction.tags.addAll(t.tags.stream().map(this::get).toList());
        transaction = transactionRepository.save(transaction); //idk if these extra safes are actually necessary

        //update event
        transaction.event.addTransaction(transaction);
        eventRepository.save(transaction.event); //idk if these extra safes are actually necessary

        return transaction;
    }
    public Transaction update(TransactionDTO t) {
        return null;
    }

    public boolean delete (TransactionDTO t) {
        return false;
    }

    //PARTICIPANT
    public Participant get(ParticipantDTO p){
        return null;
    }
    public Participant create(ParticipantDTO p){
        return null;
    }
    public Participant update(ParticipantDTO p) {
        Participant participant = participantRepository.findById(p.getId()).get();
        p.firstName = participant.firstName;
        p.lastName = participant.lastName;
        p.email = participant.email;
        p.iban = participant.iban;

        participantRepository.save(participant);
        return participant;
    }

    public boolean delete (ParticipantDTO p) {
        return false;
    }

    //TAG
    public Tag get(TagDTO t){
        return null;
    }
    public Tag create(TagDTO t){
        return null;
    }
    public Tag update(TagDTO t) {
        return null;
    }

    public boolean delete (TagDTO t) {
        return false;
    }
}
