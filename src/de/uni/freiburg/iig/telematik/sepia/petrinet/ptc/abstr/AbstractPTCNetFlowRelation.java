package de.uni.freiburg.iig.telematik.sepia.petrinet.ptc.abstr;

import de.uni.freiburg.iig.telematik.sepia.petrinet.pt.abstr.AbstractPTFlowRelation;

public abstract class AbstractPTCNetFlowRelation<P extends AbstractPTCNetPlace<? extends AbstractPTCNetFlowRelation<P,T>>, 
			T extends AbstractPTCNetTransition<? extends AbstractPTCNetFlowRelation<P,T>>> 

		  extends AbstractPTFlowRelation<P, T>{


private static final long serialVersionUID = -85597409112336075L;

public AbstractPTCNetFlowRelation(P place, T transition, Integer weight) {
super(place, transition, weight);
}

public AbstractPTCNetFlowRelation(P place, T transition) {
super(place, transition);
}

public AbstractPTCNetFlowRelation(T transition, P place, Integer weight) {
super(transition, place, weight);
}

public AbstractPTCNetFlowRelation(T transition, P place) {
super(transition, place);
}

}
