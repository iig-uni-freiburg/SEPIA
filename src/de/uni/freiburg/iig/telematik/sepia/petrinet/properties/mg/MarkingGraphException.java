package de.uni.freiburg.iig.telematik.sepia.petrinet.properties.mg;

import de.uni.freiburg.iig.telematik.sepia.exception.PNException;


public class MarkingGraphException extends PNException{

	private static final long serialVersionUID = -2462194457587624528L;

	public MarkingGraphException() {
		super();
	}

	public MarkingGraphException(String message) {
		super(message);
	}

	public MarkingGraphException(String message, Throwable cause) {
		super(message, cause);
	}

	public MarkingGraphException(Throwable cause) {
		super(cause);
	}
	
}
