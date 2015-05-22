package de.uni.freiburg.iig.telematik.sepia.property;

import de.invation.code.toval.validate.Validate;
import de.uni.freiburg.iig.telematik.sepia.petrinet.AbstractFlowRelation;
import de.uni.freiburg.iig.telematik.sepia.petrinet.AbstractMarking;
import de.uni.freiburg.iig.telematik.sepia.petrinet.AbstractPetriNet;
import de.uni.freiburg.iig.telematik.sepia.petrinet.AbstractPlace;
import de.uni.freiburg.iig.telematik.sepia.petrinet.AbstractTransition;

public abstract class AbstractCallableGenerator<P extends AbstractPlace<F,S>, 
												T extends AbstractTransition<F,S>, 
												F extends AbstractFlowRelation<P,T,S>, 
												M extends AbstractMarking<S>, 
												S extends Object> {
	
	private AbstractPetriNet<P,T,F,M,S> petriNet = null;
	
	protected AbstractCallableGenerator(AbstractPetriNet<P,T,F,M,S> petriNet){
		Validate.notNull(petriNet);
		this.petriNet = petriNet;
	}
	
	protected <N extends AbstractCallableGenerator<P,T,F,M,S>> AbstractCallableGenerator(N generator){
		Validate.notNull(generator);
		this.petriNet = generator.getPetriNet();
	}
	
	public AbstractPetriNet<P,T,F,M,S> getPetriNet() {
		return petriNet;
	}

}
