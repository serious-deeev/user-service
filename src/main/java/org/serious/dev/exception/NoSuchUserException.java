package org.serious.dev.exception;

import static org.serious.dev.exception.GrpcErrorCode.USER_NOT_FOUND;

public class NoSuchUserException extends RuntimeException implements HasGrpcErrorCode {

    private static final GrpcErrorCode ERROR_TYPE = USER_NOT_FOUND;
    private static final String TEMPLATE = "пользователь с id %d не найден";

    public NoSuchUserException(Long id) {
        super(String.format(TEMPLATE, id));
    }

    @Override
    public GrpcErrorCode getErrorType() {
        return ERROR_TYPE;
    }
}
