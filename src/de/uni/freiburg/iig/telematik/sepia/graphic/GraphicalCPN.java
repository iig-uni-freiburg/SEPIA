package de.uni.freiburg.iig.telematik.sepia.graphic;

import de.invation.code.toval.types.Multiset;
import de.uni.freiburg.iig.telematik.sepia.petrinet.AbstractPetriNet;
import de.uni.freiburg.iig.telematik.sepia.petrinet.cpn.CPN;
import de.uni.freiburg.iig.telematik.sepia.petrinet.cpn.CPNFlowRelation;
import de.uni.freiburg.iig.telematik.sepia.petrinet.cpn.CPNMarking;
import de.uni.freiburg.iig.telematik.sepia.petrinet.cpn.CPNPlace;
import de.uni.freiburg.iig.telematik.sepia.petrinet.cpn.CPNTransition;

/**
 * Container class with a {@link CPN} and its graphical information as {@link CPNGraphics}.
 * 
 * @author Thomas Stocker
 * @author Adrian Lange
 */
public class GraphicalCPN extends GraphicalPN<CPNPlace, CPNTransition, CPNFlowRelation, CPNMarking, Multiset<String>> {

	public GraphicalCPN(AbstractPetriNet<CPNPlace, CPNTransition, CPNFlowRelation, CPNMarking, Multiset<String>> petriNet, AbstractPNGraphics<CPNPlace, CPNTransition, CPNFlowRelation, CPNMarking, Multiset<String>> petriNetGraphics) {
		super(petriNet, petriNetGraphics);
	}

	@Override
	public CPN getPetriNet() {
		return (CPN) super.getPetriNet();
	}

	@Override
	public CPNGraphics getPetriNetGraphics() {
		return (CPNGraphics) super.getPetriNetGraphics();
	}
}
