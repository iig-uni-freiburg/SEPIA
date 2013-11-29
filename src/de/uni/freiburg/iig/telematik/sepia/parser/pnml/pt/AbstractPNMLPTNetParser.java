package de.uni.freiburg.iig.telematik.sepia.parser.pnml.pt;

import java.util.HashSet;
import java.util.Set;

import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import de.invation.code.toval.parser.ParserException;
import de.invation.code.toval.validate.ParameterException;
import de.invation.code.toval.validate.Validate;
import de.uni.freiburg.iig.telematik.sepia.graphic.AbstractGraphicalPN;
import de.uni.freiburg.iig.telematik.sepia.graphic.netgraphics.AbstractPTGraphics;
import de.uni.freiburg.iig.telematik.sepia.graphic.netgraphics.AnnotationGraphics;
import de.uni.freiburg.iig.telematik.sepia.graphic.netgraphics.ArcGraphics;
import de.uni.freiburg.iig.telematik.sepia.graphic.netgraphics.NodeGraphics;
import de.uni.freiburg.iig.telematik.sepia.graphic.netgraphics.TokenGraphics;
import de.uni.freiburg.iig.telematik.sepia.parser.pnml.AbstractPNMLParser;
import de.uni.freiburg.iig.telematik.sepia.parser.pnml.PNMLParserException;
import de.uni.freiburg.iig.telematik.sepia.parser.pnml.PNMLParserException.ErrorCode;
import de.uni.freiburg.iig.telematik.sepia.petrinet.pt.abstr.AbstractPTFlowRelation;
import de.uni.freiburg.iig.telematik.sepia.petrinet.pt.abstr.AbstractPTMarking;
import de.uni.freiburg.iig.telematik.sepia.petrinet.pt.abstr.AbstractPTNet;
import de.uni.freiburg.iig.telematik.sepia.petrinet.pt.abstr.AbstractPTPlace;
import de.uni.freiburg.iig.telematik.sepia.petrinet.pt.abstr.AbstractPTTransition;

/**
 * <p>
 * Abstract parser for abstract PT nets.
 * </p>
 * 
 * @author Adrian Lange
 */
public abstract class AbstractPNMLPTNetParser<P extends AbstractPTPlace<F>,
											  T extends AbstractPTTransition<F>,
											  F extends AbstractPTFlowRelation<P, T>,
											  M extends AbstractPTMarking,
											  N extends AbstractPTNet<P, T, F, M>,
											  G extends AbstractPTGraphics<P, T, F, M>>

	extends AbstractPNMLParser<P, T, F, M, Integer, N, G> {

	/**
	 * Reads all arcs given in a list of DOM nodes and adds them to the {@link AbstractGraphicalPN}.
	 */
	@Override
	protected void readArcs(NodeList arcNodes) throws ParameterException, ParserException {
		// read and add each arc/flow relation
		for (int a = 0; a < arcNodes.getLength(); a++) {
			if (arcNodes.item(a).getNodeType() == Node.ELEMENT_NODE) {
				Element arc = (Element) arcNodes.item(a);
				// ID must be available in a valid net
				String sourceName = arc.getAttribute("source");
				String targetName = arc.getAttribute("target");

				// get inscription
				int inscription = 1;
				NodeList arcInscriptions = arc.getElementsByTagName("inscription");
				if (arcInscriptions.getLength() == 1) {
					String inscriptionStr = readText(arcInscriptions.item(0));
					if (inscriptionStr != null)
						inscription = Integer.parseInt(inscriptionStr);
				}

				F flowRelation;
				// if PT relation
				if (net.getPlace(sourceName) != null && net.getTransition(targetName) != null) {
					flowRelation = getNet().addFlowRelationPT(sourceName, targetName, inscription);
				}
				// if TP relation
				else if (net.getPlace(targetName) != null && net.getTransition(sourceName) != null) {
					flowRelation = getNet().addFlowRelationTP(sourceName, targetName, inscription);
				} else {
					throw new PNMLParserException(ErrorCode.INVALID_FLOW_RELATION, "Couldn't determine flow relation between \"" + sourceName + "\" and \"" + targetName + "\".");
				}

				// Check if there's a label
				String arcName = arc.getAttribute("id");
				if (arcName != null && arcName.length() > 0)
					flowRelation.setName(arcName);

				// annotation graphics
				if (arcInscriptions.getLength() == 1) {
					AnnotationGraphics arcAnnotationGraphics = readAnnotationGraphicsElement((Element) arcInscriptions.item(0));
					if (arcAnnotationGraphics != null)
						graphics.getArcAnnotationGraphics().put(flowRelation.getName(), arcAnnotationGraphics);
				}

				// get graphical information
				ArcGraphics arcGraphics = readArcGraphicsElement(arc);
				if (arcGraphics != null)
					graphics.getArcGraphics().put(flowRelation.getName(), arcGraphics);
			}
		}
	}

	/**
	 * Reads all places given in a list of DOM nodes and adds them to the {@link AbstractGraphicalPN}.
	 */
	@Override
	protected void readPlaces(NodeList placeNodes) throws ParameterException, ParserException {
		// add each place
		M marking = net.getMarking();
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
					if (readText(placeLabels.item(0)) != null) {
						placeLabel = readText(placeLabels.item(0));
					}
					// annotation graphics
					AnnotationGraphics placeAnnotationGraphics = readAnnotationGraphicsElement((Element) placeLabels.item(0));
					if (placeAnnotationGraphics != null)
						graphics.getPlaceLabelAnnotationGraphics().put(placeName, placeAnnotationGraphics);
				} else {
					placeLabel = placeName;
				}
				net.addPlace(placeName, placeLabel);

				// Read marking with graphical information
				NodeList placeInitialMarkings = place.getElementsByTagName("initialMarking");
				if (placeInitialMarkings.getLength() == 1) {
					int initialMarking = readInitialMarking(placeInitialMarkings.item(0));
					if (initialMarking < 0) {
						throw new PNMLParserException(ErrorCode.VALIDATION_FAILED, "Place initial markings must not be a negative number.");
					} else if (initialMarking > 0) {
						marking.set(placeName, initialMarking);

						// graphics
						NodeList graphicsList = ((Element) placeInitialMarkings.item(0)).getElementsByTagName("tokenposition");
						if (graphicsList.getLength() > 0) {
							Set<TokenGraphics> tokenGraphics = new HashSet<TokenGraphics>();
							for (int tp = 0; tp < graphicsList.getLength(); tp++) {
								Element tokenPos = (Element) graphicsList.item(tp);
								TokenGraphics tokenGraphic = new TokenGraphics();
								tokenGraphic.setTokenposition(readTokenPosition(tokenPos));
								tokenGraphics.add(tokenGraphic);
							}
							graphics.getTokenGraphics().put(placeName, tokenGraphics);
						}
					}
				}

				// Read and add place capacities
				NodeList placeCapacitiesList = place.getElementsByTagName("capacity");
				for (int i = 0; i < placeCapacitiesList.getLength(); i++) {
					// If node is element node and is direct child of the place node
					if (placeCapacitiesList.item(i).getNodeType() == Node.ELEMENT_NODE && placeCapacitiesList.item(i).getParentNode().equals(place)) {
						Element placeCapacitiesElement = (Element) placeCapacitiesList.item(i);
						Integer placeCapacity = readPlaceCapacity(placeCapacitiesElement);
						// add place capacity
						if (placeCapacity != null) {
							P currentPlace = net.getPlace(placeName);
							currentPlace.setCapacity(placeCapacity);
						}
					}
				}

				// Read graphical information
				NodeGraphics placeGraphics = readNodeGraphicsElement(place);
				if (placeGraphics != null)
					graphics.getPlaceGraphics().put(placeName, placeGraphics);
			}
		}
		net.setInitialMarking(marking);
	}

	/**
	 * Gets the place capacity element of a PN and returns an {@link Integer} value.
	 */
	public Integer readPlaceCapacity(Element placeCapacityElement) throws ParameterException, PNMLParserException {
		Validate.notNull(placeCapacityElement);

		int capacity = Integer.parseInt(placeCapacityElement.getTextContent());

		if (capacity < 1)
			throw new PNMLParserException(ErrorCode.VALIDATION_FAILED, "Capacity must be 1 or higher.");

		return capacity;
	}
}
