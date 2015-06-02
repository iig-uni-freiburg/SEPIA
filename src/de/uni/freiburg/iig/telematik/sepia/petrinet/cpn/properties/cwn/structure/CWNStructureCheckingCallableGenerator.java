package de.uni.freiburg.iig.telematik.sepia.petrinet.cpn.properties.cwn.structure;

import de.invation.code.toval.types.Multiset;
import de.uni.freiburg.iig.telematik.sepia.petrinet.cpn.abstr.AbstractCPN;
import de.uni.freiburg.iig.telematik.sepia.petrinet.cpn.abstr.AbstractCPNFlowRelation;
import de.uni.freiburg.iig.telematik.sepia.petrinet.cpn.abstr.AbstractCPNMarking;
import de.uni.freiburg.iig.telematik.sepia.petrinet.cpn.abstr.AbstractCPNPlace;
import de.uni.freiburg.iig.telematik.sepia.petrinet.cpn.abstr.AbstractCPNTransition;
import de.uni.freiburg.iig.telematik.sepia.petrinet.properties.threaded.AbstractCallableGenerator;

public class CWNStructureCheckingCallableGenerator<P extends AbstractCPNPlace<F>,
										  T extends AbstractCPNTransition<F>, 
										  F extends AbstractCPNFlowRelation<P,T>, 
										  M extends AbstractCPNMarking> extends AbstractCallableGenerator<P,T,F,M,Multiset<String>> {

	public CWNStructureCheckingCallableGenerator(AbstractCPN<P,T,F,M> cpn) {
		super(cpn);
	}
	
	@Override
	public AbstractCPN<P,T,F,M> getPetriNet() {
		return (AbstractCPN<P,T,F,M>) super.getPetriNet();
	}
}
