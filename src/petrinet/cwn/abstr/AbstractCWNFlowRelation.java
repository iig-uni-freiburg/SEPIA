package petrinet.cwn.abstr;

import petrinet.cpn.abstr.AbstractCPNFlowRelation;
import validate.ParameterException;

public abstract class AbstractCWNFlowRelation<P extends AbstractCWNPlace<? extends AbstractCWNFlowRelation<P,T>>, 
		  									  T extends AbstractCWNTransition<? extends AbstractCWNFlowRelation<P,T>>> 

												extends AbstractCPNFlowRelation<P, T>{

	public AbstractCWNFlowRelation(P place, T transition) throws ParameterException {
		super(place, transition, true);
	}

	public AbstractCWNFlowRelation(T transition, P place) throws ParameterException {
		super(transition, place, true);
	}

}
