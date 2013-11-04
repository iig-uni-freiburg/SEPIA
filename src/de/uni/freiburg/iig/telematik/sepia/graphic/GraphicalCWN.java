package de.uni.freiburg.iig.telematik.sepia.graphic;

import de.uni.freiburg.iig.telematik.sepia.graphic.netgraphics.AbstractCWNGraphics;
import de.uni.freiburg.iig.telematik.sepia.graphic.netgraphics.CWNGraphics;
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
public class GraphicalCWN extends AbstractGraphicalCWN<CWNPlace, CWNTransition, CWNFlowRelation, CWNMarking, CWN, CWNGraphics> {

	public GraphicalCWN(CWN petriNet, CWNGraphics petriNetGraphics) {
		super(petriNet, petriNetGraphics);
	}
//
//	@Override
//	public CWN getPetriNet() {
//		return (CWN) super.getPetriNet();
//	}
//
//	@Override
//	public CWNGraphics getPetriNetGraphics() {
//		return (CWNGraphics) super.getPetriNetGraphics();
//	}
}
