package com.expensetracker;

import com.expensetracker.exception.*;
import com.expensetracker.model.*;
import com.expensetracker.model.Budget.Alert;
import com.expensetracker.model.RecurringExpense.Frequency;
import com.expensetracker.service.ExpenseTracker;
import com.expensetracker.service.TransactionRepository;
import com.expensetracker.util.Exportable;

import java.time.LocalDate;
import java.time.Month;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

public class Main {

    public static void main(String[] args) {

        printBanner();

        System.out.println("\n[1] CREATING TRANSACTIONS\n");

        Expense lunch = new Expense(18.50, "Lunch at deli", "Food",
                LocalDate.of(2026, Month.MAY, 1));
        Income salary = new Income(5000.00, "May salary", "Employer",
                LocalDate.of(2026, Month.MAY, 1));
        RecurringExpense netflix = new RecurringExpense(
                15.99, "Netflix subscription", "Entertainment",
                LocalDate.of(2026, Month.MAY, 2),
                Frequency.MONTHLY,
                LocalDate.of(2026, Month.JUNE, 2));
        OneTimeExpense laptop = new OneTimeExpense(
                1299.00, "Laptop for work", "Technology",
                LocalDate.of(2026, Month.MAY, 3),
                "RCP-20260503-001", true);
        RecurringExpense rent = new RecurringExpense(
                1200.00, "Monthly rent", "Housing",
                LocalDate.of(2026, Month.MAY, 1),
                Frequency.MONTHLY,
                LocalDate.of(2026, Month.JUNE, 1));
        Expense groceries = new Expense(143.75, "Weekly groceries", "Food");
        Income  freelance  = new Income(800.00, "Website project", "Freelance");

        printCreated("Expense (Single Inheritance)",            lunch);
        printCreated("Income  (Hierarchical from Transaction)", salary);
        printCreated("RecurringExpense (Multilevel)",           netflix);
        printCreated("OneTimeExpense   (Hierarchical Expense)", laptop);

        ExpenseTracker tracker = new ExpenseTracker("Alex Johnson");
        for (Transaction t : List.of(salary, freelance, lunch, groceries,
                                     laptop, netflix, rent)) {
            tracker.addTransaction(t);
        }

        tracker.printAllTransactions();
        tracker.demonstrateMethodHidingVsOverriding();
        tracker.demonstrateMultilevelInheritance(netflix);
        tracker.demonstrateHierarchicalInheritance(lunch, salary);
        tracker.demonstrateMultipleInheritanceRestriction(lunch);
        tracker.demonstrateProtectedAccess(netflix);
        tracker.demonstrateThisAndSuperConstructors();
        tracker.demonstrateFinalClassAndMethod(
                tracker.getTransactionsByMonth(Month.MAY, 2026));

        System.out.println("\n" + sep("super keyword — Methods & Fields"));
        System.out.println("  netflix.getSummary(): " + netflix.getSummary());
        System.out.println("  laptop.getSummary() : " + laptop.getSummary());
        System.out.println("  getId() is FINAL — netflix: "
                + netflix.getId() + " | laptop: " + laptop.getId());

        System.out.println("\n" + sep("IMPLICIT super() RULES"));
        System.out.println("  Every constructor inserts implicit super() if not stated.");
        System.out.println("  Transaction has no no-arg constructor, so all subclasses");
        System.out.println("  must call super(...) explicitly or the compiler errors.");

        System.out.println("\n" + sep("VISIBILITY & PACKAGES"));
        System.out.println("  com.expensetracker           -> Main");
        System.out.println("  com.expensetracker.model     -> Transaction, Expense, Income,");
        System.out.println("                                  RecurringExpense, OneTimeExpense,");
        System.out.println("                                  ExpenseReport, Budget");
        System.out.println("  com.expensetracker.service   -> ExpenseTracker, TransactionRepository");
        System.out.println("  com.expensetracker.util      -> Categorizable, Printable, Exportable");
        System.out.println("  com.expensetracker.exception -> TrackerException + sub-exceptions");

        System.out.println("\n" + sep("CUSTOM EXCEPTION HIERARCHY"));
        System.out.println("  TrackerException extends RuntimeException extends Exception");
        System.out.println("  +-- InvalidAmountException");
        System.out.println("  +-- DuplicateTransactionException");
        System.out.println("  +-- CategoryNotFoundException");
        System.out.println();

        demonstrateExceptions();

        System.out.println("\n" + sep("GENERIC CLASS  TransactionRepository<T extends Transaction>"));
        System.out.println("  T is bounded: must be Transaction or a subclass.\n");

        TransactionRepository<Expense> expenseRepo =
                TransactionRepository.create("Expense Store");
        TransactionRepository<Income>  incomeRepo  =
                TransactionRepository.create("Income Store");

        expenseRepo.save(lunch);
        expenseRepo.save(groceries);
        expenseRepo.save(laptop);
        expenseRepo.save(netflix);
        expenseRepo.save(rent);
        incomeRepo.save(salary);
        incomeRepo.save(freelance);

        TransactionRepository.QueryResult<Expense> allExpenses = expenseRepo.findAll();
        System.out.println("  " + allExpenses);
        System.out.println("  Items in QueryResult<Expense>:");
        allExpenses.getItems().forEach(e -> System.out.println("    " + e.getSummary()));

        TransactionRepository.QueryResult<Expense> bigExpenses =
                expenseRepo.findByMinAmount(100.0);
        System.out.println("\n  Expenses >= $100 (sorted desc via anonymous Comparator):");
        bigExpenses.getItems().forEach(e -> System.out.println("    " + e.getSummary()));

        System.out.println("\n" + sep("STATIC NESTED CLASS  TransactionRepository.QueryResult"));
        System.out.println("  QueryResult<R> is static — no enclosing instance required:");
        TransactionRepository.QueryResult<String> standalone =
                new TransactionRepository.QueryResult<>(List.of("a", "b", "c"), "demo");
        System.out.println("  " + standalone);
        System.out.println("  Budget.Alert is also a static nested class:");

        Alert alert = new Budget.Alert("Food", 145.00, 200.00);
        System.out.println("  " + alert);
        alert = new Budget.Alert("Housing", 1200.00, 1200.00);
        System.out.println("  " + alert);

        System.out.println("\n" + sep("NON-STATIC INNER CLASS  Budget.Tracker / SortOptions"));
        System.out.println("  Inner class holds implicit reference to its enclosing instance.");
        System.out.println("  Created as: budget.new Tracker(...)");
        System.out.println("  Accesses outer fields via Budget.this.fieldName\n");

        Budget budget = new Budget("May 2026", 2000.00);
        budget.setCategoryLimit("food", 250.00);
        budget.setCategoryLimit("entertainment", 50.00);
        budget.setCategoryLimit("housing", 1200.00);
        budget.setCategoryLimit("technology", 300.00);

        Map<String, Double> actual = Map.of(
                "food",          162.25,
                "entertainment",  15.99,
                "housing",      1200.00,
                "technology",   1299.00);

        Budget.Tracker budgetTracker = budget.new Tracker(actual);
        budgetTracker.printStatus();

        System.out.println("\n  SortOptions (inner class of TransactionRepository):");
        TransactionRepository<Expense>.SortOptions sorter = expenseRepo.sortOptions();
        System.out.println("  Sorted by amount ascending:");
        sorter.byAmountAscending().forEach(
                e -> System.out.printf("    $%7.2f  %s%n", e.getAmount(), e.getDescription()));

        System.out.println("\n" + sep("ANONYMOUS CLASS"));
        System.out.println("  A nameless subclass created inline — used as Comparator<T>.\n");

        Runnable reportTask = new Runnable() {
            @Override
            public void run() {
                System.out.println("  [Anonymous Runnable] Repository: "
                        + expenseRepo.getRepositoryName());
                System.out.println("  Total expense records: " + expenseRepo.size());
            }
        };
        reportTask.run();

        Runnable lambdaTask = () ->
                System.out.println("  [Lambda equivalent] Same result, less boilerplate.");
        lambdaTask.run();

        System.out.println("\n" + sep("INTERFACE default / static methods & override"));
        System.out.println("  Exportable: abstract toCSVRow(), default toCSVHeader(), static csvDelimiter()\n");

        System.out.println("  Exportable.csvDelimiter() [static] = '" + Exportable.csvDelimiter() + "'");

        Expense expRef = lunch;
        System.out.println("\n  Expense.toCSVHeader() [overrides default]:");
        System.out.println("    " + expRef.toCSVHeader());
        System.out.println("  Expense.toCSVRow():");
        System.out.println("    " + expRef.toCSVRow());

        System.out.println("\n  Income.toCSVHeader() [uses default, not overridden]:");
        System.out.println("    " + salary.toCSVHeader());
        System.out.println("  Income.toCSVRow():");
        System.out.println("    " + salary.toCSVRow());

        System.out.println("\n  All expenses as CSV:");
        System.out.println("  " + lunch.toCSVHeader());
        allExpenses.getItems().forEach(e -> System.out.println("  " + e.toCSVRow()));

        System.out.println("\n" + sep("FULL REPORT"));
        ExpenseReport report = new ExpenseReport("May 2026 — Full Report",
                tracker.getTransactions());
        report.printReport();
        report.printCategorySummary();

        System.out.println("\n" + "=".repeat(65));
        System.out.println("  All OOP concepts demonstrated successfully.");
        System.out.println("=".repeat(65) + "\n");
    }

    private static void demonstrateExceptions() {
        TransactionRepository<Expense> repo = TransactionRepository.create("test");

        System.out.println("  [a] Catching InvalidAmountException:");
        try {
            repo.save(new Expense(-50, "bad entry", "misc"));
        } catch (InvalidAmountException e) {
            System.out.println("      Caught specific -> " + e);
            System.out.println("      errorCode: " + e.getErrorCode());
        }

        System.out.println("  [b] Catching DuplicateTransactionException:");
        Expense e1 = new Expense(20, "coffee", "Food");
        repo.save(e1);
        try {
            repo.save(e1);
        } catch (DuplicateTransactionException e) {
            System.out.println("      Caught specific -> " + e);
        }

        System.out.println("  [c] Catching base TrackerException catches all subtypes:");
        try {
            repo.save(new Expense(-1, "negative", "misc"));
        } catch (TrackerException e) {
            System.out.println("      Caught as base -> [" + e.getErrorCode() + "] " + e.getMessage());
            System.out.println("      Runtime type   -> " + e.getClass().getSimpleName());
        }

        System.out.println("  [d] CategoryNotFoundException:");
        try {
            throw new CategoryNotFoundException("UNKNOWN_CAT");
        } catch (TrackerException e) {
            System.out.println("      " + e);
        }
    }

    private static void printBanner() {
        System.out.println("=".repeat(65));
        System.out.println("   Java Expense Tracker — OOP Demonstration");
        System.out.println("=".repeat(65));
        System.out.println("  Hierarchy:");
        System.out.println("    Transaction (abstract superclass)");
        System.out.println("    +-- Expense   (single inheritance)");
        System.out.println("    |   +-- RecurringExpense  (multilevel)");
        System.out.println("    |   +-- OneTimeExpense    (hierarchical)");
        System.out.println("    +-- Income    (hierarchical alongside Expense)");
        System.out.println("  final class: ExpenseReport");
        System.out.println("  interfaces:  Categorizable, Printable, Exportable");
        System.out.println("=".repeat(65));
    }

    private static void printCreated(String label, Transaction t) {
        System.out.printf("  + %-45s id=%s%n", label + ":", t.getId());
    }

    private static String sep(String title) {
        return "===== " + title + " " + "=".repeat(Math.max(0, 58 - title.length()));
    }
}
