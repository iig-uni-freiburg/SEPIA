package de.uni.freiburg.iig.telematik.sepia.petrinet.cpn.properties.soundness;

import de.uni.freiburg.iig.telematik.sepia.exception.PNSoundnessException;
import de.uni.freiburg.iig.telematik.sepia.exception.PNValidationException;
import de.uni.freiburg.iig.telematik.sepia.petrinet.cpn.abstr.AbstractCPN;
import de.uni.freiburg.iig.telematik.sepia.petrinet.cpn.abstr.AbstractCPNFlowRelation;
import de.uni.freiburg.iig.telematik.sepia.petrinet.cpn.abstr.AbstractCPNMarking;
import de.uni.freiburg.iig.telematik.sepia.petrinet.cpn.abstr.AbstractCPNPlace;
import de.uni.freiburg.iig.telematik.sepia.petrinet.cpn.abstr.AbstractCPNTransition;
import de.uni.freiburg.iig.telematik.sepia.petrinet.cpn.properties.validity.CPNValidity;
import de.uni.freiburg.iig.telematik.sepia.petrinet.properties.soundness.PNSoundness;

public class CPNSoundness extends PNSoundness {

	public static <P extends AbstractCPNPlace<F>,
	  			   T extends AbstractCPNTransition<F>, 
	  			   F extends AbstractCPNFlowRelation<P,T>, 
	  			   M extends AbstractCPNMarking> 
	
		void checkSoundness(AbstractCPN<P,T,F,M> cpn, boolean checkValidity) 
				throws PNSoundnessException {
		if(checkValidity){
			try {
				CPNValidity.checkValidity(cpn);
			} catch (PNValidationException e) {
				throw new PNSoundnessException("Underlying net is not valid.\nReason: " + e.getMessage(), e);
			}
		}
	} 

}
