package de.uni.freiburg.iig.telematik.sepia.parser.pnml;

import org.w3c.dom.Document;

import de.uni.freiburg.iig.telematik.sepia.graphic.GraphicalPetriNet;
import de.uni.freiburg.iig.telematik.sepia.petrinet.pt.PTFlowRelation;
import de.uni.freiburg.iig.telematik.sepia.petrinet.pt.PTMarking;
import de.uni.freiburg.iig.telematik.sepia.petrinet.pt.PTNet;
import de.uni.freiburg.iig.telematik.sepia.petrinet.pt.PTPlace;
import de.uni.freiburg.iig.telematik.sepia.petrinet.pt.PTTransition;

/**
 * TODO
 * 
 * @author Adrian Lange
 */
public class PNMLPTNetParser {

	/**
	 * TODO
	 * 
	 * @param pnmlDocument
	 * @return
	 */
	public static GraphicalPetriNet<?, ?, ?, ?, ?> parse(Document pnmlDocument) {
		PTNet net = new PTNet();
		GraphicalPetriNet<PTPlace, PTTransition, PTFlowRelation, PTMarking, Integer> graphicalNet = new GraphicalPetriNet<PTPlace, PTTransition, PTFlowRelation, PTMarking, Integer>(net);

		return graphicalNet;
	}
}
