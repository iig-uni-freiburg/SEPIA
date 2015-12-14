package de.uni.freiburg.iig.telematik.sepia.petrinet.timedNet.abstr;

import java.util.AbstractMap;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import de.uni.freiburg.iig.telematik.sepia.petrinet.timedNet.concepts.ExecutionState;
import de.uni.freiburg.iig.telematik.sepia.petrinet.timedNet.concepts.IStatisticListener;

public class StatisticListener implements IStatisticListener{
	
	private static StatisticListener myself=new StatisticListener();
	
	private Map<String,List<Entry<Double, ExecutionState>>> workingTimes = new HashMap<>();
	private Map<String,List<Entry<Double, ExecutionState>>> resourceUsage = new HashMap<>();
	private Map<String,List<Entry<Double,Boolean>>> deadlineMisses = new HashMap<>();
	
	private StatisticListener() {
		
	}
	
	public static StatisticListener getInstance(){
		return myself;
	}


	@Override
	public void transitionStateChange(double time, ExecutionState state, AbstractTimedTransition transition) {
		if(!workingTimes.containsKey(transition.getLabel()))
				workingTimes.put(transition.getLabel(), new LinkedList<>());

		workingTimes.get(transition.getLabel()).add(new AbstractMap.SimpleEntry<Double, ExecutionState>(time, state));
		
	}

	@Override
	public void ressourceUsageChange(double time, ExecutionState state, AbstractTimedTransition transition, List<String> resources) {
		for(String res:resources){
			if(!resourceUsage.containsKey(res))
				resourceUsage.put(res, new LinkedList<>());
			resourceUsage.get(res).add(new AbstractMap.SimpleEntry<Double, ExecutionState>(time, state));
		}
		
	}

	@Override
	public void reachedDeadline(String netName, double time, double deadline, boolean missed) {
		if(!deadlineMisses.containsKey(netName))
			deadlineMisses.put(netName, new LinkedList<>());
		
		deadlineMisses.get(netName).add(new AbstractMap.SimpleEntry(time,missed));
	}
	
	class netWorkingTime {
		private Map<String,List<Entry<Double, ExecutionState>>> workingTimes = new HashMap<>();
	}

}
