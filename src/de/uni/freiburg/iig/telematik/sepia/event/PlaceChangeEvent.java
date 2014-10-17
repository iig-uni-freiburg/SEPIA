package de.uni.freiburg.iig.telematik.sepia.event;

import java.util.EventObject;

import de.uni.freiburg.iig.telematik.sepia.petrinet.AbstractPlace;


public class PlaceChangeEvent<P extends AbstractPlace<?,?>> extends EventObject {
	
	private static final long serialVersionUID = -5415894461740664843L;
	
	public P place = null;
	public int affectedRelations = 0;

	public PlaceChangeEvent(P place) {
		super(place);
		this.place = place;
	}
	
	public PlaceChangeEvent(P place, int affectedRelations) {
		this(place);
		this.affectedRelations = affectedRelations;
	}

}
