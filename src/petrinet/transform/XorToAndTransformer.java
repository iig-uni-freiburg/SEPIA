package petrinet.transform;

import de.invation.code.toval.validate.ParameterException;
import de.invation.code.toval.validate.Validate;
import petrinet.AbstractFlowRelation;
import petrinet.AbstractMarking;
import petrinet.AbstractPetriNet;
import petrinet.AbstractPlace;
import petrinet.AbstractTransition;

public class XorToAndTransformer<P extends AbstractPlace<F,S>, 
								 T extends AbstractTransition<F,S>, 
								 F extends AbstractFlowRelation<P,T,S>, 
								 M extends AbstractMarking<S>, 
								 S extends Object>  extends PNTransformer<P,T,F,M,S>{
	
	protected P xorSplit = null;
	protected P xorJoin = null;
	
	public XorToAndTransformer(AbstractPetriNet<P, T, F, M, S> net, P xorSplit, P xorJoin) 
			throws ParameterException {
		super(net);
		Validate.notNull(xorSplit);
		Validate.notNull(xorJoin);
		this.xorSplit = xorSplit;
		this.xorJoin = xorJoin;
	}


	@Override
	public AbstractPetriNet<P, T, F, M, S> applyTransformation() {
		// TODO Auto-generated method stub
		AbstractPetriNet<P,T,F,M,S> result = net.clone();
		
		return result;
	}

}
