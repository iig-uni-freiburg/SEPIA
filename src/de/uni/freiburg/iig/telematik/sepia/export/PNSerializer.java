package de.uni.freiburg.iig.telematik.sepia.export;

import de.uni.freiburg.iig.telematik.sepia.petrinet.AbstractFlowRelation;
import de.uni.freiburg.iig.telematik.sepia.petrinet.AbstractMarking;
import de.uni.freiburg.iig.telematik.sepia.petrinet.AbstractPetriNet;
import de.uni.freiburg.iig.telematik.sepia.petrinet.AbstractPlace;
import de.uni.freiburg.iig.telematik.sepia.petrinet.AbstractTransition;

public interface PNSerializer<N extends AbstractPetriNet<P,T,F,M,S>,
							  P extends AbstractPlace<F,S>, 
   							  T extends AbstractTransition<F,S>, 
   							  F extends AbstractFlowRelation<P,T,S>, 
   							  M extends AbstractMarking<S>, 
   							  S extends Object> {
	
	public String toString(N net) throws Exception;
			
}
