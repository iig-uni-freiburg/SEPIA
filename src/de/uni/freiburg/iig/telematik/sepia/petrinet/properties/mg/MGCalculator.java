package de.uni.freiburg.iig.telematik.sepia.petrinet.properties.mg;

import de.invation.code.toval.thread.AbstractCallable;
import de.invation.code.toval.thread.ExecutorListener;
import de.uni.freiburg.iig.telematik.sepia.mg.abstr.AbstractMarkingGraph;
import de.uni.freiburg.iig.telematik.sepia.petrinet.abstr.AbstractFlowRelation;
import de.uni.freiburg.iig.telematik.sepia.petrinet.abstr.AbstractMarking;
import de.uni.freiburg.iig.telematik.sepia.petrinet.abstr.AbstractPlace;
import de.uni.freiburg.iig.telematik.sepia.petrinet.abstr.AbstractTransition;

public interface MGCalculator<	P extends AbstractPlace<F,S>, 
								T extends AbstractTransition<F,S>, 
								F extends AbstractFlowRelation<P,T,S>, 
								M extends AbstractMarking<S>, 
								S extends Object> {
	
	public AbstractMarkingGraph<M,S,?,?> getMarkingGraph() throws MarkingGraphException;
	
	public void runCalculation();
	
	public AbstractCallable<AbstractMarkingGraph<M,S,?,?>> getCallable();
	
	public void addExecutorListener(ExecutorListener listener);
	
	public void removeExecutorListener(ExecutorListener listener);

}
