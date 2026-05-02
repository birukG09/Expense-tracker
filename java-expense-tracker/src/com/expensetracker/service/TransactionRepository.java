package com.expensetracker.service;

import com.expensetracker.exception.DuplicateTransactionException;
import com.expensetracker.exception.InvalidAmountException;
import com.expensetracker.model.Transaction;

import java.util.*;
import java.util.stream.Collectors;

public class TransactionRepository<T extends Transaction> {

    public static class QueryResult<R> {

        private final List<R> items;
        private final int totalCount;
        private final String queryDescription;

        public QueryResult(List<R> items, String queryDescription) {
            this.items            = Collections.unmodifiableList(items);
            this.totalCount       = items.size();
            this.queryDescription = queryDescription;
        }

        public List<R>  getItems()            { return this.items; }
        public int      getTotalCount()       { return this.totalCount; }
        public String   getQueryDescription() { return this.queryDescription; }
        public boolean  isEmpty()             { return this.items.isEmpty(); }

        @Override
        public String toString() {
            return String.format("QueryResult[query='%s', count=%d]",
                    queryDescription, totalCount);
        }
    }

    public class SortOptions {
        public List<T> byAmountAscending() {
            return store.values().stream()
                    .sorted(Comparator.comparingDouble(Transaction::getAmount))
                    .collect(Collectors.toList());
        }

        public List<T> byDateDescending() {
            return store.values().stream()
                    .sorted(Comparator.comparing(Transaction::getDate).reversed())
                    .collect(Collectors.toList());
        }
    }

    private final Map<String, T> store = new LinkedHashMap<>();
    private final String         repositoryName;

    public TransactionRepository(String repositoryName) {
        this.repositoryName = repositoryName;
    }

    public static <T extends Transaction> TransactionRepository<T> create(String name) {
        return new TransactionRepository<>(name);
    }

    public T save(T transaction) {
        if (transaction.getAmount() <= 0) {
            throw new InvalidAmountException(transaction.getAmount());
        }
        if (store.containsKey(transaction.getId())) {
            throw new DuplicateTransactionException(transaction.getId());
        }
        store.put(transaction.getId(), transaction);
        return transaction;
    }

    public Optional<T> findById(String id) {
        return Optional.ofNullable(store.get(id));
    }

    public QueryResult<T> findAll() {
        return new QueryResult<>(new ArrayList<>(store.values()),
                "all transactions in " + repositoryName);
    }

    public QueryResult<T> findByMinAmount(double minAmount) {
        Comparator<T> byAmountDesc = new Comparator<T>() {
            @Override
            public int compare(T a, T b) {
                return Double.compare(b.getAmount(), a.getAmount());
            }
        };

        List<T> results = store.values().stream()
                .filter(t -> t.getAmount() >= minAmount)
                .sorted(byAmountDesc)
                .collect(Collectors.toList());

        return new QueryResult<>(results,
                "amount >= " + minAmount + " in " + repositoryName);
    }

    public SortOptions sortOptions() {
        return new SortOptions();
    }

    public int      size()               { return store.size(); }
    public String   getRepositoryName()  { return this.repositoryName; }
    public boolean  contains(String id)  { return store.containsKey(id); }
}
