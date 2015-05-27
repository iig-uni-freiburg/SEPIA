package de.uni.freiburg.iig.telematik.sepia.petrinet.cpn.properties.cwn;

import java.util.concurrent.CancellationException;
import java.util.concurrent.ExecutionException;

import de.invation.code.toval.thread.AbstractCallable;
import de.invation.code.toval.types.Multiset;
import de.uni.freiburg.iig.telematik.sepia.petrinet.cpn.abstr.AbstractCPNFlowRelation;
import de.uni.freiburg.iig.telematik.sepia.petrinet.cpn.abstr.AbstractCPNMarking;
import de.uni.freiburg.iig.telematik.sepia.petrinet.cpn.abstr.AbstractCPNPlace;
import de.uni.freiburg.iig.telematik.sepia.petrinet.cpn.abstr.AbstractCPNTransition;
import de.uni.freiburg.iig.telematik.sepia.petrinet.properties.threaded.AbstractThreadedPNPropertyChecker;

public class ThreadedCWNChecker<P extends AbstractCPNPlace<F>,
								T extends AbstractCPNTransition<F>, 
								F extends AbstractCPNFlowRelation<P,T>, 
								M extends AbstractCPNMarking> extends AbstractThreadedPNPropertyChecker<P,T,F,M,Multiset<String>,CWNProperties>{
	
	public ThreadedCWNChecker(CWNCheckingCallableGenerator<P,T,F,M> generator){
		super(generator);
	}
	
	@Override
	protected CWNCheckingCallableGenerator<P,T,F,M> getGenerator() {
		return (CWNCheckingCallableGenerator<P,T,F,M>) super.getGenerator();
	}

	public CWNProperties getCWNProperties() throws CWNException{
		try {
			return getResult();
		} catch (CancellationException e) {
			throw new CWNException("CWN property check cancelled.", e);
		} catch (InterruptedException e) {
			throw new CWNException("CWN property check interrupted.", e);
		} catch (ExecutionException e) {
			Throwable cause = e.getCause();
			if(cause == null){
				throw new CWNException("Exception during CWN property check.\n" + e.getMessage(), e);
			}
			if(cause instanceof CWNException){
				throw (CWNException) cause;
			}
			throw new CWNException("Exception during CWN property check.\n" + e.getMessage(), e);
		} catch(Exception e){
			throw new CWNException("Exception during CWN property check.\n" + e.getMessage(), e);
		}
	}
	
	@Override
	public AbstractCallable<CWNProperties> getCallable() {
		return new CWNCheckingCallable<P,T,F,M>(getGenerator());
	}
	
	@Override
	public void runCalculation() {
		setUpAndRun();
	}

}
