package de.uni.freiburg.iig.telematik.sepia.exception;


public class PNValidationException extends PNException {
	
	private static final long serialVersionUID = 3412900049637701904L;

	public PNValidationException() {
		super();
	}

	public PNValidationException(String message) {
		super(message);
	}

	public PNValidationException(String message, Throwable cause) {
		super(message, cause);
	}

	public PNValidationException(Throwable cause) {
		super(cause);
	}
	
}
