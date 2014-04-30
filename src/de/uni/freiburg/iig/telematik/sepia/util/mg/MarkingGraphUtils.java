package de.uni.freiburg.iig.telematik.sepia.util.mg;

import de.uni.freiburg.iig.telematik.sepia.petrinet.AbstractFlowRelation;
import de.uni.freiburg.iig.telematik.sepia.petrinet.AbstractMarking;
import de.uni.freiburg.iig.telematik.sepia.petrinet.AbstractPetriNet;
import de.uni.freiburg.iig.telematik.sepia.petrinet.AbstractPlace;
import de.uni.freiburg.iig.telematik.sepia.petrinet.AbstractTransition;
import de.uni.freiburg.iig.telematik.sepia.util.mg.abstr.AbstractMarkingGraph;
import de.uni.freiburg.iig.telematik.sepia.util.mg.abstr.AbstractMarkingGraphRelation;
import de.uni.freiburg.iig.telematik.sepia.util.mg.abstr.AbstractMarkingGraphState;

public class MarkingGraphUtils {

	public static <	P extends AbstractPlace<F, S>, 
					T extends AbstractTransition<F, S>, 
					F extends AbstractFlowRelation<P, T, S>, 
					M extends AbstractMarking<S>, 
					S extends Object, 
					X extends AbstractMarkingGraphState<M, S>,
					Y extends AbstractMarkingGraphRelation<M, X, S>> MGTraversalResult getSequences(AbstractPetriNet<P,T,F,M,S> petriNet, AbstractMarkingGraph<M,S,X,Y> markingGraph, boolean includeSilentTransitions){
		
//		MarkingGraphTraverser<P,T,F,M,S,X> traverser = new MarkingGraphTraverser<P,T,F,M,S,X>(petriNet, markingGraph, includeSilentTransitions);
//		traverser.iterate();
//		return new MGTraversalResult(traverser.getSequences(), traverser.getCompleteSequences());
		MGSequenceGenerator<P,T,F,M,S,X,Y> sequenceGenerator = new MGSequenceGenerator<P,T,F,M,S,X,Y>(petriNet, markingGraph, includeSilentTransitions);
		return sequenceGenerator.getSequences();
	}

}
