package commons.DTOs;

import commons.Transaction;

import java.math.BigDecimal;
import java.util.Date;
import java.util.UUID;

public class TransactionDTO {
    public UUID id;
    public Date date;
    public String currency;
    public BigDecimal amount;

    public TransactionDTO(Transaction transaction) {
        this.id = transaction.getId();
        this.date = transaction.getDate();
        this.currency = transaction.getCurrencyCode();
        this.amount = transaction.getAmount();
    }
}
