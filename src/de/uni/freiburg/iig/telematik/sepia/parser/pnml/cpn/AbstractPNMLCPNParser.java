package de.uni.freiburg.iig.telematik.sepia.parser.pnml.cpn;

import java.awt.Color;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.Vector;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import de.invation.code.toval.parser.ParserException;
import de.invation.code.toval.types.Multiset;
import de.invation.code.toval.validate.ParameterException;
import de.invation.code.toval.validate.Validate;
import de.uni.freiburg.iig.telematik.sepia.graphic.netgraphics.AbstractCPNGraphics;
import de.uni.freiburg.iig.telematik.sepia.graphic.netgraphics.AnnotationGraphics;
import de.uni.freiburg.iig.telematik.sepia.graphic.netgraphics.ArcGraphics;
import de.uni.freiburg.iig.telematik.sepia.graphic.netgraphics.NodeGraphics;
import de.uni.freiburg.iig.telematik.sepia.graphic.netgraphics.TokenGraphics;
import de.uni.freiburg.iig.telematik.sepia.parser.pnml.AbstractPNMLParser;
import de.uni.freiburg.iig.telematik.sepia.parser.pnml.PNMLParserException;
import de.uni.freiburg.iig.telematik.sepia.parser.pnml.PNMLParserException.ErrorCode;
import de.uni.freiburg.iig.telematik.sepia.parser.pnml.PlaceFiringRules;
import de.uni.freiburg.iig.telematik.sepia.petrinet.cpn.CPN;
import de.uni.freiburg.iig.telematik.sepia.petrinet.cpn.FiringRule;
import de.uni.freiburg.iig.telematik.sepia.petrinet.cpn.abstr.AbstractCPN;
import de.uni.freiburg.iig.telematik.sepia.petrinet.cpn.abstr.AbstractCPNFlowRelation;
import de.uni.freiburg.iig.telematik.sepia.petrinet.cpn.abstr.AbstractCPNMarking;
import de.uni.freiburg.iig.telematik.sepia.petrinet.cpn.abstr.AbstractCPNPlace;
import de.uni.freiburg.iig.telematik.sepia.petrinet.cpn.abstr.AbstractCPNTransition;

/**
 * <p>
 * Abstract parser for abstract CPNs.
 * </p>
 * 
 * @author Adrian Lange
 */
public abstract class AbstractPNMLCPNParser<P extends AbstractCPNPlace<F>,
											T extends AbstractCPNTransition<F>,
											F extends AbstractCPNFlowRelation<P, T>,
											M extends AbstractCPNMarking,
											N extends AbstractCPN<P, T, F, M>,
											G extends AbstractCPNGraphics<P, T, F, M>>

	extends AbstractPNMLParser<P, T, F, M, Multiset<String>, N, G> {

	private Map<String, Color> tokencolors = null;

	private Map<String, Map<String, PlaceFiringRules>> transitionFiringRules = new HashMap<String, Map<String, PlaceFiringRules>>();

	public void parseDocument(Document pnmlDocument) throws ParameterException, ParserException {

		// Check if the net is defined on a single page
		NodeList pageNodes = pnmlDocument.getElementsByTagName("page");
		if (pageNodes.getLength() > 1)
			throw new PNMLParserException(ErrorCode.NOT_ON_ONE_PAGE);

		NodeList tokencolorsNodes = pnmlDocument.getElementsByTagName("tokencolors");
		for (int i = 0; i < tokencolorsNodes.getLength(); i++) {
			if (tokencolorsNodes.item(i).getNodeType() == Node.ELEMENT_NODE && tokencolorsNodes.item(i).getParentNode().getNodeName().equals("net")) {
				tokencolors = readTokenColors((Element) tokencolorsNodes.item(i));
				getGraphics().setColors(tokencolors);
			}
		}

		// Read places and transitions
		NodeList placeNodes = pnmlDocument.getElementsByTagName("place");
		readPlaces(placeNodes);
		NodeList transitionNodes = pnmlDocument.getElementsByTagName("transition");
		readTransitions(transitionNodes);
		// Read arcs
		NodeList arcNodes = pnmlDocument.getElementsByTagName("arc");
		readArcs(arcNodes);

		addFiringRulesToNet();

		// Read net ID as name
		String netName = readNetName(pnmlDocument);
		if (netName != null)
			net.setName(netName);
	}

	/**
	 * Reads all arcs given in a list of DOM nodes and adds them to the {@link Abstract}.
	 */
	protected void readArcs(NodeList arcNodes) throws ParameterException, ParserException {

		// read and add each arc/flow relation
		for (int a = 0; a < arcNodes.getLength(); a++) {
			if (arcNodes.item(a).getNodeType() == Node.ELEMENT_NODE) {
				Element arc = (Element) arcNodes.item(a);
				// ID must be available in a valid net
				String sourceName = arc.getAttribute("source");
				String targetName = arc.getAttribute("target");

				// get inscriptions
				int inscription = 0;
				Map<String, Integer> colorInscription = null;
				NodeList arcInscriptions = arc.getElementsByTagName("inscription");
				if (arcInscriptions.getLength() == 1) {
					Element inscriptionElement = (Element) arcInscriptions.item(0);
					String inscriptionStr = readText(inscriptionElement);
					if (inscriptionStr != null)
						inscription = Integer.parseInt(inscriptionStr);
					NodeList arcColorInscriptions = inscriptionElement.getElementsByTagName("colors");
					if (arcColorInscriptions.getLength() == 1)
						colorInscription = readColorInscription(arcColorInscriptions.item(0));
				}

				F flowRelation;
				// if PT relation
				if (net.getPlace(sourceName) != null && net.getTransition(targetName) != null) {
					flowRelation = getNet().addFlowRelationPT(sourceName, targetName);

					// Add black tokens
					if (inscription > 0) {
						if (transitionFiringRules.containsKey(targetName) == false) {
							transitionFiringRules.put(targetName, new HashMap<String, PlaceFiringRules>());
						}

						if (transitionFiringRules.get(targetName).containsKey(sourceName)) {
							transitionFiringRules.get(targetName).get(sourceName).addOutgoingColorTokens(CPN.DEFAULT_TOKEN_COLOR, inscription);
						} else {
							PlaceFiringRules tempPlaceFiringRule = new PlaceFiringRules();
							tempPlaceFiringRule.addOutgoingColorTokens(CPN.DEFAULT_TOKEN_COLOR, inscription);
							transitionFiringRules.get(targetName).put(sourceName, tempPlaceFiringRule);
						}
					}

					// Add color tokens
					if (colorInscription != null && colorInscription.size() > 0) {
						if (transitionFiringRules.containsKey(targetName) == false) {
							transitionFiringRules.put(targetName, new HashMap<String, PlaceFiringRules>());
						}

						if (transitionFiringRules.get(targetName).containsKey(sourceName)) {
							for (Map.Entry<String, Integer> color : colorInscription.entrySet()) {
								if (color.getValue() > 0) {
									transitionFiringRules.get(targetName).get(sourceName).addOutgoingColorTokens(color.getKey(), color.getValue());
								}
							}
						} else {
							PlaceFiringRules tempPlaceFiringRule = new PlaceFiringRules();
							for (Map.Entry<String, Integer> color : colorInscription.entrySet()) {
								if (color.getValue() > 0) {
									tempPlaceFiringRule.addOutgoingColorTokens(color.getKey(), color.getValue());
								}
							}
							transitionFiringRules.get(targetName).put(sourceName, tempPlaceFiringRule);
						}
					}
				}
				// if TP relation
				else if (net.getPlace(targetName) != null && net.getTransition(sourceName) != null) {
					flowRelation = getNet().addFlowRelationTP(sourceName, targetName);

					// Add black tokens
					if (inscription > 0) {
						if (transitionFiringRules.containsKey(sourceName) == false) {
							transitionFiringRules.put(sourceName, new HashMap<String, PlaceFiringRules>());
						}

						if (transitionFiringRules.get(sourceName).containsKey(targetName)) {
							transitionFiringRules.get(sourceName).get(targetName).addIncomingColorTokens(CPN.DEFAULT_TOKEN_COLOR, inscription);
						} else {
							PlaceFiringRules tempPlaceFiringRule = new PlaceFiringRules();
							tempPlaceFiringRule.addIncomingColorTokens(CPN.DEFAULT_TOKEN_COLOR, inscription);
							transitionFiringRules.get(sourceName).put(targetName, tempPlaceFiringRule);
						}
					}

					// Add color tokens
					if (colorInscription != null && colorInscription.size() > 0) {
						if (transitionFiringRules.containsKey(sourceName) == false) {
							transitionFiringRules.put(sourceName, new HashMap<String, PlaceFiringRules>());
						}

						if (transitionFiringRules.get(sourceName).containsKey(targetName)) {
							for (Map.Entry<String, Integer> color : colorInscription.entrySet()) {
								if (color.getValue() > 0) {
									transitionFiringRules.get(sourceName).get(targetName).addIncomingColorTokens(color.getKey(), color.getValue());
								}
							}
						} else {
							PlaceFiringRules tempPlaceFiringRule = new PlaceFiringRules();
							for (Map.Entry<String, Integer> color : colorInscription.entrySet()) {
								if (color.getValue() > 0) {
									tempPlaceFiringRule.addIncomingColorTokens(color.getKey(), color.getValue());
								}
							}
							transitionFiringRules.get(sourceName).put(targetName, tempPlaceFiringRule);
						}
					}
				} else {
					throw new PNMLParserException(ErrorCode.INVALID_FLOW_RELATION, "Couldn't determine flow relation between \"" + sourceName + "\" and \"" + targetName + "\".");
				}

				// Check if there's a label
				String arcName = arc.getAttribute("id");
				if (arcName != null && arcName.length() > 0)
					flowRelation.setName(arcName);

				// annotation graphics for inscription
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
	 * Reads all places given in a list of DOM nodes and adds them to the {@link AbstractCPNGraphics}.
	 */
	protected void readPlaces(NodeList placeNodes) throws ParameterException, ParserException {
		// add each place
		M marking = net.getMarking();
		for (int p = 0; p < placeNodes.getLength(); p++) {
			Node placeNode = placeNodes.item(p);
			if (placeNode.getNodeType() == Node.ELEMENT_NODE) {
				Multiset<String> markingMultiset = new Multiset<String>();
				Element place = (Element) placeNode;
				// ID must be available in a valid net
				String placeName = place.getAttribute("id");
				String placeLabel = null;
				// Check if there's a label
				NodeList placeLabels = place.getElementsByTagName("name");
				if (placeLabels.getLength() == 1) {
					if (readText(placeLabels.item(0)) != null)
						placeLabel = readText(placeLabels.item(0));
					// annotation graphics
					AnnotationGraphics placeAnnotationGraphics = readAnnotationGraphicsElement((Element) placeLabels.item(0));
					if (placeAnnotationGraphics != null)
						graphics.getPlaceLabelAnnotationGraphics().put(placeName, placeAnnotationGraphics);
				} else {
					placeLabel = placeName;
				}
				net.addPlace(placeName, placeLabel);

				Set<TokenGraphics> tokenGraphics = new HashSet<TokenGraphics>();

				// Read initial marking of control flow tokens with graphical information
				NodeList placeInitialMarkings = place.getElementsByTagName("initialMarking");
				if (placeInitialMarkings.getLength() == 1) {
					Element initialMarkingElement = (Element) placeInitialMarkings.item(0);
					int initialMarking = readInitialMarking(initialMarkingElement);
					if (initialMarking < 0) {
						throw new PNMLParserException(ErrorCode.VALIDATION_FAILED, "Place initial markings must not be a negative number.");
					} else if (initialMarking > 0) {
						for (int i = 0; i < initialMarking; i++) {
							markingMultiset.add(CPN.DEFAULT_TOKEN_COLOR);
						}

						// graphics
						NodeList graphicsList = initialMarkingElement.getElementsByTagName("tokenposition");
						if (graphicsList.getLength() > 0) {
							for (int tp = 0; tp < graphicsList.getLength(); tp++) {
								Element tokenPos = (Element) graphicsList.item(tp);
								TokenGraphics tokenGraphic = new TokenGraphics();
								tokenGraphic.setTokenposition(readTokenPosition(tokenPos));
								tokenGraphics.add(tokenGraphic);
							}
						}
					}

					// Read initial marking of color tokens with graphical information
					NodeList placeInitialColorMarkings = initialMarkingElement.getElementsByTagName("colors");
					if (placeInitialColorMarkings.getLength() == 1) {
						Map<String, Integer> initialColorMarking = readInitialColorMarking(placeInitialColorMarkings.item(0));
						if (initialColorMarking != null) {
							Vector<String> colorStrings = new Vector<String>(initialColorMarking.size());
							for (Entry<String, Integer> color : initialColorMarking.entrySet()) {
								for (int c = 0; c < color.getValue(); c++) {
									markingMultiset.add(color.getKey());
									colorStrings.add(color.getKey());
								}
							}
						}
					}
				}

				// If there is a marking, add the multiset
				if (markingMultiset.size() > 0)
					marking.set(placeName, markingMultiset);

				if (!tokenGraphics.isEmpty())
					graphics.getTokenGraphics().put(placeName, tokenGraphics);

				// Read and add place capacities
				NodeList placeCapacitiesList = place.getElementsByTagName("capacities");
				for (int i = 0; i < placeCapacitiesList.getLength(); i++) {
					// If node is element node and is direct child of the place node
					if (placeCapacitiesList.item(i).getNodeType() == Node.ELEMENT_NODE && placeCapacitiesList.item(i).getParentNode().equals(place)) {
						Element placeCapacitiesElement = (Element) placeCapacitiesList.item(i);
						P currentPlace = net.getPlace(placeName);

						Map<String, Integer> placeCapacities = readPlaceColorCapacities(placeCapacitiesElement);
						// add place color capacities
						if (placeCapacities != null) {
							for (Entry<String, Integer> c : placeCapacities.entrySet()) {
								currentPlace.setColorCapacity(c.getKey(), c.getValue());
							}
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
	 * Reads all transitions given in a list of DOM nodes and adds them to the {@link AbstractCPNGraphics}.
	 */
	protected void readTransitions(NodeList transitionNodes) throws ParameterException, ParserException {
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
					transitionLabel = readText(transitionLabels.item(0));
					if (transitionLabel != null && transitionLabel.length() == 0)
						transitionLabel = null;
					// annotation graphics
					AnnotationGraphics transitionLabelAnnotationGraphics = readAnnotationGraphicsElement((Element) transitionLabels.item(0));
					if (transitionLabelAnnotationGraphics != null)
						graphics.getTransitionLabelAnnotationGraphics().put(transitionName, transitionLabelAnnotationGraphics);
				}
				if (transitionLabel != null)
					net.addTransition(transitionName, transitionLabel);
				else
					net.addTransition(transitionName);

				// read graphical information
				NodeGraphics transitionGraphics = readNodeGraphicsElement(transition);
				if (transitionGraphics != null)
					graphics.getTransitionGraphics().put(transitionName, transitionGraphics);
			}
		}
	}

	protected void addFiringRulesToNet() throws ParameterException {
		for (Map.Entry<String, Map<String, PlaceFiringRules>> placeFiringRules : transitionFiringRules.entrySet()) {
			FiringRule firingRule = new FiringRule();
	
			for (Entry<String, PlaceFiringRules> placeRule : placeFiringRules.getValue().entrySet()) {
				if (placeRule.getValue().getOutgoingColorTokens().size() > 0)
					firingRule.addRequirement(placeRule.getKey(), placeRule.getValue().getOutgoingColorTokens());
				if (placeRule.getValue().getIncomingColorTokens().size() > 0)
					firingRule.addProduction(placeRule.getKey(), placeRule.getValue().getIncomingColorTokens());
			}
	
			if (firingRule.containsRequirements() || firingRule.containsProductions())
				getNet().addFiringRule(placeFiringRules.getKey(), firingRule);
		}
	}

	@Override
	public G getGraphics() {
		return (G) graphics;
	}

	@Override
	public N getNet() {
		return (N) net;
	}

	/**
	 * Reads an initial color marking tag and returns its values as {@link Map}.
	 */
	public Map<String, Integer> readColorInscription(Node colorInscriptionNode) throws ParameterException {
		Validate.notNull(colorInscriptionNode);

		Element initialColorMarkingElement = (Element) colorInscriptionNode;
		NodeList colorNodes = initialColorMarkingElement.getElementsByTagName("color");
		Map<String, Integer> colorInscription = new HashMap<String, Integer>();

		if (colorNodes.getLength() > 0) {
			for (int c = 0; c < colorNodes.getLength(); c++) {
				if (colorNodes.item(c).getNodeType() == Node.ELEMENT_NODE) {
					String color = colorNodes.item(c).getTextContent();
					if (colorInscription.containsKey(color))
						colorInscription.put(color, colorInscription.get(color) + 1);
					else
						colorInscription.put(color, 1);
				}
			}
		}

		if (colorInscription.isEmpty())
			return null;
		else
			return colorInscription;
	}

	/**
	 * Reads an initial color marking tag and returns its values as {@link Map}.
	 */
	public Map<String, Integer> readInitialColorMarking(Node initialColorMarkingNode) throws ParameterException {
		Validate.notNull(initialColorMarkingNode);

		Element initialColorMarkingElement = (Element) initialColorMarkingNode;
		NodeList colorNodes = initialColorMarkingElement.getElementsByTagName("color");
		Map<String, Integer> initialColorMarking = new HashMap<String, Integer>();

		if (colorNodes.getLength() > 0) {
			for (int c = 0; c < colorNodes.getLength(); c++) {
				if (colorNodes.item(c).getNodeType() == Node.ELEMENT_NODE) {
					String color = colorNodes.item(c).getTextContent();
					if (initialColorMarking.containsKey(color))
						initialColorMarking.put(color, initialColorMarking.get(color) + 1);
					else
						initialColorMarking.put(color, 1);
				}
			}
		}

		if (initialColorMarking.isEmpty())
			return null;
		else
			return initialColorMarking;
	}

	/**
	 * Gets the place color capacities element of a CPN, CWN, or IFNet and returns a {@link Map} containing all capacity values for the specific token color name.
	 */
	public Map<String, Integer> readPlaceColorCapacities(Element placeCapacitiesElement) throws ParameterException, PNMLParserException {
		Validate.notNull(placeCapacitiesElement);

		Map<String, Integer> placeCapacities = new HashMap<String, Integer>();

		NodeList placeCapacitiesList = placeCapacitiesElement.getElementsByTagName("colorcapacity");
		for (int i = 0; i < placeCapacitiesList.getLength(); i++) {
			Element placeCapacityElement = (Element) placeCapacitiesList.item(i);
			if (placeCapacityElement.getNodeType() == Node.ELEMENT_NODE && placeCapacityElement.getParentNode().equals(placeCapacitiesElement)) {

				NodeList colorNameList = placeCapacityElement.getElementsByTagName("color");
				if (colorNameList.getLength() == 0) // take first occurrence
					throw new PNMLParserException(ErrorCode.VALIDATION_FAILED, "No color name element specified.");
				String colorName = ((Element) colorNameList.item(0)).getTextContent();

				if (colorName.length() == 0)
					throw new PNMLParserException(ErrorCode.VALIDATION_FAILED, "Color token name must at least have a length of one.");

				NodeList capacityList = placeCapacityElement.getElementsByTagName("capacity");
				if (capacityList.getLength() == 0) // take first occurrence
					throw new PNMLParserException(ErrorCode.VALIDATION_FAILED, "No capacity element specified.");
				int capacity = Integer.parseInt(((Element) capacityList.item(0)).getTextContent());

				if (capacity < 1)
					throw new PNMLParserException(ErrorCode.VALIDATION_FAILED, "Capacity must be 1 or bigger.");

				if (placeCapacities.containsKey(colorName) == false)
					placeCapacities.put(colorName, capacity);
				else if (placeCapacities.get(colorName) == capacity) {
					// do nothing
				} else
					throw new PNMLParserException(ErrorCode.VALIDATION_FAILED, "There are different capacity assignments defined for the same place and token color name \"" + colorName + "\": " + capacity + " and " + placeCapacities.get(colorName) + ".");
			}
		}

		if (placeCapacities.size() > 0)
			return placeCapacities;
		else
			return null;
	}

	/**
	 * Gets the tokencolors element of a CPN, CWN, or IFNet and returns a {@link Map} containing all color values for the specific token color names.
	 */
	public Map<String, Color> readTokenColors(Element tokenColorsElement) throws ParameterException, PNMLParserException {
		Validate.notNull(tokenColorsElement);

		Map<String, Color> tokenColors = new HashMap<String, Color>();

		NodeList tokenColorList = tokenColorsElement.getElementsByTagName("tokencolor");
		for (int i = 0; i < tokenColorList.getLength(); i++) {
			Element tokenColorElement = (Element) tokenColorList.item(i);
			if (tokenColorElement.getNodeType() == Node.ELEMENT_NODE) {

				NodeList colorNameList = tokenColorElement.getElementsByTagName("color");
				if (colorNameList.getLength() != 1)
					throw new PNMLParserException(ErrorCode.VALIDATION_FAILED, "No color name element specified.");
				String colorName = ((Element) colorNameList.item(0)).getTextContent();

				NodeList rgbColorList = tokenColorElement.getElementsByTagName("rgbcolor");
				if (rgbColorList.getLength() != 1)
					throw new PNMLParserException(ErrorCode.VALIDATION_FAILED, "No RGB color element specified.");
				Element rgbColor = (Element) rgbColorList.item(0);

				int red = 0;
				int green = 0;
				int blue = 0;
				NodeList redElements = rgbColor.getElementsByTagName("r");
				if (redElements.getLength() == 1)
					red = Integer.parseInt(((Element) redElements.item(0)).getTextContent());
				NodeList greenElements = rgbColor.getElementsByTagName("g");
				if (greenElements.getLength() == 1)
					green = Integer.parseInt(((Element) greenElements.item(0)).getTextContent());
				NodeList blueElements = rgbColor.getElementsByTagName("b");
				if (blueElements.getLength() == 1)
					blue = Integer.parseInt(((Element) blueElements.item(0)).getTextContent());
				Color color = new Color(red, green, blue);
				tokenColors.put(colorName, color);
			}
		}

		return tokenColors;
	}
}
