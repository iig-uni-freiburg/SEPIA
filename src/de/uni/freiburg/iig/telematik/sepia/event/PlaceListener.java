package de.uni.freiburg.iig.telematik.sepia.event;

import de.uni.freiburg.iig.telematik.sepia.petrinet.abstr.AbstractPlace;



public interface PlaceListener<P extends AbstractPlace<?,?>> {
	
	public void capacityChanged(CapacityEvent<? extends P> o);
	
	public boolean nameChangeRequest(P place, String newName);
	
}
