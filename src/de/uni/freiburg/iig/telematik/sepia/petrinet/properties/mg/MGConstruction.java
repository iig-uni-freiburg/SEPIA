package de.uni.freiburg.iig.telematik.sepia.petrinet.properties.mg;

import de.invation.code.toval.thread.ExecutorListener;
import de.uni.freiburg.iig.telematik.sepia.mg.abstr.AbstractMarkingGraph;
import de.uni.freiburg.iig.telematik.sepia.petrinet.abstr.AbstractFlowRelation;
import de.uni.freiburg.iig.telematik.sepia.petrinet.abstr.AbstractMarking;
import de.uni.freiburg.iig.telematik.sepia.petrinet.abstr.AbstractPetriNet;
import de.uni.freiburg.iig.telematik.sepia.petrinet.abstr.AbstractPlace;
import de.uni.freiburg.iig.telematik.sepia.petrinet.abstr.AbstractTransition;

public class MGConstruction {
	
	public static final int MAX_RG_CALCULATION_STEPS = Integer.MAX_VALUE;
	
	/**
	 * Builds the marking graph for the given Petri net,<br>
	 * i.e. a graph containing all reachable markings and their relation.<br>
	 *  
	 * @param petriNet The basic Petri net for operation.
	 * @throws MarkingGraphException
	 */
	public static <	P extends AbstractPlace<F,S>, 
					T extends AbstractTransition<F,S>, 
					F extends AbstractFlowRelation<P,T,S>, 
					M extends AbstractMarking<S>, 
					S extends Object>

	void initiateMarkingGraphConstruction(AbstractPetriNet<P,T,F,M,S> petriNet, ExecutorListener listener) 
			throws MarkingGraphException {
		
		initiateMarkingGraphConstruction(new ThreadedMGCalculator<P,T,F,M,S>(petriNet), listener);
	}

	/**
	 * Builds the marking graph for the given Petri net,<br>
	 * i.e. a graph containing all reachable markings and their relation.<br>
	 *  
	 * @param calculator The marking graph calculator.
	 * @throws MarkingGraphException
	 */
	public static <	P extends AbstractPlace<F,S>, 
					T extends AbstractTransition<F,S>, 
					F extends AbstractFlowRelation<P,T,S>, 
					M extends AbstractMarking<S>, 
					S extends Object>

	void initiateMarkingGraphConstruction(MGCalculator<P,T,F,M,S> calculator, ExecutorListener listener) 
			throws MarkingGraphException {
		
		calculator.addExecutorListener(listener);
		calculator.runCalculation();
	}
	
	
	/**
	 * Builds the marking graph for the given Petri net,<br>
	 * i.e. a graph containing all reachable markings and their relation.<br>
	 *
	 * @param calculator The marking graph calculator.
	 * @return The marking graph of the given Petri net.
	 * @throws MarkingGraphException
	 */
	public static <	P extends AbstractPlace<F,S>, 
					T extends AbstractTransition<F,S>, 
					F extends AbstractFlowRelation<P,T,S>, 
					M extends AbstractMarking<S>, 
					S extends Object>

	AbstractMarkingGraph<M,S,?,?> buildMarkingGraph(MGCalculator<P,T,F,M,S> calculator) 
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
					S extends Object>

	
	AbstractMarkingGraph<M,S,?,?> buildMarkingGraph(AbstractPetriNet<P,T,F,M,S> petriNet) 
			throws MarkingGraphException {
		
		return buildMarkingGraph(new ThreadedMGCalculator<P,T,F,M,S>(petriNet));
	}

}
