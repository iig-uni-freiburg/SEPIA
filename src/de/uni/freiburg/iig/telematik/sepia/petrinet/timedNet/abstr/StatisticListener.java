package de.uni.freiburg.iig.telematik.sepia.petrinet.timedNet.abstr;

import java.text.DecimalFormat;
import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import de.uni.freiburg.iig.telematik.sepia.petrinet.timedNet.TimedTransition;
import de.uni.freiburg.iig.telematik.sepia.petrinet.timedNet.concepts.ExecutionState;
import de.uni.freiburg.iig.telematik.sepia.petrinet.timedNet.concepts.FireElement;
import de.uni.freiburg.iig.telematik.sepia.petrinet.timedNet.concepts.FireSequence;
import de.uni.freiburg.iig.telematik.sepia.petrinet.timedNet.concepts.IStatisticListener;

public class StatisticListener implements IStatisticListener{
	
	private static StatisticListener myself=new StatisticListener();
	
	private Map<String,List<Entry<Double, ExecutionState>>> workingTimes = new HashMap<>(); //Transition,<(time, state)>
	private Map<String,List<Entry<Double, ExecutionState>>> resourceUsage = new HashMap<>();
	private Map<String,List<Entry<Double,Boolean>>> deadlineMisses = new HashMap<>();
	HashMap<String,ArrayList<FireSequence>> fireSequences= new HashMap<>(); //netName: List of FireSequences (inidividual sim runs)
	private ArrayList<FireSequence> overallLog = new ArrayList<>(); //simulationRun<FireSequence>
	
	FireElement lastProcessedElement;
	
	public HashMap<String, ArrayList<FireSequence>> getFireSequences() {
		return fireSequences;
	}

	private StatisticListener() {
		overallLog.add(new FireSequence());
	}
	
	public static StatisticListener getInstance(){
		return myself;
	}


	@Override
	public void transitionStateChange(double time, ExecutionState state, AbstractTimedTransition transition) {
		if(!workingTimes.containsKey(transition))
				workingTimes.put(transition.getLabel(), new LinkedList<>());

		workingTimes.get(transition.getLabel()).add(new AbstractMap.SimpleEntry<Double, ExecutionState>(time, state));
		

		
		switch (state) {
		case START:
			lastProcessedElement = addToFireSequence(transition, time);
			break;
		case END:
			//fireSequences.get(transition.getNet().getName()).getLast().getSequence().getLast().setEndTime(time);
			lastProcessedElement.setEndTime(time);
			break;
		case INSTANT:
			addToFireSequence(transition, time).setEndTime(time);
			//fireSequences.get(transition.getNet().getName()).getLast().getSequence().getLast().setEndTime(time);
			break;

		default:
			break;
		}
			
		
	}
	
	public Map<String,List<Entry<Double, ExecutionState>>> getWorkingTimes(){
		return workingTimes;
	}

	@Override
	public void ressourceUsageChange(double time, ExecutionState state, AbstractTimedTransition transition, List<String> resources) {
		if(resources==null||resources.isEmpty())
			System.out.println("Break");
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
	
	public void reset(){
		workingTimes.clear();
		deadlineMisses.clear();
		resourceUsage.clear();
		fireSequences.clear();
		overallLog.clear();
		overallLog.add(new FireSequence());
	}
	
//	public List<FireElement> getFireSequence(String netName){
//		LinkedList<FireElement> result = new LinkedList<>();
//		
//		for (Entry<String, List<Entry<Double, ExecutionState>>> entry:workingTimes.entrySet()){
//			if(entry.getKey().getNet().getName().equals(netName)) {
//				for(Entry<Double, ExecutionState> state:entry.getValue()){
//					if (state.getValue().equals(ExecutionState.START)){
//						result.add(new FireElement(entry.getKey(),state.getKey()));
//					}
//				}
//			}
//				
//			
//		}
//		return result;
//
//	}
	
	/**adds new FireElement to the fire Sequence of the transition's net and overall Log. The corresponding FireElement is returned**/
	private FireElement addToFireSequence(AbstractTimedTransition transition, double time){
		String netName=transition.getNet().getName();
		FireElement newFireElement = new FireElement(transition, time);
		if(fireSequences.containsKey(netName)){ //add fireElement to fireSequence
			int lastElem=fireSequences.get(netName).size()-1;
			fireSequences.get(netName).get(lastElem).add(newFireElement);
		} else {
			ArrayList<FireSequence> list = new ArrayList<>(); //create fireSequence and add FireElement
			list.add(new FireSequence());
			list.get(list.size()-1).add(newFireElement);
			fireSequences.put(netName, list);
		}
		
		overallLog.get(overallLog.size()-1).add(newFireElement);
		
		return newFireElement;
	}
	
	/**inform listener that a new simulation step begins**/
	public void simulationRestarted(){
		//new FireSequence begins: create and add to end of each list.
		for(Entry<String, ArrayList<FireSequence>> entry:fireSequences.entrySet()){
			if(!entry.getValue().isEmpty())
				//entry.getValue().add(new FireSequence(entry.getKey()));
				entry.getValue().add(new FireSequence());
		}
		
		overallLog.add(new FireSequence());
	}
	
	public ArrayList<FireSequence> getOverallLog() {
		return overallLog;
	}
	
	class netWorkingTime {
		private Map<String,List<Entry<Double, ExecutionState>>> workingTimes = new HashMap<>();
	}

}
