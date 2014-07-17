package de.uni.freiburg.iig.telematik.sepia.event;

import java.io.Serializable;
import java.util.HashSet;

import de.invation.code.toval.validate.Validate;
import de.uni.freiburg.iig.telematik.sepia.petrinet.AbstractPlace;

public class TokenListenerSupport<P extends AbstractPlace<?,?>> implements Serializable{

	private static final long serialVersionUID = -9115760450580421633L;
	
	protected HashSet<TokenListener<P>> tokenListeners = new HashSet<TokenListener<P>>();
	
	public void addTokenListener(TokenListener<P> l) {
		Validate.notNull(l);
		tokenListeners.add(l);
	}
	
	public void removeTokenListener(TokenListener<P> l) {
		Validate.notNull(l);
		tokenListeners.remove(l);
	}
	
	public void notifyTokensAdded(TokenEvent<? extends P> event){
		for(TokenListener<P> l: tokenListeners)
			l.tokensAdded(event);
	}
	
	public void notifyTokensRemoved(TokenEvent<? extends P> event){
		for(TokenListener<P> l: tokenListeners)
			l.tokensRemoved(event);
	}

}
