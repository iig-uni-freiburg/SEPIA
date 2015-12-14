/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.uni.freiburg.iig.telematik.sepia.petrinet.timedNet.abstr;

import de.uni.freiburg.iig.telematik.sepia.exception.PNException;
import de.uni.freiburg.iig.telematik.sepia.exception.PNValidationException;
import de.uni.freiburg.iig.telematik.sepia.petrinet.pt.abstr.AbstractPTTransition;
import de.uni.freiburg.iig.telematik.sepia.petrinet.timedNet.TimedMarking;
import de.uni.freiburg.iig.telematik.sepia.petrinet.timedNet.concepts.ExecutionState;
import de.uni.freiburg.iig.telematik.sepia.petrinet.timedNet.concepts.WorkflowTimeMachine;

import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

/**
 *
 * @author richard
 */
public abstract class AbstractTimedTransition<E extends AbstractTimedFlowRelation<? extends AbstractTimedPlace<E>, ? extends AbstractTimedTransition<E>>>
		extends AbstractPTTransition<E> {

	private AbstractTimedNet<?, ?, ?, ?> net;

	private boolean isWorking = false;

	private List<String> usedResources = new ArrayList<>(); //keep list of used ressources to free them after execution
	
	//private Map<Double,ExecutionState> times = new HashMap<>();
	

	public AbstractTimedTransition(String name, String label) {
		super(name, label);
	}

	@Override
	public synchronized void checkValidity() throws PNValidationException {
		super.checkValidity();
		// check availability of timeContext
	}
	
	public AbstractTimedNet<?, ?, ?, ?> getNet() {
		return net;
	}

	public AbstractTimedTransition(String name) {
		super(name);
	}

	public AbstractTimedTransition(String name, boolean isSilent) {
		super(name, isSilent);
	}

	public AbstractTimedTransition(String name, String label, boolean isSilent) {
		super(name, label, isSilent);
	}

	public void setNet(AbstractTimedNet<?, ?, ?, ?> net) {
		this.net = net;
	}
	
	public void fire(List<String> resources){
		//TODO
	}

	@Override
	public void fire() throws PNException {

		if (!isEnabled())
			throw new PNException("Cannot fire transition " + this + ": not enabled");
		if(isWorking())
			StatisticListener.getInstance().transitionStateChange(net.getCurrentTime(), ExecutionState.BUSY, this);
		try {
			checkValidity();
		} catch (PNValidationException e) {
			throw new PNException("Cannot fire transition " + this + ": not in valid state [" + e.getMessage() + "]");
		}

		TimedMarking marking = (TimedMarking) net.getMarking();
		List<String> resourceSet = net.getResourceContext().getRandomAllowedResourcesFor(getLabel(),true);
		if(resourceSet==null||resourceSet.isEmpty()){
			System.out.println(getName()+": Waiting for resources!");
			StatisticListener.getInstance().transitionStateChange(net.getCurrentTime(), ExecutionState.RESOURCE_WAIT, this);
			return; //cannot fire: not available resources
		}
		//net.getTimeRessourceContext().blockResources(resourceSet);
		

		// remove tokens
		for (E p : getIncomingRelations()) {
			p.getPlace().removeTokens(p.getConstraint());
		}

		if (net.getTimeContext().containsActivity(getLabel())) {
			double neededTime = net.getTimeContext().getTimeFor(getLabel());
			// add Pending Actions to marking, insert used resources
			usedResources = resourceSet;
			setWorking(true);
			WorkflowTimeMachine.getInstance().addPendingAction(net.getCurrentTime()+neededTime, this);
			StatisticListener.getInstance().transitionStateChange(net.getCurrentTime(), ExecutionState.START, this);
			StatisticListener.getInstance().transitionStateChange(net.getCurrentTime()+neededTime, ExecutionState.END, this);


		} else {
			// fire normally, no blocking...
			net.getResourceContext().unBlockResources(resourceSet);
			for (E r : outgoingRelations.values()) {
				r.getPlace().addTokens(r.getConstraint());
			}
		}

		// inform marking has changed
		notifyFiring();
	}

	public boolean isWorking() {
		return isWorking;
	}
	
	/**puts tokens in outgoing places, frees up resources**/
	public void finishWork() throws PNException{
		//create tokens in the net
		for(E r:outgoingRelations.values()){
			r.getPlace().addTokens(r.getConstraint());
		}
		
		//stop working
		setWorking(false);
		clearResourceUsage();
		
	}

	public void setWorking(boolean working) {
		this.isWorking = working;
		if(working){
			//StatisticListener.getInstance().transitionStateChange(net.getCurrentTime(), ExecutionState.START, this);
			StatisticListener.getInstance().ressourceUsageChange(net.getCurrentTime(), ExecutionState.START, this, usedResources);
		} else {
			//StatisticListener.getInstance().transitionStateChange(net.getCurrentTime(), ExecutionState.END, this);
			StatisticListener.getInstance().ressourceUsageChange(net.getCurrentTime(), ExecutionState.END, this, usedResources);
		}
	}

	public List<String> getUsedResources() {
		return usedResources;
	}

	public void clearResourceUsage() {
		if(usedResources!=null){
			net.getResourceContext().unBlockResources(usedResources);
			this.usedResources.clear();
		}
	}
}
