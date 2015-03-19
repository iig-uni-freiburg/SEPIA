package de.uni.freiburg.iig.telematik.sepia.petrinet.pt;

import de.uni.freiburg.iig.telematik.sepia.petrinet.NetCheckingProperties;
import de.uni.freiburg.iig.telematik.sepia.petrinet.PNPropertiesChecker.InOutPlaces;
import de.uni.freiburg.iig.telematik.sepia.petrinet.cpn.CWNChecker.PropertyCheckingResult;

public class WFNetProperties extends NetCheckingProperties {

	public PropertyCheckingResult hasWFNetStructure = PropertyCheckingResult.UNKNOWN;
	public PropertyCheckingResult validInOutPlaces = PropertyCheckingResult.UNKNOWN;
	public PropertyCheckingResult strongConnectedness = PropertyCheckingResult.UNKNOWN;
	public InOutPlaces inOutPlaces = null;
	
	public boolean isSoundWFNet(){
		return exception == null;
	}
}
