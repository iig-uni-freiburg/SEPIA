package de.uni.freiburg.iig.telematik.sepia.parser.pnml.ptc;

import org.w3c.dom.Document;

import de.invation.code.toval.parser.ParserException;
import de.uni.freiburg.iig.telematik.sepia.graphic.GraphicalPTCNet;
import de.uni.freiburg.iig.telematik.sepia.graphic.netgraphics.PTCNetGraphics;
import de.uni.freiburg.iig.telematik.sepia.petrinet.ptc.PTCNet;
import de.uni.freiburg.iig.telematik.sepia.petrinet.ptc.PTCNetFlowRelation;
import de.uni.freiburg.iig.telematik.sepia.petrinet.ptc.PTCNetMarking;
import de.uni.freiburg.iig.telematik.sepia.petrinet.ptc.PTCNetPlace;
import de.uni.freiburg.iig.telematik.sepia.petrinet.ptc.abstr.AbstractPTCNetTransition;

/**
 * <p>
 * Parser for PTCost nets. The process of parsing a PNML file is the following:
 * </p>
 * <ol>
 * <li>Check if the document is well-formed XML.</li>
 * <li>Determine net type by reading the net type URI (get type from URINettypeRefs table).</li>
 * <li>Read the net type specific net components. To avoid violating a constraint, the objects must be read in multiple iterations:
 * <ol>
 * <li>Read nodes (places and transitions) with their marking and labeling.</li>
 * <li>Read edges (arcs) with their annotations and specific starting and ending nodes.</li>
 * </ol>
 * </li>
 * </ol>
 * 
 * @author Julius Holderer
 * @author Adrian Lange
 */
public class PNMLPTCNetParser extends
		AbstractPNMLPTCNetParser<PTCNetPlace, AbstractPTCNetTransition<PTCNetFlowRelation>, PTCNetFlowRelation, PTCNetMarking, PTCNet, PTCNetGraphics> {

	@Override
	public GraphicalPTCNet parse(Document pnmlDocument) throws ParserException {

		net = new PTCNet();
		graphics = new PTCNetGraphics();

		parseDocument(pnmlDocument);

		return new GraphicalPTCNet(net, graphics);
	}
}
