package de.uni.freiburg.iig.telematik.sepia.parser.pnml.cpn;

import org.w3c.dom.Document;

import de.invation.code.toval.parser.ParserException;
import de.uni.freiburg.iig.telematik.sepia.graphic.GraphicalCPN;
import de.uni.freiburg.iig.telematik.sepia.graphic.netgraphics.CPNGraphics;
import de.uni.freiburg.iig.telematik.sepia.petrinet.cpn.CPN;
import de.uni.freiburg.iig.telematik.sepia.petrinet.cpn.CPNFlowRelation;
import de.uni.freiburg.iig.telematik.sepia.petrinet.cpn.CPNMarking;
import de.uni.freiburg.iig.telematik.sepia.petrinet.cpn.CPNPlace;
import de.uni.freiburg.iig.telematik.sepia.petrinet.cpn.CPNTransition;

/**
 * <p>
 * Parser for CPNs. The process of parsing a PNML file is the following:
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
public class PNMLCPNParser extends AbstractPNMLCPNParser<CPNPlace, CPNTransition, CPNFlowRelation, CPNMarking, CPN, CPNGraphics> {

	@Override
	public GraphicalCPN parse(Document pnmlDocument) throws ParserException {

		net = new CPN();
		graphics = new CPNGraphics();

		parseDocument(pnmlDocument);

		return new GraphicalCPN(net, graphics);
	}
}
