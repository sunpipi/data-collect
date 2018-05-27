package topic.pipi.exception;

public class TaskErrorException extends RuntimeException {
    public TaskErrorException(String message, Throwable cause) {
        super(message, cause);
    }
}
