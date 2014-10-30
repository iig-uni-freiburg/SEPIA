package de.uni.freiburg.iig.telematik.sepia.petrinet.ifnet.abstr;

import de.uni.freiburg.iig.telematik.sepia.petrinet.cpn.abstr.AbstractCPNPlace;

public abstract class AbstractIFNetPlace<E extends AbstractIFNetFlowRelation<? extends AbstractIFNetPlace<E>, 
																		 	 ? extends AbstractIFNetTransition<E>>> 

										   extends AbstractCPNPlace<E> {
	
	private static final long serialVersionUID = -7056298552150837686L;

//	protected AbstractIFNetPlace(){
//		super();
//	}

	public AbstractIFNetPlace(String name, String label) {
		super(name, label);
	}

	public AbstractIFNetPlace(String name) {
		super(name);
	}

}
