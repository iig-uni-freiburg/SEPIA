package de.uni.freiburg.iig.telematik.sepia.petrinet.cpn.properties.cwn;

import de.invation.code.toval.thread.ExecutorListener;
import de.uni.freiburg.iig.telematik.sepia.petrinet.cpn.abstr.AbstractCPNFlowRelation;
import de.uni.freiburg.iig.telematik.sepia.petrinet.cpn.abstr.AbstractCPNMarking;
import de.uni.freiburg.iig.telematik.sepia.petrinet.cpn.abstr.AbstractCPNPlace;
import de.uni.freiburg.iig.telematik.sepia.petrinet.cpn.abstr.AbstractCPNTransition;

public class CWNChecking {
	
	
	public static <P extends AbstractCPNPlace<F>,
	 			   T extends AbstractCPNTransition<F>, 
	 			   F extends AbstractCPNFlowRelation<P,T>, 
	 			   M extends AbstractCPNMarking>

	void initiateCWNPropertyCheck(CWNCheckingCallableGenerator<P,T,F,M> generator, ExecutorListener<CWNProperties> listener) 
			throws CWNException {
		
		ThreadedCWNChecker<P,T,F,M> calculator = new ThreadedCWNChecker<P,T,F,M>(generator);
		calculator.addExecutorListener(listener);
		calculator.runCalculation();
	}
	
	
	public static <P extends AbstractCPNPlace<F>,
	   			   T extends AbstractCPNTransition<F>, 
	   			   F extends AbstractCPNFlowRelation<P,T>, 
	   			   M extends AbstractCPNMarking>

	CWNProperties checkCWNProperty(CWNCheckingCallableGenerator<P,T,F,M> generator) 
			throws CWNException {

		ThreadedCWNChecker<P,T,F,M> calculator = new ThreadedCWNChecker<P,T,F,M>(generator);
		calculator.runCalculation();
		return calculator.getResult();
	}
	
}