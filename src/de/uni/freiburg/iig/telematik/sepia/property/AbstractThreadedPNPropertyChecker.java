package de.uni.freiburg.iig.telematik.sepia.property;

import de.invation.code.toval.thread.SingleThreadExecutorService;
import de.invation.code.toval.validate.Validate;
import de.uni.freiburg.iig.telematik.sepia.mg.abstr.AbstractMarkingGraphRelation;
import de.uni.freiburg.iig.telematik.sepia.mg.abstr.AbstractMarkingGraphState;
import de.uni.freiburg.iig.telematik.sepia.petrinet.AbstractFlowRelation;
import de.uni.freiburg.iig.telematik.sepia.petrinet.AbstractMarking;
import de.uni.freiburg.iig.telematik.sepia.petrinet.AbstractPlace;
import de.uni.freiburg.iig.telematik.sepia.petrinet.AbstractTransition;

public abstract class AbstractThreadedPNPropertyChecker<P extends AbstractPlace<F,S>, 
														T extends AbstractTransition<F,S>, 
														F extends AbstractFlowRelation<P,T,S>, 
														M extends AbstractMarking<S>, 
														S extends Object,
														X extends AbstractMarkingGraphState<M,S>,
														Y extends AbstractMarkingGraphRelation<M,X,S>,
														Z> extends SingleThreadExecutorService<Z>{
	
	private AbstractCallableGenerator<P,T,F,M,S,X,Y> generator = null;
	
	protected AbstractThreadedPNPropertyChecker(AbstractCallableGenerator<P,T,F,M,S,X,Y> generator){
		super();
		Validate.notNull(generator);
		this.generator = generator;
	}
	
	protected AbstractCallableGenerator<P,T,F,M,S,X,Y> getGenerator(){
		return generator;
	}
	
	public void runCalculation() {
		setUpAndRun();
	}

}
