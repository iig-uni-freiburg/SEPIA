package de.uni.freiburg.iig.telematik.sepia.event;

import java.util.HashSet;

import de.invation.code.toval.validate.ParameterException;
import de.invation.code.toval.validate.Validate;
import de.uni.freiburg.iig.telematik.sepia.petrinet.AbstractPlace;


public class PlaceListenerSupport<P extends AbstractPlace<?,?>> {
	
	protected HashSet<PlaceListener<P>> listeners = new HashSet<PlaceListener<P>>();
	
	public void addCapacityListener(PlaceListener<P> l) throws ParameterException {
		Validate.notNull(l);
		listeners.add(l);
	}
	
	public void removeCapacityListener(PlaceListener<P> l) throws ParameterException {
		Validate.notNull(l);
		listeners.remove(l);
	}
	
	public void notifyCapacityChanged(CapacityEvent<? extends P> event){
		for(PlaceListener<P> l: listeners)
			l.capacityChanged(event);
	}
	
	public boolean requestNameChangePermission(P place, String newName){
		boolean nameChangePermitted = true;
		for(PlaceListener<P> listener: listeners){
			if(!listener.nameChangeRequest(place, newName)){
				nameChangePermitted = false;
			}
		}
		return nameChangePermitted;
	}

}
