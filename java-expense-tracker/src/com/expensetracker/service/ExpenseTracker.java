package com.expensetracker.service;

import com.expensetracker.model.*;
import com.expensetracker.util.Printable;

import java.time.LocalDate;
import java.time.Month;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class ExpenseTracker {

    private final String ownerName;
    private final List<Transaction> transactions;

    public ExpenseTracker(String ownerName) {
        this.ownerName    = ownerName;
        this.transactions = new ArrayList<>();
    }

    public void addTransaction(Transaction t) {
        this.transactions.add(t);
    }

    public void printAllTransactions() {
        System.out.println("\n===== All Transactions for " + ownerName + " =====");
        for (Transaction t : transactions) {
            System.out.println("  [" + t.getClass().getSimpleName() + "] " + t.getSummary());
        }
    }

    public void demonstrateMethodHidingVsOverriding() {
        System.out.println("\n===== Method Hiding vs Overriding Demo =====");

        Transaction tRef = new Expense(100, "Demo", "Food");
        Expense     eRef = (Expense) tRef;

        System.out.println("  Static getType() via Transaction reference : "
                + Transaction.getType());
        System.out.println("  Static getType() via Expense reference     : "
                + Expense.getType());
        System.out.println("  ^ Same object, different reference type -> METHOD HIDING");

        System.out.println("  Instance getSummary() via Transaction ref  : "
                + tRef.getSummary());
        System.out.println("  Instance getSummary() via Expense ref      : "
                + eRef.getSummary());
        System.out.println("  ^ Same object, same result -> METHOD OVERRIDING (polymorphism)");
    }

    public void demonstrateMultilevelInheritance(RecurringExpense re) {
        System.out.println("\n===== Multilevel Inheritance Chain =====");
        System.out.println("  RecurringExpense -> Expense -> Transaction -> Object");
        System.out.println("  instanceof Transaction    : " + (re instanceof Transaction));
        System.out.println("  instanceof Expense        : " + (re instanceof Expense));
        System.out.println("  instanceof RecurringExpense: " + (re instanceof RecurringExpense));
        System.out.println("  ID (final method getId()) : " + re.getId());
        System.out.println("  getSummary()              : " + re.getSummary());
        System.out.printf( "  Annual cost ($%.2f x %s)  : $%.2f%n",
                re.getAmount(), re.getFrequency(), re.getAnnualCost());
    }

    public void demonstrateHierarchicalInheritance(Expense e, Income i) {
        System.out.println("\n===== Hierarchical Inheritance =====");
        System.out.println("  Both Expense and Income extend Transaction (one parent, two children)");
        System.out.println("  Expense instanceof Transaction: " + (e instanceof Transaction));
        System.out.println("  Income  instanceof Transaction: " + (i instanceof Transaction));
        Transaction eAsT = e;
        System.out.println("  Expense instanceof Income     : " + (eAsT instanceof Income));
        System.out.println("  Expense summary: " + e.getSummary());
        System.out.println("  Income  summary: " + i.getSummary());
    }

    public void demonstrateMultipleInheritanceRestriction(Expense e) {
        System.out.println("\n===== Multiple Inheritance Restriction =====");
        System.out.println("  Java forbids: class Expense extends Transaction, SomeOtherClass");
        System.out.println("  Java allows:  class Expense extends Transaction");
        System.out.println("                       implements Categorizable, Printable, Exportable");
        System.out.println("  Category     : " + e.getCategory());
        System.out.println("  CategoryLabel: " + e.getCategoryLabel());
        System.out.println("  Printing via Printable interface reference:");
        Printable p = (Printable) e;
        p.print();
    }

    public void demonstrateProtectedAccess(RecurringExpense re) {
        System.out.println("\n===== Protected Access Modifier & Visibility =====");
        System.out.println("  protected fields (amount, description, date, category):");
        System.out.println("    visible inside Transaction (declared there)");
        System.out.println("    visible inside Expense (subclass)");
        System.out.println("    visible inside RecurringExpense (subclass chain)");
        System.out.println("    NOT visible here in ExpenseTracker (unrelated class)");
        System.out.println("  Via public getter: re.getAmount()   = " + re.getAmount());
        System.out.println("  Via public getter: re.getCategory() = " + re.getCategory());
    }

    public void demonstrateFinalClassAndMethod(List<Transaction> subset) {
        System.out.println("\n===== final Class & final Method =====");
        System.out.println("  ExpenseReport is a FINAL CLASS — cannot be subclassed.");
        System.out.println("  Transaction.getId() is a FINAL METHOD — cannot be overridden.");

        ExpenseReport report = new ExpenseReport("Monthly Summary", subset);
        System.out.println("  Report title (via final getTitle()): " + report.getTitle());
        report.printReport();
        report.printCategorySummary();
    }

    public void demonstrateThisAndSuperConstructors() {
        System.out.println("\n===== 'this' Keyword & super() Constructor Chaining =====");
        System.out.println("  new RecurringExpense(...)");
        System.out.println("    -> super(amount, desc, category, date)  [Expense constructor]");
        System.out.println("        -> super(amount, desc, date)         [Transaction constructor]");
        System.out.println("            -> (implicit) super()             [Object constructor]");
        System.out.println("  Each constructor uses this.field = param to assign its own fields.");
        System.out.println("  Transaction uses this(...) to chain its overloaded constructors.");
    }

    public List<Transaction> getTransactions()  { return List.copyOf(this.transactions); }
    public String getOwnerName()                { return this.ownerName; }

    public List<Transaction> getTransactionsByMonth(Month month, int year) {
        return transactions.stream()
                .filter(t -> t.getDate().getMonth() == month && t.getDate().getYear() == year)
                .collect(Collectors.toList());
    }
}
