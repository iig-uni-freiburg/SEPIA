package de.uni.freiburg.iig.telematik.sepia.petrinet.pt.properties.wfnet.soundness;

import java.util.concurrent.ExecutionException;

import de.invation.code.toval.thread.AbstractCallable;
import de.uni.freiburg.iig.telematik.sepia.petrinet.properties.threaded.AbstractThreadedPNPropertyChecker;
import de.uni.freiburg.iig.telematik.sepia.petrinet.pt.abstr.AbstractPTFlowRelation;
import de.uni.freiburg.iig.telematik.sepia.petrinet.pt.abstr.AbstractPTMarking;
import de.uni.freiburg.iig.telematik.sepia.petrinet.pt.abstr.AbstractPTPlace;
import de.uni.freiburg.iig.telematik.sepia.petrinet.pt.abstr.AbstractPTTransition;
import de.uni.freiburg.iig.telematik.sepia.petrinet.pt.properties.wfnet.WFNetException;
import de.uni.freiburg.iig.telematik.sepia.petrinet.pt.properties.wfnet.WFNetProperties;

public class ThreadedWFNetSoundnessChecker<P extends AbstractPTPlace<F>,
										   T extends AbstractPTTransition<F>, 
										   F extends AbstractPTFlowRelation<P,T>, 
										   M extends AbstractPTMarking> 

											extends AbstractThreadedPNPropertyChecker<P,T,F,M,Integer,
																		  		 	  WFNetProperties,
																		  		 	  WFNetProperties,
																		  		 	  WFNetException>{
	
	public ThreadedWFNetSoundnessChecker(WFNetSoundnessCheckingCallableGenerator<P,T,F,M> generator){
		super(generator);
	}
	
	@Override
	protected WFNetSoundnessCheckingCallableGenerator<P,T,F,M> getGenerator() {
		return (WFNetSoundnessCheckingCallableGenerator<P,T,F,M>) super.getGenerator();
	}
	
	@Override
	public AbstractCallable<WFNetProperties> createCallable() {
		return new WFNetSoundnessCheckingCallable<P,T,F,M>(getGenerator());
	}

	@Override
	protected WFNetException createException(String message, Throwable cause) {
		return new WFNetException(message, cause);
	}

	@Override
	protected WFNetException executionException(ExecutionException e) {
		if(e.getCause() instanceof WFNetException)
			return (WFNetException) e.getCause();
		return new WFNetException("Exception during CW check", e);
	}

	@Override
	protected WFNetProperties getResultFromCallableResult(WFNetProperties callableResult) throws Exception {
		return callableResult;
	}
	
	

}
