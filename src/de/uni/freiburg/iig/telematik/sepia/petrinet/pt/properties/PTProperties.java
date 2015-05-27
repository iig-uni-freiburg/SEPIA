package de.uni.freiburg.iig.telematik.sepia.petrinet.pt.properties;

import de.invation.code.toval.validate.ParameterException;
import de.invation.code.toval.validate.ParameterException.ErrorCode;
import de.invation.code.toval.validate.Validate;
import de.uni.freiburg.iig.telematik.sepia.exception.PNValidationException;
import de.uni.freiburg.iig.telematik.sepia.petrinet.pt.abstr.AbstractPTFlowRelation;
import de.uni.freiburg.iig.telematik.sepia.petrinet.pt.abstr.AbstractPTMarking;
import de.uni.freiburg.iig.telematik.sepia.petrinet.pt.abstr.AbstractPTNet;
import de.uni.freiburg.iig.telematik.sepia.petrinet.pt.abstr.AbstractPTPlace;
import de.uni.freiburg.iig.telematik.sepia.petrinet.pt.abstr.AbstractPTTransition;

public class PTProperties {

	public static 	<P extends AbstractPTPlace<F>, 
	 				 T extends AbstractPTTransition<F>, 
	 				 F extends AbstractPTFlowRelation<P,T>,
	 				 M extends AbstractPTMarking,
	 				 N extends AbstractPTNet<P,T,F,M>> 

	void checkSingleCFTokenInInitialMarking(N petriNet, String placeName) throws PNValidationException {
		Validate.notNull(petriNet);
		Validate.notNull(placeName);
		if(!petriNet.containsPlace(placeName))
			throw new ParameterException(ErrorCode.INCOMPATIBILITY, "Net does not contain a place with name \""+placeName+"\"");
		
		P place = petriNet.getPlace(placeName);
		M initialMarking = petriNet.getInitialMarking();
		if(!initialMarking.contains(place.getName()))
			throw new PNValidationException("Initial marking must contain input place " + place);
		if(initialMarking.places().size() > 1)
			throw new PNValidationException("Initial marking must only contain input place " + place);
		if((initialMarking.get(place.getName())) > 1)
			throw new PNValidationException("Initial marking must only contain one token in input place " + place);	
	}
}
