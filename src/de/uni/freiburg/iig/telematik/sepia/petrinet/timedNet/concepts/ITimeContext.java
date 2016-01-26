package de.uni.freiburg.iig.telematik.sepia.petrinet.timedNet.concepts;

import de.invation.code.toval.misc.NamedComponent;

public interface ITimeContext extends NamedComponent{
	
	public String getName();
	
	public void setName(String name);
	
	public double getTimeFor(String activity);
	
	public ITimeBehaviour getTimeObjectFor(String activity);
	
	public boolean containsActivity(String activity);
	
	public void reset();
	
	public double incrementTime(double inc);
	
	public double getTime();

}
