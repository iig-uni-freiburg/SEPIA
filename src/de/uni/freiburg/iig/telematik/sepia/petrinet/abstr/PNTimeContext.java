package de.uni.freiburg.iig.telematik.sepia.petrinet.abstr;

public interface PNTimeContext {
	
	public double getDelayPT(String placeName, String transitionName);
	
	public double getDelayTP(String transitionName, String placeName);

}
