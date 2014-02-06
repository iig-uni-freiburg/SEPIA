package de.uni.freiburg.iig.telematik.sepia.traversal;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import de.invation.code.toval.validate.ParameterException;
import de.invation.code.toval.validate.Validate;
import de.uni.freiburg.iig.telematik.sepia.exception.PNException;
import de.uni.freiburg.iig.telematik.sepia.petrinet.AbstractFlowRelation;
import de.uni.freiburg.iig.telematik.sepia.petrinet.AbstractMarking;
import de.uni.freiburg.iig.telematik.sepia.petrinet.AbstractPetriNet;
import de.uni.freiburg.iig.telematik.sepia.petrinet.AbstractPlace;
import de.uni.freiburg.iig.telematik.sepia.petrinet.AbstractTransition;


public class PNTraversalUtils {

	/**
	 * Simulates the given Petri net the given number of times and returns all distinct observed traces.<br>
	 * This method uses a random Petri net traverser.
	 * @param net The Petri net to simulate.
	 * @param runs The nubmer of times to simulate the net.
	 * @return A set of distinct observed traces.
	 * @throws ParameterException If the given net is <code>null</code>
	 */
	public static <P extends AbstractPlace<F,S>, 
	   			   T extends AbstractTransition<F,S>, 
	   			   F extends AbstractFlowRelation<P,T,S>, 
	   			   M extends AbstractMarking<S>, 
	   			   S extends Object> 
	
				   Set<List<String>> testTraces(AbstractPetriNet<P,T,F,M,S> net, int runs, int maxEventsPerTrace) 
			                         throws ParameterException{
		
		return testTraces(net, runs, maxEventsPerTrace, false, false);
	}
	
	/**
	 * Simulates the given Petri net the given number of times and returns all distinct observed traces.<br>
	 * This method uses a random Petri net traverser.
	 * @param net The Petri net to simulate.
	 * @param runs The number of times to simulate the net.
	 * @param printOut Indicates, if new distinct traces are printed out.
	 * @return A set of distinct observed traces.
	 * @throws ParameterException If the given net is <code>null</code>
	 */
	public static <P extends AbstractPlace<F,S>, 
	   			   T extends AbstractTransition<F,S>, 
	   			   F extends AbstractFlowRelation<P,T,S>, 
	   			   M extends AbstractMarking<S>, 
	   			   S extends Object> 
	
				   Set<List<String>> testTraces(AbstractPetriNet<P,T,F,M,S> net, int runs, int maxEventsPerTrace, boolean printOut, boolean useLabelNames) 
			                         throws ParameterException{
		
		Validate.notNull(net);
		Set<List<String>> traces = new HashSet<List<String>>();
		List<String> newTrace = null;
		PNTraverser<T> traverser = new RandomPNTraverser<T>(net);
		for(int i = 0; i<runs; i++){
			newTrace = new ArrayList<String>();
			net.reset();
			int c = 0;
			while(net.hasEnabledTransitions() && c++ < maxEventsPerTrace){
				T nextTransition = traverser.chooseNextTransition(net.getEnabledTransitions());
				String descriptor = useLabelNames ? nextTransition.getLabel() : nextTransition.getName();
				if(!nextTransition.isSilent()){
					newTrace.add(descriptor);
				}
				try {
					net.fire(nextTransition.getName());
				} catch (PNException e) {
					e.printStackTrace();
				}
			}
			if(traces.add(newTrace) && printOut)
				System.out.println(newTrace);
		}
		return traces;
	}

}