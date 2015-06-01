package de.uni.freiburg.iig.telematik.sepia.petrinet.ifnet.properties.validity;

import java.util.concurrent.ExecutionException;

import de.invation.code.toval.thread.AbstractCallable;
import de.invation.code.toval.types.Multiset;
import de.uni.freiburg.iig.telematik.sepia.exception.PNValidationException;
import de.uni.freiburg.iig.telematik.sepia.petrinet.ifnet.abstr.AbstractDeclassificationTransition;
import de.uni.freiburg.iig.telematik.sepia.petrinet.ifnet.abstr.AbstractIFNetFlowRelation;
import de.uni.freiburg.iig.telematik.sepia.petrinet.ifnet.abstr.AbstractIFNetMarking;
import de.uni.freiburg.iig.telematik.sepia.petrinet.ifnet.abstr.AbstractIFNetPlace;
import de.uni.freiburg.iig.telematik.sepia.petrinet.ifnet.abstr.AbstractIFNetTransition;
import de.uni.freiburg.iig.telematik.sepia.petrinet.ifnet.abstr.AbstractRegularIFNetTransition;
import de.uni.freiburg.iig.telematik.sepia.petrinet.properties.threaded.AbstractThreadedPNPropertyChecker;

public class ThreadedIFNetValidityChecker<P extends AbstractIFNetPlace<F>,
										  T extends AbstractIFNetTransition<F>, 
										  F extends AbstractIFNetFlowRelation<P,T>, 
										  M extends AbstractIFNetMarking,
										  R extends AbstractRegularIFNetTransition<F>,
										  D extends AbstractDeclassificationTransition<F>> 

										  extends AbstractThreadedPNPropertyChecker<P,T,F,M,Multiset<String>,
										  											Boolean,
										  											Boolean,
										  											PNValidationException>{
	
	public ThreadedIFNetValidityChecker(IFNetValidityCheckingCallableGenerator<P,T,F,M,R,D> generator){
		super(generator);
	}
	
	@SuppressWarnings("unchecked")
	@Override
	protected IFNetValidityCheckingCallableGenerator<P,T,F,M,R,D> getGenerator() {
		return (IFNetValidityCheckingCallableGenerator<P,T,F,M,R,D>) super.getGenerator();
	}

	@Override
	public AbstractCallable<Boolean> createCallable() {
		return new IFNetValidityCheckingCallable<P,T,F,M,R,D>(getGenerator());
	}

	@Override
	protected PNValidationException createException(String message, Throwable cause) {
		return new PNValidationException(message, cause);
	}

	@Override
	protected PNValidationException executionException(ExecutionException e) {
		if(e.getCause() instanceof PNValidationException)
			return (PNValidationException) e.getCause();
		return new PNValidationException("Exception during validation check", e);
	}

	@Override
	protected Boolean getResultFromCallableResult(Boolean callableResult) throws Exception {
		return callableResult;
	}

}
