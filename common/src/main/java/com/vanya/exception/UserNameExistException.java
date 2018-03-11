package com.vanya.exception;

public class UserNameExistException extends RuntimeException {

    private static final long serialVersionUID = 5861310537366287163L;

    public UserNameExistException() {
        super();
    }

    public UserNameExistException(final String message, final Throwable cause) {
        super(message, cause);
    }

    public UserNameExistException(final String message) {
        super(message);
    }

    public UserNameExistException(final Throwable cause) {
        super(cause);
    }
}