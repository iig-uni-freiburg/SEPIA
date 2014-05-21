package de.uni.freiburg.iig.telematik.sepia.parser.pnml.cwn;

import org.w3c.dom.Document;

import de.invation.code.toval.parser.ParserException;
import de.invation.code.toval.validate.ParameterException;
import de.uni.freiburg.iig.telematik.sepia.graphic.GraphicalCWN;
import de.uni.freiburg.iig.telematik.sepia.graphic.netgraphics.CWNGraphics;
import de.uni.freiburg.iig.telematik.sepia.mg.cwn.CWNMarkingGraphRelation;
import de.uni.freiburg.iig.telematik.sepia.mg.cwn.CWNMarkingGraphState;
import de.uni.freiburg.iig.telematik.sepia.petrinet.cwn.CWN;
import de.uni.freiburg.iig.telematik.sepia.petrinet.cwn.CWNFlowRelation;
import de.uni.freiburg.iig.telematik.sepia.petrinet.cwn.CWNMarking;
import de.uni.freiburg.iig.telematik.sepia.petrinet.cwn.CWNPlace;
import de.uni.freiburg.iig.telematik.sepia.petrinet.cwn.CWNTransition;

/**
 * <p>
 * Parser for CWNs. The process of parsing a PNML file is the following:
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
public class PNMLCWNParser extends AbstractPNMLCWNParser<CWNPlace, CWNTransition, CWNFlowRelation, CWNMarking, CWNMarkingGraphState, CWNMarkingGraphRelation, CWN, CWNGraphics> {

	@Override
	public GraphicalCWN parse(Document pnmlDocument) throws ParserException {

		net = new CWN();
		graphics = new CWNGraphics();

		parseDocument(pnmlDocument);

		return new GraphicalCWN(net, graphics);
	}
}
