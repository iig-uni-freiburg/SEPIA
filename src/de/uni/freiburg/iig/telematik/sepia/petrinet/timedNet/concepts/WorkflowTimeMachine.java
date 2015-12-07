package de.uni.freiburg.iig.telematik.sepia.petrinet.timedNet.concepts;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;
import java.util.TreeMap;
import java.util.concurrent.ThreadLocalRandom;

import de.uni.freiburg.iig.telematik.sepia.exception.PNException;
import de.uni.freiburg.iig.telematik.sepia.petrinet.timedNet.TimedNet;
import de.uni.freiburg.iig.telematik.sepia.petrinet.timedNet.abstr.AbstractTimedTransition;

public class WorkflowTimeMachine {
	
	protected static WorkflowTimeMachine myTimer =new WorkflowTimeMachine();
	protected double time;
	
	TreeMap<Double, List<AbstractTimedTransition>> pending = new TreeMap<>(); //Time,<NetName,<PendingActions>>
	HashMap<String,TimedNet> nets = new HashMap<>();
	ArrayList<String> netNames=new ArrayList<>();
	
	protected WorkflowTimeMachine(){
		time = 0.0;
	}
	
	public static WorkflowTimeMachine getInstance(){
		return myTimer;
	}
	
	/**resets the pending actions only. The nets keep their states (current time and marking)**/
	public void reset(){
		time=0.0;
		pending.clear();
	}
	
	/**resets the pending actions and the single nets*/
	public void resetAll(){
		reset();
		for(TimedNet net:nets.values()){
			//System.out.println("Context: "+net.getResourceContext().toString());
			net.reset();
			net.getResourceContext().reset();
		}
		
	}
	
	public List<AbstractTimedTransition> getNextPendingActions(){
		return pending.firstEntry().getValue();
	}
	
	public double getNextPendingTime(){
		return pending.firstKey();
	}
	
//	public void checkPendingActions() throws PNException{
//		boolean allFinished=false;
//		for (TimedTransition t:pending.firstEntry().getValue()){
//			if (t.isWorking()) return;
//		}
//		if(time<pending.firstKey())
//			time=pending.firstKey();
//		else 
//			throw new PNException("Next pending Action is in the past!");
//		pending.remove(pending.firstKey());
//	}
	
	public void addNet(TimedNet net){
		nets.put(net.getName(), net);
		netNames.add(net.getName());
	}
	
	public void removeNet(String netName){
		nets.remove(netName);
		netNames.remove(netName);
	}
	
	private HashMap<String, ArrayList<Double>> getResultMap(){
		HashMap<String, ArrayList<Double>> result = new HashMap<>();
		for(String s:nets.keySet()){ //initialize
			result.put(s, new ArrayList<>());
		}
		return result;
	}
	
	public HashMap<String, ArrayList<Double>> simulateAll(int steps){
		HashMap<String, ArrayList<Double>> result = getResultMap();
		
		for (int i = 0;i<steps;i++){
			simulateAll();
			
			//add results
			for(Entry<String, TimedNet> netEntry:nets.entrySet()){
				result.get(netEntry.getKey()).add(netEntry.getValue().getCurrentTime());
			}
			
			resetAll();
		}
		return result;
	}
	
	public void simulateAll() {
		while (canSimulate()) {
			try {
				simulateSingleStep();
			} catch (PNException e) {
				e.printStackTrace();
			}
		}

		if (!allNetsFinished()) {
			System.out.println("Error: a net is not finished");
			throw new RuntimeException("Not all nets finished!");
		}
	}
	
	public void simulateSingleStep() throws PNException{
		TimedNet net = drawRandomFireableNet();
		if(net!=null){
			//a net can fire
			try {
				net.fire();
			} catch (PNException e) {
				e.printStackTrace(); //it couldn't fire.
			}
		} else if(!pending.isEmpty()){
			//do next pending Action. Set time accordingly to all nets
			simulateNextPendingAction();
		} else {
			System.out.println("No more nets to simulating, no pending actions left");
			System.out.println("All nets finished? "+allNetsFinished());
		}
	}
	
	private void simulateNextPendingAction() throws PNException {
		double currentPendingTime=getNextPendingTime();
		
		List<AbstractTimedTransition>transitions = getNextPendingActions();
		updateTimeForWaitingNets(currentPendingTime); //fast-forward nets to next time.
		
		for (AbstractTimedTransition transition:transitions){
			transition.finishWork(); //inform
		}
		pending.remove(currentPendingTime);
		
	}
	
	protected void updateTimeForWaitingNets(double time) throws PNException{
		for(TimedNet net:nets.values()){
			if(!net.isFinished())	
				net.setCurrentTime(time); 
		}
	}
	
	public boolean canSimulate(){
		//check pending actions first (speedup)
		if(!pending.isEmpty()) return true;
		if(!allNetsFinished()) return true;
		return false; //pending actions empty, no net that can fire
	}
	
	public boolean allNetsFinished(){
		for(TimedNet net:nets.values()){
			if(!net.isFinished()) {
				//System.out.println("Net "+net.getName()+" not finished!");
				//System.out.println("can it still simulate? "+net.canFire());
				return false; //there is a net that can fire
			}
		}
		return true;
	}

	protected TimedNet drawRandomFireableNet(){
		ArrayList<String> fireableNets = new ArrayList<>();
		for(TimedNet net: nets.values()){
			if(!net.isFinished() && net.canFire())
				fireableNets.add(net.getName());
		}
		if(fireableNets.isEmpty()) return null;
		
		int index = ThreadLocalRandom.current().nextInt(fireableNets.size());
		return nets.get(fireableNets.get(index));
	}
	
	public void addPendingAction(double timePoint, AbstractTimedTransition t){
		if(pending.containsKey(timePoint)){
			pending.get(timePoint).add(t);
		} else {
			ArrayList<AbstractTimedTransition> actions = new ArrayList<>();
			actions.add(t);
			pending.put(timePoint, actions);
		}
	}
	
	public HashMap<String, TimedNet> getNets(){
		return nets;
	}
	
	

}
