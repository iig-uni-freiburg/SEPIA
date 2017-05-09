package de.uni.freiburg.iig.telematik.sepia.graphic;

import de.uni.freiburg.iig.telematik.sepia.graphic.netgraphics.AbstractPTGraphics;
import de.uni.freiburg.iig.telematik.sepia.graphic.netgraphics.PTCNetGraphics;
import de.uni.freiburg.iig.telematik.sepia.graphic.netgraphics.PTGraphics;
import de.uni.freiburg.iig.telematik.sepia.petrinet.pt.PTNet;
import de.uni.freiburg.iig.telematik.sepia.petrinet.ptc.PTCNet;
import de.uni.freiburg.iig.telematik.sepia.petrinet.ptc.PTCNetFlowRelation;
import de.uni.freiburg.iig.telematik.sepia.petrinet.ptc.PTCNetMarking;
import de.uni.freiburg.iig.telematik.sepia.petrinet.ptc.PTCNetPlace;
import de.uni.freiburg.iig.telematik.sepia.petrinet.ptc.PTCNetTransition;
import de.uni.freiburg.iig.telematik.sepia.petrinet.ptc.abstr.AbstractPTCNetTransition;

/**
 * Container class with a {@link PTNet} and its graphical information as {@link AbstractPTGraphics}.
 * 
 * @author Thomas Stocker
 * @author Adrian Lange
 */
public class GraphicalPTCNet extends AbstractGraphicalPTCNet<PTCNetPlace, AbstractPTCNetTransition<PTCNetFlowRelation>, PTCNetFlowRelation, PTCNetMarking, PTCNet, PTCNetGraphics> {

	public GraphicalPTCNet() {
		this(new PTCNet(), new PTCNetGraphics());
	}
	
	public GraphicalPTCNet(PTCNet petriNet, PTCNetGraphics petriNetGraphics) {
		super(petriNet, petriNetGraphics);
	}
}
