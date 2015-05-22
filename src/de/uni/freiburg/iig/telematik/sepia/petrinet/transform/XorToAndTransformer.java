package de.uni.freiburg.iig.telematik.sepia.petrinet.transform;

import de.invation.code.toval.validate.Validate;
import de.uni.freiburg.iig.telematik.sepia.petrinet.AbstractFlowRelation;
import de.uni.freiburg.iig.telematik.sepia.petrinet.AbstractMarking;
import de.uni.freiburg.iig.telematik.sepia.petrinet.AbstractPetriNet;
import de.uni.freiburg.iig.telematik.sepia.petrinet.AbstractPlace;
import de.uni.freiburg.iig.telematik.sepia.petrinet.AbstractTransition;

public class XorToAndTransformer<P extends AbstractPlace<F,S>, 
								 T extends AbstractTransition<F,S>, 
								 F extends AbstractFlowRelation<P,T,S>, 
								 M extends AbstractMarking<S>, 
								 S extends Object>  extends PNTransformer<P,T,F,M,S>{
	
	protected P xorSplit = null;
	protected P xorJoin = null;
	
	public XorToAndTransformer(AbstractPetriNet<P, T, F, M, S> net, P xorSplit, P xorJoin) {
		super(net);
		Validate.notNull(xorSplit);
		Validate.notNull(xorJoin);
		this.xorSplit = xorSplit;
		this.xorJoin = xorJoin;
	}

	@Override
	public AbstractPetriNet<P, T, F, M, S> applyTransformation() {
		AbstractPetriNet<P,T,F,M,S> result = net.clone();
		return result;
	}
}
