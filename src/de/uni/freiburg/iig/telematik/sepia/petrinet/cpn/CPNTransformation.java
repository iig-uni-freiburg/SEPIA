package de.uni.freiburg.iig.telematik.sepia.petrinet.cpn;

import de.invation.code.toval.types.Multiset;
import de.invation.code.toval.validate.Validate;
import de.uni.freiburg.iig.telematik.sepia.mg.cpn.AbstractCPNMarkingGraphRelation;
import de.uni.freiburg.iig.telematik.sepia.mg.cpn.AbstractCPNMarkingGraphState;
import de.uni.freiburg.iig.telematik.sepia.mg.pt.AbstractPTMarkingGraphRelation;
import de.uni.freiburg.iig.telematik.sepia.mg.pt.AbstractPTMarkingGraphState;
import de.uni.freiburg.iig.telematik.sepia.petrinet.cpn.abstr.AbstractCPN;
import de.uni.freiburg.iig.telematik.sepia.petrinet.cpn.abstr.AbstractCPNFlowRelation;
import de.uni.freiburg.iig.telematik.sepia.petrinet.cpn.abstr.AbstractCPNMarking;
import de.uni.freiburg.iig.telematik.sepia.petrinet.cpn.abstr.AbstractCPNPlace;
import de.uni.freiburg.iig.telematik.sepia.petrinet.cpn.abstr.AbstractCPNTransition;
import de.uni.freiburg.iig.telematik.sepia.petrinet.pt.abstr.AbstractPTFlowRelation;
import de.uni.freiburg.iig.telematik.sepia.petrinet.pt.abstr.AbstractPTMarking;
import de.uni.freiburg.iig.telematik.sepia.petrinet.pt.abstr.AbstractPTNet;
import de.uni.freiburg.iig.telematik.sepia.petrinet.pt.abstr.AbstractPTPlace;
import de.uni.freiburg.iig.telematik.sepia.petrinet.pt.abstr.AbstractPTTransition;

public class CPNTransformation {
	
	protected static <P1 extends AbstractPTPlace<F1>, 
	   			   	  T1 extends AbstractPTTransition<F1>, 
	   			   	  F1 extends AbstractPTFlowRelation<P1,T1>, 
	   			   	  M1 extends AbstractPTMarking,
	   			   	  X1 extends AbstractPTMarkingGraphState<M1>,
	   			   	  Y1 extends AbstractPTMarkingGraphRelation<M1,X1>,
	   			   	  N1 extends AbstractPTNet<P1,T1,F1,M1,X1,Y1>,
	   			   	  P2 extends AbstractCPNPlace<F2>,
	   			   	  T2 extends AbstractCPNTransition<F2>, 
	   			   	  F2 extends AbstractCPNFlowRelation<P2,T2>, 
	   			   	  M2 extends AbstractCPNMarking,
	   			   	  X2 extends AbstractCPNMarkingGraphState<M2>,
	   			   	  Y2 extends AbstractCPNMarkingGraphRelation<M2,X2>,
	   			   	  N2 extends AbstractCPN<P2,T2,F2,M2,X2,Y2>> 

	void transform(N1 ptNet, N2 cpn) {
		cpn.setName(ptNet.getName());
		// Add places of P/T-Net to CPN
		for(P1 place: ptNet.getPlaces()){
			cpn.addPlace(place.getName(), place.getLabel());
		}
		// Add transitions of P/T-Net to CPN
		for(T1 transition: ptNet.getTransitions()){
			cpn.addTransition(transition.getName(), transition.getLabel());
			cpn.getTransition(transition.getName()).setSilent(transition.isSilent());
		}
		// Add relations of P/T-Net to CPN
		for(F1 relation: ptNet.getFlowRelations()){
			F2 cpnRelation = null;
			if(relation.getDirectionPT())
				cpnRelation = cpn.addFlowRelationPT(relation.getPlace().getName(), relation.getTransition().getName());
			else cpnRelation = cpn.addFlowRelationTP(relation.getTransition().getName(), relation.getPlace().getName());
			cpnRelation.addConstraint(cpn.defaultTokenColor(), relation.getWeight());
		}
		// Set the initial marking of the CPN
		M2 cpnInitialMarking = cpn.createNewMarking();
		M1 initialMarking = ptNet.getInitialMarking();
		for(String place: initialMarking.places()){
			Multiset<String> colorMap = new Multiset<String>();
			colorMap.setMultiplicity(cpn.defaultTokenColor(), initialMarking.get(place));
			cpnInitialMarking.set(place, colorMap);
		}
		cpn.setInitialMarking(cpnInitialMarking);
	}
	
	public static <P extends AbstractPTPlace<F>, 
				   T extends AbstractPTTransition<F>, 
				   F extends AbstractPTFlowRelation<P,T>, 
				   M extends AbstractPTMarking,
				   X extends AbstractPTMarkingGraphState<M>,
				   Y extends AbstractPTMarkingGraphRelation<M,X>,
				   N extends AbstractPTNet<P,T,F,M,X,Y>> 
	
	CPN transformToCPN(AbstractPTNet<P,T,F,M,X,Y> ptNet) {
		Validate.notNull(ptNet);
		CPN cpn = new CPN();
		transform(ptNet, cpn);
		return cpn;
	}

}
