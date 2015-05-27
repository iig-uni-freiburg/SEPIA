package de.uni.freiburg.iig.telematik.sepia.event;

import de.invation.code.toval.event.AbstractListenerSupport;
import de.uni.freiburg.iig.telematik.sepia.petrinet.abstr.AbstractPlace;

public class PlaceListenerSupport<P extends AbstractPlace<?,?>> extends AbstractListenerSupport<PlaceListener<P>>{
	
	private static final long serialVersionUID = 6957535934703292103L;
	
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
