package de.uni.freiburg.iig.telematik.sepia.petrinet.cpn.abstr;

import java.util.HashSet;
import java.util.Set;

import de.invation.code.toval.types.Multiset;
import de.invation.code.toval.validate.ParameterException;
import de.uni.freiburg.iig.telematik.sepia.petrinet.AbstractTransition;


public abstract class AbstractCPNTransition<E extends AbstractCPNFlowRelation<? extends AbstractCPNPlace<E>, ? extends AbstractCPNTransition<E>>> extends AbstractTransition<E, Multiset<String>> {

	protected AbstractCPNTransition(){
		super();
	}
	
	public AbstractCPNTransition(String name, boolean isEmpty) {
		super(name, isEmpty);
	}

	public AbstractCPNTransition(String name, String label, boolean isEmpty) {
		super(name, label, isEmpty);
	}

	public AbstractCPNTransition(String name, String label) {
		super(name, label);
	}

	public AbstractCPNTransition(String name) {
		super(name);
	}
	
	public boolean processesColor(String color){
		return getProcessedColors().contains(color);
	}
	
	public Set<String> getProcessedColors(){
		Set<String> processedColors = getConsumedColors();
		processedColors.addAll(getProducedColors());
		return processedColors;
	}
	
	public int getConsumedTokens(String color){
		int consumedTokens = 0;
		if(color == null)
			return consumedTokens;
		for(E inputRelation : getIncomingRelations()){
			try {
				consumedTokens += inputRelation.getConstraint(color);
			} catch (ParameterException e) {
				e.printStackTrace();
			}
		}
		return consumedTokens;
	}
	
	public Set<String> getConsumedColors(){
		Set<String> colors = new HashSet<String>();
		for(E inputRelation : getIncomingRelations()){
			colors.addAll(inputRelation.getConstraint().support());
		}
		return colors;
	}
	
	public boolean consumesColor(String color) {
		for(E inputRelation : getIncomingRelations()){
			if(inputRelation.getConstraint().support().contains(color))
				return true;
		}
		return false;
	}
	
	public int getProducedTokens(String color){
		int producedTokens = 0;
		if(color == null)
			return producedTokens;
		for(E outputRelation : getOutgoingRelations()){
			try {
				producedTokens += outputRelation.getConstraint(color);
			} catch (ParameterException e) {
				e.printStackTrace();
			}
		}
		return producedTokens;
	}
	
	public Set<String> getProducedColors(){
		Set<String> colors = new HashSet<String>();
		for(E outputRelation : getOutgoingRelations()){
			colors.addAll(outputRelation.getConstraint().support());
		}
		return colors;
	}
	
	public boolean producesColor(String color){
		for(E outputRelation : getOutgoingRelations()){
			if(outputRelation.getConstraint().support().contains(color))
				return true;
		}
		return false;
	}

//	@Override
//	public void checkState() {
//		boolean oldEnabledState = enabled;
//		enabled = true;
//		//Check if there are enough tokens in input places
//		for(E r: incomingRelations) {
//			boolean disabled = false;
//			Multiset<String> tokenConstraints = r.getConstraint();
//			for(String color: tokenConstraints.support()){
//				try {
//					if(r.getPlace().getTokens(color) < tokenConstraints.multiplicity(color)){
//						enabled = false;
//						disabled = true;
//						break;
//					}
//				} catch (ParameterException e) {} //color cannot be null
//			}
//			if(disabled)
//				break;
//		}
//		//Check if capacity constraints of output places allow firing
//		if(enabled){
//			for(E r: outgoingRelations) {
//				boolean disabled = false;
//				Multiset<String> tokenConstraints = r.getConstraint();
//				for(String color: tokenConstraints.support()){
//					try {
//						if(r.getPlace().hasCapacityRestriction(color)){
//							if(r.getPlace().getColorCapacity(color) < r.getPlace().getTokens(color) + tokenConstraints.multiplicity(color)){
//								enabled = false;
//								disabled = true;
//								break;
//							}
//						}
//					} catch (ParameterException e) {} //color cannot be null
//				}
//				if(disabled)
//					break;
//			}
//		}
//		
//		if(enabled && !oldEnabledState){
//			listenerSupport.notifyEnabling(new TransitionEvent<AbstractCPNTransition<E>>(this));
//		} else if(!enabled && oldEnabledState){
//			listenerSupport.notifyDisabling(new TransitionEvent<AbstractCPNTransition<E>>(this));
//		}
//	}
	
	@Override
	protected boolean enoughTokensInInputPlaces(){
		for(E r: incomingRelations) {
			Multiset<String> tokenConstraints = r.getConstraint();
			for(String color: tokenConstraints.support()){
				try {
					if(r.getPlace().getTokens(color) < tokenConstraints.multiplicity(color)){
						return false;
					}
				} catch (ParameterException e) {} //color cannot be null
			}
		}
		return true;
	}
	
	@Override
	protected boolean enoughSpaceInOutputPlaces(){
		for(E r: outgoingRelations) {
			Multiset<String> tokenConstraints = r.getConstraint();
			for(String color: tokenConstraints.support()){
				try {
					if(r.getPlace().hasCapacityRestriction(color)){
						if(r.getPlace().getColorCapacity(color) < r.getPlace().getTokens(color) + tokenConstraints.multiplicity(color)){
							return false;
						}
					}
				} catch (ParameterException e) {} //color cannot be null
			}
		}
		return true;
	}

	@Override
	public AbstractCPNTransition<E> clone() {
		return (AbstractCPNTransition<E>) super.clone();
	}
	
	
}
