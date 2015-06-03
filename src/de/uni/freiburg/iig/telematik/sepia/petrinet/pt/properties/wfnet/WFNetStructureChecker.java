package de.uni.freiburg.iig.telematik.sepia.petrinet.pt.properties.wfnet;

import de.uni.freiburg.iig.telematik.sepia.exception.PNValidationException;
import de.uni.freiburg.iig.telematik.sepia.petrinet.properties.PNProperties;
import de.uni.freiburg.iig.telematik.sepia.petrinet.properties.PropertyCheckingResult;
import de.uni.freiburg.iig.telematik.sepia.petrinet.pt.abstr.AbstractPTFlowRelation;
import de.uni.freiburg.iig.telematik.sepia.petrinet.pt.abstr.AbstractPTMarking;
import de.uni.freiburg.iig.telematik.sepia.petrinet.pt.abstr.AbstractPTNet;
import de.uni.freiburg.iig.telematik.sepia.petrinet.pt.abstr.AbstractPTPlace;
import de.uni.freiburg.iig.telematik.sepia.petrinet.pt.abstr.AbstractPTTransition;

public class WFNetStructureChecker {

	public static 	<P extends AbstractPTPlace<F>, 
	 				 T extends AbstractPTTransition<F>, 
	 				 F extends AbstractPTFlowRelation<P,T>,
	 				 M extends AbstractPTMarking,
	 				 N extends AbstractPTNet<P,T,F,M>> 

	WFNetProperties checkWFNetStructure(N petriNet) {
		
		WFNetProperties result = new WFNetProperties();
		
		// Check if there is only one input/output place
		try {
			result.inOutPlaces = PNProperties.validateInputOutputPlace(petriNet);
		} catch (PNValidationException e) {
			result.exception = e;
			result.validInOutPlaces = PropertyCheckingResult.FALSE;
			result.hasWFNetStructure = PropertyCheckingResult.FALSE;
			return result;
		}
		result.validInOutPlaces = PropertyCheckingResult.TRUE;
		
		// Check strongly connectedness of short-circuited net
		try {
			PNProperties.validateStrongConnectedness(petriNet, result.inOutPlaces);
		} catch (PNValidationException e) {
			result.exception = e;
			result.strongConnectedness = PropertyCheckingResult.FALSE;
			result.hasWFNetStructure = PropertyCheckingResult.FALSE;
			return result;
		}
		result.strongConnectedness = PropertyCheckingResult.TRUE;
		
		result.hasWFNetStructure = PropertyCheckingResult.TRUE;
		return result;
	}
	
	//TODO: WFNet soundness + structuredness
}