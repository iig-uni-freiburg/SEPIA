package de.uni.freiburg.iig.telematik.sepia.property.boundedness;

import de.uni.freiburg.iig.telematik.sepia.petrinet.AbstractFlowRelation;
import de.uni.freiburg.iig.telematik.sepia.petrinet.AbstractMarking;
import de.uni.freiburg.iig.telematik.sepia.petrinet.AbstractPetriNet;
import de.uni.freiburg.iig.telematik.sepia.petrinet.AbstractPlace;
import de.uni.freiburg.iig.telematik.sepia.petrinet.AbstractTransition;
import de.uni.freiburg.iig.telematik.sepia.property.AbstractCallableGenerator;

public class BoundednessCheckGenerator<	P extends AbstractPlace<F,S>, 
										T extends AbstractTransition<F,S>, 
										F extends AbstractFlowRelation<P,T,S>, 
										M extends AbstractMarking<S>, 
										S extends Object> extends AbstractCallableGenerator<P,T,F,M,S>{

	public BoundednessCheckGenerator(AbstractPetriNet<P,T,F,M,S> petriNet) {
		super(petriNet);
	}

}
