package org.serious.dev.exception;

public class NoSuchUserException extends RuntimeException {

    private static final String TEMPLATE = "user id %d not found";

    public NoSuchUserException(Long id) {
        super(String.format(TEMPLATE, id));
    }
}
