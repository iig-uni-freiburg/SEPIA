package de.uni.freiburg.iig.telematik.sepia.event;

import de.uni.freiburg.iig.telematik.sepia.petrinet.AbstractPlace;



public interface CapacityListener<P extends AbstractPlace<?,?>> {
	
	public void capacityChanged(CapacityEvent<? extends P> o);
	
}
