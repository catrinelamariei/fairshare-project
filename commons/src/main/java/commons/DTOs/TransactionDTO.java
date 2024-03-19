package commons.DTOs;

import commons.Transaction;

import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Date;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

public class TransactionDTO {
    public UUID id;
    public UUID eventId;
    public Date date;
    public String currencyCode;
    public BigDecimal amount;
    public ParticipantDTO author;
    public Set<ParticipantDTO> participants;
    public Set<TagDTO> tags;
    public String subject;

    public TransactionDTO(){}

    public TransactionDTO(Transaction transaction) {
        this.id = transaction.getId();
        this.date = transaction.getDate();
        this.currencyCode = transaction.getCurrencyCode();
        this.amount = transaction.getAmount();
        this.author = new ParticipantDTO(transaction.getAuthor());
        this.participants = transaction.getParticipants().stream().map(ParticipantDTO::new)
                .collect(Collectors.toSet());
        this.tags = transaction.getTags().stream().map(TagDTO::new).collect(Collectors.toSet());
        this.eventId = transaction.getEvent().getId();
        this.subject = transaction.subject;
    }

    public TransactionDTO(UUID id, Date date, String currencyCode, BigDecimal amount,
                          ParticipantDTO author, String subject) {
        this.id = id;
        this.date = date;
        this.currencyCode = currencyCode;
        this.amount = amount;
        this.author = author;
        this.subject = subject;
    }
}
