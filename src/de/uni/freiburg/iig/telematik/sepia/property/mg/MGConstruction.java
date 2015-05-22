package de.uni.freiburg.iig.telematik.sepia.property.mg;

import de.invation.code.toval.thread.ExecutorListener;
import de.uni.freiburg.iig.telematik.sepia.mg.abstr.AbstractMarkingGraph;
import de.uni.freiburg.iig.telematik.sepia.mg.abstr.AbstractMarkingGraphRelation;
import de.uni.freiburg.iig.telematik.sepia.mg.abstr.AbstractMarkingGraphState;
import de.uni.freiburg.iig.telematik.sepia.petrinet.AbstractFlowRelation;
import de.uni.freiburg.iig.telematik.sepia.petrinet.AbstractMarking;
import de.uni.freiburg.iig.telematik.sepia.petrinet.AbstractPetriNet;
import de.uni.freiburg.iig.telematik.sepia.petrinet.AbstractPlace;
import de.uni.freiburg.iig.telematik.sepia.petrinet.AbstractTransition;

public class MGConstruction {
	
	public static final int MAX_RG_CALCULATION_STEPS = Integer.MAX_VALUE;
	
	/**
	 * Builds the marking graph for the given Petri net,<br>
	 * i.e. a graph containing all reachable markings and their relation.<br>
	 *  
	 * @param petriNet The basic Petri net for operation.
	 * @return The marking graph of the given Petri net.
	 * @throws MarkingGraphException
	 */
	public static <	P extends AbstractPlace<F,S>, 
					T extends AbstractTransition<F,S>, 
					F extends AbstractFlowRelation<P,T,S>, 
					M extends AbstractMarking<S>, 
					S extends Object,
					X extends AbstractMarkingGraphState<M,S>,
					Y extends AbstractMarkingGraphRelation<M,X,S>>

	void initiateMarkingGraphConstruction(AbstractPetriNet<P,T,F,M,S> petriNet, ExecutorListener listener) 
			throws MarkingGraphException {
		
		initiateMarkingGraphConstruction(new ThreadedMGCalculator<P,T,F,M,S,X,Y>(petriNet), listener);
	}

	/**
	 * Builds the marking graph for the given Petri net,<br>
	 * i.e. a graph containing all reachable markings and their relation.<br>
	 *  
	 * @param petriNet The basic Petri net for operation.
	 * @return The marking graph of the given Petri net.
	 * @throws MarkingGraphException
	 */
	public static <	P extends AbstractPlace<F,S>, 
					T extends AbstractTransition<F,S>, 
					F extends AbstractFlowRelation<P,T,S>, 
					M extends AbstractMarking<S>, 
					S extends Object,
					X extends AbstractMarkingGraphState<M,S>,
					Y extends AbstractMarkingGraphRelation<M,X,S>>

	void initiateMarkingGraphConstruction(MGCalculator<P,T,F,M,S,X,Y> calculator, ExecutorListener listener) 
			throws MarkingGraphException {
		
		calculator.addExecutorListener(listener);
		calculator.runCalculation();
	}
	
	
	/**
	 * Builds the marking graph for the given Petri net,<br>
	 * i.e. a graph containing all reachable markings and their relation.<br>
	 *
	 * @param petriNet The basic Petri net for operation.
	 * @return The marking graph of the given Petri net.
	 * @throws MarkingGraphException
	 */
	public static <	P extends AbstractPlace<F,S>, 
					T extends AbstractTransition<F,S>, 
					F extends AbstractFlowRelation<P,T,S>, 
					M extends AbstractMarking<S>, 
					S extends Object,
					X extends AbstractMarkingGraphState<M,S>,
					Y extends AbstractMarkingGraphRelation<M,X,S>>

	AbstractMarkingGraph<M,S,X,Y> buildMarkingGraph(MGCalculator<P,T,F,M,S,X,Y> calculator) 
			throws MarkingGraphException {
		
		calculator.runCalculation();
	
		return calculator.getMarkingGraph();
	}
	
	/**
	 * Builds the marking graph for the given Petri net,<br>
	 * i.e. a graph containing all reachable markings and their relation.<br>
	 *  
	 * @param petriNet The basic Petri net for operation.
	 * @return The marking graph of the given Petri net.
	 * @throws MarkingGraphException
	 */
	public static <	P extends AbstractPlace<F,S>, 
					T extends AbstractTransition<F,S>, 
					F extends AbstractFlowRelation<P,T,S>, 
					M extends AbstractMarking<S>, 
					S extends Object,
					X extends AbstractMarkingGraphState<M,S>,
					Y extends AbstractMarkingGraphRelation<M,X,S>>

	
	AbstractMarkingGraph<M,S,X,Y> buildMarkingGraph(AbstractPetriNet<P,T,F,M,S> petriNet) 
			throws MarkingGraphException {
		
		return buildMarkingGraph(new ThreadedMGCalculator<P,T,F,M,S,X,Y>(petriNet));
	}

}
