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
import de.uni.freiburg.iig.telematik.sepia.petrinet.timedNet.concepts.AccessContextException;
import de.uni.freiburg.iig.telematik.sepia.petrinet.timedNet.concepts.ITimeBehaviour;

import java.util.List;


/**
 *
 * @author richard
 */
public abstract class AbstractTimedTransition<E extends AbstractTimedFlowRelation<? extends AbstractTimedPlace<E>, ? extends AbstractTimedTransition<E>>> extends AbstractPTTransition<E> {

	private AbstractTimedNet<?, ?, ?, ?> net;
	
	private boolean working = false;
	
	private List<String>blockedResources;

    public AbstractTimedTransition(String name, String label) {
        super(name, label);
    }

    @Override
    public synchronized void checkValidity() throws PNValidationException {
    	
        try {
            super.checkValidity();
            List<String> subjects = net.getResourceContext().getSubjectsFor(getLabel());
            if(subjects==null||subjects.isEmpty())
            	throw new PNValidationException("No available subject for "+getLabel()+" found");
        } catch (AccessContextException ex) {
            throw new PNValidationException("No available subject for "+getLabel()+" found",ex);
        } catch (NullPointerException e) {
        	throw new PNValidationException("Transition has no correspopnding net",e);
        }
        //check availability of timeContext
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
    
    public void setNet(AbstractTimedNet<?, ?, ?, ?> net){
    	this.net=net;
    }

	@Override
	public void fire() throws PNException {

		if (!isEnabled())
			throw new PNException("Cannot fire transition " + this + ": not enabled");
		try {
			checkValidity();
		} catch (PNValidationException e) {
			throw new PNException("Cannot fire transition " + this + ": not in valid state [" + e.getMessage() + "]");
		}

		TimedMarking marking = (TimedMarking) net.getMarking();
		List<String> resourceSet = net.getTimeRessourceContext().getRandomAllowedResourcesFor(getLabel());
		net.getTimeRessourceContext().blockResources(resourceSet);
		ITimeBehaviour timeBehaviour = net.getTimeRessourceContext().getTimeFor(getLabel(), resourceSet);
		double neededTime = timeBehaviour.getNeededTime();

		// remove tokens
		for (E p : getIncomingRelations()) {
			p.getPlace().removeTokens(p.getConstraint());
		}

		if (neededTime > 0) {
			// add Pending Actions to marking, block used resources
			blockedResources=resourceSet;
			working=true;
			marking.addPendingAction(getLabel(), neededTime);
//			for (E p : getOutgoingRelations()) {
//				marking.addPendingAction(getName(), neededTime, p.getConstraint());
//			}

		} else {
			// fire normally, no blocking...
			for (E r : outgoingRelations.values()) {
				r.getPlace().addTokens(r.getConstraint());
			}
		}

		// inform marking has changed
		notifyFiring();
	}

	public boolean isWorking() {
		return working;
	}

	public void setWorking(boolean working) {
		this.working = working;
	}

	public List<String> getBlockedResources() {
		return blockedResources;
	}

	public void unsetBlockedResources() {
		this.blockedResources = null;
	}
}
