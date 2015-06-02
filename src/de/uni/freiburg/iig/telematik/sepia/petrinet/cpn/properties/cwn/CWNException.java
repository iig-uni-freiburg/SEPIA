package de.uni.freiburg.iig.telematik.sepia.petrinet.cpn.properties.cwn;

import de.uni.freiburg.iig.telematik.sepia.exception.PNException;
import de.uni.freiburg.iig.telematik.sepia.petrinet.cpn.properties.cwn.structure.CWNStructureProperties;

public class CWNException extends PNException{

	private static final long serialVersionUID = 4260371301120243956L;
	
	private CWNStructureProperties properties = null;

	public CWNException(CWNStructureProperties properties) {
		super();
		this.properties = properties;
	}

	public CWNException(String message, Throwable cause, CWNStructureProperties properties) {
		super(message, cause);
		this.properties = properties;
	}

	public CWNException(String message, CWNStructureProperties properties) {
		super(message);
		this.properties = properties;
	}

	public CWNException(Throwable cause, CWNStructureProperties properties) {
		super(cause);
		this.properties = properties;
	}
	
	public CWNStructureProperties getProperties(){
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
