package de.uni.freiburg.iig.telematik.sepia.petrinet.properties.threaded;

import de.invation.code.toval.validate.Validate;
import de.uni.freiburg.iig.telematik.sepia.petrinet.abstr.AbstractFlowRelation;
import de.uni.freiburg.iig.telematik.sepia.petrinet.abstr.AbstractMarking;
import de.uni.freiburg.iig.telematik.sepia.petrinet.abstr.AbstractPetriNet;
import de.uni.freiburg.iig.telematik.sepia.petrinet.abstr.AbstractPlace;
import de.uni.freiburg.iig.telematik.sepia.petrinet.abstr.AbstractTransition;

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
