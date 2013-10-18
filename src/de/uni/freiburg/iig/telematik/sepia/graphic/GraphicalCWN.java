package de.uni.freiburg.iig.telematik.sepia.graphic;

import de.invation.code.toval.types.Multiset;
import de.uni.freiburg.iig.telematik.sepia.graphic.netgraphics.AbstractCWNGraphics;
import de.uni.freiburg.iig.telematik.sepia.graphic.netgraphics.AbstractPNGraphics;
import de.uni.freiburg.iig.telematik.sepia.petrinet.AbstractPetriNet;
import de.uni.freiburg.iig.telematik.sepia.petrinet.cwn.CWN;
import de.uni.freiburg.iig.telematik.sepia.petrinet.cwn.CWNFlowRelation;
import de.uni.freiburg.iig.telematik.sepia.petrinet.cwn.CWNMarking;
import de.uni.freiburg.iig.telematik.sepia.petrinet.cwn.CWNPlace;
import de.uni.freiburg.iig.telematik.sepia.petrinet.cwn.CWNTransition;

/**
 * Container class with a {@link CWN} and its graphical information as {@link AbstractCWNGraphics}.
 * 
 * @author Thomas Stocker
 * @author Adrian Lange
 */
public class GraphicalCWN extends AbstractGraphicalPN<CWNPlace, CWNTransition, CWNFlowRelation, CWNMarking, Multiset<String>> {

	public GraphicalCWN(AbstractPetriNet<CWNPlace, CWNTransition, CWNFlowRelation, CWNMarking, Multiset<String>> petriNet, AbstractPNGraphics<CWNPlace, CWNTransition, CWNFlowRelation, CWNMarking, Multiset<String>> petriNetGraphics) {
		super(petriNet, petriNetGraphics);
	}

	@Override
	public CWN getPetriNet() {
		return (CWN) super.getPetriNet();
	}

	@Override
	public AbstractCWNGraphics getPetriNetGraphics() {
		return (AbstractCWNGraphics) super.getPetriNetGraphics();
	}
}
