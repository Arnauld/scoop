package org.technbolts.sog.exception;

@SuppressWarnings("serial")
public class MissingNodeException extends SogException {

    public MissingNodeException() {
    }

    public MissingNodeException(String message, Throwable cause) {
        super(message, cause);
    }

    public MissingNodeException(String message) {
        super(message);
    }

    public MissingNodeException(Throwable cause) {
        super(cause);
    }

}
