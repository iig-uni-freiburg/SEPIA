package de.uni.freiburg.iig.telematik.sepia.petrinet.timedNet.concepts;

public interface ITimeBehaviour {
	
	//for implementation maybe use colt framework
	
	public boolean isAvailable();
	
	public void setAvailability(boolean isAvailable);
	
	public double getNeededTime();
	
	

}
