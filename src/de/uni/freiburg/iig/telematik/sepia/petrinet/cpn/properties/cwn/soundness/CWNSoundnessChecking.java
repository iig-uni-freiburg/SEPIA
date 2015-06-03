package de.uni.freiburg.iig.telematik.sepia.petrinet.cpn.properties.cwn.soundness;

import de.invation.code.toval.thread.ExecutorListener;
import de.uni.freiburg.iig.telematik.sepia.petrinet.cpn.abstr.AbstractCPNFlowRelation;
import de.uni.freiburg.iig.telematik.sepia.petrinet.cpn.abstr.AbstractCPNMarking;
import de.uni.freiburg.iig.telematik.sepia.petrinet.cpn.abstr.AbstractCPNPlace;
import de.uni.freiburg.iig.telematik.sepia.petrinet.cpn.abstr.AbstractCPNTransition;
import de.uni.freiburg.iig.telematik.sepia.petrinet.cpn.properties.cwn.CWNException;
import de.uni.freiburg.iig.telematik.sepia.petrinet.cpn.properties.cwn.CWNProperties;

public class CWNSoundnessChecking {
	
	
	public static <P extends AbstractCPNPlace<F>,
	 			   T extends AbstractCPNTransition<F>, 
	 			   F extends AbstractCPNFlowRelation<P,T>, 
	 			   M extends AbstractCPNMarking>

	void initiateCWNSoundnessCheck(CWNSoundnessCheckingCallableGenerator<P,T,F,M> generator, ExecutorListener<CWNProperties> listener) 
			throws CWNException {
		
		ThreadedCWNSoundnessChecker<P,T,F,M> calculator = new ThreadedCWNSoundnessChecker<P,T,F,M>(generator);
		calculator.addExecutorListener(listener);
		calculator.runCalculation();
	}
	
	
	public static <P extends AbstractCPNPlace<F>,
	   			   T extends AbstractCPNTransition<F>, 
	   			   F extends AbstractCPNFlowRelation<P,T>, 
	   			   M extends AbstractCPNMarking>

	CWNProperties checkCWNProperty(CWNSoundnessCheckingCallableGenerator<P,T,F,M> generator) 
			throws CWNException {

		ThreadedCWNSoundnessChecker<P,T,F,M> calculator = new ThreadedCWNSoundnessChecker<P,T,F,M>(generator);
		calculator.runCalculation();
		return calculator.getResult();
	}
	
}