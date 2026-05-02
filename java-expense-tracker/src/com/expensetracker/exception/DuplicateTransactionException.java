package com.expensetracker.exception;

public class DuplicateTransactionException extends TrackerException {

    private final String duplicateId;

    public DuplicateTransactionException(String transactionId) {
        super("ERR_DUPLICATE", "Transaction already exists: " + transactionId);
        this.duplicateId = transactionId;
    }

    public String getDuplicateId() {
        return this.duplicateId;
    }

    @Override
    public String toString() {
        return "[" + super.errorCode + "] Duplicate ID=" + duplicateId;
    }
}
