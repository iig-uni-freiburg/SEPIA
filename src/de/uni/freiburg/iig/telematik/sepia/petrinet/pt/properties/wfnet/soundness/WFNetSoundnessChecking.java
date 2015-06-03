package de.uni.freiburg.iig.telematik.sepia.petrinet.pt.properties.wfnet.soundness;

import de.invation.code.toval.thread.ExecutorListener;
import de.uni.freiburg.iig.telematik.sepia.petrinet.pt.abstr.AbstractPTFlowRelation;
import de.uni.freiburg.iig.telematik.sepia.petrinet.pt.abstr.AbstractPTMarking;
import de.uni.freiburg.iig.telematik.sepia.petrinet.pt.abstr.AbstractPTPlace;
import de.uni.freiburg.iig.telematik.sepia.petrinet.pt.abstr.AbstractPTTransition;
import de.uni.freiburg.iig.telematik.sepia.petrinet.pt.properties.wfnet.WFNetException;
import de.uni.freiburg.iig.telematik.sepia.petrinet.pt.properties.wfnet.WFNetProperties;

public class WFNetSoundnessChecking {
	
	
	public static <P extends AbstractPTPlace<F>,
	 			   T extends AbstractPTTransition<F>, 
	 			   F extends AbstractPTFlowRelation<P,T>, 
	 			   M extends AbstractPTMarking>

	void initiateWFNetSoundnessCheck(WFNetSoundnessCheckingCallableGenerator<P,T,F,M> generator, ExecutorListener<WFNetProperties> listener) 
			throws WFNetException {
		
		ThreadedWFNetSoundnessChecker<P,T,F,M> calculator = new ThreadedWFNetSoundnessChecker<P,T,F,M>(generator);
		calculator.addExecutorListener(listener);
		calculator.runCalculation();
	}
	
	
	public static <P extends AbstractPTPlace<F>,
	   			   T extends AbstractPTTransition<F>, 
	   			   F extends AbstractPTFlowRelation<P,T>, 
	   			   M extends AbstractPTMarking>

	WFNetProperties checkWFNetProperty(WFNetSoundnessCheckingCallableGenerator<P,T,F,M> generator) 
			throws WFNetException {

		ThreadedWFNetSoundnessChecker<P,T,F,M> calculator = new ThreadedWFNetSoundnessChecker<P,T,F,M>(generator);
		calculator.runCalculation();
		return calculator.getResult();
	}
	
}