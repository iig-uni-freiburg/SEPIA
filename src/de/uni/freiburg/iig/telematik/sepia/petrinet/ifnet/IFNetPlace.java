package de.uni.freiburg.iig.telematik.sepia.petrinet.ifnet;

import de.uni.freiburg.iig.telematik.sepia.petrinet.ifnet.abstr.AbstractIFNetPlace;

public class IFNetPlace extends AbstractIFNetPlace<IFNetFlowRelation> {

	private static final long serialVersionUID = 4230385963571421717L;

	protected IFNetPlace(){
		super();
	}

	public IFNetPlace(String name, String label) {
		super(name, label);
	}

	public IFNetPlace(String name) {
		super(name);
	}

	@Override
	protected IFNetPlace newInstance() {
		return new IFNetPlace();
	}

}
