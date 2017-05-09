package de.uni.freiburg.iig.telematik.sepia.petrinet.ptc.abstr;

import de.uni.freiburg.iig.telematik.sepia.petrinet.pt.abstr.AbstractPTTransition;

public abstract class AbstractPTCNetTransition<E extends AbstractPTCNetFlowRelation<? extends AbstractPTCNetPlace<E>, ? extends AbstractPTCNetTransition<E>>>
		extends AbstractPTTransition<E> {

	private static final long serialVersionUID = 478252467742688294L;
	private static final Double DEFAULT_TRANSITION_COST = 1.0;
	protected Double cost = DEFAULT_TRANSITION_COST;

	public AbstractPTCNetTransition(String name, boolean isEmpty) {
		super(name, isEmpty);
	}

	public AbstractPTCNetTransition(String name, String label, boolean isEmpty) {
		super(name, label, isEmpty);
	}

	public AbstractPTCNetTransition(String name, String label) {
		super(name, label);
	}

	public AbstractPTCNetTransition(String name) {
		super(name);
	}
	
	public Double getCost() {
		return cost;
	}

	public void setCost(Double cost) {
		this.cost = cost;
	}

}