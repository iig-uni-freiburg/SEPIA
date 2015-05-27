package de.uni.freiburg.iig.telematik.sepia.petrinet.transform;

import de.invation.code.toval.validate.Validate;
import de.uni.freiburg.iig.telematik.sepia.petrinet.abstr.AbstractFlowRelation;
import de.uni.freiburg.iig.telematik.sepia.petrinet.abstr.AbstractMarking;
import de.uni.freiburg.iig.telematik.sepia.petrinet.abstr.AbstractPetriNet;
import de.uni.freiburg.iig.telematik.sepia.petrinet.abstr.AbstractPlace;
import de.uni.freiburg.iig.telematik.sepia.petrinet.abstr.AbstractTransition;

public abstract class PNTransformer<P extends AbstractPlace<F,S>, 
						   			T extends AbstractTransition<F,S>, 
						   			F extends AbstractFlowRelation<P,T,S>, 
						   			M extends AbstractMarking<S>, 
						   			S extends Object>  {
	
	protected AbstractPetriNet<P,T,F,M,S> net = null;
	
	public PNTransformer(AbstractPetriNet<P,T,F,M,S> net) {
		Validate.notNull(net);
		this.net = net;
	}
	
	public abstract AbstractPetriNet<P,T,F,M,S> applyTransformation();
}
