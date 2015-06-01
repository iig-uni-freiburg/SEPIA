package de.uni.freiburg.iig.telematik.sepia.petrinet.ifnet.properties.validity;

import de.invation.code.toval.thread.ExecutorListener;
import de.uni.freiburg.iig.telematik.sepia.exception.PNValidationException;
import de.uni.freiburg.iig.telematik.sepia.petrinet.ifnet.abstr.AbstractDeclassificationTransition;
import de.uni.freiburg.iig.telematik.sepia.petrinet.ifnet.abstr.AbstractIFNet;
import de.uni.freiburg.iig.telematik.sepia.petrinet.ifnet.abstr.AbstractIFNetFlowRelation;
import de.uni.freiburg.iig.telematik.sepia.petrinet.ifnet.abstr.AbstractIFNetMarking;
import de.uni.freiburg.iig.telematik.sepia.petrinet.ifnet.abstr.AbstractIFNetPlace;
import de.uni.freiburg.iig.telematik.sepia.petrinet.ifnet.abstr.AbstractIFNetTransition;
import de.uni.freiburg.iig.telematik.sepia.petrinet.ifnet.abstr.AbstractRegularIFNetTransition;

public class IFNetValidity {
	
	public static <P extends AbstractIFNetPlace<F>,
	   			   T extends AbstractIFNetTransition<F>, 
	   			   F extends AbstractIFNetFlowRelation<P,T>, 
	   			   M extends AbstractIFNetMarking,
	   			   R extends AbstractRegularIFNetTransition<F>,
	   			   D extends AbstractDeclassificationTransition<F>>

	void initiateValidityCheck(AbstractIFNet<P,T,F,M,R,D> ifnet, ExecutorListener<Boolean> listener){

		initiateValidityCheck(new IFNetValidityCheckingCallableGenerator<P,T,F,M,R,D>(ifnet), listener);
	}
	
	public static <P extends AbstractIFNetPlace<F>,
	   			   T extends AbstractIFNetTransition<F>, 
	   			   F extends AbstractIFNetFlowRelation<P,T>, 
	   			   M extends AbstractIFNetMarking,
	   			   R extends AbstractRegularIFNetTransition<F>,
	   			   D extends AbstractDeclassificationTransition<F>>
	
		void initiateValidityCheck(IFNetValidityCheckingCallableGenerator<P,T,F,M,R,D> generator, ExecutorListener<Boolean> listener){
		
		ThreadedIFNetValidityChecker<P,T,F,M,R,D> constructor = new ThreadedIFNetValidityChecker<P,T,F,M,R,D>(generator);
		constructor.addExecutorListener(listener);
		constructor.runCalculation();
	}
	
	public static <P extends AbstractIFNetPlace<F>,
	   			   T extends AbstractIFNetTransition<F>, 
	   			   F extends AbstractIFNetFlowRelation<P,T>, 
	   			   M extends AbstractIFNetMarking,
	   			   R extends AbstractRegularIFNetTransition<F>,
	   			   D extends AbstractDeclassificationTransition<F>>

	void checkValidity(AbstractIFNet<P,T,F,M,R,D> ifnet) throws PNValidationException{

		checkValidity(new IFNetValidityCheckingCallableGenerator<P,T,F,M,R,D>(ifnet));
	}

	public static <P extends AbstractIFNetPlace<F>,
				   T extends AbstractIFNetTransition<F>, 
				   F extends AbstractIFNetFlowRelation<P,T>, 
				   M extends AbstractIFNetMarking,
				   R extends AbstractRegularIFNetTransition<F>,
				   D extends AbstractDeclassificationTransition<F>>

	void checkValidity(IFNetValidityCheckingCallableGenerator<P,T,F,M,R,D> generator) throws PNValidationException{

		ThreadedIFNetValidityChecker<P,T,F,M,R,D> constructor = new ThreadedIFNetValidityChecker<P,T,F,M,R,D>(generator);
		constructor.runCalculation();
		constructor.getResult();
	}

}
