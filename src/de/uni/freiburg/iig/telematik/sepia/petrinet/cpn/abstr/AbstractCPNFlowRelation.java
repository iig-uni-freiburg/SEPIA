package de.uni.freiburg.iig.telematik.sepia.petrinet.cpn.abstr;

import de.invation.code.toval.types.Multiset;
import de.invation.code.toval.validate.Validate;
import de.uni.freiburg.iig.telematik.sepia.petrinet.AbstractFlowRelation;

public abstract class AbstractCPNFlowRelation<P extends AbstractCPNPlace<? extends AbstractCPNFlowRelation<P,T>>, 
											  T extends AbstractCPNTransition<? extends AbstractCPNFlowRelation<P,T>>> 
												extends AbstractFlowRelation<P, T, Multiset<String>>{
	

	private static final long serialVersionUID = -8307588526926192566L;

//	public AbstractCPNFlowRelation(P place, T transition, boolean addDefaultConstraint) {
//		super(place, transition);
//		initialize(addDefaultConstraint);
//	}
//	
//	public AbstractCPNFlowRelation(T transition, P place, boolean addDefaultConstraint) {
//		super(transition, place);
//		initialize(addDefaultConstraint);
//	}
	
	
	
	public AbstractCPNFlowRelation(P place, T transition, Multiset<String> constraint) {
		super(place, transition, constraint);
	}

	public AbstractCPNFlowRelation(T transition, P place, Multiset<String> constraint) {
		super(transition, place, constraint);
	}
	
	public AbstractCPNFlowRelation(P place, T transition) {
		super(place, transition);
	}

	public AbstractCPNFlowRelation(T transition, P place) {
		super(transition, place);
	}

//	private void initialize(boolean addDefaultConstraint) {
//		if(addDefaultConstraint){
//			constraint = getDefaultConstraint();
//		} else {
//			constraint = new Multiset<String>();
//		}
//	}

	@Override
	public Multiset<String> getDefaultConstraint(){
		Multiset<String> defaultConstraint = new Multiset<String>();
		defaultConstraint.add(AbstractCPN.DEFAULT_TOKEN_COLOR);
		return defaultConstraint;
	}

	public void addConstraint(String color, Integer number) {
		Validate.notNull(color);
		Validate.notNull(number);
		Validate.bigger(number, 0);
		constraint.setMultiplicity(color, number);
		relationListenerSupport.notifyCapacityChanged(this);
	}
	
	public int getConstraint(String color) {
		return getConstraint().multiplicity(color);
	}
	
	@Override
	public Multiset<String> getConstraint(){
		return super.getConstraint().clone();
	}
	
	public boolean hasConstraints(){
		return !getConstraint().isEmpty();
	}
	
	@Override
	protected void validateConstraint(Multiset<String> constraint) {
		Validate.notNull(constraint);
	}
}
