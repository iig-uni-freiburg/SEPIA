package petrinet.cwn.abstr;

import exception.PNSoundnessException;
import graph.Graph;
import graph.Vertex;

import java.util.HashSet;
import java.util.Set;

import petrinet.AbstractPetriNet;
import petrinet.AbstractPetriNet.Boundedness;
import util.ReachabilityUtils;
import validate.ParameterException;
import validate.Validate;


public class AbstractCWNUtils {
	
	/**
	 * Checks the "option to complete" and "proper completion" soundness property for CWNs.<br>
	 * "Option to complete" requires that from each reachable marking m there is a
	 * reachable marking m' which is an end state of the CWN, i.e.<br>
	 * it contains the output place which in turn contains exactly one token.<br>
	 * "Proper completion" requires, that when an end state is reached, no transition can be fired anymore.<br>
	 * <br>
	 * Attention: Only works for bounded nets, i.e. nets for which the {@link AbstractPetriNet#isBounded()} method returns {@link Boundedness#BOUNDED}<br>
	 * If the boundedness property is set explicitly, the method may not halt.
	 * 
	 * @param cwn The basic CWN for operation
	 * @return <code>true</code> if the given CWN fulfills the property;<br>
	 * <code>false</code> otherwise. 
	 * @throws ParameterException If the CWN parameter is <code>null<code> or the net is not bounded.
	 * @throws PNSoundnessException 
	 */
	public static void validCompletion(@SuppressWarnings("rawtypes") AbstractCWN cwn) throws ParameterException, PNSoundnessException{
		Validate.notNull(cwn);
		
		@SuppressWarnings("unchecked")
		Graph<AbstractCWNMarking> markingGraph = ReachabilityUtils.buildMarkingGraph(cwn);
		Set<Vertex<AbstractCWNMarking>> drains = new HashSet<Vertex<AbstractCWNMarking>>(markingGraph.getDrains());
		for(Vertex<AbstractCWNMarking> drainVertex : drains){			
			if(!isEndState(drainVertex.getElement(), cwn)){
				throw new PNSoundnessException("At least one drain in the marking graph of the given cwn is not a valid end state:\n" + drainVertex.getElement());
			}
		}
		Set<Vertex<AbstractCWNMarking>> otherVertexes = new HashSet<Vertex<AbstractCWNMarking>>(markingGraph.getVertexes());
		otherVertexes.removeAll(drains);
		for(Vertex<AbstractCWNMarking> otherVertex : otherVertexes){			
			if(isEndState(otherVertex.getElement(), cwn)){
				throw new PNSoundnessException("At least one non-drain in the marking graph of the given cwn is an end state:\n" + otherVertex.getElement());
			}
		}
	}
	
	
	/**
	 * Checks if the given marking is an end state of the CWN, i.e.<br>
	 * it contains the output place which in turn contains exactly one token.
	 * 
	 * @param traversalMarking The marking of interest.
	 * @param cwn The corresponding CWN.
	 * @return <code>true</code> if the given marking is an end state;<br>
	 * <code>false</code> otherwise. 
	 */
	private static boolean isEndState(AbstractCWNMarking traversalMarking, @SuppressWarnings("rawtypes") AbstractCWN cwn){
		if(traversalMarking.contains(cwn.getOutputPlace().getName())){
			try{
				// Check if output place contains tokens
				if(!traversalMarking.contains(cwn.getOutputPlace().getName())){
					return false;
				}
				// Check if output place contains at least one control flow token
				if(traversalMarking.get(cwn.getOutputPlace().getName()).multiplicity(AbstractCWN.CONTROL_FLOW_TOKEN_COLOR) > 0){
					return true;
				}
			} catch(ParameterException e){
				e.printStackTrace();
			}
		}
		return false;
	}

}
