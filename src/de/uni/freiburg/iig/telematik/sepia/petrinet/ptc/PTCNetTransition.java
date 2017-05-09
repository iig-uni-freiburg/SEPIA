package de.uni.freiburg.iig.telematik.sepia.petrinet.ptc;

import de.uni.freiburg.iig.telematik.sepia.petrinet.ptc.abstr.AbstractPTCNetTransition;

public class PTCNetTransition extends AbstractPTCNetTransition<PTCNetFlowRelation> {

	private static final long serialVersionUID = 8765864248775516722L;

	// protected PTCNetTransition() {
	// super();
	// }

	public PTCNetTransition(String name, boolean isEmpty) {
		super(name, isEmpty);
	}

	public PTCNetTransition(String name, String label, boolean isEmpty) {
		super(name, label, isEmpty);
	}

	public PTCNetTransition(String name, String label) {
		super(name, label);
	}

	public PTCNetTransition(String name) {
		super(name);
	}

	@Override
	protected PTCNetTransition newInstance(String name) {
		return new PTCNetTransition(name);
	}
}
