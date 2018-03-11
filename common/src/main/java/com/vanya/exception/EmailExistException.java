package com.vanya.exception;

public class EmailExistException extends RuntimeException {
    private static final long serialVersionUID = 5861310537366287163L;

    public EmailExistException() {
        super();
    }

    public EmailExistException(final String message, final Throwable cause) {
        super(message, cause);
    }

    public EmailExistException(final String message) {
        super(message);
    }

    public EmailExistException(final Throwable cause) {
        super(cause);
    }
}
