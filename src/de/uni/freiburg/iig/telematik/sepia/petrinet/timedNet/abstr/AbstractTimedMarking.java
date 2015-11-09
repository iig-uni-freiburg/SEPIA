/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.uni.freiburg.iig.telematik.sepia.petrinet.timedNet.abstr;

import java.util.ArrayList;
import java.util.List;
import java.util.TreeMap;

import de.uni.freiburg.iig.telematik.sepia.petrinet.pt.abstr.AbstractPTMarking;

/**
 *
 * @author richard
 */
public abstract class AbstractTimedMarking extends AbstractPTMarking {

	private static final long serialVersionUID = -5795568492094277347L;
	
	protected TreeMap<Double, List<String>> pendingActions = new TreeMap<>(); //Time and active Transitions

	public AbstractTimedMarking() {
        super();
    }
	
	public void addPendingAction(String transitionName, double time) {
		if (pendingActions.containsKey(time)) {
			pendingActions.get(time).add(transitionName);
		} else {
			ArrayList<String> transitions = new ArrayList<>();
			transitions.add(transitionName);
			pendingActions.put(time, transitions);
		}
//		TokenConstraints<Integer> constraint = new TokenConstraints<>(transitionName);
//		if (pendingActions.containsKey(time) && pendingActions.get(time) != null) {
//			// time entry is available
//			pendingActions.get(time).add(constraint);
//		} else { // List not initialized. Create and add List
//			ArrayList<TokenConstraints<Integer>> pendingActionList = new ArrayList<>();
//			pendingActionList.add(constraint);
//			pendingActions.put(time, pendingActionList);
//		}
	}
	
	public List<String> getNextPendingAction(){
		return pendingActions.firstEntry().getValue();
	}
	
	public double getTimeOfNextPendingAction(){
		return pendingActions.firstKey();
	}
	
	public void removeNextPendingAction(){
		pendingActions.remove(pendingActions.firstKey());
	}
	
	public boolean hasPendingActions(){
		return !pendingActions.isEmpty();
	}

}
