package bank.model;

import java.math.BigDecimal;
import java.time.LocalDate;

public class Transaction {

    private final TransactionType type;
    private final BigDecimal amount;
    private final LocalDate date;
    private final String description;

    public Transaction(TransactionType type, BigDecimal amount, LocalDate date, String description) {
        this.type = type;
        this.amount = amount;
        this.date = date;
        this.description = description;
    }

    public TransactionType getType() {
        return type;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public LocalDate getDate() {
        return date;
    }

    public String getDescription() {
        return description;
    }
}