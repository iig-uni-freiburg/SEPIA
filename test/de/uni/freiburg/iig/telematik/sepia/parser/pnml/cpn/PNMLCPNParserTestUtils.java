package de.uni.freiburg.iig.telematik.sepia.parser.pnml.cpn;

import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactoryConfigurationError;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

import de.uni.freiburg.iig.telematik.sepia.parser.pnml.AbstractPNMLPNParserTestUtils;

/**
 * @author Adrian Lange
 */
public class PNMLCPNParserTestUtils extends AbstractPNMLPNParserTestUtils {

	/**
	 * Creates a complete CPN arc
	 */
	public static Node createCPNArc(boolean showIDAttribute, boolean showSourceAttribute, boolean showTargetAttribute, boolean showName, boolean showNameGraphics, boolean showCompleteNameGraphics, boolean showCompleteNameOffsetGraphics, boolean showInscription, int inscriptionValue, boolean showInscriptionGraphics, boolean showCompleteInscriptionGraphics, boolean showCompleteInscriptionOffsetGraphics,
			boolean showGraphics, boolean showCompleteGraphics, boolean showCompletePositionGraphics, boolean showExistingColorTokenName) {
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
				placeNameGraphics.appendChild(createOffsetAttributeGraphics(a, true, showCompleteNameOffsetGraphics, "1.0", "-5.0"));
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
			Element arcInscription = createTextElement(a, "inscription", Integer.toString(inscriptionValue));
			arc.appendChild(arcInscription);
			// colors
			Element placeInitialMarkingColors = a.createElement("colors");
			arcInscription.appendChild(placeInitialMarkingColors);
			Element placeInitialMarkingColor1 = a.createElement("color");
			String colorTokenName;
			if (showExistingColorTokenName)
				colorTokenName = "yellow";
			else
				colorTokenName = "blabla";
			placeInitialMarkingColor1.setTextContent(colorTokenName);
			placeInitialMarkingColors.appendChild(placeInitialMarkingColor1);
			Element placeInitialMarkingColor2 = a.createElement("color");
			placeInitialMarkingColor2.setTextContent("yellow");
			placeInitialMarkingColors.appendChild(placeInitialMarkingColor2);
			Element placeInitialMarkingColor3 = a.createElement("color");
			placeInitialMarkingColor3.setTextContent("green");
			placeInitialMarkingColors.appendChild(placeInitialMarkingColor3);
			Element placeInitialMarkingColor4 = a.createElement("color");
			placeInitialMarkingColor4.setTextContent("blue");
			placeInitialMarkingColors.appendChild(placeInitialMarkingColor4);
			if (showInscriptionGraphics) {
				// Arc inscription graphics
				Element arcInscriptionGraphics = a.createElement("graphics");
				arcInscription.appendChild(arcInscriptionGraphics);
				// Add an offset element to the arc inscription graphics
				arcInscriptionGraphics.appendChild(createOffsetAttributeGraphics(a, true, showCompleteInscriptionOffsetGraphics, "1.0", "-5.0"));
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
	 * Creates a complete CPN place
	 */
	public static Node createCPNPlace(boolean showIDAttribute, boolean showName, boolean showNameGraphics, boolean showCompleteNameGraphics, boolean showCompleteNameOffsetGraphics, boolean showInitialMarking, boolean showValidInitialMarking, boolean showInitialMarkingGraphics, boolean showCompleteInitialMarkingTokenpositionGraphics, boolean showCapacities, boolean showGraphics,
			boolean showCompleteGraphics, boolean showCompletePositionGraphics, boolean showCompleteDimensionGraphics, boolean showExistingColorTokenName) {
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
				placeNameGraphics.appendChild(createOffsetAttributeGraphics(p, true, showCompleteNameOffsetGraphics, "1.0", "-5.0"));
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
			// colors
			Element placeInitialMarkingColors = p.createElement("colors");
			placeInitialMarking.appendChild(placeInitialMarkingColors);
			Element placeInitialMarkingColor1 = p.createElement("color");
			String colorTokenName;
			if (showExistingColorTokenName)
				colorTokenName = "green";
			else
				colorTokenName = "blabla";
			placeInitialMarkingColor1.setTextContent(colorTokenName);
			placeInitialMarkingColors.appendChild(placeInitialMarkingColor1);
			Element placeInitialMarkingColor2 = p.createElement("color");
			placeInitialMarkingColor2.setTextContent("green");
			placeInitialMarkingColors.appendChild(placeInitialMarkingColor2);
			Element placeInitialMarkingColor3 = p.createElement("color");
			placeInitialMarkingColor3.setTextContent("yellow");
			placeInitialMarkingColors.appendChild(placeInitialMarkingColor3);
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
				placeInitialMarkingToolspecificTokengraphics.appendChild(createTokenPositionAttributeGraphics(p, true, true, "-1.0", "-1.0"));
				placeInitialMarkingToolspecificTokengraphics.appendChild(createTokenPositionAttributeGraphics(p, true, true, "1.0", "0.0"));
				placeInitialMarkingToolspecificTokengraphics.appendChild(createTokenPositionAttributeGraphics(p, true, true, "-1.0", "1.0"));
			}
		}

		if (showCapacities) {
			// Capacities element for the place
			Element placeCapacities = p.createElement("capacities");
			place.appendChild(placeCapacities);
			// Capacity for green tokens
			Element placeCapacity1 = p.createElement("colorcapacity");
			placeCapacities.appendChild(placeCapacity1);
			Element placeCapacity1Color = p.createElement("color");
			placeCapacity1Color.setTextContent("green");
			placeCapacity1.appendChild(placeCapacity1Color);
			Element placeCapacity1Capacity = p.createElement("capacity");
			placeCapacity1Capacity.setTextContent("2");
			placeCapacity1.appendChild(placeCapacity1Capacity);
			// Capacity for yellow tokens
			Element placeCapacity2 = p.createElement("colorcapacity");
			placeCapacities.appendChild(placeCapacity2);
			Element placeCapacity2Color = p.createElement("color");
			placeCapacity2Color.setTextContent("yellow");
			placeCapacity2.appendChild(placeCapacity2Color);
			Element placeCapacity2Capacity = p.createElement("capacity");
			placeCapacity2Capacity.setTextContent("5");
			placeCapacity2.appendChild(placeCapacity2Capacity);
		}

		if (showGraphics) {
			// Graphics element for the place
			Element placeGraphics = p.createElement("graphics");
			place.appendChild(placeGraphics);
			// Add a position element to the place graphics
			placeGraphics.appendChild(createPositionAttributeGraphics(p, true, showCompletePositionGraphics, "20.0", "25.0"));
			if (showCompleteGraphics) {
				// Add a dimension element to the place graphics
				placeGraphics.appendChild(createDimensionAttributeGraphics(p, true, showCompleteDimensionGraphics, "37.0", "33.0"));
				// Add a fill element to the place graphics
				placeGraphics.appendChild(createFillAttributeGraphics(p, true, true, true, true));
				// Add a line element to the place graphics
				placeGraphics.appendChild(createLineAttributeGraphics(p, true, true, true, true));
			}
		}

		return p;
	}

	public static Node createTokenColors(boolean showColorName, boolean showRgbcolor, boolean noMissingRGBAttribute) {
		Document t = createDocumentInstance();

		Element tokencolors = t.createElement("tokencolors");
		t.appendChild(tokencolors);

		// First color "green"
		Element tokencolor1 = t.createElement("tokencolor");
		tokencolors.appendChild(tokencolor1);
		// color
		if (showColorName) {
			Element tokencolor1Color = t.createElement("color");
			tokencolor1Color.setTextContent("green");
			tokencolor1.appendChild(tokencolor1Color);
		}
		// rgbcolor
		if (showRgbcolor) {
			Element tokencolor1RGB = t.createElement("rgbcolor");
			tokencolor1.appendChild(tokencolor1RGB);
			Element tokencolor1RGBR = t.createElement("r");
			tokencolor1RGBR.setTextContent("0");
			tokencolor1RGB.appendChild(tokencolor1RGBR);
			Element tokencolor1RGBG = t.createElement("g");
			tokencolor1RGBG.setTextContent("255");
			tokencolor1RGB.appendChild(tokencolor1RGBG);
			Element tokencolor1RGBB = t.createElement("b");
			tokencolor1RGBB.setTextContent("0");
			tokencolor1RGB.appendChild(tokencolor1RGBB);
		}

		// First color "yellow"
		Element tokencolor2 = t.createElement("tokencolor");
		tokencolors.appendChild(tokencolor2);
		// color
		Element tokencolor2Color = t.createElement("color");
		tokencolor2Color.setTextContent("yellow");
		tokencolor2.appendChild(tokencolor2Color);
		// rgbcolor
		Element tokencolor2RGB = t.createElement("rgbcolor");
		tokencolor2.appendChild(tokencolor2RGB);
		if (noMissingRGBAttribute) {
			Element tokencolor2RGBR = t.createElement("r");
			tokencolor2RGBR.setTextContent("255");
			tokencolor2RGB.appendChild(tokencolor2RGBR);
		}
		Element tokencolor2RGBG = t.createElement("g");
		tokencolor2RGBG.setTextContent("255");
		tokencolor2RGB.appendChild(tokencolor2RGBG);
		Element tokencolor2RGBB = t.createElement("b");
		tokencolor2RGBB.setTextContent("0");
		tokencolor2RGB.appendChild(tokencolor2RGBB);

		// First color "blue"
		Element tokencolor3 = t.createElement("tokencolor");
		tokencolors.appendChild(tokencolor3);
		// color
		Element tokencolor3Color = t.createElement("color");
		tokencolor3Color.setTextContent("blue");
		tokencolor3.appendChild(tokencolor3Color);
		// rgbcolor
		Element tokencolor3RGB = t.createElement("rgbcolor");
		tokencolor3.appendChild(tokencolor3RGB);
		Element tokencolor3RGBR = t.createElement("r");
		tokencolor3RGBR.setTextContent("0");
		tokencolor3RGB.appendChild(tokencolor3RGBR);
		Element tokencolor3RGBG = t.createElement("g");
		tokencolor3RGBG.setTextContent("0");
		tokencolor3RGB.appendChild(tokencolor3RGBG);
		Element tokencolor3RGBB = t.createElement("b");
		tokencolor3RGBB.setTextContent("255");
		tokencolor3RGB.appendChild(tokencolor3RGBB);

		return t;
	}

	/**
	 * Creates a complete CPN transition
	 */
	public static Node createCPNTransition(boolean showIDAttribute, boolean showName, boolean showNameGraphics, boolean showCompleteNameGraphics, boolean showCompleteNameOffsetGraphics, boolean showGraphics, boolean showCompleteGraphics, boolean showCompletePositionGraphics, boolean showCompleteDimensionGraphics) {
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
				transitionNameGraphics.appendChild(createOffsetAttributeGraphics(t, true, showCompleteNameOffsetGraphics, "1.0", "-5.0"));
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
				transitionGraphics.appendChild(createDimensionAttributeGraphics(t, true, showCompleteDimensionGraphics, "37.0", "33.0"));
				// Add a fill element to the transition graphics
				transitionGraphics.appendChild(createFillAttributeGraphics(t, true, true, true, true));
				// Add a line element to the transition graphics
				transitionGraphics.appendChild(createLineAttributeGraphics(t, true, true, true, true));
			}
		}

		return t;
	}

	public static void main(String[] args) throws TransformerFactoryConfigurationError, TransformerException {
		System.out.println(toXML(createCPNArc(true, true, true, true, true, true, true, true, 2, true, true, true, true, true, true, true)));
		System.out.println(toXML(createCPNPlace(true, true, true, true, true, true, true, true, true, true, true, true, true, true, true)));
		System.out.println(toXML(createTokenColors(true, true, true)));
		System.out.println(toXML(createCPNTransition(true, true, true, true, true, true, true, true, true)));
	}
}
