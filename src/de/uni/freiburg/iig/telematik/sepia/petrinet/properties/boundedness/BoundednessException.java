package de.uni.freiburg.iig.telematik.sepia.petrinet.properties.boundedness;

import de.uni.freiburg.iig.telematik.sepia.exception.PNException;


public class BoundednessException extends PNException{

	private static final long serialVersionUID = -2462194457587624528L;

	public BoundednessException() {
		super();
	}

	public BoundednessException(String message) {
		super(message);
	}

	public BoundednessException(String message, Throwable cause) {
		super(message, cause);
	}

	public BoundednessException(Throwable cause) {
		super(cause);
	}
	
}
