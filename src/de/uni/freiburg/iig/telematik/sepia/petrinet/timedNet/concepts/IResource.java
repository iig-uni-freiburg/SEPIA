package de.uni.freiburg.iig.telematik.sepia.petrinet.timedNet.concepts;

public interface IResource {
	
	public String getName();
	
	public void setName(String name);
	
	public boolean isAvailable();
	
	public void use();
	
	public void unUse();
	
	public void reset();

}
