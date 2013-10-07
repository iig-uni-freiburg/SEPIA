package de.uni.freiburg.iig.telematik.sepia.parser.pnml.test;

import java.io.StringWriter;
import java.io.Writer;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.TransformerFactoryConfigurationError;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

/**
 * @author Adrian Lange
 */
public class AbstractPNMLPNParserTestUtils {

	protected static Element createDimensionAttributeGraphics(Document d, boolean showX, boolean showY, String xValue, String yValue) {
		Element dimensionGraphics = d.createElement("dimension");
		if (showX)
			dimensionGraphics.setAttribute("x", xValue);
		if (showY)
			dimensionGraphics.setAttribute("y", yValue);
		return dimensionGraphics;
	}

	protected static Element createFillAttributeGraphics(Document d, boolean showColor, boolean showGradientColor, boolean showGradientRotation, boolean showImage) {
		Element fillGraphics = d.createElement("fill");
		if (showColor)
			fillGraphics.setAttribute("color", "#FF0000");
		if (showGradientColor)
			fillGraphics.setAttribute("gradient-color", "#FFFFFF");
		if (showGradientRotation)
			fillGraphics.setAttribute("gradient-rotation", "diagonal");
		if (showImage)
			fillGraphics.setAttribute("image", "http://www.some-url.com/image.png");
		return fillGraphics;
	}

	protected static Element createFontAttributeGraphics(Document d, boolean showFamily, boolean showStyle, boolean showWeight, boolean showSize, boolean showDecoration, boolean showAlign, boolean showRotation) {
		Element fontGraphics = d.createElement("font");
		if (showFamily)
			fontGraphics.setAttribute("family", "arial,sans-serif");
		if (showStyle)
			fontGraphics.setAttribute("style", "italic");
		if (showWeight)
			fontGraphics.setAttribute("weight", "bold");
		if (showSize)
			fontGraphics.setAttribute("size", "medium");
		if (showDecoration)
			fontGraphics.setAttribute("decoration", "underline");
		if (showAlign)
			fontGraphics.setAttribute("align", "left");
		if (showRotation)
			fontGraphics.setAttribute("rotation", "0.0");
		return fontGraphics;
	}

	protected static Element createLineAttributeGraphics(Document d, boolean showShape, boolean showColor, boolean showStyle, boolean showWidth) {
		Element lineGraphics = d.createElement("line");
		if (showShape)
			lineGraphics.setAttribute("shape", "line");
		if (showColor)
			lineGraphics.setAttribute("color", "black");
		if (showStyle)
			lineGraphics.setAttribute("style", "solid");
		if (showWidth)
			lineGraphics.setAttribute("width", "1.5");
		return lineGraphics;
	}

	protected static Element createOffsetAttributeGraphics(Document d, boolean showX, boolean showY, String xValue, String yValue) {
		Element offsetGraphics = d.createElement("offset");
		if (showX)
			offsetGraphics.setAttribute("x", xValue);
		if (showY)
			offsetGraphics.setAttribute("y", yValue);
		return offsetGraphics;
	}

	protected static Element createPositionAttributeGraphics(Document d, boolean showX, boolean showY, String xValue, String yValue) {
		Element positionGraphics = d.createElement("position");
		if (showX)
			positionGraphics.setAttribute("x", xValue);
		if (showY)
			positionGraphics.setAttribute("y", yValue);
		return positionGraphics;
	}

	protected static Element createTextElement(Document d, String textElementName, String textElementValue) {
		Element placeName = d.createElement(textElementName);
		Element placeNameText = d.createElement("text");
		placeNameText.setTextContent(textElementValue);
		placeName.appendChild(placeNameText);
		return placeName;
	}

	protected static Element createTokenPositionAttributeGraphics(Document d, boolean showX, boolean showY, String xValue, String yValue) {
		Element tokenpositionGraphics = d.createElement("tokenposition");
		if (showX)
			tokenpositionGraphics.setAttribute("x", xValue);
		if (showY)
			tokenpositionGraphics.setAttribute("y", yValue);
		return tokenpositionGraphics;
	}

	/**
	 * Takes a {@link Node} object and converts it to a {@link String} representing the valid XML structure.
	 * 
	 * @param node
	 *            {@link Node} to show as {@link String}
	 * @return The {@link Node} as {@link String}
	 */
	public static final String toXML(Node node) throws TransformerFactoryConfigurationError, TransformerException {
		Transformer tf = TransformerFactory.newInstance().newTransformer();
		tf.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
		tf.setOutputProperty(OutputKeys.INDENT, "yes");
		tf.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "2");
		tf.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");
		Writer out = new StringWriter();
		tf.transform(new DOMSource(node), new StreamResult(out));
		return out.toString();
	}

	/**
	 * Returns a new {@link Document} instance from a {@link DocumentBuilderFactory}.
	 */
	protected static final Document createDocumentInstance() {
		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		DocumentBuilder db = null;
		try {
			db = dbf.newDocumentBuilder();
		} catch (ParserConfigurationException e) {
			e.printStackTrace();
		}
		Document doc = db.newDocument();

		return doc;
	}
}
