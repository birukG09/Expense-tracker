package com.expensetracker.model;

import com.expensetracker.util.Exportable;
import com.expensetracker.util.Printable;

import java.time.LocalDate;

public class Income extends Transaction implements Printable, Exportable {

    protected String source;

    public Income(double amount, String description, String source, LocalDate date) {
        super(amount, description, date);
        this.source = source;
    }

    public Income(double amount, String description, String source) {
        super(amount, description);
        this.source = source;
    }

    public static String getType() {
        return "INCOME";
    }

    @Override
    public String getSummary() {
        return String.format("INCOME  | %s | $%.2f | %s | %s",
                source.toUpperCase(),
                super.amount,
                super.description,
                super.date);
    }

    @Override
    public String toString() {
        return super.toString().replace("Transaction[", "Income[")
                + ", source='" + source + "'";
    }

    @Override
    public void print() {
        System.out.println("  " + getSummary());
    }

    @Override
    public String toCSVRow() {
        String d = Exportable.csvDelimiter();
        return super.id + d + "INCOME" + d + super.amount + d
                + super.description + d + super.date + d + source;
    }

    public String getSource()              { return this.source; }
    public void setSource(String source)   { this.source = source; }
}
