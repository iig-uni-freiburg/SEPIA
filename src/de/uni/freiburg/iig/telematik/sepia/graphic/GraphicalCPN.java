package de.uni.freiburg.iig.telematik.sepia.graphic;

import de.uni.freiburg.iig.telematik.sepia.graphic.netgraphics.AbstractCPNGraphics;
import de.uni.freiburg.iig.telematik.sepia.graphic.netgraphics.CPNGraphics;
import de.uni.freiburg.iig.telematik.sepia.petrinet.cpn.CPN;
import de.uni.freiburg.iig.telematik.sepia.petrinet.cpn.CPNFlowRelation;
import de.uni.freiburg.iig.telematik.sepia.petrinet.cpn.CPNMarking;
import de.uni.freiburg.iig.telematik.sepia.petrinet.cpn.CPNPlace;
import de.uni.freiburg.iig.telematik.sepia.petrinet.cpn.CPNTransition;

/**
 * Container class with a {@link CPN} and its graphical information as {@link AbstractCPNGraphics}.
 * 
 * @author Thomas Stocker
 * @author Adrian Lange
 */
public class GraphicalCPN extends AbstractGraphicalCPN<CPNPlace, CPNTransition, CPNFlowRelation, CPNMarking, CPN, CPNGraphics> {

	public GraphicalCPN() {
		this(new CPN(), new CPNGraphics());
	}
	
	public GraphicalCPN(CPN petriNet, CPNGraphics petriNetGraphics) {
		super(petriNet, petriNetGraphics);
	}
}
