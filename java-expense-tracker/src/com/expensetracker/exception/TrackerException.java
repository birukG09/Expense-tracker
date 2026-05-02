package com.expensetracker.exception;

public class TrackerException extends RuntimeException {

    protected final String errorCode;

    public TrackerException(String errorCode, String message) {
        super(message);
        this.errorCode = errorCode;
    }

    public TrackerException(String errorCode, String message, Throwable cause) {
        super(message, cause);
        this.errorCode = errorCode;
    }

    public final String getErrorCode() {
        return this.errorCode;
    }

    @Override
    public String toString() {
        return "[" + errorCode + "] " + getMessage();
    }
}
