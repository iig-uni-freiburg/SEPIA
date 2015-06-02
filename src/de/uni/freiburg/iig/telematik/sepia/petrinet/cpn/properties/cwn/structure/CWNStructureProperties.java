package de.uni.freiburg.iig.telematik.sepia.petrinet.cpn.properties.cwn.structure;

import de.uni.freiburg.iig.telematik.sepia.petrinet.properties.NetCheckingProperties;
import de.uni.freiburg.iig.telematik.sepia.petrinet.properties.PNProperties.InOutPlaces;
import de.uni.freiburg.iig.telematik.sepia.petrinet.properties.PropertyCheckingResult;

public class CWNStructureProperties extends NetCheckingProperties {

	public PropertyCheckingResult validInOutPlaces = PropertyCheckingResult.UNKNOWN;
	public PropertyCheckingResult strongConnectedness = PropertyCheckingResult.UNKNOWN;
	public PropertyCheckingResult validInitialMarking = PropertyCheckingResult.UNKNOWN;
	public PropertyCheckingResult controlFlowDependency = PropertyCheckingResult.UNKNOWN;
	public InOutPlaces inOutPlaces = null;
	
	public boolean hasCWNStructure(){
		return exception == null;
	}
}
