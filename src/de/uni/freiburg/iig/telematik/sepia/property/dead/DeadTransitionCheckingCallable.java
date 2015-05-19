package de.uni.freiburg.iig.telematik.sepia.property.dead;

import java.util.HashSet;
import java.util.Set;

import de.invation.code.toval.validate.Validate;
import de.uni.freiburg.iig.telematik.sepia.mg.abstr.AbstractMarkingGraph;
import de.uni.freiburg.iig.telematik.sepia.mg.abstr.AbstractMarkingGraphRelation;
import de.uni.freiburg.iig.telematik.sepia.mg.abstr.AbstractMarkingGraphState;
import de.uni.freiburg.iig.telematik.sepia.petrinet.AbstractFlowRelation;
import de.uni.freiburg.iig.telematik.sepia.petrinet.AbstractMarking;
import de.uni.freiburg.iig.telematik.sepia.petrinet.AbstractPetriNet;
import de.uni.freiburg.iig.telematik.sepia.petrinet.AbstractPlace;
import de.uni.freiburg.iig.telematik.sepia.petrinet.AbstractTransition;
import de.uni.freiburg.iig.telematik.sepia.property.AbstractPNPropertyCheckerCallable;
import de.uni.freiburg.iig.telematik.sepia.property.mg.MGConstructorCallable;
import de.uni.freiburg.iig.telematik.sepia.property.mg.MarkingGraphException;
import de.uni.freiburg.iig.telematik.sepia.property.mg.StateSpaceException;
import de.uni.freiburg.iig.telematik.sepia.util.PNUtils;

public class DeadTransitionCheckingCallable<P extends AbstractPlace<F,S>, 
											T extends AbstractTransition<F,S>, 
											F extends AbstractFlowRelation<P,T,S>, 
											M extends AbstractMarking<S>, 
											S extends Object,
											X extends AbstractMarkingGraphState<M,S>,
											Y extends AbstractMarkingGraphRelation<M,X,S>> extends AbstractPNPropertyCheckerCallable<P,T,F,M,S,X,Y,DeadTransitionCheckResult> {

	private AbstractMarkingGraph<M,S,X,Y> markingGraph = null;

	protected DeadTransitionCheckingCallable(AbstractPetriNet<P,T,F,M,S,X,Y> petriNet) {
		super(petriNet);
	}
	
	protected DeadTransitionCheckingCallable(AbstractPetriNet<P,T,F,M,S,X,Y> petriNet, AbstractMarkingGraph<M,S,X,Y> markingGraph) {
		super(petriNet);
		Validate.notNull(markingGraph);
		this.markingGraph = markingGraph;
	}

	@Override
	protected DeadTransitionCheckResult callRoutine() throws DeadTransitionCheckException, InterruptedException {
		// Check if marking graph is available and construct it in case it is not
		if(markingGraph == null){
			MGConstructorCallable<P,T,F,M,S,X,Y> mgConstructionCallable = new MGConstructorCallable<P,T,F,M,S,X,Y>(petriNet);
			try {
				markingGraph = mgConstructionCallable.callRoutine();
			} catch(MarkingGraphException e){
				// Abort when Petri net is unbounded
				if(e.getCause() != null && e.getCause() instanceof StateSpaceException){
					throw new DeadTransitionCheckException("Cannot check dead transitions of unbounded net", e);
				}
				throw new DeadTransitionCheckException(e);
			}
		}
		
		Set<T> netTransitions = new HashSet<T>(petriNet.getTransitions());
		try {
			for (AbstractMarkingGraphState<M, S> reachableMarking : markingGraph.getStates()) {
				if (Thread.currentThread().isInterrupted()) {
					throw new InterruptedException();
				}
				petriNet.setMarking(reachableMarking.getElement());
				netTransitions.removeAll(petriNet.getEnabledTransitions());
				if (netTransitions.isEmpty()) {
					break;
				}
			}
		} catch (InterruptedException e) {
			throw e;
		} catch (Exception e) {
			throw new DeadTransitionCheckException("Exception during dead transition check.<br>Reason: " + e.getMessage(), e);
		}
		DeadTransitionCheckResult result = new DeadTransitionCheckResult();
		result.setDeadTransitions(PNUtils.getNameSetFromTransitions(netTransitions, true));
		return result;
	}
	
}
