package de.uni.freiburg.iig.telematik.sepia.property.boundedness;

import java.util.concurrent.CancellationException;
import java.util.concurrent.ExecutionException;

import de.invation.code.toval.thread.AbstractCallable;
import de.uni.freiburg.iig.telematik.sepia.mg.abstr.AbstractMarkingGraph;
import de.uni.freiburg.iig.telematik.sepia.mg.abstr.AbstractMarkingGraphRelation;
import de.uni.freiburg.iig.telematik.sepia.mg.abstr.AbstractMarkingGraphState;
import de.uni.freiburg.iig.telematik.sepia.petrinet.AbstractFlowRelation;
import de.uni.freiburg.iig.telematik.sepia.petrinet.AbstractMarking;
import de.uni.freiburg.iig.telematik.sepia.petrinet.AbstractPetriNet;
import de.uni.freiburg.iig.telematik.sepia.petrinet.AbstractPetriNet.Boundedness;
import de.uni.freiburg.iig.telematik.sepia.petrinet.AbstractPlace;
import de.uni.freiburg.iig.telematik.sepia.petrinet.AbstractTransition;
import de.uni.freiburg.iig.telematik.sepia.property.AbstractThreadedPNPropertyChecker;
import de.uni.freiburg.iig.telematik.sepia.property.mg.MGConstructorCallable;
import de.uni.freiburg.iig.telematik.sepia.property.mg.StateSpaceException;

public class ThreadedBoundednessChecker<P extends AbstractPlace<F,S>, 
										T extends AbstractTransition<F,S>, 
										F extends AbstractFlowRelation<P,T,S>, 
										M extends AbstractMarking<S>, 
										S extends Object,
										X extends AbstractMarkingGraphState<M,S>,
										Y extends AbstractMarkingGraphRelation<M,X,S>> extends AbstractThreadedPNPropertyChecker<P,T,F,M,S,X,Y,AbstractMarkingGraph<M,S,X,Y>>{
	
	public ThreadedBoundednessChecker(AbstractPetriNet<P,T,F,M,S,X,Y> petriNet){
		super(petriNet);
	}
	
	@Override
	protected AbstractCallable<AbstractMarkingGraph<M,S,X,Y>> getCallable() {
		return new MGConstructorCallable<P,T,F,M,S,X,Y>(petriNet);
	}
	
	public void runCalculation(){
		setUpAndRun();
	}

	public Boundedness getBoundedness() throws BoundednessException{
		try {
			getResult();
			return Boundedness.BOUNDED;
		} catch (CancellationException e) {
			return Boundedness.UNKNOWN;
		} catch (InterruptedException e) {
			return Boundedness.UNKNOWN;
		} catch (ExecutionException e) {
			if(e.getCause() != null && e.getCause() instanceof StateSpaceException){
				return Boundedness.UNBOUNDED;
			}
			throw new BoundednessException("Exception during marking graph construction", e);
		} catch(Exception e){
			throw new BoundednessException("Exception during marking graph construction", e);
		}
	}

}
