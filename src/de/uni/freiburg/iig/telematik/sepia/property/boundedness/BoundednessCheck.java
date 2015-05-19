package de.uni.freiburg.iig.telematik.sepia.property.boundedness;

import de.invation.code.toval.thread.ExecutorListener;
import de.uni.freiburg.iig.telematik.sepia.mg.abstr.AbstractMarkingGraphRelation;
import de.uni.freiburg.iig.telematik.sepia.mg.abstr.AbstractMarkingGraphState;
import de.uni.freiburg.iig.telematik.sepia.petrinet.AbstractFlowRelation;
import de.uni.freiburg.iig.telematik.sepia.petrinet.AbstractMarking;
import de.uni.freiburg.iig.telematik.sepia.petrinet.AbstractPetriNet;
import de.uni.freiburg.iig.telematik.sepia.petrinet.AbstractPetriNet.Boundedness;
import de.uni.freiburg.iig.telematik.sepia.petrinet.AbstractPlace;
import de.uni.freiburg.iig.telematik.sepia.petrinet.AbstractTransition;
import de.uni.freiburg.iig.telematik.sepia.property.mg.MGConstruction;

public class BoundednessCheck {
	
	/**
	 * Checks if the given net is capacity bounded.<br>
	 * A net is capacity bounded when all its places are bounded.
	 *  
	 * @param petriNet The input Petri net.
	 * @return The marking graph of the given Petri net.
	 */
	public static <	P extends AbstractPlace<F,S>, 
					T extends AbstractTransition<F,S>, 
					F extends AbstractFlowRelation<P,T,S>, 
					M extends AbstractMarking<S>, 
					S extends Object,
					X extends AbstractMarkingGraphState<M,S>,
					Y extends AbstractMarkingGraphRelation<M,X,S>>

	boolean checkCapacityBoundedness(AbstractPetriNet<P,T,F,M,S,X,Y> petriNet) {
		for(P p: petriNet.getPlaces()){
			if(!p.isBounded())
				return false;
		}
		return true;
	}

	/**
	 * Checks if the Petri net is bounded.<br>
	 * In case the marking graph of the net cannot be constructed with the maximum number of elements (see {@link MGConstruction#MAX_RG_CALCULATION_STEPS}),<br>
	 * it is assumed to be unbounded; otherwise bounded.<br>
	 * @param petriNet The basic Petri net for operation.
	 * @return The marking graph of the given Petri net.
	 * @throws BoundednessException
	 */
	public static <	P extends AbstractPlace<F,S>, 
					T extends AbstractTransition<F,S>, 
					F extends AbstractFlowRelation<P,T,S>, 
					M extends AbstractMarking<S>, 
					S extends Object,
					X extends AbstractMarkingGraphState<M,S>,
					Y extends AbstractMarkingGraphRelation<M,X,S>>

	void initiateBoundednessCheck(AbstractPetriNet<P,T,F,M,S,X,Y> petriNet, ExecutorListener listener) throws BoundednessException{
		
		ThreadedBoundednessChecker<P,T,F,M,S,X,Y> checker = new ThreadedBoundednessChecker<P,T,F,M,S,X,Y>(petriNet);
		checker.addExecutorListener(listener);
		
		checker.runCalculation();
	}
	
	/**
	 * Checks if the Petri net is bounded.<br>
	 * In case the marking graph of the net cannot be constructed with the maximum number of elements (see {@link MGConstruction#MAX_RG_CALCULATION_STEPS}),<br>
	 * it is assumed to be unbounded; otherwise bounded.<br>
	 * @param petriNet The basic Petri net for operation.
	 * @return The marking graph of the given Petri net.
	 * @throws BoundednessException
	 */
	public static <	P extends AbstractPlace<F,S>, 
					T extends AbstractTransition<F,S>, 
					F extends AbstractFlowRelation<P,T,S>, 
					M extends AbstractMarking<S>, 
					S extends Object,
					X extends AbstractMarkingGraphState<M,S>,
					Y extends AbstractMarkingGraphRelation<M,X,S>>

	Boundedness getBoundedness(AbstractPetriNet<P,T,F,M,S,X,Y> petriNet) throws BoundednessException{
		
		ThreadedBoundednessChecker<P,T,F,M,S,X,Y> checker = new ThreadedBoundednessChecker<P,T,F,M,S,X,Y>(petriNet);
		checker.runCalculation();
		
		Boundedness boundedness = null;
		try{
			boundedness = checker.getBoundedness();
		} catch (BoundednessException e) {
			throw new BoundednessException("Exception during marking graph construction.\nReason: " + e.getMessage(), e);
		}
		return boundedness;
	}

}
