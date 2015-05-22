package de.uni.freiburg.iig.telematik.sepia.petrinet.pt;

import de.uni.freiburg.iig.telematik.sepia.exception.PNValidationException;
import de.uni.freiburg.iig.telematik.sepia.petrinet.PNPropertiesChecker;
import de.uni.freiburg.iig.telematik.sepia.petrinet.cpn.CWNChecker.PropertyCheckingResult;
import de.uni.freiburg.iig.telematik.sepia.petrinet.pt.abstr.AbstractPTFlowRelation;
import de.uni.freiburg.iig.telematik.sepia.petrinet.pt.abstr.AbstractPTMarking;
import de.uni.freiburg.iig.telematik.sepia.petrinet.pt.abstr.AbstractPTNet;
import de.uni.freiburg.iig.telematik.sepia.petrinet.pt.abstr.AbstractPTPlace;
import de.uni.freiburg.iig.telematik.sepia.petrinet.pt.abstr.AbstractPTTransition;

public class WFNetChecker {

	public static 	<P extends AbstractPTPlace<F>, 
	 				 T extends AbstractPTTransition<F>, 
	 				 F extends AbstractPTFlowRelation<P,T>,
	 				 M extends AbstractPTMarking,
	 				 N extends AbstractPTNet<P,T,F,M>> 

	WFNetProperties checkWFNetStructure(N petriNet) {
		
		WFNetProperties result = new WFNetProperties();
		
		// Check if there is only one input/output place
		try {
			result.inOutPlaces = PNPropertiesChecker.validateInputOutputPlace(petriNet);
		} catch (PNValidationException e) {
			result.exception = e;
			result.validInOutPlaces = PropertyCheckingResult.FALSE;
			result.hasWFNetStructure = PropertyCheckingResult.FALSE;
			return result;
		}
		P input = petriNet.getPlace(result.inOutPlaces.getInput());
		result.validInOutPlaces = PropertyCheckingResult.TRUE;
		
		// Check strongly connectedness of short-circuited net
//		try {
//			PNPropertiesChecker.validateStrongConnectedness(petriNet, result.inOutPlaces);
//		} catch (PNValidationException e) {
//			result.exception = e;
//			result.strongConnectedness = PropertyCheckingResult.FALSE;
//			result.hasWFNetStructure = PropertyCheckingResult.FALSE;
//			return result;
//		}
//		result.strongConnectedness = PropertyCheckingResult.TRUE;
		
		result.hasWFNetStructure = PropertyCheckingResult.TRUE;
		return result;
	}
	
	public static 	<P extends AbstractPTPlace<F>, 
	 				 T extends AbstractPTTransition<F>, 
	 				 F extends AbstractPTFlowRelation<P,T>,
	 				 M extends AbstractPTMarking,
	 				 N extends AbstractPTNet<P,T,F,M>> 

	boolean hasWFNetStructure(N petriNet) {
		return checkWFNetStructure(petriNet).exception == null;
	}
	
	//TODO: WFNet soundness + structuredness
}