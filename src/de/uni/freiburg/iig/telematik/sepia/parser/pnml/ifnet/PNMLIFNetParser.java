package de.uni.freiburg.iig.telematik.sepia.parser.pnml.ifnet;

import org.w3c.dom.Document;

import de.invation.code.toval.parser.ParserException;
import de.invation.code.toval.validate.ParameterException;
import de.uni.freiburg.iig.telematik.sepia.graphic.GraphicalIFNet;
import de.uni.freiburg.iig.telematik.sepia.graphic.netgraphics.IFNetGraphics;
import de.uni.freiburg.iig.telematik.sepia.petrinet.ifnet.IFNet;
import de.uni.freiburg.iig.telematik.sepia.petrinet.ifnet.IFNetFlowRelation;
import de.uni.freiburg.iig.telematik.sepia.petrinet.ifnet.IFNetMarking;
import de.uni.freiburg.iig.telematik.sepia.petrinet.ifnet.IFNetPlace;
import de.uni.freiburg.iig.telematik.sepia.petrinet.ifnet.RegularIFNetTransition;
import de.uni.freiburg.iig.telematik.sepia.petrinet.ifnet.DeclassificationTransition;
import de.uni.freiburg.iig.telematik.sepia.petrinet.ifnet.abstr.AbstractIFNetTransition;

/**
 * <p>
 * Parser for IF-nets. The process of parsing a PNML file is the following:
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
public class PNMLIFNetParser extends AbstractPNMLIFNetParser<IFNetPlace, AbstractIFNetTransition<IFNetFlowRelation>, IFNetFlowRelation, IFNetMarking, RegularIFNetTransition, DeclassificationTransition, IFNet, IFNetGraphics> {

	public GraphicalIFNet parse(Document pnmlDocument) throws ParameterException, ParserException {

		net = new IFNet();
		graphics = new IFNetGraphics();

		parseDocument(pnmlDocument);

		return new GraphicalIFNet(net, graphics);
	}

	@Override
	public IFNetGraphics getGraphics() {
		return (IFNetGraphics) graphics;
	}

	@Override
	public IFNet getNet() {
		return (IFNet) net;
	}
}
