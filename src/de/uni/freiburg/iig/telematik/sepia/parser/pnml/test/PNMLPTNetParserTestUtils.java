package de.uni.freiburg.iig.telematik.sepia.parser.pnml.test;

import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactoryConfigurationError;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

/**
 * @author Adrian Lange
 */
public class PNMLPTNetParserTestUtils extends AbstractPNMLPNParserTestUtils {

	/**
	 * Creates a complete PT-net arc
	 */
	public static Node createPTNetArc(boolean showIDAttribute, boolean showSourceAttribute, boolean showTargetAttribute, boolean showName, boolean showNameGraphics, boolean showCompleteNameGraphics, boolean showCompleteNameOffsetGraphics,
			boolean showInscription, boolean showInscriptionGraphics, boolean showCompleteInscriptionGraphics, boolean showCompleteInscriptionOffsetGraphics, boolean showGraphics, boolean showCompleteGraphics,
			boolean showCompletePositionGraphics) {
		Document a = createDocumentInstance();

		// Root element "arc"
		Element arc = a.createElement("arc");
		if (showIDAttribute)
			arc.setAttribute("id", "a1");
		if (showSourceAttribute)
			arc.setAttribute("source", "p1");
		if (showTargetAttribute)
			arc.setAttribute("target", "p2");
		a.appendChild(arc);

		if (showName) {
			// Name element for the arc
			Element arcName = createTextElement(a, "name", "arcName");
			arc.appendChild(arcName);
			// Arc name graphics
			Element placeNameGraphics = a.createElement("graphics");
			arcName.appendChild(placeNameGraphics);
			if (showNameGraphics) {
				// Add an offset element to the arc name graphics
				placeNameGraphics.appendChild(createOffsetAttributeGraphics(a, true, showCompleteNameOffsetGraphics, "0.0", "-5.0"));
				if (showCompleteNameGraphics) {
					// Add a fill element to the arc name graphics
					placeNameGraphics.appendChild(createFillAttributeGraphics(a, true, true, true, true));
					// Add a line element to the arc name graphics
					placeNameGraphics.appendChild(createLineAttributeGraphics(a, true, true, true, true));
					// Add a font element to the arc name graphics
					placeNameGraphics.appendChild(createFontAttributeGraphics(a, true, true, true, true, true, true, true));
				}
			}
		}

		if (showInscription) {
			// Inscription for the arc
			Element arcInscription = createTextElement(a, "inscription", "2");
			arc.appendChild(arcInscription);
			if (showInscriptionGraphics) {
				// Arc inscription graphics
				Element arcInscriptionGraphics = a.createElement("graphics");
				arcInscription.appendChild(arcInscriptionGraphics);
				// Add an offset element to the arc inscription graphics
				arcInscriptionGraphics.appendChild(createOffsetAttributeGraphics(a, true, showCompleteInscriptionOffsetGraphics, "0.0", "-5.0"));
				if (showCompleteInscriptionGraphics) {
					// Add a fill element to the arc inscription graphics
					arcInscriptionGraphics.appendChild(createFillAttributeGraphics(a, true, true, true, true));
					// Add a line element to the arc inscription graphics
					arcInscriptionGraphics.appendChild(createLineAttributeGraphics(a, true, true, true, true));
					// Add a font element to the arc inscription graphics
					arcInscriptionGraphics.appendChild(createFontAttributeGraphics(a, true, true, true, true, true, true, true));
				}
			}
		}

		if (showGraphics) {
			// Graphics element for the place
			Element arcGraphics = a.createElement("graphics");
			arc.appendChild(arcGraphics);
			// Add position elements to the arc graphics
			arcGraphics.appendChild(createPositionAttributeGraphics(a, true, showCompletePositionGraphics, "20.0", "25.0"));
			arcGraphics.appendChild(createPositionAttributeGraphics(a, true, true, "15.0", "20.0"));
			if (showCompleteGraphics) {
				// Add a line element to the arc graphics
				arcGraphics.appendChild(createLineAttributeGraphics(a, true, true, true, true));
			}
		}

		return a;
	}

	/**
	 * Creates a complete PT-net place
	 */
	public static Node createPTNetPlace(boolean showIDAttribute, boolean showName, boolean showNameGraphics, boolean showCompleteNameGraphics, boolean showCompleteNameOffsetGraphics, boolean showInitialMarking,
			boolean showValidInitialMarking, boolean showInitialMarkingGraphics, boolean showCompleteInitialMarkingTokenpositionGraphics, boolean showCapacity, boolean showGraphics, boolean showCompleteGraphics,
			boolean showCompletePositionGraphics) {
		Document p = createDocumentInstance();

		// Root element "place"
		Element place = p.createElement("place");
		if (showIDAttribute)
			place.setAttribute("id", "p1");
		p.appendChild(place);

		if (showName) {
			// Name element for the place
			Element placeName = createTextElement(p, "name", "ready");
			place.appendChild(placeName);
			if (showNameGraphics) {
				// Place name graphics
				Element placeNameGraphics = p.createElement("graphics");
				placeName.appendChild(placeNameGraphics);
				// Add an offset element to the place name graphics
				placeNameGraphics.appendChild(createOffsetAttributeGraphics(p, true, showCompleteNameOffsetGraphics, "0.0", "-5.0"));
				if (showCompleteNameGraphics) {
					// Add a fill element to the place name graphics
					placeNameGraphics.appendChild(createFillAttributeGraphics(p, true, true, true, true));
					// Add a line element to the place name graphics
					placeNameGraphics.appendChild(createLineAttributeGraphics(p, true, true, true, true));
					// Add a font element to the place name graphics
					placeNameGraphics.appendChild(createFontAttributeGraphics(p, true, true, true, true, true, true, true));
				}
			}
		}

		if (showInitialMarking) {
			// Initial marking for the place
			Element placeInitialMarking;
			if (showValidInitialMarking)
				placeInitialMarking = createTextElement(p, "initialMarking", "3");
			else
				placeInitialMarking = createTextElement(p, "initialMarking", "-1");
			place.appendChild(placeInitialMarking);
			if (showInitialMarkingGraphics) {
				// Add tool specific tag for token graphics
				Element placeInitialMarkingToolspecific = p.createElement("toolspecific");
				placeInitialMarkingToolspecific.setAttribute("tool", "org.pnml.tool");
				placeInitialMarkingToolspecific.setAttribute("version", "1.0");
				placeInitialMarking.appendChild(placeInitialMarkingToolspecific);
				// Add token graphics element
				Element placeInitialMarkingToolspecificTokengraphics = p.createElement("tokengraphics");
				placeInitialMarkingToolspecific.appendChild(placeInitialMarkingToolspecificTokengraphics);
				// Add three token positions representing their offset from the place center
				placeInitialMarkingToolspecificTokengraphics.appendChild(createTokenPositionAttributeGraphics(p, true, showCompleteInitialMarkingTokenpositionGraphics, "-2.0", "-2.0"));
				placeInitialMarkingToolspecificTokengraphics.appendChild(createTokenPositionAttributeGraphics(p, true, true, "2.0", "0.0"));
				placeInitialMarkingToolspecificTokengraphics.appendChild(createTokenPositionAttributeGraphics(p, true, true, "-2.0", "2.0"));
			}
		}

		if (showCapacity) {
			// Capacity element for the place
			Element placeCapacity = p.createElement("capacity");
			placeCapacity.setTextContent("10");
			place.appendChild(placeCapacity);
		}

		if (showGraphics) {
			// Graphics element for the place
			Element placeGraphics = p.createElement("graphics");
			place.appendChild(placeGraphics);
			// Add a position element to the place graphics
			placeGraphics.appendChild(createPositionAttributeGraphics(p, true, showCompletePositionGraphics, "20.0", "25.0"));
			if (showCompleteGraphics) {
				// Add a dimension element to the place graphics
				placeGraphics.appendChild(createDimensionAttributeGraphics(p, true, true, "30.0", "30.0"));
				// Add a fill element to the place graphics
				placeGraphics.appendChild(createFillAttributeGraphics(p, true, true, true, true));
				// Add a line element to the place graphics
				placeGraphics.appendChild(createLineAttributeGraphics(p, true, true, true, true));
			}
		}

		return p;
	}

	/**
	 * Creates a complete PT-net transition
	 */
	public static Node createPTNetTransition(boolean showIDAttribute, boolean showName, boolean showNameGraphics, boolean showCompleteNameGraphics, boolean showCompleteNameOffsetGraphics, boolean showGraphics, boolean showCompleteGraphics,
			boolean showCompletePositionGraphics) {
		Document t = createDocumentInstance();

		// Root element "transition"
		Element transition = t.createElement("transition");
		if (showIDAttribute)
			transition.setAttribute("id", "t1");
		t.appendChild(transition);

		if (showName) {
			// Name element for the transition
			Element transitionName = createTextElement(t, "name", "do smth");
			transition.appendChild(transitionName);
			if (showNameGraphics) {
				// Transition name graphics
				Element transitionNameGraphics = t.createElement("graphics");
				transitionName.appendChild(transitionNameGraphics);
				// Add an offset element to the transition name graphics
				transitionNameGraphics.appendChild(createOffsetAttributeGraphics(t, true, showCompleteNameOffsetGraphics, "0.0", "-5.0"));
				if (showCompleteNameGraphics) {
					// Add a fill element to the transition name graphics
					transitionNameGraphics.appendChild(createFillAttributeGraphics(t, true, true, true, true));
					// Add a line element to the transition name graphics
					transitionNameGraphics.appendChild(createLineAttributeGraphics(t, true, true, true, true));
					// Add a font element to the transition name graphics
					transitionNameGraphics.appendChild(createFontAttributeGraphics(t, true, true, true, true, true, true, true));
				}
			}
		}

		if (showGraphics) {
			// Graphics element for the transition
			Element transitionGraphics = t.createElement("graphics");
			transition.appendChild(transitionGraphics);
			// Add a position element to the transition graphics
			transitionGraphics.appendChild(createPositionAttributeGraphics(t, true, showCompletePositionGraphics, "20.0", "25.0"));
			if (showCompleteGraphics) {
				// Add a dimension element to the transition graphics
				transitionGraphics.appendChild(createDimensionAttributeGraphics(t, true, true, "30.0", "30.0"));
				// Add a fill element to the transition graphics
				transitionGraphics.appendChild(createFillAttributeGraphics(t, true, true, true, true));
				// Add a line element to the transition graphics
				transitionGraphics.appendChild(createLineAttributeGraphics(t, true, true, true, true));
			}
		}

		return t;
	}

	public static void main(String[] args) throws TransformerFactoryConfigurationError, TransformerException {
		System.out.println(toXML(createPTNetArc(true, true, true, true, true, true, true, true, true, true, true, true, true, true)));
		System.out.println(toXML(createPTNetPlace(true, true, true, true, true, true, true, true, true, true, true, true, true)));
		System.out.println(toXML(createPTNetTransition(true, true, true, true, true, true, true, true)));
	}
}
