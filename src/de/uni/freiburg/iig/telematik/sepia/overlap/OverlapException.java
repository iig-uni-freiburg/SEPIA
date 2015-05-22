package de.uni.freiburg.iig.telematik.sepia.overlap;

import de.uni.freiburg.iig.telematik.sepia.exception.PNException;


public class OverlapException extends PNException{

	private static final long serialVersionUID = -501840063427403599L;

	public OverlapException() {
		super();
	}

	public OverlapException(String message) {
		super(message);
	}

	public OverlapException(String message, Throwable cause) {
		super(message, cause);
	}

	public OverlapException(Throwable cause) {
		super(cause);
	}
	
}
