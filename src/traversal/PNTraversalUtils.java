package traversal;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import petrinet.AbstractFlowRelation;
import petrinet.AbstractMarking;
import petrinet.AbstractPetriNet;
import petrinet.AbstractPlace;
import petrinet.AbstractTransition;
import validate.ParameterException;
import validate.Validate;
import exception.PNException;

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
	
				   Set<List<String>> testTraces(AbstractPetriNet<P,T,F,M,S> net, int runs) 
			                         throws ParameterException{
		
		return testTraces(net, runs, false);
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
	
				   Set<List<String>> testTraces(AbstractPetriNet<P,T,F,M,S> net, int runs, boolean printOut) 
			                         throws ParameterException{
		
		Validate.notNull(net);
		Set<List<String>> traces = new HashSet<List<String>>();
		List<String> newTrace = null;
		PNTraverser<T> traverser = new RandomPNTraverser<T>(net);
		for(int i = 0; i<100; i++){
			newTrace = new ArrayList<String>();
			net.reset();
			while(net.hasEnabledTransitions()){
				String nextTransition = traverser.chooseNextTransition(net.getEnabledTransitions()).getName();
				if(!net.getTransition(nextTransition).isSilent()){
					newTrace.add(nextTransition);
				}
				try {
					net.fire(nextTransition);
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