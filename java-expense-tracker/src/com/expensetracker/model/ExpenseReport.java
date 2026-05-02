package com.expensetracker.model;

import java.util.List;

public final class ExpenseReport {

    private final String title;
    private final List<Transaction> transactions;

    public ExpenseReport(String title, List<Transaction> transactions) {
        this.title        = title;
        this.transactions = List.copyOf(transactions);
    }

    public final void printReport() {
        System.out.println();
        System.out.println("╔══════════════════════════════════════════════════════════╗");
        System.out.printf( "║  %-56s  ║%n", this.title);
        System.out.println("╠══════════════════════════════════════════════════════════╣");

        double totalExpenses = 0;
        double totalIncome   = 0;

        for (Transaction t : this.transactions) {
            System.out.printf("║  %-56s  ║%n", t.getSummary());
            if (t instanceof Expense) totalExpenses += t.getAmount();
            if (t instanceof Income)  totalIncome   += t.getAmount();
        }

        System.out.println("╠══════════════════════════════════════════════════════════╣");
        System.out.printf( "║  Total Income :  $%-37.2f  ║%n", totalIncome);
        System.out.printf( "║  Total Expenses: $%-37.2f  ║%n", totalExpenses);
        System.out.printf( "║  Net Balance :   $%-37.2f  ║%n", (totalIncome - totalExpenses));
        System.out.println("╚══════════════════════════════════════════════════════════╝");
    }

    public final String getTitle() {
        return this.title;
    }

    public final int getTransactionCount() {
        return this.transactions.size();
    }

    public final void printCategorySummary() {
        System.out.println("\n--- Category Breakdown ---");
        transactions.stream()
            .filter(t -> t instanceof Expense)
            .map(t -> (Expense) t)
            .collect(java.util.stream.Collectors.groupingBy(
                    Expense::getCategory,
                    java.util.stream.Collectors.summingDouble(Transaction::getAmount)))
            .forEach((cat, total) ->
                    System.out.printf("  %-20s $%.2f%n", cat + ":", total));
    }
}
