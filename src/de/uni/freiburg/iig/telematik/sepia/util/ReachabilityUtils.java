package de.uni.freiburg.iig.telematik.sepia.util;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ArrayBlockingQueue;

import de.invation.code.toval.validate.ParameterException;
import de.invation.code.toval.validate.ParameterException.ErrorCode;
import de.invation.code.toval.validate.Validate;
import de.uni.freiburg.iig.telematik.jagal.ts.exception.TSException;
import de.uni.freiburg.iig.telematik.sepia.exception.BoundednessException;
import de.uni.freiburg.iig.telematik.sepia.exception.MarkingGraphException;
import de.uni.freiburg.iig.telematik.sepia.exception.PNException;
import de.uni.freiburg.iig.telematik.sepia.exception.PNValidationException;
import de.uni.freiburg.iig.telematik.sepia.mg.MGTraversalResult;
import de.uni.freiburg.iig.telematik.sepia.mg.MarkingGraphUtils;
import de.uni.freiburg.iig.telematik.sepia.mg.abstr.AbstractMarkingGraph;
import de.uni.freiburg.iig.telematik.sepia.mg.abstr.AbstractMarkingGraphRelation;
import de.uni.freiburg.iig.telematik.sepia.mg.abstr.AbstractMarkingGraphState;
import de.uni.freiburg.iig.telematik.sepia.petrinet.AbstractFlowRelation;
import de.uni.freiburg.iig.telematik.sepia.petrinet.AbstractMarking;
import de.uni.freiburg.iig.telematik.sepia.petrinet.AbstractPetriNet;
import de.uni.freiburg.iig.telematik.sepia.petrinet.AbstractPetriNet.Boundedness;
import de.uni.freiburg.iig.telematik.sepia.petrinet.AbstractPlace;
import de.uni.freiburg.iig.telematik.sepia.petrinet.AbstractTransition;


/**
 * This class provides methods concerned with reachability issues of Petri nets.<br>
 * It allows to build the reachability graph of a Petri net containing all reachable markings and their relation,<br>
 * but also provides methods for checking reachability oriented properties of Petri nets,<br>
 * such as dead transitions.
 * 
 * @author Thomas Stocker
 */
public class ReachabilityUtils {
	
	private static final String rgGraphNodeFormat = "s%s";
	public static final int MAX_RG_CALCULATION_STEPS = Integer.MAX_VALUE;
	
	/**
	 * Checks if the transition within the given net with the given name is dead,<br>
	 * i.e. if there is not reachable marking where the transition is enabled.<br>
	 * <br>
	 * Attention: Only works for bounded nets, i.e. nets for which the {@link AbstractPetriNet#isBounded()} method returns {@link Boundedness#BOUNDED}<br>
	 * If the boundedness property is set explicitly, the method may not halt.
	 * 
	 * @param petriNet The basic Petri net.
	 * @param transitionName The name of the transition of interest.
	 * @return <code>true</code> if the transition is dead;<br>
	 * <code>false</code> otherwise.
	 * @throws TSException 
	 * @throws PNException 
	 * @throws ParameterException If some parameters are <code>null</code>, the Petri net
	 * does not contain a transition with the given name or the given net is not bounded.
	 * 
	 * @see #getDeadTransitions(AbstractPetriNet)
	 */
	public static <P extends AbstractPlace<F,S>, 
	   			   T extends AbstractTransition<F,S>, 
	   			   F extends AbstractFlowRelation<P,T,S>, 
	   			   M extends AbstractMarking<S>, 
	   			   S extends Object,
	   			   X extends AbstractMarkingGraphState<M,S>,
	   			   Y extends AbstractMarkingGraphRelation<M,X,S>> 
	
				   boolean isDead(AbstractPetriNet<P,T,F,M,S,X,Y> petriNet, String transitionName) throws PNException, TSException{
		
		Validate.notNull(petriNet);
		Validate.notNull(transitionName);
		if(!petriNet.containsTransition(transitionName))
			throw new ParameterException(ErrorCode.INCOMPATIBILITY, "Unknown transition");
		
		T transition = petriNet.getTransition(transitionName);
		return getDeadTransitions(petriNet).contains(transition);
	}
	
	/**
	 * Checks if the given Petri net contains dead transitions,<br>
	 * i.e. transitions which are never enabled (in no reachable marking).<br>
	 * <br>
	 * Attention: Only works for bounded nets, i.e. nets for which the {@link AbstractPetriNet#isBounded()} method returns {@link Boundedness#BOUNDED}<br>
	 * If the boundedness property is set explicitly, the method may not halt.
	 * 
	 * @param petriNet The basic Patri net for operation.
	 * @return <code>true</code> if the Petri net contains dead transitions;<br>
	 * <code>false</code> otherwise.
	 * @throws PNException 
	 * @throws ParameterException If the Petri net parameter is <code>null</code> or the given net is not bounded.
	 * 
	 * @see #getDeadTransitions(AbstractPetriNet)
	 */
	public static <P extends AbstractPlace<F,S>, 
	   			   T extends AbstractTransition<F,S>, 
	   			   F extends AbstractFlowRelation<P,T,S>, 
	   			   M extends AbstractMarking<S>, 
	   			   S extends Object,
	   			   X extends AbstractMarkingGraphState<M,S>,
	   			   Y extends AbstractMarkingGraphRelation<M,X,S>> 

	boolean hasDeadTransitions(AbstractPetriNet<P, T, F, M, S, X, Y> petriNet) throws PNException {
		Set<T> deadTransitions = getDeadTransitions(petriNet);
		if (!deadTransitions.isEmpty())
			return false;
		return true;
	}
	
	/**
	 * REturns all dead transition of the given Petri net,<br>
	 * i.e. all transitions which are never enabled (in no reachable marking).<br>
	 * <br>
	 * Attention: Only works for bounded nets, i.e. nets for which the {@link AbstractPetriNet#isBounded()} method returns {@link Boundedness#BOUNDED}<br>
	 * If the boundedness property is set explicitly, the method may not halt.
	 *  
	 * 
	 * @param petriNet The basic Petri net for operation
	 * @return A set of all dead transitions.
	 * @throws TSException 
	 * @throws PNException 
	 * @throws ParameterException If the Petri net parameter is <code>null</code> or the given net is not bounded.
	 */
	public static <	P extends AbstractPlace<F,S>, 
	   				T extends AbstractTransition<F,S>, 
	   				F extends AbstractFlowRelation<P,T,S>, 
	   				M extends AbstractMarking<S>, 
	   				S extends Object,
	   				X extends AbstractMarkingGraphState<M,S>,
	   				Y extends AbstractMarkingGraphRelation<M,X,S>> 

	   Set<T> getDeadTransitions(AbstractPetriNet<P,T,F,M,S,X,Y> petriNet) throws PNException{

		Validate.notNull(petriNet);
		
		Set<T> netTransitions = new HashSet<T>(petriNet.getTransitions());
		AbstractMarkingGraph<M,S,X,Y> markingGraph = petriNet.getMarkingGraph();
		for(AbstractMarkingGraphState<M,S> reachableMarking: markingGraph.getStates()){
			petriNet.setMarking(reachableMarking.getElement());
			netTransitions.removeAll(petriNet.getEnabledTransitions());
			if(netTransitions.isEmpty()){
				break;
			}
		}
		return netTransitions;
	}
	
	public static <P extends AbstractPlace<F, S>, 
				   T extends AbstractTransition<F, S>, 
				   F extends AbstractFlowRelation<P, T, S>, 
				   M extends AbstractMarking<S>, 
				   S extends Object, 
				   X extends AbstractMarkingGraphState<M, S>, 
				   Y extends AbstractMarkingGraphRelation<M, X, S>, 
				   N extends AbstractPetriNet<P, T, F, M, S, X, Y>>

	void checkDeadTransitions(N petriNet) throws PNValidationException, PNException {

		Set<T> deadTransitions = getDeadTransitions(petriNet);
		if (!deadTransitions.isEmpty())
			throw new PNValidationException("CWN has dead transitions: " + deadTransitions);
	}
	
	/**
	 * Builds the marking graph for the given Petri net,<br>
	 * i.e. a graph containing all reachable markings and their relation.<br>
	 * <br>
	 * Attention: Only works for bounded nets, i.e. nets for which the {@link AbstractPetriNet#isBounded()} method returns {@link Boundedness#BOUNDED}<br>
	 * If the boundedness property is set explicitly, the method may not halt.
	 *  
	 * @param petriNet The basic Petri net for operation.
	 * @return The marking graph of the given Petri net.
	 * @throws MarkingGraphException 
	 * @throws ParameterException If the Petri net parameter is <code>null</code> or the given net is nor bounded.
	 */
	@SuppressWarnings("unchecked")
	public static <	P extends AbstractPlace<F,S>, 
					T extends AbstractTransition<F,S>, 
					F extends AbstractFlowRelation<P,T,S>, 
					M extends AbstractMarking<S>, 
					S extends Object,
					X extends AbstractMarkingGraphState<M,S>,
					Y extends AbstractMarkingGraphRelation<M,X,S>>

	AbstractMarkingGraph<M,S,X,Y> buildMarkingGraph(AbstractPetriNet<P,T,F,M,S,X,Y> petriNet, boolean acceptOnlyBoundedNet) throws BoundednessException, MarkingGraphException {

		Validate.notNull(petriNet);
		if(acceptOnlyBoundedNet & !petriNet.isBounded())
			throw new ParameterException(ErrorCode.INCOMPATIBILITY, "Cannot determine marking graph for unbounded nets.");

		ArrayBlockingQueue<M> queue = new ArrayBlockingQueue<M>(10);
		Set<M> allKnownStates = new HashSet<M>();

		allKnownStates.add(petriNet.getInitialMarking());
		AbstractMarkingGraph<M,S,X,Y> markingGraph = petriNet.createNewMarkingGraph();
		int stateCount = 0;
		Map<String, String> stateNames = new HashMap<String, String>();
		M initialMarking = petriNet.getInitialMarking();
		queue.offer(initialMarking);
		String stateName = String.format(rgGraphNodeFormat, stateCount++);
		markingGraph.addState(stateName, (M) initialMarking.clone());
		markingGraph.setInitialState(markingGraph.getState(stateName));
		markingGraph.addStartState(stateName);
		stateNames.put(initialMarking.toString(), stateName);
		allKnownStates.add((M) initialMarking.clone());
		
		int calculationSteps = 0;
		try {
			while (!queue.isEmpty()) {
				calculationSteps++;
				if((calculationSteps >= MAX_RG_CALCULATION_STEPS))
					throw new BoundednessException("Reached maximum calculation steps for building marking graph.");
				M nextMarking = queue.poll();
				petriNet.setMarking(nextMarking);
//				M marking = (M) nextMarking.clone();
				String nextStateName = stateNames.get(nextMarking.toString());
//				System.out.println("Next marking (" + nextStateName + "): " + nextMarking);

				if(petriNet.hasEnabledTransitions()){
					String newStateName = null;
					for (T enabledTransition : petriNet.getEnabledTransitions()) {

//						System.out.println("   enabled: " + enabledTransition.getName());
						M newMarking = petriNet.fireCheck(enabledTransition.getName());
						
						// Check if this marking is already known
						M equalMarking = null;
						for(M storedMarking: allKnownStates){
							if(storedMarking.equals(newMarking)){
								equalMarking = storedMarking;
								break;
							}
						}

//						System.out.println("   new marking: " + newMarking);
						if(equalMarking == null) {
							// This is a new marking
//							System.out.println("   -> New marking");
							queue.offer(newMarking);
							allKnownStates.add((M) newMarking.clone());
							newStateName = String.format(rgGraphNodeFormat, stateCount++);
							markingGraph.addState(newStateName, (M) newMarking.clone());
							stateNames.put(newMarking.toString(), newStateName);
						} else {
							// This marking is already known
//							System.out.println("   -> Known marking");
							newStateName = stateNames.get(newMarking.toString());
						}
						if (!markingGraph.containsEvent(enabledTransition.getName())) {
							markingGraph.addEvent(enabledTransition.getName(), enabledTransition.getLabel());
						}
//						System.out.println("   add relation: " + nextStateName + " to " + newStateName + " via " + enabledTransition.getName());
						markingGraph.addRelation(nextStateName, newStateName, enabledTransition.getName());
					}
				} else {
					markingGraph.addEndState(nextStateName);
				}
				
			}
		} catch (Exception e) {
			throw new MarkingGraphException("Exception during marking graph construction.<br>Reason: " + e.getMessage());
		}
		return markingGraph;
	}
	
	public static <	P extends AbstractPlace<F, S>, 
					T extends AbstractTransition<F, S>, 
					F extends AbstractFlowRelation<P, T, S>, 
					M extends AbstractMarking<S>, 
					S extends Object,
					X extends AbstractMarkingGraphState<M,S>,
	   				Y extends AbstractMarkingGraphRelation<M,X,S>>

		MGTraversalResult getFiringSequences(AbstractPetriNet<P, T, F, M, S, X, Y> petriNet, boolean includeSilentTransitions)
			throws BoundednessException, MarkingGraphException {
		
		AbstractMarkingGraph<M,S,X,Y> markingGraph = ReachabilityUtils.buildMarkingGraph(petriNet, true);
		return MarkingGraphUtils.getSequences(petriNet, markingGraph, includeSilentTransitions);
	}
	
	
}
