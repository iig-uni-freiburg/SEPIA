package de.uni.freiburg.iig.telematik.sepia.parser.pnml.ptc;

import java.util.HashSet;
import java.util.Set;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import de.invation.code.toval.parser.ParserException;
import de.invation.code.toval.validate.Validate;
import de.uni.freiburg.iig.telematik.sepia.graphic.AbstractGraphicalPN;
import de.uni.freiburg.iig.telematik.sepia.graphic.GraphicalPTCNet;
import de.uni.freiburg.iig.telematik.sepia.graphic.netgraphics.AbstractPTCNetGraphics;
import de.uni.freiburg.iig.telematik.sepia.graphic.netgraphics.AnnotationGraphics;
import de.uni.freiburg.iig.telematik.sepia.graphic.netgraphics.NodeGraphics;
import de.uni.freiburg.iig.telematik.sepia.graphic.netgraphics.TokenGraphics;
import de.uni.freiburg.iig.telematik.sepia.parser.pnml.PNMLParserException;
import de.uni.freiburg.iig.telematik.sepia.parser.pnml.PNMLParserException.ErrorCode;
import de.uni.freiburg.iig.telematik.sepia.parser.pnml.pt.AbstractPNMLPTNetParser;
import de.uni.freiburg.iig.telematik.sepia.petrinet.ptc.abstr.AbstractPTCNet;
import de.uni.freiburg.iig.telematik.sepia.petrinet.ptc.abstr.AbstractPTCNetFlowRelation;
import de.uni.freiburg.iig.telematik.sepia.petrinet.ptc.abstr.AbstractPTCNetMarking;
import de.uni.freiburg.iig.telematik.sepia.petrinet.ptc.abstr.AbstractPTCNetPlace;
import de.uni.freiburg.iig.telematik.sepia.petrinet.ptc.abstr.AbstractPTCNetTransition;
import de.uni.freiburg.iig.telematik.sepia.util.PNUtils;

/**
 * <p>
 * Abstract parser for abstract PTCost nets.
 * </p>
 *
 * @version 1.0
 * @author Julius Holderer
 * @author Adrian Lange
 *
 * @param <P> Type of Petri net places
 * @param <T> Type of Petri net transitions
 * @param <F> Type of Petri net relations
 * @param <M> Type of Petri net markings
 * @param <N> Type of the Petri net
 * @param <G> Type of the Petri net graphics
 */

public abstract class AbstractPNMLPTCNetParser<P extends AbstractPTCNetPlace<F>, T extends AbstractPTCNetTransition<F>, F extends AbstractPTCNetFlowRelation<P, T>, M extends AbstractPTCNetMarking, N extends AbstractPTCNet<P, T, F, M>, G extends AbstractPTCNetGraphics<P, T, F, M>>
		extends AbstractPNMLPTNetParser<P, T, F, M, N, G> {

	@Override
	public void parseDocument(Document pnmlDocument) throws ParserException {

		super.parseDocument(pnmlDocument);
	}

	/**
	 * Reads all transitions given in a list of DOM nodes and adds them to the
	 * {@link GraphicalPTCNet}.
	 *
	 * @throws ParserException
	 */
	@Override
	protected void readTransitions(NodeList transitionNodes) throws ParserException {
		// read and add each transition
		for (int t = 0; t < transitionNodes.getLength(); t++) {
			if (transitionNodes.item(t).getNodeType() == Node.ELEMENT_NODE) {
				Element transition = (Element) transitionNodes.item(t);
				// ID must be available in a valid net
				String transitionName = PNUtils.sanitizeElementName(transition.getAttribute("id"), "t");
				String transitionLabel = null;
				// Check if there's a label
				NodeList transitionLabels = transition.getElementsByTagName("name");
				if (transitionLabels.getLength() == 1) {
					transitionLabel = readText(transitionLabels.item(0));
					if (transitionLabel != null && transitionLabel.length() == 0) {
						transitionLabel = null;
					}
					// annotation graphics
					AnnotationGraphics transitionLabelAnnotationGraphics = readAnnotationGraphicsElement(
							(Element) transitionLabels.item(0));
					if (transitionLabelAnnotationGraphics != null) {
						graphics.getTransitionLabelAnnotationGraphics().put(transitionName,
								transitionLabelAnnotationGraphics);
					}
				}

                if (transitionLabel != null) {
                    net.addTransition(transitionName, transitionLabel);
                } else {
                    net.addTransition(transitionName);
                }
                if (readSilent(transition)) {
                    net.getTransition(transitionName).setSilent(true);
                }
                
                NodeList transitionCostList = transition.getElementsByTagName("cost");
                for (int i = 0; i < transitionCostList.getLength(); i++) {
                    // If node is element node and is direct child of the place node
                    if (transitionCostList.item(i).getNodeType() == Node.ELEMENT_NODE && transitionCostList.item(i).getParentNode().equals(transition)) {
                        Element transitionCostElement = (Element) transitionCostList.item(i);
                        Double transitionCost = readTransitionCost(transitionCostElement);
                        // add place capacity
                        if (transitionCost != null) {
                            T currentTransition = net.getTransition(transitionName);
                            currentTransition.setCost(transitionCost);
                        }
                    }
                }
                

				// read graphical information
				NodeGraphics transitionGraphics = readNodeGraphicsElement(transition);
				if (transitionGraphics != null) {
					graphics.getTransitionGraphics().put(transitionName, transitionGraphics);
				}
			}
		}
	}

    /**
     * Gets the transition cost element of a PN and returns an {@link Integer}
     * value.
     *
     * @param transitionCostElement Transition Cost element to read from
     * @return Double value representing the transition cost
     * @throws PNMLParserException
     */
    public Double readTransitionCost(Element transitionCostElement) throws PNMLParserException {
        Validate.notNull(transitionCostElement);
        return Double.parseDouble(transitionCostElement.getTextContent());
    }
    
    /**
     * Reads all places given in a list of DOM nodes and adds them to the
     * {@link AbstractGraphicalPN}.
     *
     * @throws ParserException
     */
    @Override
    protected void readPlaces(NodeList placeNodes) throws ParserException {
        // add each place
        M marking = net.getMarking();
        for (int p = 0; p < placeNodes.getLength(); p++) {
            Node placeNode = placeNodes.item(p);
            if (placeNode.getNodeType() == Node.ELEMENT_NODE) {
                Element place = (Element) placeNode;
                // ID must be available in a valid net
                String placeName = PNUtils.sanitizeElementName(place.getAttribute("id"), "p");
                String placeLabel = null;
                // Check if there's a label
                NodeList placeLabels = place.getElementsByTagName("name");
                if (placeLabels.getLength() == 1) {
                    if (readText(placeLabels.item(0)) != null) {
                        placeLabel = readText(placeLabels.item(0));
                    }
                    // annotation graphics
                    AnnotationGraphics placeAnnotationGraphics = readAnnotationGraphicsElement((Element) placeLabels.item(0));
                    if (placeAnnotationGraphics != null) {
                        graphics.getPlaceLabelAnnotationGraphics().put(placeName, placeAnnotationGraphics);
                    }
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
                            Set<TokenGraphics> tokenGraphics = new HashSet<>();
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
                
                // Read and add place Cost
                NodeList placeCostList = place.getElementsByTagName("cost");
                for (int i = 0; i < placeCostList.getLength(); i++) {
                    // If node is element node and is direct child of the place node
                    if (placeCostList.item(i).getNodeType() == Node.ELEMENT_NODE && placeCostList.item(i).getParentNode().equals(place)) {
                        Element placeCostElement = (Element) placeCostList.item(i);
                        Double placecost = readPlaceCost(placeCostElement);
                        // add place cost
                        if (placecost != null) {
                            P currentPlace = net.getPlace(placeName);
                            currentPlace.setCost(placecost);
                        }
                    }
                }
                

                // Read graphical information
                NodeGraphics placeGraphics = readNodeGraphicsElement(place);
                if (placeGraphics != null) {
                    graphics.getPlaceGraphics().put(placeName, placeGraphics);
                }
            }
        }
        net.setInitialMarking(marking);
    }
    
    /**
     * Gets the transition cost element of a PN and returns an {@link Integer}
     * value.
     *
     * @param transitionCostElement Transition Cost element to read from
     * @return Double value representing the transition cost
     * @throws PNMLParserException
     */
    public Double readPlaceCost(Element placeCostElement) throws PNMLParserException {
        Validate.notNull(placeCostElement);
        return Double.parseDouble(placeCostElement.getTextContent());
    }

}
