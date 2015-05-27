package de.uni.freiburg.iig.telematik.sepia.petrinet.pt.properties.soundness;

import de.uni.freiburg.iig.telematik.sepia.exception.PNValidationException;
import de.uni.freiburg.iig.telematik.sepia.petrinet.pt.abstr.AbstractPTFlowRelation;
import de.uni.freiburg.iig.telematik.sepia.petrinet.pt.abstr.AbstractPTMarking;
import de.uni.freiburg.iig.telematik.sepia.petrinet.pt.abstr.AbstractPTNet;
import de.uni.freiburg.iig.telematik.sepia.petrinet.pt.abstr.AbstractPTPlace;
import de.uni.freiburg.iig.telematik.sepia.petrinet.pt.abstr.AbstractPTTransition;
import de.uni.freiburg.iig.telematik.sepia.petrinet.pt.properties.validity.PTNetValidity;

public class PTNetSoundness {

	/**
	 * Checks if a P/T-net is sound.<br>
	 * In contrast to validity, soundness of a net relates to more complex net properties,
	 * such as reachability properties or specific structural restrictions.<br>
	 * Soundness implies validity.<br>
	 * 
	 * @param checkValidity Indicates, if the method also checks the validity of the net as a precondition.
	 * @throws PNValidationException
	 */
	public static <P extends AbstractPTPlace<F>, 
	   			   T extends AbstractPTTransition<F>, 
	   			   F extends AbstractPTFlowRelation<P,T>, 
	   			   M extends AbstractPTMarking>

		void checkSoundness(AbstractPTNet<P,T,F,M> petriNet, boolean checkValidity) 
				throws PNValidationException{
		if(checkValidity)
			PTNetValidity.checkValidity(petriNet);
	}
	
}
