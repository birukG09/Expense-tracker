package com.expensetracker.exception;

public class InvalidAmountException extends TrackerException {

    private final double attemptedAmount;

    public InvalidAmountException(double attemptedAmount) {
        super("ERR_INVALID_AMOUNT", "Amount must be > 0, got: " + attemptedAmount);
        this.attemptedAmount = attemptedAmount;
    }

    public double getAttemptedAmount() {
        return this.attemptedAmount;
    }

    @Override
    public String toString() {
        return super.toString() + " (attempted: " + attemptedAmount + ")";
    }
}
