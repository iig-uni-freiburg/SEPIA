package de.uni.freiburg.iig.telematik.sepia.petrinet.pt.abstr;

import de.invation.code.toval.validate.ParameterException;
import de.invation.code.toval.validate.Validate;
import de.uni.freiburg.iig.telematik.sepia.petrinet.AbstractFlowRelation;

public abstract class AbstractPTFlowRelation<P extends AbstractPTPlace<? extends AbstractPTFlowRelation<P,T>>, T extends AbstractPTTransition<? extends AbstractPTFlowRelation<P,T>>> extends AbstractFlowRelation<P, T, Integer> {

	private String toStringFormat = "%s -%s-> %s";
	
	public static final int DEFAULT_WEIGHT = 1;
	
	public AbstractPTFlowRelation(P place, T transition, int weight) throws ParameterException {
		super(place, transition);
		setWeight(weight);
	}
	
	public AbstractPTFlowRelation(T transition, P place, int weight) throws ParameterException {
		super(transition, place);
		setWeight(weight);
	}

	public AbstractPTFlowRelation(P place, T transition) throws ParameterException {
		this(place, transition, DEFAULT_WEIGHT);
	}

	public AbstractPTFlowRelation(T transition, P place) throws ParameterException {
		this(transition, place, DEFAULT_WEIGHT);
	}
	
	public void setWeight(int weight) throws ParameterException {
		setConstraint(weight);
	}
	
	public int getWeight() {
		return getConstraint();
	}
	
	@Override
	protected void validateConstraint(Integer constraint) throws ParameterException {
		Validate.notNull(constraint);
		Validate.bigger(constraint, 0);
	}

	@Override
	public String toString() {
		if(directionPT)
			return String.format(toStringFormat, place.getName(), getWeight(), transition.getName());
		return String.format(toStringFormat, transition.getName(), getWeight(), place.getName());
	}

	@Override
	public Integer getConstraint() {
		return new Integer(constraint);
	}
	
}
