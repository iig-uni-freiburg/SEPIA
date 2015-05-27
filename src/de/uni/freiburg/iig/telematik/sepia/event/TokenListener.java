package de.uni.freiburg.iig.telematik.sepia.event;

import de.uni.freiburg.iig.telematik.sepia.petrinet.abstr.AbstractPlace;

public interface TokenListener<P extends AbstractPlace<?,?>> {
	
	public void tokensAdded(TokenEvent<? extends P> o);
	
	public void tokensRemoved(TokenEvent<? extends P> o);

}
