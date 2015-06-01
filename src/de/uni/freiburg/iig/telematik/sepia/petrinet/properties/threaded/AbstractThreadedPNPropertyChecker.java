package de.uni.freiburg.iig.telematik.sepia.petrinet.properties.threaded;

import de.invation.code.toval.thread.SingleThreadExecutorService;
import de.invation.code.toval.validate.Validate;
import de.uni.freiburg.iig.telematik.sepia.petrinet.abstr.AbstractFlowRelation;
import de.uni.freiburg.iig.telematik.sepia.petrinet.abstr.AbstractMarking;
import de.uni.freiburg.iig.telematik.sepia.petrinet.abstr.AbstractPlace;
import de.uni.freiburg.iig.telematik.sepia.petrinet.abstr.AbstractTransition;

public abstract class AbstractThreadedPNPropertyChecker<P extends AbstractPlace<F,S>, 
														T extends AbstractTransition<F,S>, 
														F extends AbstractFlowRelation<P,T,S>, 
														M extends AbstractMarking<S>, 
														S extends Object,
														V,Z,E extends Exception> extends SingleThreadExecutorService<V,Z,E>{
	
	private AbstractCallableGenerator<P,T,F,M,S> generator = null;
	
	protected AbstractThreadedPNPropertyChecker(AbstractCallableGenerator<P,T,F,M,S> generator){
		super();
		Validate.notNull(generator);
		this.generator = generator;
	}
	
	protected AbstractCallableGenerator<P,T,F,M,S> getGenerator(){
		return generator;
	}
	
	public final void runCalculation() {
		setUpAndRun();
	}

}
