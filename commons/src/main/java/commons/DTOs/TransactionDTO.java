package commons.DTOs;

import commons.Transaction;
import java.math.BigDecimal;
import java.util.Date;
import java.util.UUID;

public class TransactionDTO {
    public UUID id;
    public Date date;
    public String currencyCode;
    public BigDecimal amount;

    public TransactionDTO(Transaction transaction) {
        this.id = transaction.getId();
        this.date = transaction.getDate();
        this.currencyCode = transaction.getCurrencyCode();
        this.amount = transaction.getAmount();
    }

    public TransactionDTO(UUID id, Date date, String currencyCode, BigDecimal amount) {
        this.id = id;
        this.date = date;
        this.currencyCode = currencyCode;
        this.amount = amount;
    }
}
