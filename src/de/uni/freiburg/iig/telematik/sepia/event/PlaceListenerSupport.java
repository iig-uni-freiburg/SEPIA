package de.uni.freiburg.iig.telematik.sepia.event;

import java.io.Serializable;
import java.util.HashSet;

import de.invation.code.toval.validate.Validate;
import de.uni.freiburg.iig.telematik.sepia.petrinet.AbstractPlace;


public class PlaceListenerSupport<P extends AbstractPlace<?,?>> implements Serializable{
	
	private static final long serialVersionUID = 6957535934703292103L;
	
	protected HashSet<PlaceListener<P>> listeners = new HashSet<PlaceListener<P>>();
	
	public void addCapacityListener(PlaceListener<P> l) {
		Validate.notNull(l);
		listeners.add(l);
	}
	
	public void removeCapacityListener(PlaceListener<P> l) {
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
