package de.uni.freiburg.iig.telematik.sepia.petrinet.ptc.abstr;

import java.util.Set;

import de.uni.freiburg.iig.telematik.sepia.petrinet.NetType;
import de.uni.freiburg.iig.telematik.sepia.petrinet.pt.abstr.AbstractPTNet;

public abstract class AbstractPTCNet<P extends AbstractPTCNetPlace<F>, 
									T extends AbstractPTCNetTransition<F>, 
									F extends AbstractPTCNetFlowRelation<P, T>, 
									M extends AbstractPTCNetMarking>

		extends AbstractPTNet<P, T, F, M> {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2427014420202147657L;

	public AbstractPTCNet() {
		super();
		initialMarking = createNewMarking();
		marking = createNewMarking();
	}

	public AbstractPTCNet(Set<String> places, Set<String> transitions, M initialMarking) {
		super(places, transitions, initialMarking);
	}

	@Override
	public NetType getNetType() {
		return NetType.PTCNet;
	}

	@Override
	public M getInitialMarking() {
		return super.getInitialMarking();
	}

	@Override
	public M getMarking() {
		return super.getMarking();
	}

}