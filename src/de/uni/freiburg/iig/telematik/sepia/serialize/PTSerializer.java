package de.uni.freiburg.iig.telematik.sepia.serialize;

import de.invation.code.toval.validate.ParameterException;
import de.uni.freiburg.iig.telematik.sepia.petrinet.AbstractPetriNet;
import de.uni.freiburg.iig.telematik.sepia.petrinet.NetType;
import de.uni.freiburg.iig.telematik.sepia.petrinet.pt.abstr.AbstractPTFlowRelation;
import de.uni.freiburg.iig.telematik.sepia.petrinet.pt.abstr.AbstractPTMarking;
import de.uni.freiburg.iig.telematik.sepia.petrinet.pt.abstr.AbstractPTNet;
import de.uni.freiburg.iig.telematik.sepia.petrinet.pt.abstr.AbstractPTPlace;
import de.uni.freiburg.iig.telematik.sepia.petrinet.pt.abstr.AbstractPTTransition;

public abstract class PTSerializer<P extends AbstractPTPlace<F>, 
								   T extends AbstractPTTransition<F>, 
								   F extends AbstractPTFlowRelation<P,T>, 
								   M extends AbstractPTMarking> extends PNSerializer<P,T,F,M,Integer>{

	public PTSerializer(AbstractPetriNet<P, T, F, M, Integer> petriNet) throws ParameterException {
		super(petriNet);
	}

	@Override
	public NetType acceptedNetType() {
		return NetType.PTNet;
	}

	@Override
	public AbstractPTNet<P, T, F, M> getPetriNet() {
		return (AbstractPTNet<P,T,F,M>) super.getPetriNet();
	}

}
