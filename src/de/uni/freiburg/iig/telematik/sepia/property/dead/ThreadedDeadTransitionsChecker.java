package de.uni.freiburg.iig.telematik.sepia.property.dead;

import java.util.concurrent.CancellationException;
import java.util.concurrent.ExecutionException;

import de.invation.code.toval.thread.AbstractCallable;
import de.invation.code.toval.validate.Validate;
import de.uni.freiburg.iig.telematik.sepia.mg.abstr.AbstractMarkingGraph;
import de.uni.freiburg.iig.telematik.sepia.mg.abstr.AbstractMarkingGraphRelation;
import de.uni.freiburg.iig.telematik.sepia.mg.abstr.AbstractMarkingGraphState;
import de.uni.freiburg.iig.telematik.sepia.petrinet.AbstractFlowRelation;
import de.uni.freiburg.iig.telematik.sepia.petrinet.AbstractMarking;
import de.uni.freiburg.iig.telematik.sepia.petrinet.AbstractPetriNet;
import de.uni.freiburg.iig.telematik.sepia.petrinet.AbstractPlace;
import de.uni.freiburg.iig.telematik.sepia.petrinet.AbstractTransition;
import de.uni.freiburg.iig.telematik.sepia.property.AbstractThreadedPNPropertyChecker;
import de.uni.freiburg.iig.telematik.sepia.property.mg.StateSpaceException;

public class ThreadedDeadTransitionsChecker<P extends AbstractPlace<F,S>, 
											T extends AbstractTransition<F,S>, 
											F extends AbstractFlowRelation<P,T,S>, 
											M extends AbstractMarking<S>, 
											S extends Object,
											X extends AbstractMarkingGraphState<M,S>,
											Y extends AbstractMarkingGraphRelation<M,X,S>> extends AbstractThreadedPNPropertyChecker<P,T,F,M,S,X,Y,DeadTransitionCheckResult>{

	private AbstractMarkingGraph<M,S,X,Y> markingGraph = null;
	
	protected ThreadedDeadTransitionsChecker(AbstractPetriNet<P,T,F,M,S,X,Y> petriNet){
		super(petriNet);
	}
	
	protected ThreadedDeadTransitionsChecker(AbstractPetriNet<P,T,F,M,S,X,Y> petriNet, AbstractMarkingGraph<M,S,X,Y> markingGraph){
		this(petriNet);
		Validate.notNull(markingGraph);
		this.markingGraph = markingGraph;
	}
	
	@Override
	protected AbstractCallable<DeadTransitionCheckResult> getCallable() {
		if(markingGraph != null){
			return new DeadTransitionCheckingCallable<P,T,F,M,S,X,Y>(petriNet, markingGraph);
		} else {
			return new DeadTransitionCheckingCallable<P,T,F,M,S,X,Y>(petriNet);
		}
	}
	
	public void runCalculation(){
		setUpAndRun();
	}
	
	public DeadTransitionCheckResult getTransitionCheckingResult() throws DeadTransitionCheckException{
		try {
			return getResult();
		} catch (CancellationException e) {
			throw new DeadTransitionCheckException("Dead transition check cancelled.", e);
		} catch (InterruptedException e) {
			throw new DeadTransitionCheckException("Dead transition check interrupted.", e);
		} catch (ExecutionException e) {
			Throwable cause = e.getCause();
			if(cause == null){
				throw new DeadTransitionCheckException("Exception during dead transition check.\n" + e.getMessage(), e);
			}
			if(cause instanceof StateSpaceException){
				throw new DeadTransitionCheckException("Exception during dead transition check.\nCannot build whole marking graph", cause);
			}
			if(cause instanceof DeadTransitionCheckException){
				throw (DeadTransitionCheckException) cause;
			}
			throw new DeadTransitionCheckException("Exception during dead transition check.\n" + e.getMessage(), e);
		} catch(Exception e){
			throw new DeadTransitionCheckException("Exception during dead transition check.\n" + e.getMessage(), e);
		}
	}
}
