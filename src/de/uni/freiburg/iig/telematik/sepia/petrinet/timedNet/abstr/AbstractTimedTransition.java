/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.uni.freiburg.iig.telematik.sepia.petrinet.timedNet.abstr;

import de.uni.freiburg.iig.telematik.sepia.event.TransitionEvent;
import de.uni.freiburg.iig.telematik.sepia.exception.PNException;
import de.uni.freiburg.iig.telematik.sepia.exception.PNValidationException;
import de.uni.freiburg.iig.telematik.sepia.petrinet.pt.abstr.AbstractPTTransition;
import de.uni.freiburg.iig.telematik.sepia.petrinet.timedNet.TimedMarking;
import de.uni.freiburg.iig.telematik.sepia.petrinet.timedNet.concepts.ExecutionState;
import de.uni.freiburg.iig.telematik.sepia.petrinet.timedNet.concepts.IResourceContext;
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
	
	
	/**
	 * returns if the transition can reserve the required ressources to fire and
	 * is enabled according to the fire rules specified in the petri-net class.
	 * After a call to this function the transition must fire instantenouse as
	 * fireing of a nother net might reserve the required ressources and render
	 * this transition unfireable
	 **/
	public boolean canFire() {
		IResourceContext context = getNet().getResourceContext();
		List<String> resources = context.getRandomAvailableResourceSetFor(getLabel(), false);
		return (isEnabled() && resources != null && !resources.isEmpty() && !isWorking());

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
		if(isWorking()){
			StatisticListener.getInstance().transitionStateChange(net.getCurrentTime(), ExecutionState.BUSY, this);
			throw new PNException("Transition "+this+" is currently working");
		}
		try {
			checkValidity();
		} catch (PNValidationException e) {
			throw new PNException("Cannot fire transition " + this + ": not in valid state [" + e.getMessage() + "]");
		}

		
		if (net.getResourceContext().needsResources(getLabel())) {
			// TimedMarking marking = (TimedMarking) net.getMarking();
			 usedResources = net.getResourceContext().getRandomAvailableResourceSetFor(getLabel(), true);
			if (usedResources == null || usedResources.isEmpty()) {
				StatisticListener.getInstance().transitionStateChange(net.getCurrentTime(), ExecutionState.RESOURCE_WAIT, this);
				return;
			}
		} else {
			usedResources = null;
		}
		// net.getTimeRessourceContext().blockResources(resourceSet);

		// remove tokens
		for (E p : getIncomingRelations()) {
			p.getPlace().removeTokens(p.getConstraint());
		}

		if (net.getTimeContext().containsActivity(getLabel())&&net.getTimeContext().getTimeFor(getLabel())>0) {
			double neededTime = net.getTimeContext().getTimeFor(getLabel());
			// add Pending Actions to marking, insert used resources
			//usedResources = resourceSet;
			setWorking(true);

			WorkflowTimeMachine.getInstance().addPendingAction(net.getCurrentTime()+neededTime, this);
			StatisticListener.getInstance().transitionStateChange(net.getCurrentTime(), ExecutionState.START, this);
			StatisticListener.getInstance().transitionStateChange(net.getCurrentTime()+neededTime, ExecutionState.END, this);


		} else {
			// fire normally, no blocking as this transition needs no time...
			StatisticListener.getInstance().transitionStateChange(net.getCurrentTime(), ExecutionState.INSTANT, this);
			net.getResourceContext().unBlockResources(usedResources);
			for (E r : outgoingRelations.values()) {
				r.getPlace().addTokens(r.getConstraint());
			}
		}

		// inform marking has changed
		notifyFiring();
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + ((net == null) ? 0 : net.getName().hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!super.equals(obj))
			return false;
		if (getClass() != obj.getClass())
			return false;
		AbstractTimedTransition other = (AbstractTimedTransition) obj;
		if (net == null) {
			if (other.net != null)
				return false;
		} else if (!net.equals(other.net))
			return false;
		return true;
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
		//this.isWorking = working;
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
		//isWorking=false;
		if(usedResources!=null){
			net.getResourceContext().unBlockResources(usedResources);
			//System.out.println(net.getName()+"("+getLabel()+"): Resources unblocked -> "+usedResources.toString());
			this.usedResources.clear();
		}
	}
	
	protected void notifyFiring(){
		listenerSupport.notifyFiring(new TransitionEvent<>(this));
	}
}
