package de.uni.freiburg.iig.telematik.sepia.parser.pnml;

import java.awt.Color;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Vector;
import java.util.Map.Entry;
import java.util.Set;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import de.invation.code.toval.parser.ParserException;
import de.invation.code.toval.parser.XMLParserException;
import de.invation.code.toval.types.Multiset;
import de.invation.code.toval.validate.ParameterException;
import de.uni.freiburg.iig.telematik.sepia.graphic.AnnotationGraphics;
import de.uni.freiburg.iig.telematik.sepia.graphic.CPNGraphics;
import de.uni.freiburg.iig.telematik.sepia.graphic.EdgeGraphics;
import de.uni.freiburg.iig.telematik.sepia.graphic.GraphicalCPNet;
import de.uni.freiburg.iig.telematik.sepia.graphic.GraphicalPN;
import de.uni.freiburg.iig.telematik.sepia.graphic.GraphicalPTNet;
import de.uni.freiburg.iig.telematik.sepia.graphic.NodeGraphics;
import de.uni.freiburg.iig.telematik.sepia.graphic.TokenGraphics;
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

	private Map<String, Map<String, PlaceFiringRules>> transitionFiringRules = new HashMap<String, Map<String, AbstractPNMLParser<CPNPlace, CPNTransition, CPNFlowRelation, CPNMarking, Multiset<String>>.PlaceFiringRules>>();

	public GraphicalPN<CPNPlace, CPNTransition, CPNFlowRelation, CPNMarking, Multiset<String>>

	parse(Document pnmlDocument) throws ParameterException, ParserException {

		net = new CPN();
		graphics = new CPNGraphics();

		// Check if the net is defined on a single page
		NodeList netElement = pnmlDocument.getElementsByTagName("page");
		if (netElement.getLength() > 1)
			throw new PNMLParserException(ErrorCode.NOT_ON_ONE_PAGE);

		NodeList tokencolorsNodes = pnmlDocument.getElementsByTagName("tokencolors");
		if (tokencolorsNodes.getLength() > 0) {
			tokencolors = readTokenColors((Element) tokencolorsNodes.item(0));
			((CPNGraphics) graphics).setColors(tokencolors);
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

		return new GraphicalCPNet(net, graphics);
	}

	/**
	 * Reads all arcs given in a list of DOM nodes and adds them to the {@link GraphicalPTNet}.
	 */
	protected void readArcs(NodeList arcNodes) throws ParameterException, XMLParserException, PNMLParserException {

		// read and add each arc/flow relation
		for (int a = 0; a < arcNodes.getLength(); a++) {
			if (arcNodes.item(a).getNodeType() == Node.ELEMENT_NODE) {
				Element arc = (Element) arcNodes.item(a);
				// ID must be available in a valid net
				String sourceName = arc.getAttribute("source");
				String targetName = arc.getAttribute("target");

				// get inscriptions
				int inscription = 0;
				NodeList arcInscriptions = arc.getElementsByTagName("inscription");
				if (arcInscriptions.getLength() == 1) {
					String inscriptionStr = readText(arcInscriptions.item(0));
					if (inscriptionStr != null)
						inscription = Integer.parseInt(inscriptionStr);
				}
				Map<String, Integer> colorInscription = null;
				NodeList arcColorInscriptions = arc.getElementsByTagName("colorInscription");
				if (arcColorInscriptions.getLength() == 1)
					colorInscription = readColorInscription(arcColorInscriptions.item(0));

				CPNFlowRelation flowRelation;
				// if PT relation
				if (net.getPlace(sourceName) != null && net.getTransition(targetName) != null) {
					flowRelation = ((CPN) net).addFlowRelationPT(sourceName, targetName);

					// Add black tokens
					if (inscription > 0) {
						if (transitionFiringRules.containsKey(targetName) == false) {
							transitionFiringRules.put(targetName, new HashMap<String, AbstractPNMLParser<CPNPlace, CPNTransition, CPNFlowRelation, CPNMarking, Multiset<String>>.PlaceFiringRules>());
						}

						if (transitionFiringRules.get(targetName).containsKey(sourceName)) {
							transitionFiringRules.get(targetName).get(sourceName).addOutgoingColorTokens("black", inscription);
						} else {
							PlaceFiringRules tempPlaceFiringRule = new PlaceFiringRules();
							tempPlaceFiringRule.addOutgoingColorTokens("black", inscription);
							transitionFiringRules.get(targetName).put(sourceName, tempPlaceFiringRule);
						}
					}

					// Add color tokens
					if (colorInscription.size() > 0) {
						if (transitionFiringRules.containsKey(targetName) == false) {
							transitionFiringRules.put(targetName, new HashMap<String, AbstractPNMLParser<CPNPlace, CPNTransition, CPNFlowRelation, CPNMarking, Multiset<String>>.PlaceFiringRules>());
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
					flowRelation = ((CPN) net).addFlowRelationTP(sourceName, targetName);

					// Add black tokens
					if (inscription > 0) {
						if (transitionFiringRules.containsKey(sourceName) == false) {
							transitionFiringRules.put(sourceName, new HashMap<String, AbstractPNMLParser<CPNPlace, CPNTransition, CPNFlowRelation, CPNMarking, Multiset<String>>.PlaceFiringRules>());
						}

						if (transitionFiringRules.get(sourceName).containsKey(targetName)) {
							transitionFiringRules.get(sourceName).get(targetName).addIncomingColorTokens("black", inscription);
						} else {
							PlaceFiringRules tempPlaceFiringRule = new PlaceFiringRules();
							tempPlaceFiringRule.addIncomingColorTokens("black", inscription);
							transitionFiringRules.get(sourceName).put(targetName, tempPlaceFiringRule);
						}
					}

					// Add color tokens
					if (colorInscription.size() > 0) {
						if (transitionFiringRules.containsKey(sourceName) == false) {
							transitionFiringRules.put(sourceName, new HashMap<String, AbstractPNMLParser<CPNPlace, CPNTransition, CPNFlowRelation, CPNMarking, Multiset<String>>.PlaceFiringRules>());
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

				// annotation graphics for inscription
				if (arcInscriptions.getLength() == 1) {
					AnnotationGraphics edgeAnnotationGraphics = (AnnotationGraphics) readGraphics((Element) arcInscriptions.item(0));
					if (edgeAnnotationGraphics != null)
						graphics.getEdgeAnnotationGraphics().put(flowRelation, edgeAnnotationGraphics);
				}

				// annotation graphics for color inscription
				// FIXME is ignored if there's already an edge annotation graphics object for the flow relation from the inscription part
				if (arcColorInscriptions.getLength() == 1 && !graphics.getEdgeAnnotationGraphics().containsKey(flowRelation)) {
					AnnotationGraphics edgeAnnotationGraphics = (AnnotationGraphics) readGraphics((Element) arcColorInscriptions.item(0));
					if (edgeAnnotationGraphics != null)
						graphics.getEdgeAnnotationGraphics().put(flowRelation, edgeAnnotationGraphics);
				}

				// get graphical information
				EdgeGraphics arcGraphics = (EdgeGraphics) readGraphics(arc);
				if (arcGraphics != null)
					graphics.getEdgeGraphics().put(flowRelation, arcGraphics);
			}
		}
	}

	/**
	 * Reads all places given in a list of DOM nodes and adds them to the {@link GraphicalPTNet}.
	 */
	protected void readPlaces(NodeList placeNodes) throws ParameterException, XMLParserException {
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
				} else {
					placeLabel = placeName;
				}
				net.addPlace(placeName, placeLabel);

				// Read graphical information
				NodeGraphics placeGraphics = (NodeGraphics) readGraphics(place);
				if (placeGraphics != null)
					graphics.getPlaceGraphics().put(net.getPlace(placeName), placeGraphics);

				Set<TokenGraphics> tokenGraphics = new HashSet<TokenGraphics>();

				// Read initial marking of control flow tokens with graphical information
				NodeList placeInitialMarkings = place.getElementsByTagName("initialMarking");
				if (placeInitialMarkings.getLength() == 1) {
					int initialMarking = readInitialMarking(placeInitialMarkings.item(0));
					if (initialMarking > 0) {
						for (int i = 0; i < initialMarking; i++) {
							markingMultiset.add(CPN.DEFAULT_TOKEN_COLOR);
						}

						// graphics
						NodeList graphicsList = ((Element) placeInitialMarkings.item(0)).getElementsByTagName("tokenposition");
						if (graphicsList.getLength() > 0) {
							for (int tp = 0; tp < graphicsList.getLength(); tp++) {
								Element tokenPos = (Element) graphicsList.item(tp);
								TokenGraphics tokenGraphic = new TokenGraphics();
								tokenGraphic.setTokenposition(readTokenPosition(tokenPos));
								tokenGraphics.add(tokenGraphic);
							}
						}
					}
				}

				// Read initial marking of color tokens with graphical information
				NodeList placeInitialColorMarkings = place.getElementsByTagName("initialColorMarking");
				if (placeInitialColorMarkings.getLength() == 1) {
					Map<String, Integer> initialColorMarking = readInitialColorMarking(placeInitialColorMarkings.item(0));
					if (initialColorMarking != null) {
						Iterator<Entry<String, Integer>> iterator = initialColorMarking.entrySet().iterator();
						Vector<String> colorStrings = new Vector<String>(initialColorMarking.size());
						while (iterator.hasNext()) {
							Entry<String, Integer> color = iterator.next();
							for (int c = 0; c < color.getValue(); c++) {
								markingMultiset.add(color.getKey());
								colorStrings.add(color.getKey());
							}
							iterator.remove(); // avoid ConcurrentModificationException
						}

						// graphics
						NodeList graphicsList = ((Element) placeInitialColorMarkings.item(0)).getElementsByTagName("tokenposition");
						if (graphicsList.getLength() > 0) {
							for (int tp = 0; tp < graphicsList.getLength(); tp++) {
								Element tokenPos = (Element) graphicsList.item(tp);
								TokenGraphics tokenGraphic = new TokenGraphics();
								tokenGraphic.setTokenposition(readTokenPosition(tokenPos));
								if (tp < colorStrings.size() && tokencolors.containsKey(colorStrings.get(tp))) {
									tokenGraphic.setColorName(colorStrings.get(tp));
								}
								tokenGraphics.add(tokenGraphic);
							}
						}
					}
				}

				// If there is a marking, add the multiset
				if (markingMultiset.size() > 0)
					marking.set(placeName, markingMultiset);
				//
				if (!tokenGraphics.isEmpty())
					graphics.getTokenGraphics().put(net.getPlace(placeName), tokenGraphics);
			}
		}
		net.setInitialMarking(marking);
	}

	/**
	 * Reads all transitions given in a list of DOM nodes and adds them to the {@link GraphicalPTNet}.
	 */
	protected void readTransitions(NodeList transitionNodes) throws XMLParserException, ParameterException {
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
				}
				if (transitionLabel != null)
					net.addTransition(transitionName, transitionLabel);
				else
					net.addTransition(transitionName);

				// read graphical information
				NodeGraphics transitionGraphics = (NodeGraphics) readGraphics(transition);
				if (transitionGraphics != null)
					graphics.getTransitionGraphics().put(net.getTransition(transitionName), transitionGraphics);

				// transitions have no inscription/marking
			}
		}
	}

	private void addFiringRulesToNet() throws ParameterException {
		Iterator<Entry<String, Map<String, PlaceFiringRules>>> transitionFiringRulesIterator = transitionFiringRules.entrySet().iterator();
		while (transitionFiringRulesIterator.hasNext()) {
			Entry<String, Map<String, PlaceFiringRules>> placeFiringRules = transitionFiringRulesIterator.next();

			FiringRule firingRule = new FiringRule();

			Iterator<Entry<String, PlaceFiringRules>> placeFiringRulesIterator = placeFiringRules.getValue().entrySet().iterator();
			while (placeFiringRulesIterator.hasNext()) {
				Entry<String, PlaceFiringRules> placeRule = placeFiringRulesIterator.next();

				if (placeRule.getValue().getOutgoingColorTokens().size() > 0)
					firingRule.addRequirement(placeRule.getKey(), placeRule.getValue().getOutgoingColorTokens());
				if (placeRule.getValue().getIncomingColorTokens().size() > 0)
					firingRule.addProduction(placeRule.getKey(), placeRule.getValue().getIncomingColorTokens());

				placeFiringRulesIterator.remove(); // avoid ConcurrentModificationException
			}

			if (firingRule.containsRequirements() || firingRule.containsProductions())
				((CPN) net).addFiringRule(placeFiringRules.getKey(), firingRule);

			transitionFiringRulesIterator.remove(); // avoid ConcurrentModificationException
		}
	}
}
