package de.uni.freiburg.iig.telematik.sepia.replay;

import de.uni.freiburg.iig.telematik.sepia.exception.PNException;


public class ReplayException extends PNException{

	private static final long serialVersionUID = -2462194457587624528L;

	public ReplayException() {
		super();
	}

	public ReplayException(String message) {
		super(message);
	}

	public ReplayException(String message, Throwable cause) {
		super(message, cause);
	}

	public ReplayException(Throwable cause) {
		super(cause);
	}
	
}
