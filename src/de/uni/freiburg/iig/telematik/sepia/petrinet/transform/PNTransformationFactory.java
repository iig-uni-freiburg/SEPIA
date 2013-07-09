package de.uni.freiburg.iig.telematik.sepia.petrinet.transform;

import de.invation.code.toval.validate.ParameterException;
import de.uni.freiburg.iig.telematik.sepia.petrinet.AbstractFlowRelation;
import de.uni.freiburg.iig.telematik.sepia.petrinet.AbstractMarking;
import de.uni.freiburg.iig.telematik.sepia.petrinet.AbstractPetriNet;
import de.uni.freiburg.iig.telematik.sepia.petrinet.AbstractPlace;
import de.uni.freiburg.iig.telematik.sepia.petrinet.AbstractTransition;

public class PNTransformationFactory {

	public static <P extends AbstractPlace<F,S>, 
	   			   T extends AbstractTransition<F,S>, 
	   			   F extends AbstractFlowRelation<P,T,S>, 
	   			   M extends AbstractMarking<S>, 
	   			   S extends Object> PNTransformer<P,T,F,M,S> 
	
				   getAndToXorTransformer(AbstractPetriNet<P,T,F,M,S> net, T andSplit, T andJoin) 
						   
				   throws ParameterException{
		
		return new AndToXorTransformer<P, T, F, M, S>(net, andSplit, andJoin);
	}
	
	public static <P extends AbstractPlace<F,S>, 
	   			   T extends AbstractTransition<F,S>, 
	   			   F extends AbstractFlowRelation<P,T,S>, 
	   			   M extends AbstractMarking<S>, 
	   			   S extends Object> PNTransformer<P,T,F,M,S> 

	   			   getXorToAndTransformer(AbstractPetriNet<P,T,F,M,S> net, P xorSplit, P xorJoin) 
			   
	   		       throws ParameterException{

		return new XorToAndTransformer<P, T, F, M, S>(net, xorSplit, xorJoin);
	}
	
}
