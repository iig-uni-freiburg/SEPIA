package de.uni.freiburg.iig.telematik.sepia.petrinet.ifnet.abstr;

import de.uni.freiburg.iig.telematik.sepia.petrinet.cwn.abstr.AbstractCWNFlowRelation;

public abstract class AbstractIFNetFlowRelation<P extends AbstractIFNetPlace<? extends AbstractIFNetFlowRelation<P,T>>, 
		  										T extends AbstractIFNetTransition<? extends AbstractIFNetFlowRelation<P,T>>> 

												  extends AbstractCWNFlowRelation<P, T>{


	private static final long serialVersionUID = -85597409112336075L;

	public AbstractIFNetFlowRelation(P place, T transition) {
		super(place, transition);
	}

	public AbstractIFNetFlowRelation(T transition, P place) {
		super(transition, place);
	}

}
