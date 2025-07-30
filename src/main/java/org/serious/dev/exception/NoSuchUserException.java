package org.serious.dev.exception;

import static org.serious.dev.exception.ErrorCode.USER_NOT_FOUND;

public class NoSuchUserException extends RuntimeException implements HasErrorCode {

    private static final ErrorCode ERROR_TYPE = USER_NOT_FOUND;
    private static final String TEMPLATE = "пользователь с id %d не найден";

    public NoSuchUserException(Long id) {
        super(String.format(TEMPLATE, id));
    }

    @Override
    public Enum<?> getErrorType() {
        return ERROR_TYPE;
    }
}
