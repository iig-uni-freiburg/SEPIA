package de.uni.freiburg.iig.telematik.sepia.petrinet.cpn.properties.cwn;

import de.uni.freiburg.iig.telematik.sepia.petrinet.properties.NetCheckingProperties;
import de.uni.freiburg.iig.telematik.sepia.petrinet.properties.PNProperties.InOutPlaces;
import de.uni.freiburg.iig.telematik.sepia.petrinet.properties.PropertyCheckingResult;

public class CWNProperties extends NetCheckingProperties {

	public PropertyCheckingResult hasCWNStructure = PropertyCheckingResult.UNKNOWN;
	public PropertyCheckingResult optionToCompleteAndProperCompletion = PropertyCheckingResult.UNKNOWN;
	public PropertyCheckingResult noDeadTransitions = PropertyCheckingResult.UNKNOWN;
	public PropertyCheckingResult validInOutPlaces = PropertyCheckingResult.UNKNOWN;
	public PropertyCheckingResult strongConnectedness = PropertyCheckingResult.UNKNOWN;
	public PropertyCheckingResult validInitialMarking = PropertyCheckingResult.UNKNOWN;
	public PropertyCheckingResult controlFlowDependency = PropertyCheckingResult.UNKNOWN;
	public PropertyCheckingResult isBounded = PropertyCheckingResult.UNKNOWN;
	public PropertyCheckingResult isSoundCWN = PropertyCheckingResult.UNKNOWN;
	public InOutPlaces inOutPlaces = null;
	
	public boolean hasCWNStructure(){
		return hasCWNStructure.equals(PropertyCheckingResult.TRUE);
	}
	
	public boolean isSoundCWN(){
		return isSoundCWN.equals(PropertyCheckingResult.TRUE);
	}
}
