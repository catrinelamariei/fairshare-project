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

import java.math.BigDecimal;
import java.util.stream.Collectors;

@Service //singleton bean managed by Spring
public class DTOtoEntity {
    private final EventRepository eventRepository;
    private final TransactionRepository transactionRepository;
    private final TagRepository tagRepository;
    private final ParticipantRepository participantRepository;
    private CurrencyExchange currencyExchange;

    public DTOtoEntity(EventRepository eventRepository,
                       TransactionRepository transactionRepository,
                       TagRepository tagRepository,
                       ParticipantRepository participantRepository,
                       CurrencyExchange currencyExchange){
        this.eventRepository = eventRepository;
        this.transactionRepository = transactionRepository;
        this.tagRepository = tagRepository;
        this.participantRepository = participantRepository;
        this.currencyExchange = currencyExchange;
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
        BigDecimal amount = t.amount;
        if(!t.currencyCode.equals("EUR")){
            //convert to EUR
            try {
                amount = amount.multiply(new BigDecimal(
                        currencyExchange.getRate(t.currencyCode, "EUR", t.date).rate));
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
        t.amount = amount;
        t.currencyCode = "EUR";
        //create & save transactionEntity
        Transaction transaction = new Transaction(t);
        transaction.event = eventRepository.getReferenceById(t.eventId);
        transaction.author = get(t.author);
        transaction.participants.addAll(t.participants.stream().map(this::get).toList());
        for (ParticipantDTO p : t.participants) {
            Participant participant = participantRepository.getReferenceById(p.id);
            participant.addTransaction(transaction);
        }
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
        BigDecimal amount = t.amount;
        if(!t.currencyCode.equals("EUR")){
            //convert to EUR
            try {
                amount = amount.multiply(new BigDecimal(
                        currencyExchange.getRate(t.currencyCode, "EUR", t.date).rate));
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
        t.amount = amount;
        t.currencyCode = "EUR";
        Transaction transaction = transactionRepository.getReferenceById(t.id);
        transaction.setDate(t.date);
        transaction.setCurrencyCode(t.currencyCode);
        transaction.setAmount(t.amount);
        transaction.setAuthor(get(t.author));
        transaction.setSubject(t.subject);
        transaction.setParticipants(t.participants.stream().map(this::get)
                .collect(Collectors.toSet()));
        transaction.setTags(t.tags.stream().map(this::get).collect(Collectors.toSet()));

        return transactionRepository.save(transaction);
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
        participant.setFirstName(p.firstName);
        participant.setLastName(p.lastName);
        participant.setEmail(p.email);
        participant.setIban(p.iban);
        participant.setBic(p.bic);

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
        tag = tagRepository.save(tag);

        //update event
        tag.event.addTag(tag);
        eventRepository.save(tag.event);

        return tag;
    }
    public Tag update(TagDTO t) {
        Tag tag = tagRepository.getReferenceById(t.id);
        tag.setName(t.name);
        tag.setColor(t.color);
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
