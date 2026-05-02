package com.expensetracker.model;

import com.expensetracker.util.Categorizable;
import com.expensetracker.util.Exportable;
import com.expensetracker.util.Printable;

import java.time.LocalDate;

public class Expense extends Transaction implements Categorizable, Printable, Exportable {

    protected String category;

    public Expense(double amount, String description, String category, LocalDate date) {
        super(amount, description, date);
        this.category = category;
    }

    public Expense(double amount, String description, String category) {
        super(amount, description);
        this.category = category;
    }

    public static String getType() {
        return "EXPENSE";
    }

    @Override
    public String getSummary() {
        return String.format("EXPENSE | %s | $%.2f | %s | %s",
                category.toUpperCase(),
                super.amount,
                super.description,
                super.date);
    }

    @Override
    public String toString() {
        return super.toString().replace("Transaction[", "Expense[")
                + ", category='" + category + "'";
    }

    @Override
    public String getCategory() {
        return this.category;
    }

    @Override
    public void setCategory(String category) {
        this.category = category;
    }

    @Override
    public void print() {
        System.out.println("  " + getSummary());
    }

    @Override
    public String toCSVRow() {
        String d = Exportable.csvDelimiter();
        return super.id + d + "EXPENSE" + d + super.amount + d
                + super.description + d + super.date + d + category;
    }

    @Override
    public String toCSVHeader() {
        return Exportable.super.toCSVHeader() + ",category";
    }

    public String getExpenseKind() {
        return "One-Time Expense";
    }
}
