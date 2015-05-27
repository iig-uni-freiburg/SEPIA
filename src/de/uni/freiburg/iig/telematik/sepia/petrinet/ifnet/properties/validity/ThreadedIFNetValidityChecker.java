package de.uni.freiburg.iig.telematik.sepia.petrinet.ifnet.properties.validity;

import java.util.concurrent.CancellationException;
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
										  D extends AbstractDeclassificationTransition<F>> extends AbstractThreadedPNPropertyChecker<P,T,F,M,Multiset<String>,Boolean>{
	
	public ThreadedIFNetValidityChecker(IFNetValidityCheckingCallableGenerator<P,T,F,M,R,D> generator){
		super(generator);
	}
	
	@SuppressWarnings("unchecked")
	@Override
	protected IFNetValidityCheckingCallableGenerator<P,T,F,M,R,D> getGenerator() {
		return (IFNetValidityCheckingCallableGenerator<P,T,F,M,R,D>) super.getGenerator();
	}

	public Boolean getValidationResult() throws PNValidationException{
		try {
			return getResult();
		} catch (CancellationException e) {
			throw new PNValidationException("Validation check cancelled.", e);
		} catch (InterruptedException e) {
			throw new PNValidationException("Validation check interrupted.", e);
		} catch (ExecutionException e) {
			Throwable cause = e.getCause();
			if(cause == null){
				throw new PNValidationException("Exception during validation check.\n" + e.getMessage(), e);
			}
			if(cause instanceof PNValidationException){
				throw (PNValidationException) cause;
			}
			throw new PNValidationException("Exception during validation check.\n" + e.getMessage(), e);
		} catch(Exception e){
			throw new PNValidationException("Exception during validation check.\n" + e.getMessage(), e);
		}
	}
	
	@Override
	public AbstractCallable<Boolean> getCallable() {
		return new IFNetValidityCheckingCallable<P,T,F,M,R,D>(getGenerator());
	}
	
	@Override
	public void runCalculation() {
		setUpAndRun();
	}

}
