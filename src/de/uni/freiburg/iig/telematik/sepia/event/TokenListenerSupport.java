package de.uni.freiburg.iig.telematik.sepia.event;

import de.invation.code.toval.event.AbstractListenerSupport;
import de.uni.freiburg.iig.telematik.sepia.petrinet.AbstractPlace;

public class TokenListenerSupport<P extends AbstractPlace<?,?>> extends AbstractListenerSupport<TokenListener<P>>{

	private static final long serialVersionUID = -9115760450580421633L;
	
	public void notifyTokensAdded(TokenEvent<? extends P> event){
		for(TokenListener<P> l: listeners)
			l.tokensAdded(event);
	}
	
	public void notifyTokensRemoved(TokenEvent<? extends P> event){
		for(TokenListener<P> l: listeners)
			l.tokensRemoved(event);
	}

}
