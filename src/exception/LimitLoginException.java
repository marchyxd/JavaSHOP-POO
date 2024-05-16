package exception;

public class LimitLoginException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public LimitLoginException() {
		super("Maximum number of attempts has been reached");
	}
}
