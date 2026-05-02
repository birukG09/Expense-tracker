package com.expensetracker.model;

import java.time.LocalDate;

public class RecurringExpense extends Expense {

    public enum Frequency {
        DAILY, WEEKLY, MONTHLY, QUARTERLY, ANNUALLY
    }

    private Frequency frequency;
    private LocalDate nextDueDate;

    public RecurringExpense(double amount, String description,
                            String category, LocalDate date,
                            Frequency frequency, LocalDate nextDueDate) {
        super(amount, description, category, date);
        this.frequency   = frequency;
        this.nextDueDate = nextDueDate;
    }

    public RecurringExpense(double amount, String description,
                            String category, Frequency frequency) {
        super(amount, description, category);
        this.frequency   = frequency;
        this.nextDueDate = LocalDate.now().plusMonths(1);
    }

    @Override
    public String getSummary() {
        return super.getSummary()
                + String.format(" | RECURRING [%s] | next due: %s",
                        frequency, nextDueDate);
    }

    @Override
    public String toString() {
        return super.toString().replace("Expense[", "RecurringExpense[")
                + ", frequency=" + frequency
                + ", nextDue=" + nextDueDate;
    }

    @Override
    public String getExpenseKind() {
        return "Recurring Expense (" + frequency + ")";
    }

    public double getAnnualCost() {
        return switch (frequency) {
            case DAILY     -> super.amount * 365;
            case WEEKLY    -> super.amount * 52;
            case MONTHLY   -> super.amount * 12;
            case QUARTERLY -> super.amount * 4;
            case ANNUALLY  -> super.amount;
        };
    }

    public Frequency getFrequency()              { return this.frequency; }
    public LocalDate getNextDueDate()            { return this.nextDueDate; }
    public void setFrequency(Frequency frequency){ this.frequency = frequency; }
    public void setNextDueDate(LocalDate date)   { this.nextDueDate = date; }
}
