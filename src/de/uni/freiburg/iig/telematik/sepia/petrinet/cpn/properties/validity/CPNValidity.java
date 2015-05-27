package de.uni.freiburg.iig.telematik.sepia.petrinet.cpn.properties.validity;

import de.uni.freiburg.iig.telematik.sepia.exception.PNValidationException;
import de.uni.freiburg.iig.telematik.sepia.petrinet.abstr.AbstractPetriNet;
import de.uni.freiburg.iig.telematik.sepia.petrinet.cpn.abstr.AbstractCPN;
import de.uni.freiburg.iig.telematik.sepia.petrinet.cpn.abstr.AbstractCPNFlowRelation;
import de.uni.freiburg.iig.telematik.sepia.petrinet.cpn.abstr.AbstractCPNMarking;
import de.uni.freiburg.iig.telematik.sepia.petrinet.cpn.abstr.AbstractCPNPlace;
import de.uni.freiburg.iig.telematik.sepia.petrinet.cpn.abstr.AbstractCPNTransition;
import de.uni.freiburg.iig.telematik.sepia.petrinet.properties.validity.PNValidity;

public class CPNValidity extends PNValidity{
	
	/**
	 * CPNs require relation effectiveness.<br>
	 * Each relation must move at least one token from a place to a transition or vice versa.
	 * 
	 * @see AbstractPetriNet#checkValidity()
	 * @throws PNValidationException 
	 * @see AbstractCPNFlowRelation#hasConstraints()
	 */
	public static <P extends AbstractCPNPlace<F>,
	  			   T extends AbstractCPNTransition<F>, 
	  			   F extends AbstractCPNFlowRelation<P,T>, 
	  			   M extends AbstractCPNMarking> 
	
		void checkValidity(AbstractCPN<P,T,F,M> cpn) 
				throws PNValidationException {
		
		defaultValidity(cpn);
		
		for(AbstractCPNFlowRelation<P,T> relation: cpn.getFlowRelations()){
			if(!relation.hasConstraints()){
				throw new PNValidationException("Inoperative relation: " + relation.toString());
			} 
		}
	} 

}
