package de.uni.freiburg.iig.telematik.sepia.petrinet.ifnet.abstr;

import de.invation.code.toval.validate.ParameterException;
import de.uni.freiburg.iig.telematik.sepia.petrinet.cwn.abstr.AbstractCWNFlowRelation;

public abstract class AbstractIFNetFlowRelation<P extends AbstractIFNetPlace<? extends AbstractIFNetFlowRelation<P,T>>, 
		  										T extends AbstractIFNetTransition<? extends AbstractIFNetFlowRelation<P,T>>> 

												  extends AbstractCWNFlowRelation<P, T>{

	public AbstractIFNetFlowRelation(P place, T transition) throws ParameterException {
		super(place, transition);
	}

	public AbstractIFNetFlowRelation(T transition, P place) throws ParameterException {
		super(transition, place);
	}

}
