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
import sun.security.action.GetLongAction;

import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import com.sun.javafx.geom.transform.GeneralTransform3D;

/**
 *
 * @author richard
 */
public abstract class AbstractTimedTransition<E extends AbstractTimedFlowRelation<? extends AbstractTimedPlace<E>, ? extends AbstractTimedTransition<E>>>
		extends AbstractPTTransition<E> {

	private AbstractTimedNet<?, ?, ?, ?> net;

	private boolean isWorking = false;
	
	private boolean isWaiting = false;

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
		//if(isWaiting)
		//	return true;
		//return isEnabled()&&!isWorking; // <-- results in deadlock
		IResourceContext context = getNet().getResourceContext();
		List<String> resources = context.getRandomAvailableResourceSetFor(getLabel(), false);
		return (isEnabled() && resources != null && !resources.isEmpty() && !isWorking());

	}

	@Override
	public synchronized void checkValidity() throws PNValidationException {
		//if(isWaiting) return;
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
		fireWithResult();
	}
	
	/**if the AbstractTimesTransition could not fire because of ressource shortage, 
	 * this method will return itself. If the transition could fire it will return null **/
	public AbstractTimedTransition fireWithResult() throws PNException {
		System.out.println("Trying to fire "+getLabel()+" ("+getName()+") from net "+getNet().getName());
		if(isWaiting)
			System.out.println("is waiting");

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
			if (usedResources == null || usedResources.isEmpty()) { //cannot fire because of resource shortage
				if(!isWaiting)
					StatisticListener.getInstance().transitionStateChange(net.getCurrentTime(), ExecutionState.RESOURCE_WAIT, this);
				//System.out.println(getLabel()+" ("+getNet().getName()+"): waiting for resource!");
				removeTokens();
				isWaiting=true;
				getNet().addWaitingTransition(this);
				return this;
			}
		} else {
			System.out.println("Does not need ressources: "+getLabel()+"( "+getNet().getName()+")");
			usedResources = null;
		}
		// net.getTimeRessourceContext().blockResources(resourceSet);

		removeTokens();


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
			putTokens();
		}

		// inform marking has changed
		notifyFiring();
		isWaiting=false;
		System.out.println("... successfully!");
		return null;
	}
	
	private void removeTokens(){
		if (isWaiting){
			System.out.println(this+" not removing anything... isWaiting");
			return; //there is nothing to remove if this transition is waiting
		}
					
		for (E p : getIncomingRelations()) {
			System.out.println("Removing tokens from "+p.getPlace());
			p.getPlace().removeTokens(p.getConstraint());
		}
	}
	
	private void putTokens(){
		System.out.println(getName()+"("+getLabel()+") putting tokens in... ");
		for (E r : outgoingRelations.values()) {
			r.getPlace().addTokens(r.getConstraint());
			System.out.println(r.getPlace().getLabel()+", ");
		}
		System.out.println("");
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
		putTokens();
		
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


	/**if the transition is in a waiting state, resume its work here
	 * @throws PNException **/
	public boolean resume() throws PNException {
		
		if(!isWaiting) 
			throw new PNException("This transition cannot resume. It does not wait for resources to become available!");
		
		if (net.getResourceContext().needsResources(getLabel())) {
			 usedResources = net.getResourceContext().getRandomAvailableResourceSetFor(getLabel(), true);
			if (usedResources == null || usedResources.isEmpty()) { //cannot fire because of resource shortage
				System.out.println(getLabel()+" ("+getNet().getName()+"): STILL waiting for resource! (time: "+getNet().getCurrentTime()+")");
				return false; //could not fire
			}
		} else { 
			System.out.println("Does not need ressources: "+getLabel()+"( "+getNet().getName()+")");
			usedResources = null;
		}


		if (net.getTimeContext().containsActivity(getLabel())&&net.getTimeContext().getTimeFor(getLabel())>0) {
			double neededTime = net.getTimeContext().getTimeFor(getLabel());
			// add Pending Actions to marking, insert used resources
			//usedResources = resourceSet;
			setWorking(true);
			isWaiting=false;
			net.removeFromWaitingTransitions(this);

			WorkflowTimeMachine.getInstance().addPendingAction(net.getCurrentTime()+neededTime, this);
			StatisticListener.getInstance().transitionStateChange(net.getCurrentTime(), ExecutionState.RESUME, this);
			StatisticListener.getInstance().transitionStateChange(net.getCurrentTime()+neededTime, ExecutionState.END, this);


		} else {
			// fire normally, no blocking as this transition needs no time...
			StatisticListener.getInstance().transitionStateChange(net.getCurrentTime(), ExecutionState.INSTANT, this);
			net.getResourceContext().unBlockResources(usedResources);
			isWaiting=false;
			net.removeFromWaitingTransitions(this);
			putTokens();
		}
		return true;
		
	}
	
//	public boolean isEnabled() {
//		return (isWaiting||enabled);
//	}
	
	public boolean isWaiting() {
		return isWaiting;
	}
}
