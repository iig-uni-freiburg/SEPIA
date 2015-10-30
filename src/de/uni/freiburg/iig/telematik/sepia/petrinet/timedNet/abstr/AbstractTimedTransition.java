/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.uni.freiburg.iig.telematik.sepia.petrinet.timedNet.abstr;

import de.invation.code.toval.types.Multiset;
import de.uni.freiburg.iig.telematik.sepia.exception.PNException;
import de.uni.freiburg.iig.telematik.sepia.exception.PNValidationException;
import de.uni.freiburg.iig.telematik.sepia.petrinet.abstr.AbstractPlace;
import de.uni.freiburg.iig.telematik.sepia.petrinet.abstr.AbstractTransition;
import de.uni.freiburg.iig.telematik.sepia.petrinet.pt.abstr.AbstractPTTransition;
import de.uni.freiburg.iig.telematik.sepia.petrinet.timedNet.TimedMarking;
import de.uni.freiburg.iig.telematik.sepia.petrinet.timedNet.concepts.AccessContextException;
import de.uni.freiburg.iig.telematik.sepia.petrinet.timedNet.concepts.ITimeBehaviour;
import de.uni.freiburg.iig.telematik.sewol.accesscontrol.AbstractACModel;
import de.uni.freiburg.iig.telematik.sewol.accesscontrol.properties.ACModelProperties;
import de.uni.freiburg.iig.telematik.sepia.petrinet.timedNet.concepts.ResourceContext;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;


/**
 *
 * @author richard
 */
public abstract class AbstractTimedTransition<E extends AbstractTimedFlowRelation<? extends AbstractTimedPlace<E>, ? extends AbstractTimedTransition<E>>> extends AbstractPTTransition<E> {

	private AbstractTimedNet<?, ?, ?, ?> net;

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
		
		if(!isEnabled())
			throw new PNException("Cannot fire transition "+this+": not enabled");
		try{
			checkValidity();
		} catch(PNValidationException e){
			throw new PNException("Cannot fire transition "+this+": not in valid state ["+e.getMessage()+"]");
		}
		
		TimedMarking marking = (TimedMarking) net.getMarking(); 
		List<String> resourceSet = net.getTimeRessourceContext().getRandomAllowedRessourcesFor(getLabel(), true);
		ITimeBehaviour timeBehaviour = net.getTimeRessourceContext().getTimeFor(getLabel(), resourceSet);
		double neededTime = timeBehaviour.getNeededTime();
		
		//remove tokens
		for (E p:getIncomingRelations()){
			p.getPlace().removeTokens(p.getConstraint());
		}
		
		//add Pending Actions to marking
		for (E p:getOutgoingRelations()){
			marking.addPendingAction(p.getPlace().getName(), neededTime, p.getConstraint());
		}
		
		//inform marking has changed
		notifyFiring();
	}
    
//    public void setAccessContext(ResourceContext accessContext){
//        this.accessContext=accessContext;
//    }
//    
//    public ResourceContext getAccessContext(){
//        return accessContext;
//    }
    
}
