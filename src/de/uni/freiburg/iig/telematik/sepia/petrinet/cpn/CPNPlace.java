package de.uni.freiburg.iig.telematik.sepia.petrinet.cpn;

import de.uni.freiburg.iig.telematik.sepia.petrinet.cpn.abstr.AbstractCPNPlace;

public class CPNPlace extends AbstractCPNPlace<CPNFlowRelation> {

	private static final long serialVersionUID = 3215280429385825876L;

//	protected CPNPlace(){
//		super();
//	}

	public CPNPlace(String name) {
		super(name);
	}

	public CPNPlace(String name, String label) {
		super(name, label);
	}

	@Override
	protected CPNPlace newInstance(String name) {
		return new CPNPlace(name);
	}
	
	@Override
	public CPNPlace clone(){
		return (CPNPlace) super.clone();
	}
	
}
