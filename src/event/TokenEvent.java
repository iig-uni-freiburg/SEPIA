package event;

import java.util.EventObject;

import petrinet.AbstractPlace;

@SuppressWarnings("serial")
public class TokenEvent<P extends AbstractPlace<?,?>> extends EventObject {
	
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
