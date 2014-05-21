package de.uni.freiburg.iig.telematik.sepia.petrinet.transform;

import de.invation.code.toval.validate.ParameterException;
import de.invation.code.toval.validate.Validate;
import de.uni.freiburg.iig.telematik.sepia.mg.abstr.AbstractMarkingGraphRelation;
import de.uni.freiburg.iig.telematik.sepia.mg.abstr.AbstractMarkingGraphState;
import de.uni.freiburg.iig.telematik.sepia.petrinet.AbstractFlowRelation;
import de.uni.freiburg.iig.telematik.sepia.petrinet.AbstractMarking;
import de.uni.freiburg.iig.telematik.sepia.petrinet.AbstractPetriNet;
import de.uni.freiburg.iig.telematik.sepia.petrinet.AbstractPlace;
import de.uni.freiburg.iig.telematik.sepia.petrinet.AbstractTransition;

public class XorToAndTransformer<P extends AbstractPlace<F,S>, 
								 T extends AbstractTransition<F,S>, 
								 F extends AbstractFlowRelation<P,T,S>, 
								 M extends AbstractMarking<S>, 
								 S extends Object,
								 X extends AbstractMarkingGraphState<M, S>,
						   		 Y extends AbstractMarkingGraphRelation<M, X, S>>  extends PNTransformer<P,T,F,M,S,X,Y>{
	
	protected P xorSplit = null;
	protected P xorJoin = null;
	
	public XorToAndTransformer(AbstractPetriNet<P, T, F, M, S, X, Y> net, P xorSplit, P xorJoin) 
			throws ParameterException {
		super(net);
		Validate.notNull(xorSplit);
		Validate.notNull(xorJoin);
		this.xorSplit = xorSplit;
		this.xorJoin = xorJoin;
	}


	@Override
	public AbstractPetriNet<P, T, F, M, S, X, Y> applyTransformation() {
		// TODO Auto-generated method stub
		AbstractPetriNet<P,T,F,M,S,X,Y> result = net.clone();
		
		return result;
	}

}
