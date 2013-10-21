package de.uni.freiburg.iig.telematik.sepia.parser.pnml;

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
import de.uni.freiburg.iig.telematik.sepia.graphic.GraphicalCPN;
import de.uni.freiburg.iig.telematik.sepia.graphic.netgraphics.AnnotationGraphics;
import de.uni.freiburg.iig.telematik.sepia.graphic.netgraphics.ArcGraphics;
import de.uni.freiburg.iig.telematik.sepia.graphic.netgraphics.CPNGraphics;
import de.uni.freiburg.iig.telematik.sepia.graphic.netgraphics.NodeGraphics;
import de.uni.freiburg.iig.telematik.sepia.graphic.netgraphics.TokenGraphics;
import de.uni.freiburg.iig.telematik.sepia.parser.pnml.PNMLParserException.ErrorCode;
import de.uni.freiburg.iig.telematik.sepia.petrinet.cpn.CPN;
import de.uni.freiburg.iig.telematik.sepia.petrinet.cpn.CPNFlowRelation;
import de.uni.freiburg.iig.telematik.sepia.petrinet.cpn.CPNMarking;
import de.uni.freiburg.iig.telematik.sepia.petrinet.cpn.CPNPlace;
import de.uni.freiburg.iig.telematik.sepia.petrinet.cpn.CPNTransition;
import de.uni.freiburg.iig.telematik.sepia.petrinet.cpn.FiringRule;

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
public class PNMLCPNParser extends AbstractPNMLParser<CPNPlace, CPNTransition, CPNFlowRelation, CPNMarking, Multiset<String>> {

	private Map<String, Color> tokencolors = null;

	private Map<String, Map<String, PlaceFiringRules>> transitionFiringRules = new HashMap<String, Map<String, PlaceFiringRules>>();

	public GraphicalCPN parse(Document pnmlDocument) throws ParameterException, ParserException {

		net = new CPN();
		graphics = new CPNGraphics();

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

		return new GraphicalCPN(net, graphics);
	}

	/**
	 * Reads all arcs given in a list of DOM nodes and adds them to the {@link GraphicalCPN}.
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

				CPNFlowRelation flowRelation;
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
	 * Reads all places given in a list of DOM nodes and adds them to the {@link GraphicalCPN}.
	 */
	protected void readPlaces(NodeList placeNodes) throws ParameterException, ParserException {
		// add each place
		CPNMarking marking = new CPNMarking();
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
						CPNPlace currentPlace = net.getPlace(placeName);

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
	 * Reads all transitions given in a list of DOM nodes and adds them to the {@link GraphicalCPN}.
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

				// transitions have no inscription/marking
			}
		}
	}

	private void addFiringRulesToNet() throws ParameterException {
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
	public CPNGraphics getGraphics() {
		return (CPNGraphics) graphics;
	}

	@Override
	public CPN getNet() {
		return (CPN) net;
	}
}
