package de.uni.freiburg.iig.telematik.sepia.petrinet.timedNet.abstr;


public class TokenConstraints<S> {
	public String placeName = null;
	public S tokens = null;

	public void setPlaceName(String placeName) {
		this.placeName = placeName;
	}
	public void setTokens(S tokens) {
		this.tokens = tokens;
	}
	
	public TokenConstraints(String transitionName, S tokens){
		this.placeName=transitionName;
		this.tokens=tokens;
	}
}
