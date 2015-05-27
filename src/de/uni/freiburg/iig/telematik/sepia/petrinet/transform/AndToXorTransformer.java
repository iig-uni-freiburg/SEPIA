package de.uni.freiburg.iig.telematik.sepia.petrinet.transform;

import de.invation.code.toval.validate.Validate;
import de.uni.freiburg.iig.telematik.sepia.petrinet.abstr.AbstractFlowRelation;
import de.uni.freiburg.iig.telematik.sepia.petrinet.abstr.AbstractMarking;
import de.uni.freiburg.iig.telematik.sepia.petrinet.abstr.AbstractPetriNet;
import de.uni.freiburg.iig.telematik.sepia.petrinet.abstr.AbstractPlace;
import de.uni.freiburg.iig.telematik.sepia.petrinet.abstr.AbstractTransition;

public class AndToXorTransformer<P extends AbstractPlace<F,S>, 
								 T extends AbstractTransition<F,S>, 
								 F extends AbstractFlowRelation<P,T,S>, 
								 M extends AbstractMarking<S>, 
								 S extends Object>  extends PNTransformer<P,T,F,M,S>{
	
	protected T andSplit = null;
	protected T andJoin = null;

	public AndToXorTransformer(AbstractPetriNet<P, T, F, M, S> net, T andSplit, T andJoin) {
		super(net);
		Validate.notNull(andSplit);
		Validate.notNull(andJoin);
		this.andSplit = andSplit;
		this.andJoin = andJoin;
	}

	@Override
	public AbstractPetriNet<P, T, F, M, S> applyTransformation() {
		// TODO Auto-generated method stub
		AbstractPetriNet<P,T,F,M,S> result = net.clone();
		
		return result;
	}
}
