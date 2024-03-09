package commons.DTOs;

import commons.Participant;
import commons.Tag;
import commons.Transaction;
import java.math.BigDecimal;
import java.util.Date;
import java.util.Set;
import java.util.UUID;

public class TransactionDTO {
    public UUID id;
    public UUID eventId;
    public Date date;
    public String currencyCode;
    public BigDecimal amount;
    public Participant author;
    public Set<Participant> participants;
    public Set<Tag> tags;
    public String subject;

    public TransactionDTO(Transaction transaction) {
        this.id = transaction.getId();
        this.date = transaction.getDate();
        this.currencyCode = transaction.getCurrencyCode();
        this.amount = transaction.getAmount();
        this.author = transaction.getAuthor();
        this.participants = transaction.getParticipants();
        this.tags = transaction.getTags();
        this.eventId = transaction.getEvent().getId();
        this.subject = transaction.subject;
    }

    public TransactionDTO(UUID id, Date date, String currencyCode, BigDecimal amount,
                          Participant author, String subject) {
        this.id = id;
        this.date = date;
        this.currencyCode = currencyCode;
        this.amount = amount;
        this.author = author;
        this.subject = subject;
    }
}
