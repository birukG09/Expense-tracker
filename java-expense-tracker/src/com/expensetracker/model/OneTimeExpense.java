package com.expensetracker.model;

import java.time.LocalDate;

public class OneTimeExpense extends Expense {

    private String receiptNumber;
    private boolean reimbursable;

    public OneTimeExpense(double amount, String description,
                          String category, LocalDate date,
                          String receiptNumber, boolean reimbursable) {
        super(amount, description, category, date);
        this.receiptNumber = receiptNumber;
        this.reimbursable  = reimbursable;
    }

    public OneTimeExpense(double amount, String description, String category) {
        super(amount, description, category);
        this.receiptNumber = "N/A";
        this.reimbursable  = false;
    }

    @Override
    public String getSummary() {
        String base = super.getSummary();
        String reimbTag = reimbursable ? " [REIMBURSABLE]" : "";
        return base + String.format(" | ONE-TIME | receipt: %s%s",
                receiptNumber, reimbTag);
    }

    @Override
    public String toString() {
        return super.toString().replace("Expense[", "OneTimeExpense[")
                + ", receipt='" + receiptNumber + "'"
                + ", reimbursable=" + reimbursable;
    }

    @Override
    public String getExpenseKind() {
        return reimbursable ? "Reimbursable One-Time Expense" : "One-Time Expense";
    }

    public String getReceiptNumber()               { return this.receiptNumber; }
    public boolean isReimbursable()                { return this.reimbursable; }
    public void setReceiptNumber(String r)         { this.receiptNumber = r; }
    public void setReimbursable(boolean r)         { this.reimbursable = r; }
}
