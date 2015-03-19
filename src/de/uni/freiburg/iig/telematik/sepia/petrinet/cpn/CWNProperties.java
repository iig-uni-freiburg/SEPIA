package de.uni.freiburg.iig.telematik.sepia.petrinet.cpn;

import de.uni.freiburg.iig.telematik.sepia.petrinet.NetCheckingProperties;
import de.uni.freiburg.iig.telematik.sepia.petrinet.PNPropertiesChecker.InOutPlaces;
import de.uni.freiburg.iig.telematik.sepia.petrinet.cpn.CWNChecker.PropertyCheckingResult;

public class CWNProperties extends NetCheckingProperties {

	public PropertyCheckingResult hasCWNStructure = PropertyCheckingResult.UNKNOWN;
	public PropertyCheckingResult optionToCompleteAndProperCompletion = PropertyCheckingResult.UNKNOWN;
	public PropertyCheckingResult noDeadTransitions = PropertyCheckingResult.UNKNOWN;
	public PropertyCheckingResult validInOutPlaces = PropertyCheckingResult.UNKNOWN;
	public PropertyCheckingResult strongConnectedness = PropertyCheckingResult.UNKNOWN;
	public PropertyCheckingResult validInitialMarking = PropertyCheckingResult.UNKNOWN;
	public PropertyCheckingResult controlFlowDependency = PropertyCheckingResult.UNKNOWN;
	public InOutPlaces inOutPlaces = null;
	
	public boolean isSoundCWN(){
		return exception == null;
	}
}
