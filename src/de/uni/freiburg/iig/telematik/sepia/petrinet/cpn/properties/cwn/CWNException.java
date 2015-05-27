package de.uni.freiburg.iig.telematik.sepia.petrinet.cpn.properties.cwn;

import de.uni.freiburg.iig.telematik.sepia.exception.PNException;

public class CWNException extends PNException{

	private static final long serialVersionUID = 4260371301120243956L;
	
	private CWNProperties properties = null;

	public CWNException(CWNProperties properties) {
		super();
		this.properties = properties;
	}

	public CWNException(String message, Throwable cause, CWNProperties properties) {
		super(message, cause);
		this.properties = properties;
	}

	public CWNException(String message, CWNProperties properties) {
		super(message);
		this.properties = properties;
	}

	public CWNException(Throwable cause, CWNProperties properties) {
		super(cause);
		this.properties = properties;
	}
	
	public CWNProperties getProperties(){
		return properties;
	}

	public CWNException() {
		super();
	}

	public CWNException(String message, Throwable cause) {
		super(message, cause);
	}

	public CWNException(String message) {
		super(message);
	}

	public CWNException(Throwable cause) {
		super(cause);
	}
	
	
	
}
