package de.uni.freiburg.iig.telematik.sepia.petrinet.transform;

import de.invation.code.toval.validate.ParameterException;
import de.invation.code.toval.validate.Validate;
import de.uni.freiburg.iig.telematik.sepia.petrinet.AbstractFlowRelation;
import de.uni.freiburg.iig.telematik.sepia.petrinet.AbstractMarking;
import de.uni.freiburg.iig.telematik.sepia.petrinet.AbstractPetriNet;
import de.uni.freiburg.iig.telematik.sepia.petrinet.AbstractPlace;
import de.uni.freiburg.iig.telematik.sepia.petrinet.AbstractTransition;

public abstract class PNTransformer<P extends AbstractPlace<F,S>, 
						   			T extends AbstractTransition<F,S>, 
						   			F extends AbstractFlowRelation<P,T,S>, 
						   			M extends AbstractMarking<S>, 
						   			S extends Object>  {
	
	protected AbstractPetriNet<P,T,F,M,S> net = null;
	
	public PNTransformer(AbstractPetriNet<P,T,F,M,S> net) throws ParameterException{
		Validate.notNull(net);
		this.net = net;
	}
	
	public abstract AbstractPetriNet<P,T,F,M,S> applyTransformation();

}
