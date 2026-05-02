Java Expense Tracker

A console-based expense tracking application built in Java, demonstrating core OOP concepts.

OOP Concepts Covered 

Inheritance Expense, Income extend abstract Transaction; RecurringExpense, OneTimeExpense extend Expense

Polymorphism Overridden methods across the class hierarchy

Abstraction  Abstract class Transaction with abstract methods

Interfaces  Categorizable, Printable, Exportable with default and static methods

Encapsulation Private fields with getters/setters throughout

Generics  TransactionRepository<T> generic class

Inner/Nested Classes Budget.Alert, Budget.Tracker, TransactionRepository.QueryResult

Anonymous Classes  Used for custom comparators

Custom Exceptions TrackerException hierarchy with InvalidAmountException, DuplicateTransactionException, CategoryNotFoundException

Static Members  Static fields and methods in multiple classes
