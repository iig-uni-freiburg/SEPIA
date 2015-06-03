package de.uni.freiburg.iig.telematik.sepia.petrinet.pt.properties.wfnet;

import de.uni.freiburg.iig.telematik.sepia.exception.PNException;

public class WFNetException extends PNException{

	private static final long serialVersionUID = 4260371301120243956L;
	
	private WFNetProperties properties = null;

	public WFNetException(WFNetProperties properties) {
		super();
		this.properties = properties;
	}

	public WFNetException(String message, Throwable cause, WFNetProperties properties) {
		super(message, cause);
		this.properties = properties;
	}

	public WFNetException(String message, WFNetProperties properties) {
		super(message);
		this.properties = properties;
	}

	public WFNetException(Throwable cause, WFNetProperties properties) {
		super(cause);
		this.properties = properties;
	}
	
	public WFNetProperties getProperties(){
		return properties;
	}

	public WFNetException() {
		super();
	}

	public WFNetException(String message, Throwable cause) {
		super(message, cause);
	}

	public WFNetException(String message) {
		super(message);
	}

	public WFNetException(Throwable cause) {
		super(cause);
	}
	
	
	
}
