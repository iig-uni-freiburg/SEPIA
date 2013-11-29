package de.uni.freiburg.iig.telematik.sepia.parser.pnml.ifnet;

import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactoryConfigurationError;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

import de.uni.freiburg.iig.telematik.sepia.parser.pnml.cwn.PNMLCWNParserTestUtils;

/**
 * @author Adrian Lange
 */
public class PNMLIFNetParserTestUtils extends PNMLCWNParserTestUtils {

	/**
	 * Creates a complete IF-net transition
	 */
	public static Node createTransition(boolean showIDAttribute, boolean showName, boolean showNameGraphics, boolean showCompleteNameGraphics, boolean showCompleteNameOffsetGraphics, boolean showGraphics, boolean showCompleteGraphics, boolean showCompletePositionGraphics, boolean showCompleteDimensionGraphics, boolean showTransitiontype, boolean isRegularTransition, boolean showAccessFunctions, boolean showAccessFunctionColor, boolean showAccessFunctionAccessModes, boolean showSilent, boolean isSilent, boolean showValidSilent) {
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

		if (showTransitiontype) {
			// Type element for the transition
			Element transitionTransitiontype = t.createElement("transitiontype");
			if (isRegularTransition)
				transitionTransitiontype.setTextContent("regular");
			else
				transitionTransitiontype.setTextContent("declassification");
			transition.appendChild(transitionTransitiontype);
		}

		if (showAccessFunctions) {
			// Type element for the transition
			Element transitionAccessFunctions = t.createElement("accessfunctions");
			transition.appendChild(transitionAccessFunctions);

			// Access function for the token color green
			Element transitionAccessFunctionGreen = t.createElement("accessfunction");
			transitionAccessFunctions.appendChild(transitionAccessFunctionGreen);
			if (showAccessFunctionColor) {
				// Color name
				Element transitionAccessFunctionGreenColor = t.createElement("color");
				transitionAccessFunctionGreenColor.setTextContent("green");
				transitionAccessFunctionGreen.appendChild(transitionAccessFunctionGreenColor);
			}
			if (showAccessFunctionAccessModes) {
				// Access modes
				Element transitionAccessFunctionGreenAccessModes = t.createElement("accessmodes");
				transitionAccessFunctionGreen.appendChild(transitionAccessFunctionGreenAccessModes);
				Element transitionAccessFunctionGreenAccessModesRead = t.createElement("read");
				transitionAccessFunctionGreenAccessModesRead.setTextContent("true");
				transitionAccessFunctionGreenAccessModes.appendChild(transitionAccessFunctionGreenAccessModesRead);
				Element transitionAccessFunctionGreenAccessModesCreate = t.createElement("create");
				transitionAccessFunctionGreenAccessModesCreate.setTextContent("true");
				transitionAccessFunctionGreenAccessModes.appendChild(transitionAccessFunctionGreenAccessModesCreate);
				Element transitionAccessFunctionGreenAccessModesWrite = t.createElement("write");
				transitionAccessFunctionGreenAccessModesWrite.setTextContent("false");
				transitionAccessFunctionGreenAccessModes.appendChild(transitionAccessFunctionGreenAccessModesWrite);
				Element transitionAccessFunctionGreenAccessModesDelete = t.createElement("delete");
				transitionAccessFunctionGreenAccessModesDelete.setTextContent("false");
				transitionAccessFunctionGreenAccessModes.appendChild(transitionAccessFunctionGreenAccessModesDelete);
			}
		}

		if (showSilent) {
			Element transitionSilent = t.createElement("silent");
			if (isSilent)
				transitionSilent.setTextContent("true");
			else
				transitionSilent.setTextContent("false");
			if (!showValidSilent)
				transitionSilent.setTextContent("invalid");
			transition.appendChild(transitionSilent);
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
		System.out.println(toXML(createArc(true, true, true, true, true, true, true, true, 2, true, true, true, true, true, true, true)));
		System.out.println(toXML(createPlace(true, true, true, true, true, true, true, true, true, true, true, true, true, true, true)));
		System.out.println(toXML(createTokenColors(true, true, true)));
		System.out.println(toXML(createTransition(true, true, true, true, true, true, true, true, true, true, true, true, true, true, true, false, true)));
	}
}
