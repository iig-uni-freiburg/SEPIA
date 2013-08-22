package de.uni.freiburg.iig.telematik.sepia.parser.pnml;

import java.util.HashSet;
import java.util.Set;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import de.invation.code.toval.parser.XMLParserException;
import de.invation.code.toval.validate.ParameterException;
import de.invation.code.toval.validate.Validate;
import de.uni.freiburg.iig.telematik.sepia.graphic.AnnotationGraphics;
import de.uni.freiburg.iig.telematik.sepia.graphic.EdgeGraphics;
import de.uni.freiburg.iig.telematik.sepia.graphic.GraphicalPTNet;
import de.uni.freiburg.iig.telematik.sepia.graphic.NodeGraphics;
import de.uni.freiburg.iig.telematik.sepia.graphic.PTGraphics;
import de.uni.freiburg.iig.telematik.sepia.graphic.TokenGraphics;
import de.uni.freiburg.iig.telematik.sepia.parser.pnml.PNMLParserException.ErrorCode;
import de.uni.freiburg.iig.telematik.sepia.petrinet.AbstractFlowRelation;
import de.uni.freiburg.iig.telematik.sepia.petrinet.AbstractMarking;
import de.uni.freiburg.iig.telematik.sepia.petrinet.AbstractPlace;
import de.uni.freiburg.iig.telematik.sepia.petrinet.AbstractTransition;
import de.uni.freiburg.iig.telematik.sepia.petrinet.pt.PTFlowRelation;
import de.uni.freiburg.iig.telematik.sepia.petrinet.pt.PTMarking;
import de.uni.freiburg.iig.telematik.sepia.petrinet.pt.PTNet;

/**
 * <p>
 * Parser for PT nets. The process of parsing a PNMLimport de.uni.freiburg.iig.telematik.sepia.tmp.PNMLElementReader;
 file is the following:
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
public class PNMLPTNetParser {

	private static PTNet net = new PTNet();
	private static PTGraphics graphics = new PTGraphics();

	public static <P extends AbstractPlace<F, S>, T extends AbstractTransition<F, S>, F extends AbstractFlowRelation<P, T, S>, M extends AbstractMarking<S>, S extends Object>

	GraphicalPTNet

	parse(Document pnmlDocument) throws PNMLParserException, ParameterException, XMLParserException {

		// Check if the net is defined on a single page
		NodeList netElement = pnmlDocument.getElementsByTagName("page");
		if (netElement.getLength() != 1)
			throw new PNMLParserException(ErrorCode.NOT_ON_ONE_PAGE);

		// Read places and transitions
		NodeList placeNodes = pnmlDocument.getElementsByTagName("place");
		readPlaces(placeNodes);
		NodeList transitionNodes = pnmlDocument.getElementsByTagName("transition");
		readTransitions(transitionNodes);
		// Read arcs
		NodeList arcNodes = pnmlDocument.getElementsByTagName("arc");
		readArcs(arcNodes);

		return new GraphicalPTNet(net, graphics);
	}

	private static void readArcs(NodeList arcNodes) throws ParameterException, XMLParserException {
		// read and add each arc/flow relation
		for (int a = 0; a < arcNodes.getLength(); a++) {
			if (arcNodes.item(a).getNodeType() == Node.ELEMENT_NODE) {
				Element arc = (Element) arcNodes.item(a);
				// ID must be available in a valid net
				// FIXME arc id is ignored
				String sourceName = arc.getAttribute("source");
				String targetName = arc.getAttribute("target");

				// get inscription
				int inscription = 1;
				NodeList arcInscriptions = arc.getElementsByTagName("inscription");
				if (arcInscriptions.getLength() == 1) {
					String inscriptionStr = PNMLElementReader.readText(arcInscriptions.item(0));
					inscription = Integer.parseInt(inscriptionStr);
				}

				PTFlowRelation flowRelation;
				// if PT relation
				if (net.getPlace(sourceName) != null) {
					flowRelation = net.addFlowRelationPT(sourceName, targetName, inscription);
				}
				// if TP relation
				else {
					flowRelation = net.addFlowRelationTP(sourceName, targetName, inscription);
				}

				// annotation graphics
				AnnotationGraphics edgeAnnotationGraphics = (AnnotationGraphics) PNMLElementReader.readGraphics((Element) arcInscriptions.item(0));
				if (edgeAnnotationGraphics != null)
					graphics.getEdgeAnnotationGraphics().put(flowRelation, edgeAnnotationGraphics);

				// get graphical information
				EdgeGraphics arcGraphics = (EdgeGraphics) PNMLElementReader.readGraphics(arc);
				if (arcGraphics != null)
					graphics.getEdgeGraphics().put(flowRelation, arcGraphics);
			}
		}
	}

	private static void readPlaces(NodeList placeNodes) throws ParameterException, XMLParserException {
		// add each place
		PTMarking marking = new PTMarking();
		for (int p = 0; p < placeNodes.getLength(); p++) {
			Node placeNode = placeNodes.item(p);
			if (placeNode.getNodeType() == Node.ELEMENT_NODE) {
				Element place = (Element) placeNode;
				// ID must be available in a valid net
				String placeName = place.getAttribute("id");
				String placeLabel = null;
				// Check if there's a label
				NodeList placeLabels = place.getElementsByTagName("name");
				if (placeLabels.getLength() == 1) {
					placeLabel = PNMLElementReader.readText(placeLabels.item(0));
				}
				Validate.notNull(placeLabel);
				net.addPlace(placeName, placeLabel);

				// Read graphical information
				NodeGraphics placeGraphics = (NodeGraphics) PNMLElementReader.readGraphics(place);
				graphics.getPlaceGraphics().put(net.getPlace(placeName), placeGraphics);

				// Read marking with graphical information
				NodeList placeInitialMarkings = place.getElementsByTagName("initialMarking");
				if (placeInitialMarkings.getLength() == 1) {
					int initialMarking = PNMLElementReader.readInitialMarking(placeInitialMarkings.item(0));
					if (initialMarking > 0) {
						marking.set(placeName, initialMarking);

						// graphics
						NodeList graphicsList = ((Element) placeInitialMarkings.item(0)).getElementsByTagName("tokenposition");
						if (graphicsList.getLength() > 0) {
							Set<TokenGraphics> tokenGraphics = new HashSet<TokenGraphics>();
							for (int tp = 0; tp < graphicsList.getLength(); tp++) {
								Element tokenPos = (Element) graphicsList.item(tp);
								TokenGraphics tokenGraphic = new TokenGraphics();
								tokenGraphic.setTokenposition(PNMLElementReader.readTokenPosition(tokenPos));
								tokenGraphics.add(tokenGraphic);
							}
							graphics.getTokenGraphics().put(net.getPlace(placeName), tokenGraphics);
						}
					}
				}
			}
		}
		net.setInitialMarking(marking);
	}

	private static void readTransitions(NodeList transitionNodes) throws XMLParserException, ParameterException {
		// read and add each transition
		for (int t = 0; t < transitionNodes.getLength(); t++) {
			if (transitionNodes.item(t).getNodeType() == Node.ELEMENT_NODE) {
				Element transition = (Element) transitionNodes.item(t);
				// ID must be available in a valid net
				String transitionName = transition.getAttribute("id");
				String transitionLabel = null;
				// Check if there's a label
				NodeList transitionLabels = transition.getElementsByTagName("name");
				if (transitionLabels.getLength() == 1) {
					transitionLabel = PNMLElementReader.readText(transitionLabels.item(0));
					if (transitionLabel != null && transitionLabel.length() == 0)
						transitionLabel = null;
				}
				if (transitionLabel != null)
					net.addTransition(transitionName, transitionLabel);
				else
					net.addTransition(transitionName);

				// read graphical information
				NodeGraphics transitionGraphics = (NodeGraphics) PNMLElementReader.readGraphics(transition);
				graphics.getTransitionGraphics().put(net.getTransition(transitionName), transitionGraphics);

				// transitions have no inscription/marking
			}
		}
	}
}
