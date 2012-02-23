package org.technbolts.scoop.exception;

@SuppressWarnings("serial")
public class ScoopException extends RuntimeException {

    public ScoopException() {
    }

    public ScoopException(String message) {
        super(message);
    }

    public ScoopException(Throwable cause) {
        super(cause);
    }

    public ScoopException(String message, Throwable cause) {
        super(message, cause);
    }

}
