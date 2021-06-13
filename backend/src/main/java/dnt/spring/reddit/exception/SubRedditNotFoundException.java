package dnt.spring.reddit.exception;

public class SubRedditNotFoundException extends RuntimeException {
	private static final long serialVersionUID = 1L;

	public SubRedditNotFoundException(String message) {
        super(message);
    }
}
