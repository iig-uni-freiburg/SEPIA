package de.uni.freiburg.iig.telematik.sepia.petrinet;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.invation.code.toval.validate.Validate;
import de.uni.freiburg.iig.telematik.sepia.exception.PNException;
import de.uni.freiburg.iig.telematik.sepia.exception.PNValidationException;
import de.uni.freiburg.iig.telematik.sepia.mg.abstr.AbstractMarkingGraphRelation;
import de.uni.freiburg.iig.telematik.sepia.mg.abstr.AbstractMarkingGraphState;

public class TimeMachine<P extends AbstractPlace<F,S>, 
						 T extends AbstractTransition<F,S>, 
						 F extends AbstractFlowRelation<P,T,S>,
						 M extends AbstractMarking<S>,
						 S extends Object,
						 X extends AbstractMarkingGraphState<M,S>, 
						 Y extends AbstractMarkingGraphRelation<M,X,S>> {

	private long time = 0L;
	private PNTimeContext timeContext = null;
	private AbstractPetriNet<P,T,F,M,S,X,Y> petriNet = null;
	private Map<Long,List<TokenConstraint>> pendingActions = new HashMap<Long,List<TokenConstraint>>();
	
	public TimeMachine(AbstractPetriNet<P, T, F, M, S, X, Y> petriNet, PNTimeContext timeContext) {
		super();
		Validate.notNull(petriNet);
		Validate.notNull(timeContext);
		this.petriNet = petriNet;
		this.timeContext = timeContext;
	}
	
	public boolean hasPendingActions(){
		return !pendingActions.isEmpty();
	}
	
	public AbstractPetriNet<P,T,F,M,S,X,Y> getPetriNet(){
		return petriNet;
	}
	
	public void reset(){
		time = 0L;
		pendingActions.clear();
		petriNet.reset();
	}
	
	public long getTime(){
		return time;
	}
	
	public boolean incTime(){
		time = Collections.min(pendingActions.keySet());
		return checkPendingActions();
	}
	
	private boolean checkPendingActions(){
		List<Long> actionTimepoints = new ArrayList<Long>();
		for(long pendingActionTime: pendingActions.keySet()){
			if(pendingActionTime <= time){
				actionTimepoints.add(pendingActionTime);
			}
		}
		if(actionTimepoints.isEmpty())
			return false;
		
		Collections.sort(actionTimepoints);
		for(long actionTimepoint: actionTimepoints){
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
			long delay = timeContext.getDelayTP(transitionName, r.getPlace().getName());
			putConstraint(delay, newConstraint);
		}
		transition.notifyFiring();
	}
	
	private void putConstraint(long delay, TokenConstraint constraint){
		long actionTime = time + delay;
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
