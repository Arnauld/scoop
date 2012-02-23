package org.technbolts.scoop.exception;

@SuppressWarnings("serial")
public class InvocationException extends ScoopException {

    public InvocationException() {
    }

    public InvocationException(String message) {
        super(message);
    }

    public InvocationException(Throwable cause) {
        super(cause);
    }

    public InvocationException(String message, Throwable cause) {
        super(message, cause);
    }

}
