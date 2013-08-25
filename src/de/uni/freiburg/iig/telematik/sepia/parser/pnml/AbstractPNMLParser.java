package de.uni.freiburg.iig.telematik.sepia.parser.pnml;

import java.awt.Color;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.Map;
import java.util.Vector;

import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import de.invation.code.toval.parser.ParserException;
import de.invation.code.toval.parser.XMLParserException;
import de.invation.code.toval.validate.ParameterException;
import de.invation.code.toval.validate.Validate;
import de.uni.freiburg.iig.telematik.sepia.graphic.AnnotationGraphics;
import de.uni.freiburg.iig.telematik.sepia.graphic.EdgeGraphics;
import de.uni.freiburg.iig.telematik.sepia.graphic.GraphicalPN;
import de.uni.freiburg.iig.telematik.sepia.graphic.NodeGraphics;
import de.uni.freiburg.iig.telematik.sepia.graphic.ObjectGraphics;
import de.uni.freiburg.iig.telematik.sepia.graphic.PNGraphics;
import de.uni.freiburg.iig.telematik.sepia.graphic.attributes.Dimension;
import de.uni.freiburg.iig.telematik.sepia.graphic.attributes.Fill;
import de.uni.freiburg.iig.telematik.sepia.graphic.attributes.Fill.GradientRotation;
import de.uni.freiburg.iig.telematik.sepia.graphic.attributes.Font;
import de.uni.freiburg.iig.telematik.sepia.graphic.attributes.Font.Align;
import de.uni.freiburg.iig.telematik.sepia.graphic.attributes.Font.Decoration;
import de.uni.freiburg.iig.telematik.sepia.graphic.attributes.Line;
import de.uni.freiburg.iig.telematik.sepia.graphic.attributes.Line.Shape;
import de.uni.freiburg.iig.telematik.sepia.graphic.attributes.Line.Style;
import de.uni.freiburg.iig.telematik.sepia.graphic.attributes.Offset;
import de.uni.freiburg.iig.telematik.sepia.graphic.attributes.Position;
import de.uni.freiburg.iig.telematik.sepia.petrinet.AbstractFlowRelation;
import de.uni.freiburg.iig.telematik.sepia.petrinet.AbstractMarking;
import de.uni.freiburg.iig.telematik.sepia.petrinet.AbstractPetriNet;
import de.uni.freiburg.iig.telematik.sepia.petrinet.AbstractPlace;
import de.uni.freiburg.iig.telematik.sepia.petrinet.AbstractTransition;

/**
 * Static reader class containing methods to read elements and attributes occurring in different net types.
 * 
 * @author Adrian Lange
 */
public abstract class AbstractPNMLParser<P extends AbstractPlace<F, S>, T extends AbstractTransition<F, S>, F extends AbstractFlowRelation<P, T, S>, M extends AbstractMarking<S>, S extends Object> {

	protected AbstractPetriNet<P, T, F, M, S> net;
	protected PNGraphics<P, T, F, M, S> graphics;

	public PNGraphics<P, T, F, M, S> getGraphics() {
		return graphics;
	}

	public AbstractPetriNet<P, T, F, M, S> getNet() {
		return net;
	}

	/**
	 * Parses a DOM document file and returns a graphical petri net, which is a container for a petri net and its graphical information.
	 * 
	 * @param pnmlDocument
	 *            DOM document to parse
	 * @return Petri net with graphical information
	 */
	public abstract GraphicalPN<P, T, F, M, S> parse(Document pnmlDocument) throws ParameterException, ParserException;

	/**
	 * Reads all arcs given in a list of DOM nodes and adds them to the {@link GraphicalPN}.
	 */
	protected abstract void readArcs(NodeList arcNodes) throws ParameterException, ParserException;

	/**
	 * Reads all places given in a list of DOM nodes and adds them to the {@link GraphicalPN}.
	 */
	protected abstract void readPlaces(NodeList placeNodes) throws ParameterException, XMLParserException;

	/**
	 * Reads all transitions given in a list of DOM nodes and adds them to the {@link GraphicalPN}.
	 */
	protected abstract void readTransitions(NodeList transitionNodes) throws XMLParserException, ParameterException;

	/**
	 * Reads a dimension tag and returns it as {@link Dimension}. If validated, a dimension tag must contain a x and a y value. If one of them is missed, its
	 * value will be set to 0.
	 * 
	 * @throws ParameterException
	 */
	protected static Dimension readDimension(Element dimensionNode) throws ParameterException {
		Validate.notNull(dimensionNode);

		Dimension dimension = new Dimension();

		// read and set x and y values
		Attr dimXAttr = dimensionNode.getAttributeNode("x");
		String dimXStr = dimXAttr.getValue();
		if (dimXStr != null && dimXStr.length() > 0) {
			double dimX = Double.parseDouble(dimXStr);
			dimension.setX(dimX);
		}

		Attr dimYAttr = dimensionNode.getAttributeNode("y");
		String dimYStr = dimYAttr.getValue();
		if (dimYStr != null && dimYStr.length() > 0) {
			double dimY = Double.parseDouble(dimYStr);
			dimension.setY(dimY);
		}

		return dimension;
	}

	/**
	 * Reads a fill tag and returns it as {@link Fill}.
	 */
	protected static Fill readFill(Element fillNode) throws ParameterException {
		Validate.notNull(fillNode);

		Fill fill = new Fill();

		// read and set color, gradientColor, gradientRotation, and image values
		Attr fillColorAttr = fillNode.getAttributeNode("color");
		String fillColorStr = fillColorAttr.getValue();
		if (fillColorStr != null && fillColorStr.length() > 0)
			fill.setColor(fillColorStr);

		Attr fillGradientColorAttr = fillNode.getAttributeNode("gradient-color");
		String fillGradientColorStr = fillGradientColorAttr.getValue();
		if (fillGradientColorStr != null && fillGradientColorStr.length() > 0)
			fill.setGradientColor(fillGradientColorStr);

		Attr fillGradientRotationAttr = fillNode.getAttributeNode("gradient-rotation");
		String fillGradientRotationStr = fillGradientRotationAttr.getValue();
		if (fillGradientRotationStr != null && fillGradientRotationStr.length() > 0) {
			GradientRotation gradientRotation = GradientRotation.getGradientRotation(fillGradientRotationStr);
			fill.setGradientRotation(gradientRotation);
		}

		Attr fillImageAttr = fillNode.getAttributeNode("image");
		String fillImageStr = fillImageAttr.getValue();
		if (fillImageStr != null && fillImageStr.length() > 0) {
			try {
				URI fillImage = new URI(fillImageStr);
				fill.setImage(fillImage);
			} catch (URISyntaxException e) {
				e.printStackTrace();
			}
		}

		return fill;
	}

	/**
	 * Reads a font tag and returns it as {@link Font}.
	 */
	protected static Font readFont(Element fontNode) throws ParameterException {
		Validate.notNull(fontNode);

		Font font = new Font();

		// read and set align, decoration, family, rotation, size, style, and weight values
		Attr fontAlignAttr = fontNode.getAttributeNode("align");
		String fontAlignStr = fontAlignAttr.getValue();
		if (fontAlignStr != null && fontAlignStr.length() > 0)
			font.setAlign(Align.getAlign(fontAlignStr));

		Attr fontDecorationAttr = fontNode.getAttributeNode("decoration");
		String fontDecorationStr = fontDecorationAttr.getValue();
		if (fontDecorationStr != null && fontDecorationStr.length() > 0) {
			Decoration decoration = Decoration.getDecoration(fontDecorationStr);
			font.setDecoration(decoration);
		}

		Attr fontFamilyAttr = fontNode.getAttributeNode("family");
		String fontFamilyStr = fontFamilyAttr.getValue();
		if (fontFamilyStr != null && fontFamilyStr.length() > 0)
			font.setFamily(fontFamilyStr);

		Attr fontRotationAttr = fontNode.getAttributeNode("rotation");
		String fontRotationStr = fontRotationAttr.getValue();
		double fontRotation = Double.parseDouble(fontRotationStr);
		if (fontRotationStr != null && fontRotation != 0)
			font.setRotation(fontRotation);

		Attr fontSizeAttr = fontNode.getAttributeNode("size");
		String fontSizeStr = fontSizeAttr.getValue();
		if (fontSizeStr != null && fontSizeStr.length() > 0)
			font.setSize(fontSizeStr);

		Attr fontStyleAttr = fontNode.getAttributeNode("style");
		String fontStyleStr = fontStyleAttr.getValue();
		if (fontStyleStr != null && fontStyleStr.length() > 0)
			font.setStyle(fontStyleStr);

		Attr fontWeightAttr = fontNode.getAttributeNode("weight");
		String fontWeightStr = fontWeightAttr.getValue();
		if (fontWeightStr != null && fontWeightStr.length() > 0)
			font.setWeight(fontWeightStr);

		return font;
	}

	/**
	 * Reads the graphics tag of the given element. TODO
	 */
	protected static ObjectGraphics readGraphics(Element element) throws ParameterException {
		Validate.notNull(element);

		// get node element type
		String elementType = element.getNodeName();
		if (elementType.equals("place") || elementType.equals("transition")) {
			NodeList graphicsList = element.getElementsByTagName("graphics");
			if (graphicsList.getLength() == 1) {
				NodeGraphics nodeGraphics = new NodeGraphics();
				Element graphics = (Element) element.getElementsByTagName("graphics").item(0);

				// dimension, fill, line, position
				if (graphics.getElementsByTagName("dimension").getLength() == 1) {
					Node dimension = graphics.getElementsByTagName("dimension").item(0);
					nodeGraphics.setDimension(readDimension((Element) dimension));
				}
				if (graphics.getElementsByTagName("fill").getLength() == 1) {
					Node fill = graphics.getElementsByTagName("fill").item(0);
					nodeGraphics.setFill(readFill((Element) fill));
				}
				if (graphics.getElementsByTagName("line").getLength() == 1) {
					Node line = graphics.getElementsByTagName("line").item(0);
					nodeGraphics.setLine(readLine((Element) line));
				}
				if (graphics.getElementsByTagName("position").getLength() == 1) {
					Node position = graphics.getElementsByTagName("position").item(0);
					nodeGraphics.setPosition(readPosition((Element) position));
				}

				return nodeGraphics;
			}
		} else if (elementType.equals("arc")) {
			NodeList graphicsList = element.getElementsByTagName("graphics");

			for (int i = 0; i < graphicsList.getLength(); i++) {
				if (graphicsList.item(i).getParentNode().equals(element)) {
					EdgeGraphics edgeGraphics = new EdgeGraphics();
					Element graphics = (Element) graphicsList.item(i);

					// positions and line
					if (graphics.getElementsByTagName("position").getLength() > 0) {
						Vector<Position> positions = new Vector<Position>(graphics.getElementsByTagName("position").getLength());
						for (int pos = 0; pos < graphics.getElementsByTagName("position").getLength(); pos++) {
							positions.add(readPosition((Element) graphics.getElementsByTagName("position").item(pos)));
						}
						edgeGraphics.setPositions(positions);
					}
					if (graphics.getElementsByTagName("line").getLength() == 1) {
						Node line = graphics.getElementsByTagName("line").item(0);
						edgeGraphics.setLine(readLine((Element) line));
					}

					return edgeGraphics;
				}
			}
		} else if (elementType.equals("inscription")) {
			NodeList graphicsList = element.getElementsByTagName("graphics");
			if (graphicsList.getLength() == 1) {
				AnnotationGraphics annotationGraphics = new AnnotationGraphics();
				Element graphics = (Element) graphicsList.item(0);

				// fill, font, line, and offset
				if (graphics.getElementsByTagName("fill").getLength() == 1) {
					Node fill = graphics.getElementsByTagName("fill").item(0);
					annotationGraphics.setFill(readFill((Element) fill));
				}
				if (graphics.getElementsByTagName("font").getLength() == 1) {
					Node font = graphics.getElementsByTagName("font").item(0);
					annotationGraphics.setFont(readFont((Element) font));
				}
				if (graphics.getElementsByTagName("line").getLength() == 1) {
					Node line = graphics.getElementsByTagName("line").item(0);
					annotationGraphics.setLine(readLine((Element) line));
				}
				if (graphics.getElementsByTagName("offset").getLength() == 1) {
					Node offset = graphics.getElementsByTagName("offset").item(0);
					annotationGraphics.setOffset(readOffset((Element) offset));
				}

				return annotationGraphics;
			}
		}

		return null;
	}

	/**
	 * Reads an initial marking tag and returns its value as {@link Integer}.
	 */
	protected static int readInitialMarking(Node initialMarkingNode) throws XMLParserException, ParameterException {
		Validate.notNull(initialMarkingNode);

		String markingStr = readText(initialMarkingNode);
		if (markingStr != null) {
			int marking = Integer.parseInt(markingStr);
			return marking;
		} else {
			return 0;
		}
	}

	/**
	 * Reads a line tag and returns it as {@link Line}.
	 */
	protected static Line readLine(Element lineNode) throws ParameterException {
		Validate.notNull(lineNode);

		Line line = new Line();

		// read and set color, shape, style, and width values
		Attr lineColorAttr = lineNode.getAttributeNode("color");
		String lineColorStr = lineColorAttr.getValue();
		if (lineColorStr != null && lineColorStr.length() > 0)
			line.setColor(lineColorStr);

		Attr lineShapeAttr = lineNode.getAttributeNode("shape");
		String lineShapeStr = lineShapeAttr.getValue();
		if (lineShapeStr != null && lineShapeStr.length() > 0) {
			Shape shape = Shape.getShape(lineShapeStr);
			line.setShape(shape);
		}

		Attr lineStyleAttr = lineNode.getAttributeNode("style");
		String lineStyleStr = lineStyleAttr.getValue();
		if (lineStyleStr != null && lineStyleStr.length() > 0) {
			Style style = Style.getStyle(lineStyleStr);
			line.setStyle(style);
		}

		Attr lineWidthAttr = lineNode.getAttributeNode("width");
		String lineWidthStr = lineWidthAttr.getValue();
		if (lineWidthStr != null && lineWidthStr.length() > 0) {
			double lineWidth = Double.parseDouble(lineWidthStr);
			line.setWidth(lineWidth);
		}

		return line;
	}

	/**
	 * Reads an offset tag and returns it as {@link Offset}. If validated, an offset tag must contain a x and a y value. If one of them is missed, its value
	 * will be set to 0.
	 */
	protected static Offset readOffset(Element offsetNode) throws ParameterException {
		Validate.notNull(offsetNode);

		Offset offset = new Offset();

		// read and set x and y values
		Attr offsXAttr = offsetNode.getAttributeNode("x");
		String offsXStr = offsXAttr.getValue();
		if (offsXStr != null && offsXStr.length() > 0) {
			double offsetX = Double.parseDouble(offsXStr);
			offset.setX(offsetX);
		}

		Attr offsYAttr = offsetNode.getAttributeNode("y");
		String offsYStr = offsYAttr.getValue();
		if (offsYStr != null && offsYStr.length() > 0) {
			double offsetY = Double.parseDouble(offsYStr);
			offset.setY(offsetY);
		}

		return offset;
	}

	/**
	 * Reads a position tag and returns it as {@link Position}. If validated, a position tag must contain a x and a y value. If one of them is missed, its value
	 * will be set to 0.
	 */
	protected static Position readPosition(Element positionNode) throws ParameterException {
		Validate.notNull(positionNode);

		Position position = new Position();

		// read and set x and y values
		Attr posXAttr = positionNode.getAttributeNode("x");
		String posXStr = posXAttr.getValue();
		if (posXStr != null && posXStr.length() > 0) {
			double posX = Double.parseDouble(posXStr);
			position.setX(posX);
		}

		Attr posYAttr = positionNode.getAttributeNode("y");
		String posYStr = posYAttr.getValue();
		if (posYStr != null && posYStr.length() > 0) {
			double posY = Double.parseDouble(posYStr);
			position.setY(posY);
		}

		return position;
	}

	/**
	 * Reads the content of text tags and returns them as string.
	 */
	protected static String readText(Node textNode) throws XMLParserException, ParameterException {
		Validate.notNull(textNode);

		if (textNode.getNodeType() != Node.ELEMENT_NODE) {
			throw new XMLParserException(de.invation.code.toval.parser.XMLParserException.ErrorCode.TAGSTRUCTURE);
		}
		Element textElement = (Element) textNode;
		NodeList textNodes = textElement.getElementsByTagName("text");
		if (textNodes.getLength() > 0) {
			// Iterate through all text nodes and take only that with the given node as parent
			for (int i = 0; i < textNodes.getLength(); i++) {
				if (textNodes.item(i).getNodeType() == Node.ELEMENT_NODE && textNodes.item(i).getParentNode().equals(textNode)) {
					Element text = (Element) textNodes.item(i);
					return text.getTextContent();
				}
			}
		}
		return null;
	}

	/**
	 * Gets the tokencolors element of a CPN, CWN, or IFNet and returns a {@link Map} containing all color values for the specific token color names.
	 */
	protected static Map<String, Color> readTokenColors(Element tokenColorsElement) throws ParameterException {
		Validate.notNull(tokenColorsElement);

		Map<String, Color> tokenColors = new HashMap<String, Color>();

		NodeList tokenColorList = tokenColorsElement.getElementsByTagName("tokencolor");
		for (int i = 0; i < tokenColorList.getLength(); i++) {
			Element tokenColorElement = (Element) tokenColorList.item(i);
			if (tokenColorElement.getNodeType() == Node.ELEMENT_NODE) {
				String colorName = ((Element) tokenColorElement.getElementsByTagName("color")).getTextContent();
				Element rgbColor = ((Element) tokenColorElement.getElementsByTagName("rgbcolor"));
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

	/**
	 * Gets a tokenposition node and reads its x and y attributes. Returns a {@link Position}.
	 */
	protected static Position readTokenPosition(Element tokenPositionNode) throws ParameterException {
		Validate.notNull(tokenPositionNode);

		Position tokenPosition = new Position();

		// read and set x and y values
		Attr posXAttr = tokenPositionNode.getAttributeNode("x");
		String posXStr = posXAttr.getValue();
		if (posXStr != null && posXStr.length() > 0) {
			double posX = Double.parseDouble(posXStr);
			tokenPosition.setX(posX);
		}

		Attr posYAttr = tokenPositionNode.getAttributeNode("y");
		String posYStr = posYAttr.getValue();
		if (posYStr != null && posYStr.length() > 0) {
			double posY = Double.parseDouble(posYStr);
			tokenPosition.setY(posY);
		}

		return tokenPosition;
	}

	public void setGraphics(PNGraphics<P, T, F, M, S> graphics) throws ParameterException {
		Validate.notNull(graphics);

		this.graphics = graphics;
	}

	public void setNet(AbstractPetriNet<P, T, F, M, S> net) {
		this.net = net;
	}
}
