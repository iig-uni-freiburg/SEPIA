package de.uni.freiburg.iig.telematik.sepia.petrinet.properties.boundedness;

import java.util.concurrent.CancellationException;
import java.util.concurrent.ExecutionException;

import de.invation.code.toval.thread.AbstractCallable;
import de.uni.freiburg.iig.telematik.sepia.mg.abstr.AbstractMarkingGraph;
import de.uni.freiburg.iig.telematik.sepia.petrinet.abstr.AbstractFlowRelation;
import de.uni.freiburg.iig.telematik.sepia.petrinet.abstr.AbstractMarking;
import de.uni.freiburg.iig.telematik.sepia.petrinet.abstr.AbstractPetriNet.Boundedness;
import de.uni.freiburg.iig.telematik.sepia.petrinet.abstr.AbstractPlace;
import de.uni.freiburg.iig.telematik.sepia.petrinet.abstr.AbstractTransition;
import de.uni.freiburg.iig.telematik.sepia.petrinet.properties.mg.StateSpaceException;
import de.uni.freiburg.iig.telematik.sepia.petrinet.properties.threaded.AbstractThreadedPNPropertyChecker;

public class ThreadedBoundednessChecker<P extends AbstractPlace<F,S>, 
										T extends AbstractTransition<F,S>, 
										F extends AbstractFlowRelation<P,T,S>, 
										M extends AbstractMarking<S>, 
										S extends Object> extends AbstractThreadedPNPropertyChecker<P,T,F,M,S,AbstractMarkingGraph<M,S,?,?>>{
	
	public ThreadedBoundednessChecker(BoundednessCheckGenerator<P,T,F,M,S> generator){
		super(generator);
	}
	
	@Override
	protected BoundednessCheckGenerator<P,T,F,M,S> getGenerator() {
		return (BoundednessCheckGenerator<P,T,F,M,S>) super.getGenerator();
	}
	
	@Override
	protected AbstractCallable<AbstractMarkingGraph<M,S,?,?>> getCallable() {
		return new BoundednessCheckCallable<P,T,F,M,S>(getGenerator());
	}
	
	public void runCalculation(){
		setUpAndRun();
	}

	public BoundednessCheckResult<P,T,F,M,S> getBoundedness() throws BoundednessException{
		AbstractMarkingGraph<M,S,?,?> markingGraph = null;
		Boundedness boundedness = null;
		try {
			markingGraph = getResult();
			boundedness = Boundedness.BOUNDED;
		} catch (CancellationException e) {
			boundedness = Boundedness.UNKNOWN;
		} catch (InterruptedException e) {
			boundedness = Boundedness.UNKNOWN;
		} catch (ExecutionException e) {
			if(e.getCause() != null && e.getCause() instanceof StateSpaceException){
				boundedness = Boundedness.UNBOUNDED;
			}
			throw new BoundednessException("Exception during marking graph construction", e);
		} catch(Exception e){
			throw new BoundednessException("Exception during marking graph construction", e);
		}
		return new BoundednessCheckResult<P,T,F,M,S>(boundedness, markingGraph);
	}

}
