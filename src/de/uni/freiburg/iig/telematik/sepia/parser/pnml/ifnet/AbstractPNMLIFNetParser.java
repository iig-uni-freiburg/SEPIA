package de.uni.freiburg.iig.telematik.sepia.parser.pnml.ifnet;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Map.Entry;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import de.invation.code.toval.parser.ParserException;
import de.invation.code.toval.validate.Validate;
import de.uni.freiburg.iig.telematik.sepia.graphic.GraphicalIFNet;
import de.uni.freiburg.iig.telematik.sepia.graphic.netgraphics.AbstractIFNetGraphics;
import de.uni.freiburg.iig.telematik.sepia.graphic.netgraphics.AnnotationGraphics;
import de.uni.freiburg.iig.telematik.sepia.graphic.netgraphics.NodeGraphics;
import de.uni.freiburg.iig.telematik.sepia.graphic.netgraphics.attributes.Position;
import de.uni.freiburg.iig.telematik.sepia.mg.ifnet.AbstractIFNetMarkingGraphRelation;
import de.uni.freiburg.iig.telematik.sepia.mg.ifnet.AbstractIFNetMarkingGraphState;
import de.uni.freiburg.iig.telematik.sepia.parser.pnml.PNMLParserException;
import de.uni.freiburg.iig.telematik.sepia.parser.pnml.PNMLParserException.ErrorCode;
import de.uni.freiburg.iig.telematik.sepia.parser.pnml.cwn.AbstractPNMLCWNParser;
import de.uni.freiburg.iig.telematik.sepia.petrinet.ifnet.RegularIFNetTransition;
import de.uni.freiburg.iig.telematik.sepia.petrinet.ifnet.abstr.AbstractDeclassificationTransition;
import de.uni.freiburg.iig.telematik.sepia.petrinet.ifnet.abstr.AbstractIFNet;
import de.uni.freiburg.iig.telematik.sepia.petrinet.ifnet.abstr.AbstractIFNetFlowRelation;
import de.uni.freiburg.iig.telematik.sepia.petrinet.ifnet.abstr.AbstractIFNetMarking;
import de.uni.freiburg.iig.telematik.sepia.petrinet.ifnet.abstr.AbstractIFNetPlace;
import de.uni.freiburg.iig.telematik.sepia.petrinet.ifnet.abstr.AbstractIFNetTransition;
import de.uni.freiburg.iig.telematik.sepia.petrinet.ifnet.abstr.AbstractRegularIFNetTransition;
import de.uni.freiburg.iig.telematik.sepia.petrinet.ifnet.concepts.AccessMode;

/**
 * <p>
 * Parser for IF-nets.
 * </p>
 * 
 * @author Adrian Lange
 */
public abstract class AbstractPNMLIFNetParser<P extends AbstractIFNetPlace<F>, 
											  T extends AbstractIFNetTransition<F>, 
											  F extends AbstractIFNetFlowRelation<P, T>, 
											  M extends AbstractIFNetMarking, 
											  R extends AbstractRegularIFNetTransition<F>, 
											  D extends AbstractDeclassificationTransition<F>,
											  X extends AbstractIFNetMarkingGraphState<M>,
									   		  Y extends AbstractIFNetMarkingGraphRelation<M, X>,
											  N extends AbstractIFNet<P, T, F, M, R, D, X, Y>, 
											  G extends AbstractIFNetGraphics<P, T, F, M>>

extends AbstractPNMLCWNParser<P, T, F, M, X, Y, N, G> {

	@Override
	public void parseDocument(Document pnmlDocument) throws ParserException {

		super.parseDocument(pnmlDocument);

		// read positions of the classification annotations for token labels and clearances
		NodeList classificationPositionsNodes = pnmlDocument.getElementsByTagName("classificationpositions");
		if (classificationPositionsNodes.getLength() > 0) {
			Element classificationPositionsElement = (Element) classificationPositionsNodes.item(0);
			// read clearances annotation position
			NodeList clearancesList = classificationPositionsElement.getElementsByTagName("clearances");
			if (clearancesList.getLength() > 0) {
				Element clearancesElement = (Element) clearancesList.item(0);
				NodeList clearancesPositionList = clearancesElement.getElementsByTagName("position");
				if (clearancesPositionList.getLength() > 0) {
					Position clearancesPosition = readPosition((Element) clearancesPositionList.item(0));
					if (clearancesPosition != null)
						getGraphics().setClearancesPosition(clearancesPosition);
				}
			}
			// read token labels annotation position
			NodeList tokenLabelsList = classificationPositionsElement.getElementsByTagName("tokenlabels");
			if (tokenLabelsList.getLength() > 0) {
				Element tokenLabelsElement = (Element) tokenLabelsList.item(0);
				NodeList tokenLabelsPositionList = tokenLabelsElement.getElementsByTagName("position");
				if (tokenLabelsPositionList.getLength() > 0) {
					Position tokenLabelsPosition = readPosition((Element) tokenLabelsPositionList.item(0));
					if (tokenLabelsPosition != null)
						getGraphics().setTokenLabelsPosition(tokenLabelsPosition);
				}
			}
		}
	}

	/**
	 * Reads all transitions given in a list of DOM nodes and adds them to the {@link GraphicalIFNet}.
	 */
	@Override
	protected void readTransitions(NodeList transitionNodes) throws ParserException {
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

				N ifnet = net;

				// get transition type
				String transitionType = readTransitionType(transition);
				if (!transitionType.equals("regular") && !transitionType.equals("declassification"))
					throw new PNMLParserException(ErrorCode.VALIDATION_FAILED, "Couldn't determine the type of the transition " + transitionName + ".");
				if (transitionLabel != null) {
					if (transitionType.equals("regular"))
						ifnet.addTransition(transitionName, transitionLabel);
					else if (transitionType.equals("declassification"))
						ifnet.addDeclassificationTransition(transitionName, transitionLabel);
				} else {
					if (transitionType.equals("regular"))
						ifnet.addTransition(transitionName);
					else if (transitionType.equals("declassification"))
						ifnet.addDeclassificationTransition(transitionName);
				}

				if (readSilent(transition)) {
					ifnet.getTransition(transitionName).setSilent(true);
				}

				// read access modes

				// ugly unbounded wildcard as work-around for bug JDK-6932571
				Object transitionObject = ifnet.getTransition(transitionName);
				if (transitionObject instanceof RegularIFNetTransition) {
					NodeList accessFunctionsNodes = transition.getElementsByTagName("accessfunctions");
					if (accessFunctionsNodes.getLength() > 0) {
						if (accessFunctionsNodes.item(0).getNodeType() == Node.ELEMENT_NODE && accessFunctionsNodes.item(0).getParentNode().equals(transition)) {
							Element accessFunctionsElement = (Element) accessFunctionsNodes.item(0);
							Map<String, Collection<AccessMode>> accessFunctions = readAccessFunctions(accessFunctionsElement);
							if (accessFunctions != null) {
								// get transition and add access functions
								RegularIFNetTransition currentTransition = (RegularIFNetTransition) transitionObject;
								Validate.notNull(currentTransition);

								for (Entry<String, Collection<AccessMode>> accessFunction : accessFunctions.entrySet()) {
									String color = accessFunction.getKey();
									Collection<AccessMode> accessModes = accessFunction.getValue();
									currentTransition.addAccessMode(color, accessModes);
								}

								// read access function graphics
								AnnotationGraphics accessFunctionsGraphics = readAnnotationGraphicsElement(accessFunctionsElement);
								if (accessFunctionsGraphics != null)
									getGraphics().getAccessFunctionGraphics().put(currentTransition.getName(), accessFunctionsGraphics);
							}
						}
					}
				}

				// read subject graphics
				NodeList subjectgraphicsList = transition.getElementsByTagName("subjectgraphics");
				if (subjectgraphicsList.getLength() > 0) {
					// read graphics
					AnnotationGraphics subjectgraphicsGraphics = readAnnotationGraphicsElement((Element) subjectgraphicsList.item(0));
					if (subjectgraphicsGraphics != null)
						getGraphics().getSubjectGraphics().put(transitionName, subjectgraphicsGraphics);
				}

				// read graphical information
				NodeGraphics transitionGraphics = readNodeGraphicsElement(transition);
				if (transitionGraphics != null)
					graphics.getTransitionGraphics().put(transitionName, transitionGraphics);
			}
		}
	}

	/**
	 * Reads the access functions of a transition in an IF-net and returns a Map<tokenColorName, Map<accessmode, boolean>>.
	 */
	public Map<String, Collection<AccessMode>> readAccessFunctions(Element accessFunctionsElement) {
		Validate.notNull(accessFunctionsElement);

		Map<String, Collection<AccessMode>> accessFunctions = new HashMap<String, Collection<AccessMode>>();

		// iterate through all access functions
		NodeList accessFunctionNodes = accessFunctionsElement.getElementsByTagName("accessfunction");
		for (int af = 0; af < accessFunctionNodes.getLength(); af++) {
			if (accessFunctionNodes.item(af).getNodeType() == Node.ELEMENT_NODE && accessFunctionNodes.item(af).getParentNode().equals(accessFunctionsElement)) {
				Element accessFunctionElement = (Element) accessFunctionNodes.item(af);

				// read the color element and create the access function or ignore the access function if the color can't be read
				NodeList colorNodes = accessFunctionElement.getElementsByTagName("color");
				if (colorNodes.getLength() > 0) {
					Element colorElement = (Element) colorNodes.item(0);
					if (colorElement.getTextContent().length() > 0) {
						String color = colorElement.getTextContent();
						Collection<AccessMode> accessModes = new HashSet<AccessMode>();

						// read access modes and write set not listed modes to false
						NodeList accessModesNodes = accessFunctionElement.getElementsByTagName("accessmodes");
						if (accessModesNodes.getLength() > 0) {
							Element accessModesElement = (Element) accessModesNodes.item(0);
							if (readAccessMode(accessModesElement.getElementsByTagName("read")))
								accessModes.add(AccessMode.READ);
							if (readAccessMode(accessModesElement.getElementsByTagName("create")))
								accessModes.add(AccessMode.CREATE);
							if (readAccessMode(accessModesElement.getElementsByTagName("write")))
								accessModes.add(AccessMode.WRITE);
							if (readAccessMode(accessModesElement.getElementsByTagName("delete")))
								accessModes.add(AccessMode.DELETE);
						}

						// add access function
						accessFunctions.put(color, accessModes);
					}
				}
			}
		}

		// return null if there are no access functions
		if (accessFunctions.isEmpty())
			return null;
		else
			return accessFunctions;
	}

	/**
	 * Returns a boolean value for the given access mode nodes.
	 */
	private boolean readAccessMode(NodeList accessModeNodes) {
		if (accessModeNodes.getLength() > 0) {
			Element accessModeElement = (Element) accessModeNodes.item(0);
			if (accessModeElement.getTextContent().equals("true"))
				return true;
		}
		return false;
	}

	/**
	 * Reads the type of a transition of an IF-net. If there's no transition type, it returns the type "regular".
	 */
	public String readTransitionType(Element transitionElement) {
		Validate.notNull(transitionElement);

		NodeList transitionTypeNodes = transitionElement.getElementsByTagName("transitiontype");
		if (transitionTypeNodes.getLength() > 0) {
			// Iterate through all text nodes and take only that with the given node as parent
			for (int i = 0; i < transitionTypeNodes.getLength(); i++) {
				if (transitionTypeNodes.item(i).getNodeType() == Node.ELEMENT_NODE && transitionTypeNodes.item(i).getParentNode().equals(transitionElement)) {
					Element transitionType = (Element) transitionTypeNodes.item(i);
					return transitionType.getTextContent();
				}
			}
		}
		return "regular";
	}
}
