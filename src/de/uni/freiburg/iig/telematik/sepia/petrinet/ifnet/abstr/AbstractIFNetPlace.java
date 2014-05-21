package de.uni.freiburg.iig.telematik.sepia.petrinet.ifnet.abstr;

import de.uni.freiburg.iig.telematik.sepia.petrinet.cwn.abstr.AbstractCWNPlace;

public abstract class AbstractIFNetPlace<E extends AbstractIFNetFlowRelation<? extends AbstractIFNetPlace<E>, 
																		 	 ? extends AbstractIFNetTransition<E>>> 

										   extends AbstractCWNPlace<E> {
	
	protected AbstractIFNetPlace(){
		super();
	}

	public AbstractIFNetPlace(String name, String label) {
		super(name, label);
	}

	public AbstractIFNetPlace(String name) {
		super(name);
	}

}
