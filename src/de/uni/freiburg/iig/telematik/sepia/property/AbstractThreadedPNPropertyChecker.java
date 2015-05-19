package de.uni.freiburg.iig.telematik.sepia.property;

import de.invation.code.toval.thread.SingleThreadExecutorService;
import de.invation.code.toval.validate.Validate;
import de.uni.freiburg.iig.telematik.sepia.mg.abstr.AbstractMarkingGraphRelation;
import de.uni.freiburg.iig.telematik.sepia.mg.abstr.AbstractMarkingGraphState;
import de.uni.freiburg.iig.telematik.sepia.petrinet.AbstractFlowRelation;
import de.uni.freiburg.iig.telematik.sepia.petrinet.AbstractMarking;
import de.uni.freiburg.iig.telematik.sepia.petrinet.AbstractPetriNet;
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
	
	protected AbstractPetriNet<P,T,F,M,S,X,Y> petriNet = null;
	
	protected AbstractThreadedPNPropertyChecker(AbstractPetriNet<P,T,F,M,S,X,Y> petriNet){
		super();
		Validate.notNull(petriNet);
		this.petriNet = petriNet;
	}
	
	public void runCalculation() {
		setUpAndRun();
	}

}
