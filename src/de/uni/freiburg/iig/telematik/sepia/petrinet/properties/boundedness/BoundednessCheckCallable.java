package de.uni.freiburg.iig.telematik.sepia.petrinet.properties.boundedness;

import de.uni.freiburg.iig.telematik.sepia.petrinet.abstr.AbstractFlowRelation;
import de.uni.freiburg.iig.telematik.sepia.petrinet.abstr.AbstractMarking;
import de.uni.freiburg.iig.telematik.sepia.petrinet.abstr.AbstractPlace;
import de.uni.freiburg.iig.telematik.sepia.petrinet.abstr.AbstractTransition;
import de.uni.freiburg.iig.telematik.sepia.petrinet.properties.mg.MGConstructorCallable;
import de.uni.freiburg.iig.telematik.sepia.petrinet.properties.mg.MGConstructorCallableGenerator;

public class BoundednessCheckCallable<P extends AbstractPlace<F,S>, 
									  T extends AbstractTransition<F,S>, 
									  F extends AbstractFlowRelation<P,T,S>, 
									  M extends AbstractMarking<S>, 
									  S extends Object> extends MGConstructorCallable<P,T,F,M,S> {

	public BoundednessCheckCallable(BoundednessCheckGenerator<P,T,F,M,S> generator) {
		super(new MGConstructorCallableGenerator<P,T,F,M,S>(generator));
	}

}
