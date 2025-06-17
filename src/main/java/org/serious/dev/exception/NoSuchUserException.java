package org.serious.dev.exception;

public class NoSuchUserException extends RuntimeException {

    private static final String TEMPLATE = "пользователь с id %d не найден";

    public NoSuchUserException(Long id) {
        super(String.format(TEMPLATE, id));
    }
}
