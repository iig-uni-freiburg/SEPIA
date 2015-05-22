package de.uni.freiburg.iig.telematik.sepia.graphic;

import de.uni.freiburg.iig.telematik.sepia.graphic.netgraphics.AbstractPTGraphics;
import de.uni.freiburg.iig.telematik.sepia.graphic.netgraphics.PTGraphics;
import de.uni.freiburg.iig.telematik.sepia.petrinet.pt.PTFlowRelation;
import de.uni.freiburg.iig.telematik.sepia.petrinet.pt.PTMarking;
import de.uni.freiburg.iig.telematik.sepia.petrinet.pt.PTNet;
import de.uni.freiburg.iig.telematik.sepia.petrinet.pt.PTPlace;
import de.uni.freiburg.iig.telematik.sepia.petrinet.pt.PTTransition;

/**
 * Container class with a {@link PTNet} and its graphical information as {@link AbstractPTGraphics}.
 * 
 * @author Thomas Stocker
 * @author Adrian Lange
 */
public class GraphicalPTNet extends AbstractGraphicalPTNet<PTPlace, PTTransition, PTFlowRelation, PTMarking, PTNet, PTGraphics> {

	public GraphicalPTNet() {
		this(new PTNet(), new PTGraphics());
	}
	
	public GraphicalPTNet(PTNet petriNet, PTGraphics petriNetGraphics) {
		super(petriNet, petriNetGraphics);
	}
}
