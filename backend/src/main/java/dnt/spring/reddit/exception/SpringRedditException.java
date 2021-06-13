package dnt.spring.reddit.exception;

public class SpringRedditException extends RuntimeException {
	private static final long serialVersionUID = 1L;

	public SpringRedditException(String exMessage, RuntimeException exception) {
        super(exMessage, exception);
    }

    public SpringRedditException(String exMessage) {
        super(exMessage);
    }
}
