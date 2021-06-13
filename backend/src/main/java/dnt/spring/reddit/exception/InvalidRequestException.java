package dnt.spring.reddit.exception;

@SuppressWarnings("serial")
public class InvalidRequestException extends RuntimeException {
    private String msg;
    public InvalidRequestException(String message) {
        super(message);
    }

    public String getErrorMessage() {
        return this.msg;
    }
}
