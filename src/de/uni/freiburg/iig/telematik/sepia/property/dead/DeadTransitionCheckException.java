package de.uni.freiburg.iig.telematik.sepia.property.dead;

import de.uni.freiburg.iig.telematik.sepia.exception.PNException;

public class DeadTransitionCheckException extends PNException{

	private static final long serialVersionUID = 5429474977649503472L;

	public DeadTransitionCheckException() {
		super();
	}

	public DeadTransitionCheckException(String message) {
		super(message);
	}

	public DeadTransitionCheckException(String message, Throwable cause) {
		super(message, cause);
	}

	public DeadTransitionCheckException(Throwable cause) {
		super(cause);
	}
	
}