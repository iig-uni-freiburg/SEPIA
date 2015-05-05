package de.uni.freiburg.iig.telematik.sepia.util;

import de.uni.freiburg.iig.telematik.sepia.exception.MarkingGraphException;
import de.uni.freiburg.iig.telematik.sepia.mg.abstr.AbstractMarkingGraph;
import de.uni.freiburg.iig.telematik.sepia.mg.abstr.AbstractMarkingGraphRelation;
import de.uni.freiburg.iig.telematik.sepia.mg.abstr.AbstractMarkingGraphState;
import de.uni.freiburg.iig.telematik.sepia.petrinet.AbstractFlowRelation;
import de.uni.freiburg.iig.telematik.sepia.petrinet.AbstractMarking;
import de.uni.freiburg.iig.telematik.sepia.petrinet.AbstractPlace;
import de.uni.freiburg.iig.telematik.sepia.petrinet.AbstractTransition;

public interface MGCalculator<	P extends AbstractPlace<F,S>, 
								T extends AbstractTransition<F,S>, 
								F extends AbstractFlowRelation<P,T,S>, 
								M extends AbstractMarking<S>, 
								S extends Object,
								X extends AbstractMarkingGraphState<M,S>,
								Y extends AbstractMarkingGraphRelation<M,X,S>> {
	
	public AbstractMarkingGraph<M,S,X,Y> getMarkingGraph() throws MarkingGraphException;

}
