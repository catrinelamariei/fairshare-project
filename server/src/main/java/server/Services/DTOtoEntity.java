package server.Services;

import commons.DTOs.*;
import commons.*;
import org.springframework.stereotype.Service;
import server.database.*;

import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.stream.Collectors;

@Service //singleton bean managed by Spring
public class DTOtoEntity {
    private final EventRepository eventRepository;
    private final TransactionRepository transactionRepository;
    private final TagRepository tagRepository;
    private final ParticipantRepository participantRepository;
    private final CurrencyExchange currencyExchange;

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

    /**
     * creates event, UUID used if valid
     * @param e event to be created
     * @return persisted event
     */
    public Event create(EventDTO e){
        Event event = new Event(e.getName());
        try {event.id = UUID.fromString(e.id.toString());}
        catch (IllegalArgumentException | NullPointerException ign) {}
        eventRepository.save(event);
        event.addTag(tagRepository.save(new Tag(event, "food", Tag.Color.GREEN)));
        event.addTag(tagRepository.save(new Tag(event, "entrance fees", Tag.Color.BLUE)));
        event.addTag(tagRepository.save(new Tag(event, "travel", Tag.Color.RED)));
        event.addTag(tagRepository.save(new Tag(event, "debt", Tag.Color.ORANGE)));
        eventRepository.save(event);
        return event;
    }

    public Event update(EventDTO e){
        Event event = eventRepository.getReferenceById(e.id);
        event.setName(e.getName());
        eventRepository.save(event);
        return event;
    }

    @SuppressWarnings("checkstyle:LineLength")
    public Event set(EventDTO e) {
        eventRepository.deleteById(e.id);

        //basic fields
        Event event = new Event();
        event.id = e.id;
        event.setName(e.name);
        event.setCreationDate(e.date);
        eventRepository.save(event);

        //initial creation
        event.tags = e.tags.stream().map(Tag::new).collect(Collectors.toSet());
        event.participants = e.participants.stream().map(Participant::new).collect(Collectors.toSet());
        event.transactions = e.transactions.stream().map(Transaction::new).collect(Collectors.toSet());

        //break cyclic dependencies (Transaction-participant), (Transaction-Tag)
        transactionRepository.saveAll(event.transactions);

        //fill in gaps (relations) (I am sorry)
        for (Tag tag : event.tags) {
            tag.event = event;
            List<UUID> target = e.transactions.stream().filter(t -> t.tags.contains(new TagDTO(tag))).map(t -> t.id).toList();
            tag.transactions.addAll(event.transactions.stream().filter(t -> target.contains(t.id)).toList());
        } //add all transactions t in e where t.tags.id equals this tag, add event
        for (Participant participant : event.participants) {
            participant.event = event;
            List<UUID> targetPaidTransactions = e.transactions.stream().filter(t -> Objects.equals(t.author.id, participant.id)).map(t -> t.id).toList();
            participant.paidTransactions = event.transactions.stream().filter(t -> targetPaidTransactions.contains(t.id)).collect(Collectors.toSet());
            List<UUID> targetParticipatedTransactions = e.transactions.stream().filter(t -> t.participants.stream().map(p -> p.id).toList().contains(participant.id)).map(t -> t.id).toList();
            participant.participatedTransactions = event.transactions.stream().filter(t -> targetParticipatedTransactions.contains(t.id)).collect(Collectors.toSet());
        } //event, transactions, participatedTransactions
        for (Transaction transaction : event.transactions) {
            transaction.event = event;
            TransactionDTO transactionDTO = e.transactions.stream().filter(t -> Objects.equals(transaction.id, t.id)).findAny().get();
            UUID targetAuthor = transactionDTO.author.id;
            transaction.author = event.participants.stream().filter(p -> Objects.equals(p.id, targetAuthor)).findAny().get();
            List<UUID> targetParticipants =  transactionDTO.participants.stream().map(p -> p.id).toList();
            transaction.participants = event.participants.stream().filter(p -> targetParticipants.contains(p.id)).collect(Collectors.toSet());
            List<UUID> targetTags = transactionDTO.tags.stream().map(t -> t.id).toList();
            transaction.tags = event.tags.stream().filter(t -> targetTags.contains(t.id)).collect(Collectors.toSet());
        } //event, author, participant, tags

        tagRepository.saveAll(event.tags);
        participantRepository.saveAll(event.participants);
        transactionRepository.saveAll(event.transactions);
        return eventRepository.save(event);
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
        t.amount = currencyExchange.getAmount(t.currencyCode, t.amount, t.date);
        t.currencyCode = "EUR";
        //create & save transactionEntity
        Transaction transaction = new Transaction(t);
        transaction.id = UUID.randomUUID(); //creating should assign new ID
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
        t.amount = currencyExchange.getAmount(t.currencyCode, t.amount, t.date);
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
        participant.id = UUID.randomUUID();
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
