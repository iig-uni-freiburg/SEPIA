package de.uni.freiburg.iig.telematik.sepia.petrinet.pt.properties.wfnet.structure;

import de.uni.freiburg.iig.telematik.sepia.petrinet.properties.threaded.AbstractCallableGenerator;
import de.uni.freiburg.iig.telematik.sepia.petrinet.pt.abstr.AbstractPTFlowRelation;
import de.uni.freiburg.iig.telematik.sepia.petrinet.pt.abstr.AbstractPTMarking;
import de.uni.freiburg.iig.telematik.sepia.petrinet.pt.abstr.AbstractPTNet;
import de.uni.freiburg.iig.telematik.sepia.petrinet.pt.abstr.AbstractPTPlace;
import de.uni.freiburg.iig.telematik.sepia.petrinet.pt.abstr.AbstractPTTransition;

public class WFNetStructureCheckingCallableGenerator<P extends AbstractPTPlace<F>,
										  			 T extends AbstractPTTransition<F>, 
										  			 F extends AbstractPTFlowRelation<P,T>, 
										  			 M extends AbstractPTMarking> extends AbstractCallableGenerator<P,T,F,M,Integer> {

	public WFNetStructureCheckingCallableGenerator(AbstractPTNet<P,T,F,M> ptNet) {
		super(ptNet);
	}
	
	@Override
	public AbstractPTNet<P,T,F,M> getPetriNet() {
		return (AbstractPTNet<P,T,F,M>) super.getPetriNet();
	}
}
