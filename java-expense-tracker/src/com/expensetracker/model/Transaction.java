package com.expensetracker.model;

import java.time.LocalDate;
import java.util.UUID;

public abstract class Transaction {

    protected final String id;
    protected double amount;
    protected String description;
    protected LocalDate date;

    public Transaction(double amount, String description, LocalDate date) {
        this.id          = UUID.randomUUID().toString().substring(0, 8);
        this.amount      = amount;
        this.description = description;
        this.date        = date;
    }

    public Transaction(double amount, String description) {
        this(amount, description, LocalDate.now());
    }

    public final String getId() {
        return this.id;
    }

    public static String getType() {
        return "TRANSACTION";
    }

    public abstract String getSummary();

    public double getAmount()      { return amount; }
    public String getDescription() { return description; }
    public LocalDate getDate()     { return date; }

    public void setAmount(double amount)           { this.amount      = amount; }
    public void setDescription(String description) { this.description = description; }
    public void setDate(LocalDate date)            { this.date        = date; }

    @Override
    public String toString() {
        return String.format("Transaction[id=%s, amount=%.2f, desc='%s', date=%s]",
                id, amount, description, date);
    }
}
