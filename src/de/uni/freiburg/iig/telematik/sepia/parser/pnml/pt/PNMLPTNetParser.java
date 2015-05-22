package de.uni.freiburg.iig.telematik.sepia.parser.pnml.pt;

import org.w3c.dom.Document;

import de.invation.code.toval.parser.ParserException;
import de.uni.freiburg.iig.telematik.sepia.graphic.GraphicalPTNet;
import de.uni.freiburg.iig.telematik.sepia.graphic.netgraphics.PTGraphics;
import de.uni.freiburg.iig.telematik.sepia.petrinet.pt.PTFlowRelation;
import de.uni.freiburg.iig.telematik.sepia.petrinet.pt.PTMarking;
import de.uni.freiburg.iig.telematik.sepia.petrinet.pt.PTNet;
import de.uni.freiburg.iig.telematik.sepia.petrinet.pt.PTPlace;
import de.uni.freiburg.iig.telematik.sepia.petrinet.pt.PTTransition;

/**
 * <p>
 * Parser for PT nets. The process of parsing a PNML file is the following:
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
 * @author Adrian Lange
 */
public class PNMLPTNetParser extends AbstractPNMLPTNetParser<PTPlace, PTTransition, PTFlowRelation, PTMarking, PTNet, PTGraphics> {

	@Override
	public GraphicalPTNet parse(Document pnmlDocument) throws ParserException {

		net = new PTNet();
		graphics = new PTGraphics();

		parseDocument(pnmlDocument);

		return new GraphicalPTNet(net, graphics);
	}
}
