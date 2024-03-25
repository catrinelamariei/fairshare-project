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
        return eventRepository.getReferenceById(e.id);
    }
    public Event create(EventDTO e){
        Event event = new Event(e.getName());
        eventRepository.save(event); // TODO: optimise this
        event.addTag(tagRepository.save(new Tag(event, "food", Tag.Color.GREEN)));
        event.addTag(tagRepository.save(new Tag(event, "entrance fees", Tag.Color.BLUE)));
        event.addTag(tagRepository.save(new Tag(event, "travel", Tag.Color.RED)));
        eventRepository.save(event);
        return event;
    }

    public Event update(EventDTO e){
        Event event = eventRepository.getReferenceById(e.id);
        event.setName(e.getName());
        eventRepository.save(event);
        return event;
    }
    public boolean delete(EventDTO e){
        if (!eventRepository.existsById(e.id)) return false;
        eventRepository.deleteById(e.id);
        return true;
    }

    //TRANSACTION
    public Transaction get(TransactionDTO t){
        return transactionRepository.getReferenceById(t.id);
    }
    public Transaction create(TransactionDTO t){
        //create & save transactionEntity
        Transaction transaction = new Transaction(t);
        transaction.event = eventRepository.getReferenceById(t.eventId);
        transaction.author = get(t.author);
        transaction.participants.addAll(t.participants.stream().map(this::get).toList());
        if(t.tags != null || !t.tags.isEmpty()) {
            transaction.tags.addAll(t.tags.stream().map(this::get).toList());
        }
        transaction = transactionRepository.save(transaction);

        //update event
        transaction.event.addTransaction(transaction);
        eventRepository.save(transaction.event);

        return transaction;
    }
    public Transaction update(TransactionDTO t) {
        Transaction transaction = transactionRepository.getReferenceById(t.id);
        transaction.date = t.date;
        transaction.currencyCode = t.currencyCode;
        transaction.amount = t.amount;
        transaction.author = get(t.author);
        transaction.subject = t.subject;
        //TODO check if this is correct, not sure if we have to clear the sets
        transaction.participants.clear();
        transaction.participants.addAll(t.participants.stream().map(this::get).toList());
        transaction.tags.clear();
        transaction.tags.addAll(t.tags.stream().map(this::get).toList());
        transactionRepository.save(transaction);
        return transaction;
    }

    public boolean delete (TransactionDTO t) {
        if(!transactionRepository.existsById(t.id)){
            return false;
        }
        transactionRepository.deleteById(t.id);
        return true;
    }

    //PARTICIPANT
    public Participant get(ParticipantDTO p){
        return participantRepository.getReferenceById(p.id);
    }
    public Participant create(ParticipantDTO p){
        //create & save participant
        Participant participant = new Participant(p);
        participant.event = eventRepository.getReferenceById(p.eventId);
        participantRepository.save(participant);

        //update event
        participant.event.addParticipant(participant);
        eventRepository.save(participant.event);

        return participant;
    }
    public Participant update(ParticipantDTO p) {
        Participant participant = participantRepository.getReferenceById(p.id);
        p.firstName = participant.firstName;
        p.lastName = participant.lastName;
        p.email = participant.email;
        p.iban = participant.iban;
        p.bic = participant.bic;

        participantRepository.save(participant);
        return participant;
    }

    public boolean delete (ParticipantDTO p) {
        if(!participantRepository.existsById(p.id)){
            return false;
        }
        participantRepository.deleteById(p.id);
        return true;
    }

    //TAG
    public Tag get(TagDTO t){
        return tagRepository.getReferenceById(t.id);
    }
    public Tag create(TagDTO t){
        //create & save tag
        Tag tag = new Tag(t);
        tag.event = eventRepository.getReferenceById(t.eventId);
        tagRepository.save(tag);

        //update event
        tag.event.addTag(tag);
        eventRepository.save(tag.event);

        return tag;
    }
    public Tag update(TagDTO t) {
        Tag tag = tagRepository.getReferenceById(t.id);
        tag.name = t.name;
        tag.color = t.color;
        tagRepository.save(tag);
        return tag;
    }

    public boolean delete (TagDTO t) {
        if(!tagRepository.existsById(t.id)){
            return false;
        }
        tagRepository.deleteById(t.id);
        return true;
    }
}
