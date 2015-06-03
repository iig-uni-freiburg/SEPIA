package de.uni.freiburg.iig.telematik.sepia.petrinet.cpn.properties.cwn.structure;

import de.invation.code.toval.thread.ExecutorListener;
import de.uni.freiburg.iig.telematik.sepia.petrinet.cpn.abstr.AbstractCPNFlowRelation;
import de.uni.freiburg.iig.telematik.sepia.petrinet.cpn.abstr.AbstractCPNMarking;
import de.uni.freiburg.iig.telematik.sepia.petrinet.cpn.abstr.AbstractCPNPlace;
import de.uni.freiburg.iig.telematik.sepia.petrinet.cpn.abstr.AbstractCPNTransition;
import de.uni.freiburg.iig.telematik.sepia.petrinet.cpn.properties.cwn.CWNException;
import de.uni.freiburg.iig.telematik.sepia.petrinet.cpn.properties.cwn.CWNProperties;

public class CWNStructureChecking {
	
	
	public static <P extends AbstractCPNPlace<F>,
	 			   T extends AbstractCPNTransition<F>, 
	 			   F extends AbstractCPNFlowRelation<P,T>, 
	 			   M extends AbstractCPNMarking>

	void initiateCWNPropertyCheck(CWNStructureCheckingCallableGenerator<P,T,F,M> generator, ExecutorListener<CWNProperties> listener) 
			throws CWNException {
		
		ThreadedCWNStructureChecker<P,T,F,M> calculator = new ThreadedCWNStructureChecker<P,T,F,M>(generator);
		calculator.addExecutorListener(listener);
		calculator.runCalculation();
	}
	
	
	public static <P extends AbstractCPNPlace<F>,
	   			   T extends AbstractCPNTransition<F>, 
	   			   F extends AbstractCPNFlowRelation<P,T>, 
	   			   M extends AbstractCPNMarking>

	CWNProperties checkCWNProperty(CWNStructureCheckingCallableGenerator<P,T,F,M> generator) 
			throws CWNException {

		ThreadedCWNStructureChecker<P,T,F,M> calculator = new ThreadedCWNStructureChecker<P,T,F,M>(generator);
		calculator.runCalculation();
		return calculator.getResult();
	}
	
}