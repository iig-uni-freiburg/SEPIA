package de.uni.freiburg.iig.telematik.sepia.petrinet.timedNet.abstr;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import de.uni.freiburg.iig.telematik.sepia.petrinet.timedNet.TimedNet;

public class NetConstraint {
	
	protected TimedNet net;
	protected ArrayList<String> pendingTransitions=new ArrayList<>();
	
	
	public NetConstraint(TimedNet net){
		this.net=net;
	}
	
	public void addPendingAction(String transitionName) {
		pendingTransitions.add(transitionName);
	}
	
	public List<String> getPendingActions(){
		return Collections.unmodifiableList(pendingTransitions);
	}
	
	public void reset(){
		pendingTransitions.clear();
	}

}
