package event;

import petrinet.AbstractPlace;



public interface CapacityListener<P extends AbstractPlace<?,?>> {
	
	public void capacityChanged(CapacityEvent<? extends P> o);
	
}
