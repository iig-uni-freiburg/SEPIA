package de.uni.freiburg.iig.telematik.sepia.property;

import de.invation.code.toval.thread.AbstractCallable;
import de.invation.code.toval.validate.Validate;
import de.uni.freiburg.iig.telematik.sepia.petrinet.AbstractFlowRelation;
import de.uni.freiburg.iig.telematik.sepia.petrinet.AbstractMarking;
import de.uni.freiburg.iig.telematik.sepia.petrinet.AbstractPlace;
import de.uni.freiburg.iig.telematik.sepia.petrinet.AbstractTransition;

public abstract class AbstractPNPropertyCheckerCallable<P extends AbstractPlace<F,S>, 
														T extends AbstractTransition<F,S>, 
														F extends AbstractFlowRelation<P,T,S>, 
														M extends AbstractMarking<S>, 
														S extends Object,
														R> extends AbstractCallable<R> {

	private AbstractCallableGenerator<P,T,F,M,S> generator = null;
	
	protected AbstractPNPropertyCheckerCallable(AbstractCallableGenerator<P,T,F,M,S> generator){
		Validate.notNull(generator);
		this.generator = generator;
	}
	
	protected AbstractCallableGenerator<P,T,F,M,S> getGenerator(){
		return generator;
	}
}
