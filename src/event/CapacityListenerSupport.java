package event;

import java.util.HashSet;

import petrinet.AbstractPlace;
import validate.ParameterException;
import validate.Validate;

public class CapacityListenerSupport<P extends AbstractPlace<?,?>> {
	
	protected HashSet<CapacityListener<P>> capacityListeners = new HashSet<CapacityListener<P>>();
	
	public void addCapacityListener(CapacityListener<P> l) throws ParameterException {
		Validate.notNull(l);
		capacityListeners.add(l);
	}
	
	public void removeCapacityListener(CapacityListener<P> l) throws ParameterException {
		Validate.notNull(l);
		capacityListeners.remove(l);
	}
	
	public void notifyCapacityChanged(CapacityEvent<? extends P> event){
		for(CapacityListener<P> l: capacityListeners)
			l.capacityChanged(event);
	}

}
