package de.uni.freiburg.iig.telematik.sepia.petrinet.cpn;

import de.uni.freiburg.iig.telematik.sepia.petrinet.cpn.abstr.AbstractCPNTransition;

public class CPNTransition extends AbstractCPNTransition<CPNFlowRelation> {
	
	private static final long serialVersionUID = 5527205170645751350L;

	protected CPNTransition(){
		super();
	}

	public CPNTransition(String name, boolean isEmpty) {
		super(name, isEmpty);
	}

	public CPNTransition(String name, String label, boolean isEmpty) {
		super(name, label, isEmpty);
	}

	public CPNTransition(String name, String label) {
		super(name, label);
	}

	public CPNTransition(String name) {
		super(name);
	}

	@Override
	protected CPNTransition newInstance() {
		return new CPNTransition();
	}

	@Override
	public CPNTransition clone() {
		return (CPNTransition) super.clone();
	}
}
