package de.uni.freiburg.iig.telematik.sepia.petrinet.cwn;

import de.uni.freiburg.iig.telematik.sepia.petrinet.cwn.abstr.AbstractCWNPlace;

public class CWNPlace extends AbstractCWNPlace<CWNFlowRelation> {
	
	private static final long serialVersionUID = 7217642956510578681L;

	protected CWNPlace(){
		super();
	}

	public CWNPlace(String name, String label) {
		super(name, label);
	}

	public CWNPlace(String name) {
		super(name);
	}

	@Override
	protected CWNPlace newInstance() {
		return new CWNPlace();
	}

}
