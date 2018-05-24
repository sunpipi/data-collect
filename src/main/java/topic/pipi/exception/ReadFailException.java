package topic.pipi.exception;

public class ReadFailException extends RuntimeException {
    public ReadFailException(String message, Throwable cause) {
        super(message, cause);
    }
}
