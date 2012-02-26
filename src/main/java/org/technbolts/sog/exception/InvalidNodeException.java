package org.technbolts.sog.exception;

@SuppressWarnings("serial")
public class InvalidNodeException extends SogException {

    public InvalidNodeException() {
    }

    public InvalidNodeException(String message, Throwable cause) {
        super(message, cause);
    }

    public InvalidNodeException(String message) {
        super(message);
    }

    public InvalidNodeException(Throwable cause) {
        super(cause);
    }

}
