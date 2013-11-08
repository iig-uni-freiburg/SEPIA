package de.uni.freiburg.iig.telematik.sepia.petrinet.cpn.abstr;

import de.invation.code.toval.types.Multiset;
import de.invation.code.toval.validate.ParameterException;
import de.invation.code.toval.validate.Validate;
import de.uni.freiburg.iig.telematik.sepia.petrinet.AbstractFlowRelation;

public abstract class AbstractCPNFlowRelation<P extends AbstractCPNPlace<? extends AbstractCPNFlowRelation<P,T>>, 
											  T extends AbstractCPNTransition<? extends AbstractCPNFlowRelation<P,T>>> 
												extends AbstractFlowRelation<P, T, Multiset<String>>{
	
	public AbstractCPNFlowRelation(P place, T transition, boolean addDefaultConstraint) throws ParameterException {
		super(place, transition);
		initialize(addDefaultConstraint);
	}
	
	public AbstractCPNFlowRelation(T transition, P place, boolean addDefaultConstraint) throws ParameterException {
		super(transition, place);
		initialize(addDefaultConstraint);
	}
	
	public AbstractCPNFlowRelation(P place, T transition) throws ParameterException {
		this(place, transition, true);
	}
	
	public AbstractCPNFlowRelation(T transition, P place) throws ParameterException {
		this(transition, place, true);
	}
	
	private void initialize(boolean addDefaultConstraint) {
		if(addDefaultConstraint){
			constraint = getDefaultConstraint();
		} else {
			constraint = new Multiset<String>();
		}
	}
	
	public Multiset<String> getDefaultConstraint(){
		Multiset<String> defaultConstraint = new Multiset<String>();
		try {
			defaultConstraint.add(AbstractCPN.DEFAULT_TOKEN_COLOR);
		} catch (ParameterException e) {
			e.printStackTrace();
		}
		return defaultConstraint;
	}

	public void addConstraint(String color, Integer number) throws ParameterException{
		Validate.notNull(color);
		Validate.notNull(number);
		Validate.bigger(number, 0);
		constraint.setMultiplicity(color, number);
		relationListenerSupport.notifyCapacityChanged(this);
	}
	
	public int getConstraint(String color) throws ParameterException{
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
	protected void validateConstraint(Multiset<String> constraint) throws ParameterException {
		Validate.notNull(constraint);
	}
	
}
