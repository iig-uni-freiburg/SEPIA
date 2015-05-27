package de.uni.freiburg.iig.telematik.sepia.petrinet.pt.properties.validity;

import de.uni.freiburg.iig.telematik.sepia.exception.PNValidationException;
import de.uni.freiburg.iig.telematik.sepia.petrinet.properties.validity.PNValidity;
import de.uni.freiburg.iig.telematik.sepia.petrinet.pt.abstr.AbstractPTFlowRelation;
import de.uni.freiburg.iig.telematik.sepia.petrinet.pt.abstr.AbstractPTMarking;
import de.uni.freiburg.iig.telematik.sepia.petrinet.pt.abstr.AbstractPTNet;
import de.uni.freiburg.iig.telematik.sepia.petrinet.pt.abstr.AbstractPTPlace;
import de.uni.freiburg.iig.telematik.sepia.petrinet.pt.abstr.AbstractPTTransition;

public class PTNetValidity extends PNValidity {

	public static <P extends AbstractPTPlace<F>, 
				   T extends AbstractPTTransition<F>, 
				   F extends AbstractPTFlowRelation<P,T>, 
				   M extends AbstractPTMarking>

		void checkValidity(AbstractPTNet<P,T,F,M> petriNet) 
				throws PNValidationException{
		defaultValidity(petriNet);
	}
	
}
