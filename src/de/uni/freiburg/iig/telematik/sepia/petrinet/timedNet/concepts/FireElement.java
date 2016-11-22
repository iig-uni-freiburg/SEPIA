package de.uni.freiburg.iig.telematik.sepia.petrinet.timedNet.concepts;

import java.text.DecimalFormat;
import java.util.LinkedList;
import java.util.List;

import de.uni.freiburg.iig.telematik.sepia.petrinet.timedNet.abstr.AbstractTimedTransition;

public class FireElement {
	
	private double time;
	private double endTime=Double.NaN;
	private AbstractTimedTransition t;
	private List<String> resources = new LinkedList<>();
	

	private static final DecimalFormat format = new DecimalFormat("##.##");
	
	public FireElement(AbstractTimedTransition t) {
		this.t=t;
		time = t.getNet().getCurrentTime();
	}
	
	public FireElement(AbstractTimedTransition t, double time, double endTime) {
		this(t,time);
		this.endTime=endTime;
	}
	
	public FireElement(AbstractTimedTransition t, double time) {
		this.t=t;
		this.time=time;
		this.resources.addAll(t.getUsedResources());
	}
	
	public List<String> getResources() {
		return resources;
	}
	
	public double getTime() {
		return time;
	}
	public void setTime(double time) {
		this.time = time;
	}
	public AbstractTimedTransition getTransition() {
		return t;
	}
	public void setTransition(AbstractTimedTransition t) {
		this.t = t;
	}
	
	public String toString(){
		// return t.getNet().getName()+": "+t.getLabel()+" ("+time+")";
		String sTime=format.format(time);
		if(endTime==Double.NaN)
			return t.getLabel()+printRes()+" ("+sTime+")";
		else{
			String sEndTime=format.format(endTime);
			return t.getLabel()+printRes()+" ("+sTime+"-"+sEndTime+")";
		}
	}
	
	private String printRes(){
		String result = "";
		for(String res:resources)
			result+=(res+" ");
		return "["+result+"]";
	}
	
	public void setEndTime(double endTime){
		this.endTime=endTime;
	}
	
	public double getEndTime(){
		return this.endTime;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		long temp;
		temp = Double.doubleToLongBits(endTime);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		result = prime * result + ((resources == null) ? 0 : resources.hashCode());
		result = prime * result + ((t == null) ? 0 : t.hashCode());
		temp = Double.doubleToLongBits(time);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		FireElement other = (FireElement) obj;
		if (Double.doubleToLongBits(endTime) != Double.doubleToLongBits(other.endTime))
			return false;
		if (resources == null) {
			if (other.resources != null)
				return false;
		} else if (!resources.equals(other.resources))
			return false;
		if (t == null) {
			if (other.t != null)
				return false;
		} else if (!t.equals(other.t))
			return false;
		if (Double.doubleToLongBits(time) != Double.doubleToLongBits(other.time))
			return false;
		return true;
	}
	
	public String getTransitionName(){
		return t.getLabel();
	}


}
