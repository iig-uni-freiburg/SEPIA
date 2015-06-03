package de.uni.freiburg.iig.telematik.sepia.petrinet.properties.dead;

import java.util.HashSet;
import java.util.Set;

import de.uni.freiburg.iig.telematik.sepia.mg.abstr.AbstractMarkingGraph;
import de.uni.freiburg.iig.telematik.sepia.mg.abstr.AbstractMarkingGraphState;
import de.uni.freiburg.iig.telematik.sepia.petrinet.abstr.AbstractFlowRelation;
import de.uni.freiburg.iig.telematik.sepia.petrinet.abstr.AbstractMarking;
import de.uni.freiburg.iig.telematik.sepia.petrinet.abstr.AbstractPlace;
import de.uni.freiburg.iig.telematik.sepia.petrinet.abstr.AbstractTransition;
import de.uni.freiburg.iig.telematik.sepia.petrinet.properties.mg.MGConstructorCallable;
import de.uni.freiburg.iig.telematik.sepia.petrinet.properties.mg.MGConstructorCallableGenerator;
import de.uni.freiburg.iig.telematik.sepia.petrinet.properties.mg.MarkingGraphException;
import de.uni.freiburg.iig.telematik.sepia.petrinet.properties.mg.StateSpaceException;
import de.uni.freiburg.iig.telematik.sepia.petrinet.properties.threaded.AbstractPNPropertyCheckerCallable;
import de.uni.freiburg.iig.telematik.sepia.util.PNUtils;

public class DeadTransitionCheckingCallable<P extends AbstractPlace<F,S>, 
											T extends AbstractTransition<F,S>, 
											F extends AbstractFlowRelation<P,T,S>, 
											M extends AbstractMarking<S>, 
											S extends Object> extends AbstractPNPropertyCheckerCallable<P,T,F,M,S,DeadTransitionCheckResult> {

	public DeadTransitionCheckingCallable(DeadTransitionCheckCallableGenerator<P,T,F,M,S> generator) {
		super(generator);
	}
	
	@Override
	protected DeadTransitionCheckCallableGenerator<P,T,F,M,S> getGenerator() {
		return (DeadTransitionCheckCallableGenerator<P,T,F,M,S>) super.getGenerator();
	}



	@Override
	public DeadTransitionCheckResult callRoutine() throws DeadTransitionCheckException, InterruptedException {
		// Check if marking graph is available and construct it in case it is not
		if(getGenerator().getMarkingGraph() == null){
			MGConstructorCallableGenerator<P,T,F,M,S> generator = new MGConstructorCallableGenerator<P,T,F,M,S>(getGenerator().getPetriNet());
			MGConstructorCallable<P,T,F,M,S> mgConstructionCallable = new MGConstructorCallable<P,T,F,M,S>(generator);
			try {
				getGenerator().setMarkingGraph(mgConstructionCallable.callRoutine());
			} catch(MarkingGraphException e){
				// Abort when Petri net is unbounded
				if(e.getCause() != null && e.getCause() instanceof StateSpaceException){
					throw new DeadTransitionCheckException("Cannot check dead transitions of unbounded net", e);
				}
				throw new DeadTransitionCheckException(e);
			}
		}
		
		Set<T> netTransitions = new HashSet<T>(getGenerator().getPetriNet().getTransitions());
		try {
			for (AbstractMarkingGraphState<M, S> reachableMarking : getGenerator().getMarkingGraph().getStates()) {
				if (Thread.currentThread().isInterrupted()) {
					throw new InterruptedException();
				}
				getGenerator().getPetriNet().setMarking(reachableMarking.getElement());
				netTransitions.removeAll(getGenerator().getPetriNet().getEnabledTransitions());
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
	
	public AbstractMarkingGraph<M,S,?,?> getMarkingGraph(){
		return getGenerator().getMarkingGraph();
	}
	
}
