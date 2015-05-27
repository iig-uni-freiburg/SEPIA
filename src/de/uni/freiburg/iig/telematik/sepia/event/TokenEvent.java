package de.uni.freiburg.iig.telematik.sepia.event;

import java.util.EventObject;

import de.uni.freiburg.iig.telematik.sepia.petrinet.abstr.AbstractPlace;


public class TokenEvent<P extends AbstractPlace<?,?>> extends EventObject {
	
	private static final long serialVersionUID = -5436036405684466929L;
	
	public int numberOfTokens = 0;
	public String color = null;

	public TokenEvent(P source, int numberOfTokens, String color) {
		super(source);
		this.numberOfTokens = numberOfTokens;
		this.color = color;
	}
	
	public TokenEvent(P source, int numberOfTokens) {
		super(source);
		this.numberOfTokens = numberOfTokens;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public P getSource() {
		return (P) super.getSource();
	}

}
