package de.uni.freiburg.iig.telematik.sepia.traversal;

import de.uni.freiburg.iig.telematik.sepia.petrinet.AbstractFlowRelation;
import de.uni.freiburg.iig.telematik.sepia.petrinet.AbstractMarking;
import de.uni.freiburg.iig.telematik.sepia.petrinet.AbstractPetriNet;
import de.uni.freiburg.iig.telematik.sepia.petrinet.AbstractPlace;
import de.uni.freiburg.iig.telematik.sepia.petrinet.AbstractTransition;

public class PNTraverserFactory {
	
	public static <P extends AbstractPlace<F,S>, 
	   			   T extends AbstractTransition<F,S>, 
	   			   F extends AbstractFlowRelation<P,T,S>, 
	   			   M extends AbstractMarking<S>, 
	   			   S extends Object>
	
	RandomPNTraverser<T> createDefaultTraverser(AbstractPetriNet<P,T,F,M,S> net){
		
		return new RandomPNTraverser<T>(net);
	}
	
	public static <P extends AbstractPlace<F,S>, 
	   			   T extends AbstractTransition<F,S>, 
	   			   F extends AbstractFlowRelation<P,T,S>, 
	   			   M extends AbstractMarking<S>, 
	   			   S extends Object>
	
	StochasticPNTraverser<T> createStochasticTraverser(AbstractPetriNet<P,T,F,M,S> net){
		
		return new StochasticPNTraverser<T>(net);
	}
	

}
