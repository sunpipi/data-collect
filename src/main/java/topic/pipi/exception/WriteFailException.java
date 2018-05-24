package topic.pipi.exception;

public class WriteFailException extends RuntimeException {

    public WriteFailException(String message, Throwable cause) {
        super(message, cause);
    }
}
