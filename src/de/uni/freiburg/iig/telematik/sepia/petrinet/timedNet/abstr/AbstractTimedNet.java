/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.uni.freiburg.iig.telematik.sepia.petrinet.timedNet.abstr;

import java.util.List;

import de.uni.freiburg.iig.telematik.sepia.exception.PNException;
import de.uni.freiburg.iig.telematik.sepia.petrinet.NetType;
import de.uni.freiburg.iig.telematik.sepia.petrinet.pt.abstr.AbstractPTNet;
import de.uni.freiburg.iig.telematik.sepia.petrinet.timedNet.TimedMarking;
import de.uni.freiburg.iig.telematik.sepia.petrinet.timedNet.concepts.ResourceContext;
import de.uni.freiburg.iig.telematik.sepia.petrinet.timedNet.concepts.TimeRessourceContext;
import de.uni.freiburg.iig.telematik.sewol.context.process.ProcessContext;

/**
 *
 * @author richard
 */
public abstract class AbstractTimedNet<P extends AbstractTimedPlace<F>, T extends AbstractTimedTransition<F>, F extends AbstractTimedFlowRelation<P, T>, M extends AbstractTimedMarking>
        extends AbstractPTNet<P, T, F, M> {

	private static final long serialVersionUID = 7256025116225123745L;
	protected double clock = 0;
    private ResourceContext accessContext;
    private ProcessContext accessControl;
	private TimeRessourceContext<?> timeRessourceContext;
	
	String resourceContextName, timeContextName, procesContextName;
    

    public String getResourceContextName() {
		return resourceContextName;
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

	public void setTimeContextName(String timeContextName) {
		this.timeContextName = timeContextName;
	}

	public String getProcesContextName() {
		return procesContextName;
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

	/**
	 * execute the next pending action in the net
	 * @throws PNException if no pending actions exist
	 */
	private void getNextPendingAction() throws PNException {
		TimedMarking marking = (TimedMarking) getMarking();
		if (!marking.hasPendingActions())
			throw new PNException("No more pending actions left.");
		
		List<TokenConstraints<Integer>> pendingAction = marking.getNextPendingAction();
		for (TokenConstraints<Integer> c : pendingAction) {
			// execute all pending actions that are set for this time
			getPlace(c.placeName).addTokens(c.tokens);
		}
		clock += marking.getTimeOfNextPendingAction();
		marking.removeNextPendingAction();

	}

	public void setProcesContextName(String procesContextName) {
		this.procesContextName = procesContextName;
	}

	public ProcessContext getAccessControl() {
		return accessControl;
	}

	public void setAccessControl(ProcessContext accessControl) {
		this.accessControl = accessControl;
		procesContextName=accessControl.getName();
	}

    public TimeRessourceContext<?> getTimeRessourceContext() {
        return timeRessourceContext;
    }

    public void setTimeRessourceContext(TimeRessourceContext<?> timeRessourceContext) {
        this.timeRessourceContext = timeRessourceContext;
        timeContextName=timeRessourceContext.getName();
    }

    public void setResourceContext(ResourceContext accessContext) {
        this.accessContext = accessContext;
//        for (T transition : getTransitions()) {
//            transition.setAccessContext(accessContext);
//        }
        resourceContextName=accessContext.getName();
    }

    public ResourceContext getResourceContext() {
        return accessContext;
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
    	return result+"\r\n TimeContext: "+getTimeContextName()+" ResourceContext: "+getResourceContextName()+
    			" ProcessContext: "+getProcesContextName();
    }

    
    

}
