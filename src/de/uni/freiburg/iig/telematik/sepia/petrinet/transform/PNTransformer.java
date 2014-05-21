package de.uni.freiburg.iig.telematik.sepia.petrinet.transform;

import de.invation.code.toval.validate.Validate;
import de.uni.freiburg.iig.telematik.sepia.mg.abstr.AbstractMarkingGraphRelation;
import de.uni.freiburg.iig.telematik.sepia.mg.abstr.AbstractMarkingGraphState;
import de.uni.freiburg.iig.telematik.sepia.petrinet.AbstractFlowRelation;
import de.uni.freiburg.iig.telematik.sepia.petrinet.AbstractMarking;
import de.uni.freiburg.iig.telematik.sepia.petrinet.AbstractPetriNet;
import de.uni.freiburg.iig.telematik.sepia.petrinet.AbstractPlace;
import de.uni.freiburg.iig.telematik.sepia.petrinet.AbstractTransition;

public abstract class PNTransformer<P extends AbstractPlace<F,S>, 
						   			T extends AbstractTransition<F,S>, 
						   			F extends AbstractFlowRelation<P,T,S>, 
						   			M extends AbstractMarking<S>, 
						   			S extends Object,
						   			X extends AbstractMarkingGraphState<M, S>,
						   			Y extends AbstractMarkingGraphRelation<M, X, S>>  {
	
	protected AbstractPetriNet<P,T,F,M,S,X,Y> net = null;
	
	public PNTransformer(AbstractPetriNet<P,T,F,M,S,X,Y> net) {
		Validate.notNull(net);
		this.net = net;
	}
	
	public abstract AbstractPetriNet<P,T,F,M,S,X,Y> applyTransformation();
}
