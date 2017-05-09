package de.uni.freiburg.iig.telematik.sepia.petrinet.ptc.abstr;

import de.uni.freiburg.iig.telematik.sepia.petrinet.pt.abstr.AbstractPTPlace;

public abstract class AbstractPTCNetPlace<E extends AbstractPTCNetFlowRelation<? extends AbstractPTCNetPlace<E>, 
																			? extends AbstractPTCNetTransition<E>>>
								extends AbstractPTPlace<E> {

	private static final long serialVersionUID = -7056298552150837686L;
	private static final Double DEFAULT_PLACE_COST = 1.0;
	protected Double cost = DEFAULT_PLACE_COST;

	// protected AbstractPTCNetPlace(){
	// super();
	// }

	public AbstractPTCNetPlace(String name, String label) {
		super(name, label);
	}

	public AbstractPTCNetPlace(String name) {
		super(name);
	}
	
	public Double getCost() {
		return cost;
	}

	public void setCost(Double cost) {
		this.cost = cost;
	}
}
