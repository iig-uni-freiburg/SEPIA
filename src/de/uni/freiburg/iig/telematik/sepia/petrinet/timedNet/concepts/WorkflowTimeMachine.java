package de.uni.freiburg.iig.telematik.sepia.petrinet.timedNet.concepts;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map.Entry;
import java.util.TreeMap;
import java.util.concurrent.ThreadLocalRandom;

import de.uni.freiburg.iig.telematik.sepia.exception.PNException;
import de.uni.freiburg.iig.telematik.sepia.petrinet.timedNet.TimedFlowRelation;
import de.uni.freiburg.iig.telematik.sepia.petrinet.timedNet.TimedMarking;
import de.uni.freiburg.iig.telematik.sepia.petrinet.timedNet.TimedNet;
import de.uni.freiburg.iig.telematik.sepia.petrinet.timedNet.TimedNetPlace;
import de.uni.freiburg.iig.telematik.sepia.petrinet.timedNet.TimedTransition;
import de.uni.freiburg.iig.telematik.sepia.petrinet.timedNet.abstr.AbstractTimedNet;
import de.uni.freiburg.iig.telematik.sepia.petrinet.timedNet.abstr.AbstractTimedTransition;
import de.uni.freiburg.iig.telematik.sepia.petrinet.timedNet.abstr.StatisticListener;

public class WorkflowTimeMachine {
	
	protected static WorkflowTimeMachine myTimer = new WorkflowTimeMachine();
	protected double time;
	
	
	TreeMap<Double, List<AbstractTimedTransition>> pending = new TreeMap<>(); //Time,<NetName,<PendingActions>>
	HashMap<String,TimedNet> nets = new HashMap<>();
	HashMap<String, ArrayList<Double>> result;
	
	private instanceManager instances = new instanceManager();
	private int pendingActionCount;

	
	private WorkflowTimeMachine(){
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
		System.out.println("Resetting all: ");
		reset();
		
		//remove clones
		for (String clonedNet:instances.getClones()){
			removeNet(clonedNet);
		}
		
		//rest nets and resource Contexts
		for(TimedNet net:nets.values()){
			//System.out.println("Context: "+net.getResourceContext().toString());
			net.reset();
			net.getResourceContext().reset();
		}
		
		instances.reset();
		
		System.out.println("Workflow time: "+time);
		for (TimedNet n: nets.values()){
			System.out.println("Net: "+n.getName()+": "+n.getCurrentTime());
		}
		
	}
	
	public void removeNetFromSimulation(String netName){
		nets.remove(netName);
		reset();
		
	}
	
	public void clearAllNets(){
		nets.clear();
		reset();
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
	}
	
	public void addAllNets(List<TimedNet> nets){
		for(TimedNet net:nets){
			addNet(net);
		}
	}
	
	public void removeNet(String netName){
		nets.remove(netName);
	}
	
	private HashMap<String, ArrayList<Double>> createResultMap(){
		HashMap<String, ArrayList<Double>> result = new HashMap<>();
		for(String s:nets.keySet()){ //initialize
			result.put(s, new ArrayList<>());
		}
		return result;
	}
	
	public HashMap<String, ArrayList<Double>> simulateAll(int steps) throws PNException {
		HashMap<String, ArrayList<Double>> result = createResultMap();
		StatisticListener.getInstance().reset();
		
		for (int i = 0;i<steps;i++){
			if(i%5000==0)System.out.println((i*100/steps)+"%");
			simulateAll();
			
			//add results
			for(Entry<String, TimedNet> netEntry:nets.entrySet()){
				if(netEntry.getValue().isFinished() && !instances.isClonedNet(netEntry.getKey()))
					result.get(netEntry.getKey()).add(netEntry.getValue().getCurrentTime());
			}
			
			resetAll();
		}
		this.result=result;
		return result;
	}
	
	public boolean hasResult(){
		return (result!=null && !result.isEmpty());
	}
	
	public HashMap<String, ArrayList<Double>> getResult(){
		return result;
	}
	
	protected void simulateAll() throws PNException {
		while (canSimulate()) {
				simulateSingleStep();
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
			throw new PNException("No more nets to simulate. Nets not finished. Nets bounded and deadlock free?");
		}
		updateRecurringNets();
	}
	
	/** clone any net that has finished and is recurring */
	private void updateRecurringNets() {
		// clone recuring nets. Achtung, so würde jedes mal ein neues Netz erstellt werden. Netze nur einmal clonen
		LinkedList<TimedNet> clones = new LinkedList<>();
		for (TimedNet net : nets.values()) {
			TimedNet tempNet = null;
			if (net.isFinished() && net.isRecurring() && instances.newInstanceRequired(net)){
				tempNet = instances.createNewInstance(net);
				clones.add(tempNet);
			}
		}
		addAllNets(clones); //is initial marking set?
	}
	
	public void countWorkingNets(){
		int i = 0;
		for(TimedNet n:nets.values()){
			if(!n.isFinished()) i++;
		}
		System.out.println(i+" unfinished nets");
	}

	private void simulateNextPendingAction() throws PNException {
		double currentPendingTime=getNextPendingTime();
		
		if(currentPendingTime<time)
			throw new PNException("Cannot go back in time. Current time is "+time+" next pending action dictaties "+currentPendingTime);
		
		time = currentPendingTime;
		
		List<AbstractTimedTransition>transitions = getNextPendingActions();
		
		
		updateTimeForWaitingNets(currentPendingTime); //fast-forward nets to next time.
		
		for (AbstractTimedTransition transition:transitions){
			transition.finishWork(); //inform
			if(!instances.isClonedNet(transition.getNet().getName())) pendingActionCount--;
		}
		pending.remove(currentPendingTime);
		//System.out.println(pending.toString());
		
	}
	
	protected void updateTimeForWaitingNets(double time) throws PNException{
		for(TimedNet net:nets.values()){
			if(!net.isFinished()){	
				if(net.getCurrentTime()>time){
					System.out.println("Breakpoint. Something wrong. Current overall time: "+time);
					for (TimedNet n:nets.values()){
						System.out.println(n.getName()+": "+n.getCurrentTime());
					}
				}
				net.setCurrentTime(time);
			}
		}
	}
	
	public boolean canSimulate(){
		//check pending actions first (speedup)
		//if(!pending.isEmpty()) return true; //must ignore clonedNets
		if(hasPendingActions()) return true;
		if(!allNetsFinished()) return true;
		return false; //pending actions empty, no net that can fire
	}
	
	public boolean hasPendingActions(){
		return pendingActionCount>0;
	}
	
	public boolean allNetsFinished(){
		for(TimedNet net:nets.values()){
			if(!net.isFinished() && !instances.isClonedNet(net)) {
				System.out.println("Not finished: "+net.getName());
				countWorkingNets();
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
	
	public void addPendingAction(double timePoint, AbstractTimedTransition t) {
		System.out.println(t.getNet().getName()+": Adding "+t.getName()+" at "+timePoint+". Net time: "+t.getNet().getCurrentTime()+", Workflow time: "+time);
		
		if(timePoint<time)
			System.out.println("Pending action is in the past. Current time: "+time+" queued action finish time: "+timePoint);
		
		if(pending.containsKey(timePoint)){
			pending.get(timePoint).add(t);
		} else {
			ArrayList<AbstractTimedTransition> actions = new ArrayList<>();
			actions.add(t);
			pending.put(timePoint, actions);
		}
		
		if(!instances.isClonedNet(t.getNet().getName())){
			pendingActionCount++;
		}
		
			//System.out.println(pending.toString());
	}
	
	public HashMap<String, TimedNet> getNets(){
		return nets;
	}
	
	class instanceManager {
		protected HashMap<String,LinkedList<String>> instances = new HashMap<>(); //original name, cloned instances;
		protected HashMap<String, Double> offsets = new HashMap<>(); //instance name, time offset
		protected HashSet<String> activeInstances = new HashSet<>(); //contains clone names
		protected long instanceCount = 0;
		protected HashMap<String, String> reverseInstances = new HashMap<>();
		

		public TimedNet createNewInstance(TimedNet originatingNet){
			System.out.println("Cloning "+originatingNet.getName());
			TimedNet tempNet = (TimedNet) originatingNet.clone();
			try {
				tempNet.setCurrentTime(time);
			} catch (PNException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			tempNet.setName(originatingNet.getName()+instanceCount++);
			tempNet.setMarking(tempNet.getInitialMarking());
			// store instanace name and offset
			insertInstance(originatingNet.getName(), tempNet.getName());
			offsets.put(tempNet.getName(), tempNet.getCurrentTime());
			activeInstances.add(tempNet.getName());
			return tempNet;
		}
		
		private void insertInstance(String originatingNet, String instanceName){
			if(!instances.containsKey(originatingNet))
				instances.put(originatingNet, new LinkedList<>());
			instances.get(originatingNet).add(instanceName);
			reverseInstances.put(instanceName, originatingNet);
		}
		
		public boolean isClonedNet(TimedNet net) {
			return isClonedNet(net.getName());
		}

		public boolean hasClones(String netName){
			return instances.containsKey(netName);
		}
		
		public boolean hasClones(TimedNet net){
			return hasClones(net.getName());
		}
		
		public boolean isClonedNet(String netName){
			return activeInstances.contains(netName);
		}
		
		public boolean newInstanceRequired(TimedNet net){
			if(isClonedNet(net)) return false;
			if(!net.isFinished()) return false;
			if(!instances.containsKey(net.getName())) return true; //has never been cloned before
			
			//make new Instance, if it is the original net and all current instances are finished
			for(String netString:instances.get(net.getName())){
				if(!nets.get(netString).isFinished()) return false;
			}
			return true;
		}
		
		public HashSet<String> getClones(){
			return activeInstances;
		}
		
		public void reset(){
			instances.clear();
			offsets.clear();
			activeInstances.clear();
			instanceCount=0;
			reverseInstances.clear();
		}
		
		public String getOriginalNet(String clonedNet){
			if (!activeInstances.contains(clonedNet)) return clonedNet;
			return reverseInstances.get(clonedNet);
			
		}
		

	}
	
	

}
