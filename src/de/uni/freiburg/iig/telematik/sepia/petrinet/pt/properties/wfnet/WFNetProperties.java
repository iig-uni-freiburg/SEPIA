package de.uni.freiburg.iig.telematik.sepia.petrinet.pt.properties.wfnet;

import de.uni.freiburg.iig.telematik.sepia.petrinet.properties.NetCheckingProperties;
import de.uni.freiburg.iig.telematik.sepia.petrinet.properties.PNProperties.InOutPlaces;
import de.uni.freiburg.iig.telematik.sepia.petrinet.properties.PropertyCheckingResult;

public class WFNetProperties extends NetCheckingProperties {

	public PropertyCheckingResult hasWFNetStructure = PropertyCheckingResult.UNKNOWN;
	public PropertyCheckingResult validInOutPlaces = PropertyCheckingResult.UNKNOWN;
	public PropertyCheckingResult strongConnectedness = PropertyCheckingResult.UNKNOWN;
	public InOutPlaces inOutPlaces = null;
	
	public boolean isSoundWFNet(){
		return exception == null;
	}
}
