package de.uni.freiburg.iig.telematik.sepia.petrinet.cpn.properties.cwn.soundness;

import de.uni.freiburg.iig.telematik.sepia.petrinet.cpn.properties.cwn.structure.CWNStructureProperties;
import de.uni.freiburg.iig.telematik.sepia.petrinet.properties.PNProperties.InOutPlaces;
import de.uni.freiburg.iig.telematik.sepia.petrinet.properties.PropertyCheckingResult;

public class CWNSoundnessProperties extends CWNStructureProperties {
	
	public PropertyCheckingResult hasCWNStructure = PropertyCheckingResult.UNKNOWN;
	public PropertyCheckingResult optionToCompleteAndProperCompletion = PropertyCheckingResult.UNKNOWN;
	public PropertyCheckingResult noDeadTransitions = PropertyCheckingResult.UNKNOWN;
	public PropertyCheckingResult isBounded = PropertyCheckingResult.UNKNOWN;
	public InOutPlaces inOutPlaces = null;
	
	public CWNSoundnessProperties(){}
	
	public CWNSoundnessProperties(CWNStructureProperties structureProperties){
		this.validInOutPlaces = structureProperties.validInOutPlaces;
		this.controlFlowDependency = structureProperties.controlFlowDependency;
		this.strongConnectedness = structureProperties.strongConnectedness;
		this.validInitialMarking = structureProperties.validInitialMarking;
		this.exception = structureProperties.exception;
		this.hasCWNStructure = structureProperties.exception != null ? PropertyCheckingResult.FALSE : PropertyCheckingResult.TRUE;
	}
	
	public boolean isSoundCWN(){
		return exception == null;
	}
	
	@Override
	public boolean hasCWNStructure(){
		return hasCWNStructure.equals(PropertyCheckingResult.TRUE);
	}
}
