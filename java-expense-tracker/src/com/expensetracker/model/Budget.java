package com.expensetracker.model;

import com.expensetracker.exception.InvalidAmountException;

import java.util.HashMap;
import java.util.Map;

public class Budget {

    private final String        name;
    private double              totalLimit;
    private Map<String, Double> categoryLimits;

    public Budget(String name, double totalLimit) {
        if (totalLimit <= 0) throw new InvalidAmountException(totalLimit);
        this.name           = name;
        this.totalLimit     = totalLimit;
        this.categoryLimits = new HashMap<>();
    }

    public void setCategoryLimit(String category, double limit) {
        if (limit <= 0) throw new InvalidAmountException(limit);
        this.categoryLimits.put(category.toLowerCase(), limit);
    }

    public static class Alert {

        public enum Severity { INFO, WARNING, CRITICAL }

        private final String   category;
        private final double   spent;
        private final double   limit;
        private final Severity severity;

        public Alert(String category, double spent, double limit) {
            this.category = category;
            this.spent    = spent;
            this.limit    = limit;
            double ratio  = spent / limit;
            if      (ratio >= 1.0) this.severity = Severity.CRITICAL;
            else if (ratio >= 0.8) this.severity = Severity.WARNING;
            else                   this.severity = Severity.INFO;
        }

        public String   getCategory() { return category; }
        public double   getSpent()    { return spent; }
        public double   getLimit()    { return limit; }
        public Severity getSeverity() { return severity; }

        @Override
        public String toString() {
            return String.format("[%s] %s: spent $%.2f / $%.2f (%.0f%%)",
                    severity, category, spent, limit, (spent / limit) * 100);
        }
    }

    public class Tracker {

        private final Map<String, Double> actualSpend;

        public Tracker(Map<String, Double> actualSpend) {
            this.actualSpend = actualSpend;
        }

        public double getRemainingTotal() {
            double totalSpent = actualSpend.values().stream()
                    .mapToDouble(Double::doubleValue).sum();
            return Budget.this.totalLimit - totalSpent;
        }

        public Alert checkCategory(String category) {
            double spent = actualSpend.getOrDefault(category.toLowerCase(), 0.0);
            double limit = Budget.this.categoryLimits.getOrDefault(
                    category.toLowerCase(), Budget.this.totalLimit);
            return new Alert(category, spent, limit);
        }

        public void printStatus() {
            System.out.printf("  Budget '%s' | Total Limit: $%.2f | Remaining: $%.2f%n",
                    Budget.this.name,
                    Budget.this.totalLimit,
                    getRemainingTotal());
            for (String cat : actualSpend.keySet()) {
                System.out.println("    " + checkCategory(cat));
            }
        }
    }

    public String getName()                        { return this.name; }
    public double getTotalLimit()                  { return this.totalLimit; }
    public Map<String, Double> getCategoryLimits() { return Map.copyOf(categoryLimits); }
}
