package de.uni.freiburg.iig.telematik.sepia.property.sequences;

import de.uni.freiburg.iig.telematik.sepia.exception.PNException;

public class SequenceGenerationException extends PNException{

	private static final long serialVersionUID = -2753672453023372223L;

	public SequenceGenerationException() {
		super();
	}

	public SequenceGenerationException(String message) {
		super(message);
	}

	public SequenceGenerationException(String message, Throwable cause) {
		super(message, cause);
	}

	public SequenceGenerationException(Throwable cause) {
		super(cause);
	}
	
}