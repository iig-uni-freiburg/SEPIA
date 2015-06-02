package de.uni.freiburg.iig.telematik.sepia.petrinet.cpn;

import java.util.Set;

import de.invation.code.toval.types.Multiset;
import de.uni.freiburg.iig.telematik.sepia.event.CapacityEvent;
import de.uni.freiburg.iig.telematik.sepia.mg.cpn.CPNMarkingGraph;
import de.uni.freiburg.iig.telematik.sepia.petrinet.abstr.AbstractPlace;
import de.uni.freiburg.iig.telematik.sepia.petrinet.cpn.abstr.AbstractCPN;


public class CPN extends AbstractCPN<CPNPlace, CPNTransition, CPNFlowRelation, CPNMarking> {
	
	private static final long serialVersionUID = -5549362122826931975L;

	public CPN() {
		super();
	}

	public CPN(Set<String> places, Set<String> transitions,
			CPNMarking initialMarking) {
		super(places, transitions, initialMarking);
	}

	@Override
	public CPNMarking createNewMarking() {
		return new CPNMarking();
	}

	@Override
	protected CPNTransition createNewTransition(String name, String label, boolean isSilent) {
		return new CPNTransition(name, label, isSilent);
	}

	@Override
	protected CPNPlace createNewPlace(String name, String label) {
		return new CPNPlace(name, label);
	}

	@Override
	protected CPNFlowRelation createNewFlowRelation(CPNPlace place, CPNTransition transition, Multiset<String> constraint) {
		return new CPNFlowRelation(place, transition, constraint);
	}

	@Override
	protected CPNFlowRelation createNewFlowRelation(CPNTransition transition, CPNPlace place, Multiset<String> constraint) {
		return new CPNFlowRelation(transition, place, constraint);
	}
	
	@Override
	protected CPNFlowRelation createNewFlowRelation(CPNPlace place, CPNTransition transition) {
		return new CPNFlowRelation(place, transition);
	}

	@Override
	protected CPNFlowRelation createNewFlowRelation(CPNTransition transition, CPNPlace place) {
		return new CPNFlowRelation(transition, place);
	}

//	@Override
//	public CPNMarking fireCheck(String transitionName) throws PNException {
//		validateFireTransition(transitionName);
//		CPNMarking newMarking = cloneMarking();
//		CPNTransition transition = getTransition(transitionName);
//		for(CPNFlowRelation relation: transition.getIncomingRelations()){
//			String inputPlaceName = relation.getPlace().getName();
//			newMarking.set(inputPlaceName, newMarking.get(inputPlaceName).difference(relation.getConstraint()));
//		}
//		for(CPNFlowRelation relation: transition.getOutgoingRelations()){
//			String outputPlaceName = relation.getPlace().getName();
//			Multiset<String> oldState = (newMarking.get(outputPlaceName) == null ? new Multiset<String>() : newMarking.get(outputPlaceName).clone());
//			newMarking.set(outputPlaceName, oldState.sum(relation.getConstraint()));
//		}
//		return newMarking;
//	}
	
	@Override
	public CPN newInstance() {
		return new CPN();
	}

	@Override
	public void capacityChanged(CapacityEvent<? extends AbstractPlace<CPNFlowRelation, Multiset<String>>> o) {}

	@Override
	public Class<?> getMarkingGraphClass() {
		return CPNMarkingGraph.class;
	}
	
//	public static void main(String[] args) throws Exception{
//		CPN net = new CPN();
//		net.addPlace("p1");
//		net.addPlace("p2");
//		net.addPlace("p3");
//		net.addPlace("p4");
//		net.addPlace("p5");
//		net.addTransition("t1");
//		net.addTransition("t2");
//		net.addTransition("t3");
//		System.out.println(net.addFlowRelationPT("p1", "t1"));
//		System.out.println(net.addFlowRelationTP("t1", "p2"));
//		System.out.println(net.addFlowRelationPT("p2", "t2", true));
//		System.out.println(net.addFlowRelationTP("t2", "p4", true));
//		System.out.println(net.addFlowRelationPT("p4", "t3"));
//		System.out.println(net.addFlowRelationTP("t3", "p5"));
//		System.out.println(net.addFlowRelationTP("t1", "p3"));
//		System.out.println(net.addFlowRelationPT("p3", "t3"));
//		
//
//		FiringRule firingRuleT1 = new FiringRule();
//		firingRuleT1.addRequirement("p1", "black", 1);
//		firingRuleT1.addProduction("p2", "black", 1);
//		firingRuleT1.addProduction("p3", "blue", 1);
//		net.addFiringRule("t1", firingRuleT1);
//		
//		FiringRule firingRuleT3 = new FiringRule();
//		firingRuleT3.addRequirement("p4", "black", 1);
//		firingRuleT3.addRequirement("p3", "blue", 1);
//		firingRuleT3.addProduction("p5", "black", 1);
//		net.addFiringRule("t3", firingRuleT3);
//		
//		CPNMarking initialMarking = new CPNMarking();
//		Multiset<String> p1Marking = new Multiset<String>();
//		p1Marking.add("black");
//		initialMarking.set("p1", p1Marking);
//		
//		net.setInitialMarking(initialMarking);
////		while(net.hasEnabledTransitions()){
////			System.out.println(net.getMarking());
////			System.out.println(net.getEnabledTransitions());
////			net.fireNext();
////		}
//		System.out.println(net.getMarking());
//	}


}
