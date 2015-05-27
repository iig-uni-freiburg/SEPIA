package de.uni.freiburg.iig.telematik.sepia.petrinet.properties;

import de.uni.freiburg.iig.telematik.sepia.petrinet.abstr.AbstractPetriNet.Boundedness;

public enum PropertyCheckingResult {
	TRUE, FALSE, UNKNOWN;
	
	public static PropertyCheckingResult fromBoundedness(Boundedness boundedness){
		switch(boundedness){
		case BOUNDED: return TRUE;
		case UNBOUNDED: return FALSE;
		case UNKNOWN: return UNKNOWN;
		default: return null;
		}
	}
}
