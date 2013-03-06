package event;

import petrinet.AbstractPlace;



public interface TokenListener<P extends AbstractPlace<?,?>> {
	
	public void tokensAdded(TokenEvent<? extends P> o);
	
	public void tokensRemoved(TokenEvent<? extends P> o);

}
