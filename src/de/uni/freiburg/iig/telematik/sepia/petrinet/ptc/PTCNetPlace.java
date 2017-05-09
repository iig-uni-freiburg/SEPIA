package de.uni.freiburg.iig.telematik.sepia.petrinet.ptc;

import de.uni.freiburg.iig.telematik.sepia.petrinet.ptc.abstr.AbstractPTCNetPlace;

public class PTCNetPlace extends AbstractPTCNetPlace<PTCNetFlowRelation> {

	private static final long serialVersionUID = 4230385963571421717L;

	// protected PTCNetPlace(){
	// super();
	// }

	public PTCNetPlace(String name, String label) {
		super(name, label);
	}

	public PTCNetPlace(String name) {
		super(name);
	}

	@Override
	protected PTCNetPlace newInstance(String name) {
		return new PTCNetPlace(name);
	}

}
