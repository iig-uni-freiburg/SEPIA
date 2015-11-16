package de.uni.freiburg.iig.telematik.sepia.petrinet.timedNet.concepts;

public interface IResource {
	
	public String getName();
	
	public boolean isAvailable();
	
	public void use();
	
	public void unUse();

}
