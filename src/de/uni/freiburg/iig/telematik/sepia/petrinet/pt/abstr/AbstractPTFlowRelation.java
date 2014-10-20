package de.uni.freiburg.iig.telematik.sepia.petrinet.pt.abstr;

import de.invation.code.toval.validate.Validate;
import de.uni.freiburg.iig.telematik.sepia.petrinet.AbstractFlowRelation;

public abstract class AbstractPTFlowRelation<P extends AbstractPTPlace<? extends AbstractPTFlowRelation<P,T>>, T extends AbstractPTTransition<? extends AbstractPTFlowRelation<P,T>>> extends AbstractFlowRelation<P, T, Integer> {

	private static final long serialVersionUID = 6385163977362688470L;

	private String toStringFormat = "%s -%s-> %s";
	
	public static final int DEFAULT_WEIGHT = 1;
	

	
	public AbstractPTFlowRelation(P place, T transition, Integer weight) {
		super(place, transition, weight);
	}

	public AbstractPTFlowRelation(P place, T transition) {
		super(place, transition);
	}

	public AbstractPTFlowRelation(T transition, P place, Integer weight) {
		super(transition, place, weight);
	}

	public AbstractPTFlowRelation(T transition, P place) {
		super(transition, place);
	}

	@Override
	protected Integer getDefaultConstraint() {
		return DEFAULT_WEIGHT;
	}

	public void setWeight(int weight) {
		setConstraint(weight);
	}
	
	public int getWeight() {
		return getConstraint();
	}
	
	@Override
	protected void validateConstraint(Integer constraint) {
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
