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

import java.util.List;

/**
 *
 * @author richard
 */
public abstract class AbstractTimedTransition<E extends AbstractTimedFlowRelation<? extends AbstractTimedPlace<E>, ? extends AbstractTimedTransition<E>>>
		extends AbstractPTTransition<E> {

	private AbstractTimedNet<?, ?, ?, ?> net;

	private boolean isWorking = false;

	private List<String> usedResources;

	public AbstractTimedTransition(String name, String label) {
		super(name, label);
	}

	@Override
	public synchronized void checkValidity() throws PNValidationException {

		// try {
		// List<String> subjects =
		// net.getResourceContext().getSubjectsFor(getLabel());
		// if(subjects==null||subjects.isEmpty())
		// throw new PNValidationException("No available subject for
		// "+getLabel()+" found");
		// } catch (AccessContextException ex) {
		// throw new PNValidationException("No available subject for
		// "+getLabel()+" found",ex);
		// } catch (NullPointerException e) {
		// throw new PNValidationException("Transition has no correspopnding
		// net",e);
		// }

		super.checkValidity();
		// check availability of timeContext
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
		try {
			checkValidity();
		} catch (PNValidationException e) {
			throw new PNException("Cannot fire transition " + this + ": not in valid state [" + e.getMessage() + "]");
		}

		TimedMarking marking = (TimedMarking) net.getMarking();
		List<String> resourceSet = net.getResourceContext().getRandomAllowedResourcesFor(getLabel(),true);
		//net.getTimeRessourceContext().blockResources(resourceSet);
		double neededTime = net.getTimeContext().getTimeFor(getLabel());

		// remove tokens
		for (E p : getIncomingRelations()) {
			p.getPlace().removeTokens(p.getConstraint());
		}

		if (neededTime > 0) {
			// add Pending Actions to marking, insert used resources
			usedResources = resourceSet;
			setWorking(true);
			marking.addPendingAction(getLabel(), neededTime);

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
		return isWorking;
	}

	public void setWorking(boolean working) {
		this.isWorking = working;
	}

	public List<String> getUsedResources() {
		return usedResources;
	}

	public void removeResourceUsage() {
		this.usedResources.clear();
	}
}
