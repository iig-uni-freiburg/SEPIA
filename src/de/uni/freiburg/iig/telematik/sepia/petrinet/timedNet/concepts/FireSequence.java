package de.uni.freiburg.iig.telematik.sepia.petrinet.timedNet.concepts;

import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;

public class FireSequence implements Comparable<FireSequence> {
	
	protected LinkedList<FireElement> entries = new LinkedList<>();
	//private String netName;
	
	/**Create Fire Sequence**/
	public FireSequence() {
		//this.netName=netName;
	}
	
	public void add(FireElement fireElement){
		entries.add(fireElement);
	}
	
	public double getEndingTime(){
		if(entries==null||entries.isEmpty())
			return Double.NaN;
		return entries.getLast().getEndTime();
	}
	
	public double getStartingTime(){
		if(entries==null||entries.isEmpty())
			return Double.NaN;
		return entries.getFirst().getTime();
	}
	
	public LinkedList<FireElement> getSequence(){
		return entries;
	}
	
	public double getDuration(){
		return getEndingTime()-getStartingTime();
	}
	
	public String toString(){
		StringBuilder b = new StringBuilder();
		for (FireElement elem: entries){
			b.append(elem.toString());
			b.append(". ");
		}
		return b.toString();
	}

	@Override
	/**compares durations of the FireSequence**/
	public int compareTo(FireSequence o) {
		return new Double(getDuration()).compareTo(o.getDuration());
	}
	
	public Collection<String> getNetNames(){
		HashSet<String> set = new HashSet<>();
		for(FireElement elem:entries){
			set.add(elem.getTransition().getNet().getName());
		}
		return set;
	}
	
	public String getNetName(){
		return entries.getFirst().getTransition().getNet().getName();
	}

	@Override
	public int hashCode() {
//		final int prime = 31;
//		int result = 1;
//		result = prime * result + ((entries == null) ? 0 : entries.hashCode());
//		return result;
		return getTransitionString().hashCode();
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		FireSequence other = (FireSequence) obj;
		if (entries == null) {
			if (other.entries != null)
				return false;
		} 
			return getTransitionString().equals(other.getTransitionString());
		
	}
	
	/**returns the single activity names comma seperated**/
	public String getTransitionString(){
		StringBuilder b = new StringBuilder();
		for (FireElement e:entries){
			b.append(e.getTransitionName());
			b.append("[");
			b.append(e.getTransition().getNet().getName());
			b.append("]");
			b.append(",");
		}
		return b.toString();
	}
	
	

}
