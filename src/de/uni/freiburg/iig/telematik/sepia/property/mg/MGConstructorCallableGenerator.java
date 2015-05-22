package de.uni.freiburg.iig.telematik.sepia.property.mg;

import de.uni.freiburg.iig.telematik.sepia.mg.abstr.AbstractMarkingGraphRelation;
import de.uni.freiburg.iig.telematik.sepia.mg.abstr.AbstractMarkingGraphState;
import de.uni.freiburg.iig.telematik.sepia.petrinet.AbstractFlowRelation;
import de.uni.freiburg.iig.telematik.sepia.petrinet.AbstractMarking;
import de.uni.freiburg.iig.telematik.sepia.petrinet.AbstractPetriNet;
import de.uni.freiburg.iig.telematik.sepia.petrinet.AbstractPlace;
import de.uni.freiburg.iig.telematik.sepia.petrinet.AbstractTransition;
import de.uni.freiburg.iig.telematik.sepia.property.AbstractCallableGenerator;

public class MGConstructorCallableGenerator<P extends AbstractPlace<F,S>, 
											T extends AbstractTransition<F,S>, 
											F extends AbstractFlowRelation<P,T,S>, 
											M extends AbstractMarking<S>, 
											S extends Object,
											X extends AbstractMarkingGraphState<M,S>,
											Y extends AbstractMarkingGraphRelation<M,X,S>> extends AbstractCallableGenerator<P,T,F,M,S> {

	public MGConstructorCallableGenerator(AbstractPetriNet<P,T,F,M,S> petriNet) {
		super(petriNet);
	}

}
