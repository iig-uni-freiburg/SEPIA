/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.uni.freiburg.iig.telematik.sepia.petrinet.timedNet.abstr;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

import javax.xml.bind.annotation.XmlAccessorOrder;

import de.invation.code.toval.misc.soabase.SOABase;
import de.uni.freiburg.iig.telematik.sepia.exception.PNException;
import de.uni.freiburg.iig.telematik.sepia.petrinet.NetType;
import de.uni.freiburg.iig.telematik.sepia.petrinet.pt.abstr.AbstractPTNet;
import de.uni.freiburg.iig.telematik.sepia.petrinet.timedNet.concepts.IResourceContext;
import de.uni.freiburg.iig.telematik.sepia.petrinet.timedNet.concepts.ITimeContext;
import de.uni.freiburg.iig.telematik.sepia.petrinet.timedNet.concepts.IStatisticListener;

/**
 *
 * @author richard
 */
public abstract class AbstractTimedNet<P extends AbstractTimedPlace<F>, T extends AbstractTimedTransition<F>, F extends AbstractTimedFlowRelation<P, T>, M extends AbstractTimedMarking>
        extends AbstractPTNet<P, T, F, M> {

	private static final long serialVersionUID = 7256025116225123745L;
	protected double clock = 0;
	private double deadline; 
	private ITimeContext timeContext;
	private IResourceContext resourceContext;
	private SOABase accessContext;
	
	private boolean recurring;
	
	private T nextTransition;


	String resourceContextName, timeContextName, accesContextName;
    

    public String getResourceContextName() {
		return resourceContextName;
	}
    
	public double getDeadline() {
		return deadline;
	}

	public void setDeadline(double deadline) {
		this.deadline = deadline;
	}

	@Override
	public NetType getNetType() {
		return NetType.RTPTnet;
	}

	public void setResourceContextName(String resourceContextName) {
		this.resourceContextName = resourceContextName;
	}

	public String getTimeContextName() {
		return timeContextName;
	}
	
	public ITimeContext getTimeContext(){
		return timeContext;
	}

	public void setTimeContextName(String timeContextName) {
		this.timeContextName = timeContextName;
	}

	public String getAccessContextName() {
		return accesContextName;
	}

	@Override
	public T fire(String transitionName) throws PNException {
		boolean cannotFire = true;
		while (cannotFire) {
			try {
				validateFireTransition(transitionName);
				cannotFire = false;
			} catch (PNException e) {
				cannotFire = true;
				getNextPendingAction(); //if no more pending actions left, this will throw an exception
			}
		}
		T transition = getTransition(transitionName);
		transition.fire();
		lastFiredTransition = transition;
		return transition;
	}
	
	/**fires a random enabled transition. If no transition is enabled it will check pending operations until a transition is enabled
	 * @throws PNException if even after the last pending action no transition is enabled**/
	public T fire() throws PNException {
		
		//only non-working transitions may fire
		List<T> nonWorking = getEnabledAndNonWorkingTransitions();
		
		// get random transition
		int max = nonWorking.size();
		
		if(max==1){ //no need for random number
			T transition = nonWorking.get(0);
				transition.fire();
			return transition;
		}
		
		if (max > 0) {
			//get random next transition
			T transition = nonWorking.get(ThreadLocalRandom.current().nextInt(0, max));
			transition.fire();
			return transition;
		} 
		/**else { //TODO: change. do nothing if their is no pending action
			// no active transition. Check pending Actions
			while (getMarking().hasPendingActions()) {
				getNextPendingAction();
				if (getEnabledTransitions().size() > 0) {
					max = getEnabledTransitions().size();
					T transition = getEnabledTransitions().get(ThreadLocalRandom.current().nextInt(0, max));
					//T transition = getEnabledTransitions().get(r.nextInt(max));
					transition.fire();
					return transition;
				}
			}
		}**/
		throw new PNException("Cannot fire any transition.");
	}
	
	private List<T> getEnabledAndNonWorkingTransitions() {
		List<T> enabled = getEnabledTransitions();
		List<T> notWorking = new ArrayList<>(enabled.size());
		for (T transition: enabled){
			if(!transition.isWorking())
				notWorking.add(transition);
		}
		return Collections.unmodifiableList(notWorking);
	}

	public boolean canFire(){
		int max = getEnabledTransitions().size();
		if (max==0) return false;
		if(isFinished()) return false;
		for(T t:getEnabledTransitions()){
			if (t.canFire()) return true;
		}
		return false;
	}
	
	/**simulates the net until it is finisehd and return the needed time units**/
	public double simulate() throws PNException{
		while (!isFinished() && moreToSimulate()){
			fire();
		}
		
		return clock;
	}
	
	@Override
	public M getMarking() {
		return marking;
	}

	/**return true if there is a token in one of the draining places**/
	public boolean isFinished(){
		for (P place: getDrainPlaces()){
			if(place.getState()>=1) {
				return true;
			}
		}
		return false;
	}
	
	/**return true if the net contains enabled transitions or has pending actions**/
	public boolean moreToSimulate(){
		return !getEnabledTransitions().isEmpty() || getMarking().hasPendingActions();
	}

	/**
	 * execute the next pending action in the net
	 * @throws PNException if no pending actions exist
	 */
	protected void getNextPendingAction() throws PNException {
		AbstractTimedMarking marking = getMarking();
		if (!marking.hasPendingActions())
			throw new PNException("No more pending actions left.");
		
		List<String> pendingAction = marking.getNextPendingAction();
		for (String s:pendingAction) {
			T transition = getTransition(s);
			for (F rel: transition.getOutgoingRelations()){
				rel.getPlace().addTokens(rel.getConstraint());
			}
			transition.setWorking(false);
			//getTimeRessourceContext().removeResourceUsage(transition.getLabel(), transition.getUsedResources());
			getResourceContext().unBlockResources(transition.getUsedResources());
			transition.clearResourceUsage();
		}
		clock = marking.getTimeOfNextPendingAction();
		marking.removeNextPendingAction();

	}
	
	public void setTimeContext(ITimeContext context){
		this.timeContext=context;
		timeContextName=context.getName();
	}

	public void setProcesContextName(String procesContextName) {
		this.accesContextName = procesContextName;
	}

	public SOABase getAccessControl() {
		return accessContext;
	}

	public void setAccessControl(SOABase accessControl) {
		this.accessContext = accessControl;
		accesContextName=accessControl.getName();
	}

//    public TimeRessourceContext<?> getTimeRessourceContext() {
//        return timeRessourceContext;
//    }
//
//    public void setTimeRessourceContext(TimeRessourceContext<?> timeRessourceContext) {
//        this.timeRessourceContext = timeRessourceContext;
//        timeContextName=timeRessourceContext.getName();
//    }

    public void setResourceContext(IResourceContext resourceContext) {
        this.resourceContext = resourceContext;
//        for (T transition : getTransitions()) {
//            transition.setAccessContext(accessContext);
//        }
        resourceContextName=resourceContext.getName();
    }

    public IResourceContext getResourceContext() {
        return resourceContext;
    }

    @Override
    public boolean addTransition(String transitionName) {
        boolean result = super.addTransition(transitionName); 
        //getTransition(transitionName).setAccessContext(accessContext);
        getTransition(transitionName).setNet(this);
        return result;
    }
    
    public String toString(){
    	String result = super.toString();
    	String time = " current time: "+getCurrentTime();
    	String finished = " is finished: "+isFinished();
    	return result+"\r\n TimeContext: "+getTimeContextName()+" ResourceContext: "+getResourceContextName()+
    			" ProcessContext: "+getAccessContextName()+time+finished;
    }
    
	@Override
	public boolean addTransition(String transitionName, boolean notifyListeners) {
		boolean result = super.addTransition(transitionName,notifyListeners);
		getTransition(transitionName).setNet(this);
		return result;
	}

	@Override
	public boolean addTransition(String transitionName, String transitionLabel) {
		boolean result = super.addTransition(transitionName, transitionLabel);
		getTransition(transitionName).setNet(this);
		return result;
	}

	@Override
	public boolean addTransition(String transitionName, String transitionLabel, boolean notifyListeners) {
		boolean result = super.addTransition(transitionName, transitionLabel, notifyListeners);
		getTransition(transitionName).setNet(this);
		return result;
	}

	@Override
	protected boolean addTransition(T transition, boolean notifyListeners) {
		boolean result = super.addTransition(transition, notifyListeners);
		getTransition(transition.getName()).setNet(this);
		return result;
	}

	@Override
	public void reset() {
		super.reset();
		clock = 0.0;
		for(T t:getTransitions()){
			t.clearResourceUsage();
		}
	}
	
	public double getCurrentTime() {
		return clock;
	}
	
	public void setCurrentTime(double time) throws PNException {
		if(time<clock)
			throw new PNException(getName()+": Cannot go back in time! Current time: "+clock+", requested: "+time);
		if (isFinished())
			throw new PNException("This nets execution has finished!");
		clock=time;
	}
	
	public boolean isRecurring(){
		return recurring;
	}
	
	public void setRecurring(boolean recurring){
		this.recurring=recurring;
	}
	
	public AbstractTimedNet<P, T , F , M> clone(){
		AbstractTimedNet<P, T , F , M> clone = (AbstractTimedNet<P, T, F, M>) super.clone();
		clone.setTimeContext(getTimeContext());
		clone.setResourceContext(getResourceContext());
		clone.setProcesContextName(getAccessContextName());
		clone.setDeadline(getDeadline());
		clone.setRecurring(isRecurring());
		clone.clock=clock;
		for (T t: clone.getTransitions()){
			t.setNet(clone);
		}
		
		return clone;
	}

    
    

}
