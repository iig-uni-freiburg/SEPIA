package de.uni.freiburg.iig.telematik.sepia.property.dead;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public class DeadTransitionCheckResult {
	
	private Set<String> deadTransitions = new HashSet<String>();
	
	public void setDeadTransitions(Set<String> deadTransitions){
		deadTransitions.clear();
		deadTransitions.addAll(deadTransitions);
	}
	
	public boolean isDead(String transition){
		return deadTransitions.contains(transition);
	}
	
	public boolean existDeadTransitions(){
		return !deadTransitions.isEmpty();
	}
	
	public Set<String> getDeadTransitions(){
		return Collections.unmodifiableSet(deadTransitions);
	}

}
