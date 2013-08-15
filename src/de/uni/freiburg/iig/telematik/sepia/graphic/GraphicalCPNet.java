package de.uni.freiburg.iig.telematik.sepia.graphic;

import de.invation.code.toval.types.Multiset;
import de.uni.freiburg.iig.telematik.sepia.petrinet.AbstractPetriNet;
import de.uni.freiburg.iig.telematik.sepia.petrinet.cpn.CPN;
import de.uni.freiburg.iig.telematik.sepia.petrinet.cpn.CPNFlowRelation;
import de.uni.freiburg.iig.telematik.sepia.petrinet.cpn.CPNMarking;
import de.uni.freiburg.iig.telematik.sepia.petrinet.cpn.CPNPlace;
import de.uni.freiburg.iig.telematik.sepia.petrinet.cpn.CPNTransition;

/**
 * TODO
 * 
 * @author Thomas Stocker
 * @author Adrian Lange
 */
public class GraphicalCPNet extends GraphicalPN<CPNPlace, CPNTransition, CPNFlowRelation, CPNMarking, Multiset<String>> {

	public GraphicalCPNet(AbstractPetriNet<CPNPlace, CPNTransition, CPNFlowRelation, CPNMarking, Multiset<String>> petriNet, PNGraphics<CPNPlace, CPNTransition, CPNFlowRelation, CPNMarking, Multiset<String>> petriNetGraphics) {
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
