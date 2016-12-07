package de.uni.freiburg.iig.telematik.sepia.petrinet.timedNet.concepts;

import java.util.List;

import de.uni.freiburg.iig.telematik.sepia.petrinet.timedNet.abstr.AbstractTimedTransition;

public interface IStatisticListener {
	
	public void transitionStateChange(double time, ExecutionState state, AbstractTimedTransition transition);
	public void ressourceUsageChange(double time, ExecutionState state, AbstractTimedTransition transition, List<String> resources);
	//public void reachedDeadline(String netName, double time, double deadline, boolean missed);
	public void reset();
	

}
