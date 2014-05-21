package de.uni.freiburg.iig.telematik.sepia.petrinet.pt;

import de.uni.freiburg.iig.telematik.sepia.petrinet.pt.abstr.AbstractPTPlace;

public class PTPlace extends AbstractPTPlace<PTFlowRelation>{
	
	protected PTPlace(){
		super();
	}
	
	public PTPlace(String name, String label) {
		super(name, label);
	}

	public PTPlace(String name) {
		super(name);
	}
	
	@Override
	protected PTPlace newInstance() {
		return new PTPlace();
	}
	
	@Override
	public PTPlace clone(){
		return (PTPlace) super.clone();
	}
}
