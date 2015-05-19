package de.uni.freiburg.iig.telematik.sepia.property.mg;

import de.invation.code.toval.thread.AbstractCallable;
import de.invation.code.toval.thread.ExecutorListener;
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
	
	public void runCalculation();
	
	public AbstractCallable<AbstractMarkingGraph<M,S,X,Y>> getCallable();
	
	public void addExecutorListener(ExecutorListener listener);
	
	public void removeExecutorListener(ExecutorListener listener);

}
