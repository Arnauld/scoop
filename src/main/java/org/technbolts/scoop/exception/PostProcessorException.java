package org.technbolts.scoop.exception;

@SuppressWarnings("serial")
public class PostProcessorException extends ScoopException {

    public PostProcessorException() {
        super();
    }

    public PostProcessorException(String message, Throwable cause) {
        super(message, cause);
    }

    public PostProcessorException(String message) {
        super(message);
    }

    public PostProcessorException(Throwable cause) {
        super(cause);
    }

}
