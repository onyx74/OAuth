package com.vanya.exception;

public class NoConfirmRegistrationException extends RuntimeException {
    private static final long serialVersionUID = 5861310537366287163L;

    public NoConfirmRegistrationException() {
        super();
    }

    public NoConfirmRegistrationException(final String message, final Throwable cause) {
        super(message, cause);
    }

    public NoConfirmRegistrationException(final String message) {
        super(message);
    }

    public NoConfirmRegistrationException(final Throwable cause) {
        super(cause);
    }
}
