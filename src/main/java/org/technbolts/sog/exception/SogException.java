package org.technbolts.sog.exception;

@SuppressWarnings("serial")
public class SogException extends RuntimeException {

    public SogException() {
        super();
    }

    public SogException(String message, Throwable cause) {
        super(message, cause);
    }

    public SogException(String message) {
        super(message);
    }

    public SogException(Throwable cause) {
        super(cause);
    }

}
