package de.uni.freiburg.iig.telematik.sepia.petrinet.pt.properties.wfnet;

import de.uni.freiburg.iig.telematik.sepia.mg.abstr.AbstractMarkingGraph;
import de.uni.freiburg.iig.telematik.sepia.petrinet.properties.NetCheckingProperties;
import de.uni.freiburg.iig.telematik.sepia.petrinet.properties.PNProperties.InOutPlaces;
import de.uni.freiburg.iig.telematik.sepia.petrinet.properties.PropertyCheckingResult;

public class WFNetProperties extends NetCheckingProperties {

	public PropertyCheckingResult hasWFNetStructure = PropertyCheckingResult.UNKNOWN;
	public PropertyCheckingResult validInOutPlaces = PropertyCheckingResult.UNKNOWN;
	public PropertyCheckingResult strongConnectedness = PropertyCheckingResult.UNKNOWN;
	public PropertyCheckingResult safeness = PropertyCheckingResult.UNKNOWN;
	public PropertyCheckingResult optionToCompleteAndProperCompletion = PropertyCheckingResult.UNKNOWN;
	public PropertyCheckingResult isSoundWFNet = PropertyCheckingResult.UNKNOWN;
	public PropertyCheckingResult isStructuredWFNet = PropertyCheckingResult.UNKNOWN;
	public PropertyCheckingResult noDeadTransitions = PropertyCheckingResult.UNKNOWN;
	public PropertyCheckingResult validInitialMarking = PropertyCheckingResult.UNKNOWN;
	public PropertyCheckingResult isBounded = PropertyCheckingResult.UNKNOWN;
	public InOutPlaces inOutPlaces = null;
	public AbstractMarkingGraph markingGraph = null;
	
	public boolean isWFNet(){
		return exception == null;
	}
}
