package ru.mrrex.betterium.exceptions;

public class RetryLimitExceededException extends Exception {
    public RetryLimitExceededException(String message) {
        super(message);
    }

    public RetryLimitExceededException(String message, Throwable cause) {
        super(message, cause);
    }
}
