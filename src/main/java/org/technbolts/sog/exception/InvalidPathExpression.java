package org.technbolts.sog.exception;

@SuppressWarnings("serial")
public class InvalidPathExpression extends SogException {

    public InvalidPathExpression() {
    }

    public InvalidPathExpression(String message) {
        super(message);
    }

    public InvalidPathExpression(Throwable cause) {
        super(cause);
    }

    public InvalidPathExpression(String message, Throwable cause) {
        super(message, cause);
    }

}
