package ru.mrrex.betterium.utils.network;

public class RetryLimitExceededException extends Exception {

    public RetryLimitExceededException(String message, Throwable cause) {
        super(message, cause);
    }
}
