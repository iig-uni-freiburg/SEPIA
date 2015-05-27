package de.uni.freiburg.iig.telematik.sepia.petrinet.cpn.properties.soundness;

import de.uni.freiburg.iig.telematik.sepia.exception.PNSoundnessException;
import de.uni.freiburg.iig.telematik.sepia.petrinet.cpn.abstr.AbstractCPN;
import de.uni.freiburg.iig.telematik.sepia.petrinet.cpn.abstr.AbstractCPNFlowRelation;
import de.uni.freiburg.iig.telematik.sepia.petrinet.cpn.abstr.AbstractCPNMarking;
import de.uni.freiburg.iig.telematik.sepia.petrinet.cpn.abstr.AbstractCPNPlace;
import de.uni.freiburg.iig.telematik.sepia.petrinet.cpn.abstr.AbstractCPNTransition;
import de.uni.freiburg.iig.telematik.sepia.petrinet.properties.soundness.PNSoundness;

public class CPNSoundness extends PNSoundness {

	public static <P extends AbstractCPNPlace<F>,
	  			   T extends AbstractCPNTransition<F>, 
	  			   F extends AbstractCPNFlowRelation<P,T>, 
	  			   M extends AbstractCPNMarking> 
	
		void checkSoundnedd(AbstractCPN<P,T,F,M> cpn) 
				throws PNSoundnessException {
		
	} 

}
