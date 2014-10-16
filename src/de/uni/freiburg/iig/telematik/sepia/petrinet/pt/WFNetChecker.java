package de.uni.freiburg.iig.telematik.sepia.petrinet.pt;

import de.uni.freiburg.iig.telematik.sepia.exception.PNValidationException;
import de.uni.freiburg.iig.telematik.sepia.mg.pt.AbstractPTMarkingGraphRelation;
import de.uni.freiburg.iig.telematik.sepia.mg.pt.AbstractPTMarkingGraphState;
import de.uni.freiburg.iig.telematik.sepia.petrinet.PNPropertiesChecker;
import de.uni.freiburg.iig.telematik.sepia.petrinet.PNPropertiesChecker.InOutPlaces;
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
	 				 X extends AbstractPTMarkingGraphState<M>, 
	 				 Y extends AbstractPTMarkingGraphRelation<M,X>,
	 				 N extends AbstractPTNet<P,T,F,M,X,Y>> 

	void checkWFNetStructure(N petriNet) throws PNValidationException {
		InOutPlaces places = PNPropertiesChecker.validateInputOutputPlace(petriNet);
		PNPropertiesChecker.validateStrongConnectedness(petriNet, places);
	}
	
	public static 	<P extends AbstractPTPlace<F>, 
	 				 T extends AbstractPTTransition<F>, 
	 				 F extends AbstractPTFlowRelation<P,T>,
	 				 M extends AbstractPTMarking,
	 				 X extends AbstractPTMarkingGraphState<M>, 
	 				 Y extends AbstractPTMarkingGraphRelation<M,X>,
	 				 N extends AbstractPTNet<P,T,F,M,X,Y>> 

	boolean hasWFNetStructure(N petriNet) {
		try{
			checkWFNetStructure(petriNet);
		} catch(PNValidationException e){
			return false;
		}
		return true;
	}
	
	//TODO: WFNet soundness + structuredness
}