package de.uni.freiburg.iig.telematik.sepia.petrinet;

public interface PNTimeContext {
	
	public long getDelayPT(String placeName, String transitionName);
	
	public long getDelayTP(String transitionName, String placeName);

}
