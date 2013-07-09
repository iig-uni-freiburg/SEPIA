package de.uni.freiburg.iig.telematik.sepia.traversal;

import de.invation.code.toval.validate.ParameterException;
import de.uni.freiburg.iig.telematik.sepia.petrinet.AbstractPetriNet;

public class PNTraverserFactory {
	
	public static RandomPNTraverser createDefaultTraverser(AbstractPetriNet<?,?,?,?,?> net) 
			throws ParameterException{
		return new RandomPNTraverser(net);
	}
	
	public static StochasticPNTraverser createStochasticTraverser(AbstractPetriNet<?,?,?,?,?> net) 
			throws ParameterException{
		return new StochasticPNTraverser(net);
	}
	

}
