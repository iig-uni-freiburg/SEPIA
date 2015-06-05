package de.uni.freiburg.iig.telematik.sepia.petrinet.pt.properties.soundness;

import de.uni.freiburg.iig.telematik.sepia.exception.PNSoundnessException;
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
	 * @throws PNSoundnessException If the checked Petri net is not sound
	 */
	public static <P extends AbstractPTPlace<F>, 
	   			   T extends AbstractPTTransition<F>, 
	   			   F extends AbstractPTFlowRelation<P,T>, 
	   			   M extends AbstractPTMarking>

		void checkSoundness(AbstractPTNet<P,T,F,M> petriNet, boolean checkValidity) 
				throws PNSoundnessException{
		if(checkValidity){
			try {
				PTNetValidity.checkValidity(petriNet);
			} catch (PNValidationException e) {
				throw new PNSoundnessException("Underlying net is not valid.\nReason: " + e.getMessage(), e);
			}
		}
	}
	
}
