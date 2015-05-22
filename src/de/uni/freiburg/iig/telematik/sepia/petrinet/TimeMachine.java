package de.uni.freiburg.iig.telematik.sepia.petrinet;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.invation.code.toval.validate.Validate;
import de.uni.freiburg.iig.telematik.sepia.exception.PNException;
import de.uni.freiburg.iig.telematik.sepia.exception.PNValidationException;

public class TimeMachine<P extends AbstractPlace<F,S>, 
						 T extends AbstractTransition<F,S>, 
						 F extends AbstractFlowRelation<P,T,S>,
						 M extends AbstractMarking<S>,
						 S extends Object> {

	private double time = 0;
	private PNTimeContext timeContext = null;
	private AbstractPetriNet<P,T,F,M,S> petriNet = null;
	private Map<Double, List<TokenConstraint>> pendingActions = new HashMap<Double, List<TokenConstraint>>();
	
	public TimeMachine(AbstractPetriNet<P,T,F,M,S> petriNet, PNTimeContext timeContext) {
		super();
		Validate.notNull(petriNet);
		Validate.notNull(timeContext);
		this.petriNet = petriNet;
		this.timeContext = timeContext;
	}
	
	public boolean hasPendingActions(){
		return !pendingActions.isEmpty();
	}
	
	public AbstractPetriNet<P,T,F,M,S> getPetriNet(){
		return petriNet;
	}
	
	public void reset(){
		time = 0;
		pendingActions.clear();
		petriNet.reset();
	}
	
	public double getTime() {
		return time;
	}
	
	public boolean incTime(){
		if (pendingActions.keySet().size() > 0)
			time = Collections.min(pendingActions.keySet());
		return checkPendingActions();
	}
	
	private boolean checkPendingActions(){
		List<Double> actionTimepoints = new ArrayList<Double>();
		for (double pendingActionTime : pendingActions.keySet()) {
			if(pendingActionTime <= time){
				actionTimepoints.add(pendingActionTime);
			}
		}
		if(actionTimepoints.isEmpty())
			return false;
		
		Collections.sort(actionTimepoints);
		for (Double actionTimepoint : actionTimepoints) {
			for(TokenConstraint constraint: pendingActions.get(actionTimepoint)){
				petriNet.getPlace(constraint.placeName).addTokens(constraint.tokens);
			}
			pendingActions.remove(actionTimepoint);
		}
		return true;
	}
	
	public void fire(String transitionName) throws PNException{
		Validate.notNull(transitionName);
		petriNet.validateFireTransition(transitionName);
		T transition = petriNet.getTransition(transitionName);
		
		if(!transition.isEnabled())
			throw new PNException("Cannot fire transition "+this+": not enabled");
		try{
			transition.checkValidity();
		} catch(PNValidationException e){
			throw new PNException("Cannot fire transition "+this+": not in valid state ["+e.getMessage()+"]");
		}
		
		for (F r : transition.getIncomingRelations()) {
			r.getPlace().removeTokens(r.getConstraint());
		}
		
		for (F r : transition.getOutgoingRelations()) {
			TokenConstraint newConstraint = new TokenConstraint();
			newConstraint.setPlaceName(r.getPlace().getName());
			newConstraint.setTokens(r.getConstraint());
			double delay = timeContext.getDelayTP(transitionName, r.getPlace().getName());
			putConstraint(delay, newConstraint);
		}
		transition.notifyFiring();
	}
	
	private void putConstraint(double delay, TokenConstraint constraint) {
		double actionTime = time + delay;
		if(!pendingActions.containsKey(actionTime)){
			pendingActions.put(actionTime, new ArrayList<TokenConstraint>());
		}
		pendingActions.get(actionTime).add(constraint);
	}
	
	private class TokenConstraint {
		public String placeName = null;
		public S tokens = null;
		
		public void setPlaceName(String placeName) {
			this.placeName = placeName;
		}
		public void setTokens(S tokens) {
			this.tokens = tokens;
		}
	}
	

}
