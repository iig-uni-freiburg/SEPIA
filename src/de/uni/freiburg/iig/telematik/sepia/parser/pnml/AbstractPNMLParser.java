package de.uni.freiburg.iig.telematik.sepia.parser.pnml;

import java.net.URI;
import java.net.URISyntaxException;
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
import de.uni.freiburg.iig.telematik.sepia.graphic.AbstractGraphicalPN;
import de.uni.freiburg.iig.telematik.sepia.graphic.netgraphics.AbstractObjectGraphics;
import de.uni.freiburg.iig.telematik.sepia.graphic.netgraphics.AbstractPNGraphics;
import de.uni.freiburg.iig.telematik.sepia.graphic.netgraphics.AnnotationGraphics;
import de.uni.freiburg.iig.telematik.sepia.graphic.netgraphics.ArcGraphics;
import de.uni.freiburg.iig.telematik.sepia.graphic.netgraphics.NodeGraphics;
import de.uni.freiburg.iig.telematik.sepia.graphic.netgraphics.attributes.Dimension;
import de.uni.freiburg.iig.telematik.sepia.graphic.netgraphics.attributes.Fill;
import de.uni.freiburg.iig.telematik.sepia.graphic.netgraphics.attributes.Font;
import de.uni.freiburg.iig.telematik.sepia.graphic.netgraphics.attributes.Line;
import de.uni.freiburg.iig.telematik.sepia.graphic.netgraphics.attributes.Offset;
import de.uni.freiburg.iig.telematik.sepia.graphic.netgraphics.attributes.Position;
import de.uni.freiburg.iig.telematik.sepia.graphic.netgraphics.attributes.Fill.GradientRotation;
import de.uni.freiburg.iig.telematik.sepia.graphic.netgraphics.attributes.Font.Align;
import de.uni.freiburg.iig.telematik.sepia.graphic.netgraphics.attributes.Font.Decoration;
import de.uni.freiburg.iig.telematik.sepia.graphic.netgraphics.attributes.Line.Shape;
import de.uni.freiburg.iig.telematik.sepia.graphic.netgraphics.attributes.Line.Style;
import de.uni.freiburg.iig.telematik.sepia.mg.abstr.AbstractMarkingGraphRelation;
import de.uni.freiburg.iig.telematik.sepia.mg.abstr.AbstractMarkingGraphState;
import de.uni.freiburg.iig.telematik.sepia.parser.pnml.PNMLParserException.ErrorCode;
import de.uni.freiburg.iig.telematik.sepia.petrinet.AbstractFlowRelation;
import de.uni.freiburg.iig.telematik.sepia.petrinet.AbstractMarking;
import de.uni.freiburg.iig.telematik.sepia.petrinet.AbstractPetriNet;
import de.uni.freiburg.iig.telematik.sepia.petrinet.AbstractPlace;
import de.uni.freiburg.iig.telematik.sepia.petrinet.AbstractTransition;

/**
 * Abstract super class of the PNML parsers containing methods to read elements and attributes occurring in all net types.
 * 
 * @author Adrian Lange
 */
public abstract class AbstractPNMLParser<P extends AbstractPlace<F, S>,
										 T extends AbstractTransition<F, S>,
										 F extends AbstractFlowRelation<P, T, S>,
										 M extends AbstractMarking<S>,
										 S extends Object,
										 X extends AbstractMarkingGraphState<M, S>,
								   		 Y extends AbstractMarkingGraphRelation<M, X, S>,
										 N extends AbstractPetriNet<P, T, F, M, S, X, Y>,
										 G extends AbstractPNGraphics<P, T, F, M, S>> {

	protected N net;
	protected G graphics;

	public G getGraphics() {
		return graphics;
	}

	public N getNet() {
		return net;
	}

	/**
	 * Parses a DOM document file and returns a graphical petri net, which is a container for a petri net and its graphical information.
	 * 
	 * @param pnmlDocument
	 *            DOM document to parse
	 * @return Petri net with graphical information
	 */
	public abstract AbstractGraphicalPN<P, T, F, M, S, X, Y, N, G> parse(Document pnmlDocument) throws ParameterException, ParserException;

	/**
	 * Parses a PNML document into an existing instance of an {@link AbstractGraphicalPN}. Use {@link #parse(Document)} to return an {@link AbstractGraphicalPN}.
	 */
	public void parseDocument(Document pnmlDocument) throws ParameterException, ParserException {
		// Check if the net is defined on a single page
		NodeList pageNodes = pnmlDocument.getElementsByTagName("page");
		if (pageNodes.getLength() > 1)
			throw new PNMLParserException(ErrorCode.NOT_ON_ONE_PAGE);

		// Read places and transitions
		NodeList placeNodes = pnmlDocument.getElementsByTagName("place");
		readPlaces(placeNodes);
		NodeList transitionNodes = pnmlDocument.getElementsByTagName("transition");
		readTransitions(transitionNodes);
		// Read arcs
		NodeList arcNodes = pnmlDocument.getElementsByTagName("arc");
		readArcs(arcNodes);

		// Read net ID as name
		String netName = readNetName(pnmlDocument);
		if (netName != null)
			net.setName(netName);
	}

	/**
	 * Reads all arcs given in a list of DOM nodes and adds them to the {@link AbstractGraphicalPN}.
	 */
	protected abstract void readArcs(NodeList arcNodes) throws ParameterException, ParserException;

	/**
	 * Reads all places given in a list of DOM nodes and adds them to the {@link AbstractGraphicalPN}.
	 */
	protected abstract void readPlaces(NodeList placeNodes) throws ParameterException, ParserException;

	/**
	 * Reads all transitions given in a list of DOM nodes and adds them to the {@link AbstractGraphicalPN}.
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
				if (transitionLabel != null){
					net.addTransition(transitionName, transitionLabel);
				} else {
					net.addTransition(transitionName);
				}
				if(readSilent(transition)){
					net.getTransition(transitionName).setSilent(true);
				}

				// read graphical information
				NodeGraphics transitionGraphics = readNodeGraphicsElement(transition);
				if (transitionGraphics != null)
					graphics.getTransitionGraphics().put(transitionName, transitionGraphics);
			}
		}
	}

	/**
	 * Reads the graphical information of an annotation element and returns a {@link AnnotationGraphics} object.
	 */
	public AnnotationGraphics readAnnotationGraphicsElement(Element annotationGraphicsElement) throws ParameterException, ParserException {
		Validate.notNull(annotationGraphicsElement);

		String elementType = annotationGraphicsElement.getNodeName();

		if (!elementType.equals("name") && !elementType.equals("inscription") && !elementType.equals("colorInscription") && !elementType.equals("accessfunctions") && !elementType.equals("subjectgraphics"))
			throw new ParserException("The given element mustn't have an annotation.");

		NodeList graphicsList = annotationGraphicsElement.getElementsByTagName("graphics");
		for (int inscriptionIndex = 0; inscriptionIndex < graphicsList.getLength(); inscriptionIndex++) {
			if (graphicsList.item(inscriptionIndex).getParentNode().equals(annotationGraphicsElement) && graphicsList.item(inscriptionIndex).getNodeType() == Node.ELEMENT_NODE) {
				AnnotationGraphics annotationGraphics = new AnnotationGraphics();
				Element graphics = (Element) graphicsList.item(inscriptionIndex);

				// fill, font, line, offset, and offset
				if (graphics.getElementsByTagName("fill").getLength() > 0) {
					Node fill = graphics.getElementsByTagName("fill").item(0);
					annotationGraphics.setFill(readFill((Element) fill));
				}
				if (graphics.getElementsByTagName("font").getLength() > 0) {
					Node font = graphics.getElementsByTagName("font").item(0);
					annotationGraphics.setFont(readFont((Element) font));
				}
				if (graphics.getElementsByTagName("line").getLength() > 0) {
					Node line = graphics.getElementsByTagName("line").item(0);
					annotationGraphics.setLine(readLine((Element) line));
				}
				if (graphics.getElementsByTagName("offset").getLength() > 0) {
					Node offset = graphics.getElementsByTagName("offset").item(0);
					annotationGraphics.setOffset(readOffset((Element) offset));
				}

				// Read annotation visibility
				annotationGraphics.setVisibility(readAnnotationVisibility(annotationGraphicsElement));
				
				return annotationGraphics;
			}
		}
		// No graphics found
		return null;
	}

	/**
	 * Reads the visibility information of an annotation element and returns a boolean value.
	 */
	public boolean readAnnotationVisibility(Node annotationNode) throws ParameterException {
		Validate.notNull(annotationNode);

		if (annotationNode.getNodeType() != Node.ELEMENT_NODE)
			throw new ParameterException("The given annotation node is note of an element type.");
		Element annotationElement = (Element) annotationNode;

		NodeList toolspecificList = annotationElement.getElementsByTagName("toolspecific");
		for (int toolspecificIndex = 0; toolspecificIndex < toolspecificList.getLength(); toolspecificIndex++) {
			if (toolspecificList.item(toolspecificIndex).getParentNode().equals(annotationElement) && toolspecificList.item(toolspecificIndex).getNodeType() == Node.ELEMENT_NODE) {
				Element toolspecificElement = (Element) toolspecificList.item(toolspecificIndex);
				if (toolspecificElement.hasAttribute("tool") && toolspecificElement.getAttribute("tool").equals("de.uni-freiburg.telematik.editor") && toolspecificElement.hasAttribute("version") && toolspecificElement.getAttribute("version").equals("1.0")) {
					NodeList visibleList = toolspecificElement.getElementsByTagName("visible");
					for (int visibleIndex = 0; visibleIndex < visibleList.getLength(); visibleIndex++) {
						if (visibleList.item(visibleIndex).getParentNode().equals(toolspecificElement) && visibleList.item(visibleIndex).getNodeType() == Node.ELEMENT_NODE) {
							Element visibleElement = (Element) visibleList.item(visibleIndex);
							String visibleString = visibleElement.getTextContent().trim().toLowerCase();
							if (visibleString.equals("true"))
								return true;
							else if (visibleString.equals("false"))
								return false;
						}
					}
				}
			}
		}
		return AnnotationGraphics.DEFAULT_VISIBILITY;
	}

	/**
	 * Reads the graphical information of an arc element and returns a {@link ArcGraphics} object.
	 */
	public ArcGraphics readArcGraphicsElement(Element arcGraphicsElement) throws ParameterException, ParserException {
		Validate.notNull(arcGraphicsElement);

		String elementType = arcGraphicsElement.getNodeName();

		if (!elementType.equals("arc"))
			throw new ParserException("The element must be of the type \"arc\".");

		NodeList graphicsList = arcGraphicsElement.getElementsByTagName("graphics");
		for (int arcIndex = 0; arcIndex < graphicsList.getLength(); arcIndex++) {
			if (graphicsList.item(arcIndex).getParentNode().equals(arcGraphicsElement) && graphicsList.item(arcIndex).getNodeType() == Node.ELEMENT_NODE) {
				ArcGraphics arcGraphics = new ArcGraphics();
				Element graphics = (Element) graphicsList.item(arcIndex);

				// positions and line
				NodeList positionGraphics = graphics.getElementsByTagName("position");
				if (positionGraphics.getLength() > 0) {
					Vector<Position> positions = new Vector<Position>(positionGraphics.getLength());
					for (int positionIndex = 0; positionIndex < positionGraphics.getLength(); positionIndex++) {
						positions.add(readPosition((Element) positionGraphics.item(positionIndex)));
					}
					arcGraphics.setPositions(positions);
				}
				if (graphics.getElementsByTagName("line").getLength() > 0) {
					Node line = graphics.getElementsByTagName("line").item(0);
					arcGraphics.setLine(readLine((Element) line));
				}

				return arcGraphics;
			}
		}
		// No graphics found
		return null;
	}

	/**
	 * Reads a dimension tag and returns it as {@link Dimension}. If validated, a dimension tag must contain a x and a y value. If one of them is missed, its value will be set to 0.
	 */
	public Dimension readDimension(Element dimensionNode) throws ParameterException {
		Validate.notNull(dimensionNode);

		Dimension dimension = new Dimension();

		// read and set x and y values
		Attr dimXAttr = dimensionNode.getAttributeNode("x");
		if (dimXAttr != null) {
			String dimXStr = dimXAttr.getValue();
			if (dimXStr != null && dimXStr.length() > 0) {
				double dimX = Double.parseDouble(dimXStr);
				dimension.setX(dimX);
			}
		}

		Attr dimYAttr = dimensionNode.getAttributeNode("y");
		if (dimYAttr != null) {
			String dimYStr = dimYAttr.getValue();
			if (dimYStr != null && dimYStr.length() > 0) {
				double dimY = Double.parseDouble(dimYStr);
				dimension.setY(dimY);
			}
		}

		return dimension;
	}

	/**
	 * Reads a fill tag and returns it as {@link Fill}.
	 */
	public Fill readFill(Element fillNode) throws ParameterException {
		Validate.notNull(fillNode);

		Fill fill = new Fill();

		// read and set color, gradientColor, gradientRotation, and image values
		Attr fillColorAttr = fillNode.getAttributeNode("color");
		if (fillColorAttr != null) {
			String fillColorStr = fillColorAttr.getValue();
			if (fillColorStr != null && fillColorStr.length() > 0)
				fill.setColor(fillColorStr);
		}

		Attr fillGradientColorAttr = fillNode.getAttributeNode("gradient-color");
		if (fillGradientColorAttr != null) {
			String fillGradientColorStr = fillGradientColorAttr.getValue();
			if (fillGradientColorStr != null && fillGradientColorStr.length() > 0)
				fill.setGradientColor(fillGradientColorStr);
		}

		Attr fillGradientRotationAttr = fillNode.getAttributeNode("gradient-rotation");
		if (fillGradientRotationAttr != null) {
			String fillGradientRotationStr = fillGradientRotationAttr.getValue();
			if (fillGradientRotationStr != null && fillGradientRotationStr.length() > 0) {
				GradientRotation gradientRotation = GradientRotation.getGradientRotation(fillGradientRotationStr);
				fill.setGradientRotation(gradientRotation);
			}
		}

		Attr fillImageAttr = fillNode.getAttributeNode("image");
		if (fillImageAttr != null) {
			String fillImageStr = fillImageAttr.getValue();
			if (fillImageStr != null && fillImageStr.length() > 0) {
				try {
					URI fillImage = new URI(fillImageStr);
					fill.setImage(fillImage);
				} catch (URISyntaxException e) {
					e.printStackTrace();
				}
			}
		}

		return fill;
	}

	/**
	 * Reads a font tag and returns it as {@link Font}.
	 */
	public Font readFont(Element fontNode) throws ParameterException {
		Validate.notNull(fontNode);

		Font font = new Font();

		// read and set align, decoration, family, rotation, size, style, and weight values
		Attr fontAlignAttr = fontNode.getAttributeNode("align");
		if (fontAlignAttr != null) {
			String fontAlignStr = fontAlignAttr.getValue();
			if (fontAlignStr != null && fontAlignStr.length() > 0)
				font.setAlign(Align.getAlign(fontAlignStr));
		}

		Attr fontDecorationAttr = fontNode.getAttributeNode("decoration");
		if (fontDecorationAttr != null) {
			String fontDecorationStr = fontDecorationAttr.getValue();
			if (fontDecorationStr != null && fontDecorationStr.length() > 0) {
				Decoration decoration = Decoration.getDecoration(fontDecorationStr);
				font.setDecoration(decoration);
			}
		}

		Attr fontFamilyAttr = fontNode.getAttributeNode("family");
		if (fontFamilyAttr != null) {
			String fontFamilyStr = fontFamilyAttr.getValue();
			if (fontFamilyStr != null && fontFamilyStr.length() > 0)
				font.setFamily(fontFamilyStr);
		}

		Attr fontRotationAttr = fontNode.getAttributeNode("rotation");
		if (fontRotationAttr != null) {
			String fontRotationStr = fontRotationAttr.getValue();
			double fontRotation = Double.parseDouble(fontRotationStr);
			if (fontRotationStr != null && fontRotation != 0)
				font.setRotation(fontRotation);
		}

		Attr fontSizeAttr = fontNode.getAttributeNode("size");
		if (fontSizeAttr != null) {
			String fontSizeStr = fontSizeAttr.getValue();
			if (fontSizeStr != null && fontSizeStr.length() > 0)
				font.setSize(fontSizeStr);
		}

		Attr fontStyleAttr = fontNode.getAttributeNode("style");
		if (fontStyleAttr != null) {
			String fontStyleStr = fontStyleAttr.getValue();
			if (fontStyleStr != null && fontStyleStr.length() > 0)
				font.setStyle(fontStyleStr);
		}

		Attr fontWeightAttr = fontNode.getAttributeNode("weight");
		if (fontWeightAttr != null) {
			String fontWeightStr = fontWeightAttr.getValue();
			if (fontWeightStr != null && fontWeightStr.length() > 0)
				font.setWeight(fontWeightStr);
		}

		return font;
	}

	/**
	 * Reads the graphics tag of the given element.
	 */
	public AbstractObjectGraphics readGraphics(Element element) throws ParameterException, ParserException {
		Validate.notNull(element);

		// get node element type
		String elementType = element.getNodeName();

		if (elementType.equals("place") || elementType.equals("transition"))
			return readNodeGraphicsElement(element);
		else if (elementType.equals("arc"))
			return readArcGraphicsElement(element);
		else if (elementType.equals("inscription") || elementType.equals("colorInscription") || elementType.equals("accessfunctions") || elementType.equals("subject"))
			return readAnnotationGraphicsElement(element);
		else
			return null;
	}

	/**
	 * Reads an initial marking tag and returns its value as {@link Integer}.
	 */
	public int readInitialMarking(Node initialMarkingNode) throws XMLParserException, ParameterException {
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
	public Line readLine(Element lineNode) throws ParameterException {
		Validate.notNull(lineNode);

		Line line = new Line();

		// read and set color, shape, style, and width values
		Attr lineColorAttr = lineNode.getAttributeNode("color");
		if (lineColorAttr != null) {
			String lineColorStr = lineColorAttr.getValue();
			if (lineColorStr != null && lineColorStr.length() > 0)
				line.setColor(lineColorStr);
		}

		Attr lineShapeAttr = lineNode.getAttributeNode("shape");
		if (lineShapeAttr != null) {
			String lineShapeStr = lineShapeAttr.getValue();
			if (lineShapeStr != null && lineShapeStr.length() > 0) {
				Shape shape = Shape.getShape(lineShapeStr);
				line.setShape(shape);
			}
		}

		Attr lineStyleAttr = lineNode.getAttributeNode("style");
		if (lineStyleAttr != null) {
			String lineStyleStr = lineStyleAttr.getValue();
			if (lineStyleStr != null && lineStyleStr.length() > 0) {
				Style style = Style.getStyle(lineStyleStr);
				line.setStyle(style);
			}
		}

		Attr lineWidthAttr = lineNode.getAttributeNode("width");
		if (lineWidthAttr != null) {
			String lineWidthStr = lineWidthAttr.getValue();
			if (lineWidthStr != null && lineWidthStr.length() > 0) {
				double lineWidth = Double.parseDouble(lineWidthStr);
				line.setWidth(lineWidth);
			}
		}

		return line;
	}

	/**
	 * Reads the ID-attribute of the net-tag in a PNML document.
	 */
	public String readNetName(Document pnmlDocument) {
		// Read net ID as name
		NodeList netList = pnmlDocument.getElementsByTagName("net");
		for (int i = 0; i < netList.getLength(); i++) {
			if (netList.item(i).getNodeType() == Node.ELEMENT_NODE && netList.item(i).getParentNode().equals(pnmlDocument.getDocumentElement())) {
				Element netElement = (Element) netList.item(i);
				if (netElement.hasAttribute("id")) {
					String id = netElement.getAttribute("id");
					if (id.length() > 0)
						return id;
				}
			}
		}

		return null;
	}

	/**
	 * Reads the graphical information of a node element line a place or a transition and returns a {@link NodeGraphics} object.
	 */
	public NodeGraphics readNodeGraphicsElement(Element nodeGraphicsElement) throws ParameterException, ParserException {
		Validate.notNull(nodeGraphicsElement);

		String elementType = nodeGraphicsElement.getNodeName();

		if (!elementType.equals("place") && !elementType.equals("transition"))
			throw new ParserException("The node must be of the type \"place\" or \"transition\".");

		NodeList graphicsList = nodeGraphicsElement.getElementsByTagName("graphics");
		for (int placeTagIndex = 0; placeTagIndex < graphicsList.getLength(); placeTagIndex++) {
			if (graphicsList.item(placeTagIndex).getParentNode().equals(nodeGraphicsElement) && graphicsList.item(placeTagIndex).getNodeType() == Node.ELEMENT_NODE) {
				NodeGraphics nodeGraphics = new NodeGraphics();
				Element graphics = (Element) graphicsList.item(placeTagIndex);

				// dimension, fill, line, position
				if (graphics.getElementsByTagName("dimension").getLength() > 0) {
					Node dimension = graphics.getElementsByTagName("dimension").item(0);
					nodeGraphics.setDimension(readDimension((Element) dimension));
				}
				if (graphics.getElementsByTagName("fill").getLength() > 0) {
					Node fill = graphics.getElementsByTagName("fill").item(0);
					nodeGraphics.setFill(readFill((Element) fill));
				}
				if (graphics.getElementsByTagName("line").getLength() > 0) {
					Node line = graphics.getElementsByTagName("line").item(0);
					nodeGraphics.setLine(readLine((Element) line));
				}
				if (graphics.getElementsByTagName("position").getLength() > 0) {
					Node position = graphics.getElementsByTagName("position").item(0);
					nodeGraphics.setPosition(readPosition((Element) position));
				}

				return nodeGraphics;
			}
		}
		// No graphics found
		return null;
	}

	/**
	 * Reads an offset tag and returns it as {@link Offset}. If validated, an offset tag must contain a x and a y value. If one of them is missed, its value will be set to 0.
	 */
	public Offset readOffset(Element offsetNode) throws ParameterException {
		Validate.notNull(offsetNode);

		Offset offset = new Offset();

		// read and set x and y values
		Attr offsXAttr = offsetNode.getAttributeNode("x");
		if (offsXAttr != null) {
			String offsXStr = offsXAttr.getValue();
			if (offsXStr != null && offsXStr.length() > 0) {
				double offsetX = Double.parseDouble(offsXStr);
				offset.setX(offsetX);
			}
		}

		Attr offsYAttr = offsetNode.getAttributeNode("y");
		if (offsYAttr != null) {
			String offsYStr = offsYAttr.getValue();
			if (offsYStr != null && offsYStr.length() > 0) {
				double offsetY = Double.parseDouble(offsYStr);
				offset.setY(offsetY);
			}
		}

		return offset;
	}

	/**
	 * Reads a position tag and returns it as {@link Position}. If validated, a position tag must contain a x and a y value. If one of them is missed, its value will be set to 0.
	 */
	public Position readPosition(Element positionNode) throws ParameterException {
		Validate.notNull(positionNode);

		Position position = new Position();

		// read and set x and y values
		Attr posXAttr = positionNode.getAttributeNode("x");
		if (posXAttr != null) {
			String posXStr = posXAttr.getValue();
			if (posXStr != null && posXStr.length() > 0) {
				double posX = Double.parseDouble(posXStr);
				position.setX(posX);
			}
		}

		Attr posYAttr = positionNode.getAttributeNode("y");
		if (posYAttr != null) {
			String posYStr = posYAttr.getValue();
			if (posYStr != null && posYStr.length() > 0) {
				double posY = Double.parseDouble(posYStr);
				position.setY(posY);
			}
		}

		return position;
	}

	/**
	 * Reads a silent tag and returns it as {@link Boolean}. If it's not found, the method returns <code>false</code>.
	 */
	public boolean readSilent(Element transitionElement) throws ParameterException {
		Validate.notNull(transitionElement);
		
		NodeList silentList = transitionElement.getElementsByTagName("silent");
		for (int i = 0; i < silentList.getLength(); i++) {
			if (silentList.item(i).getNodeType() == Node.ELEMENT_NODE && silentList.item(i).getParentNode().equals(transitionElement)) {
				Element silentElement = (Element) silentList.item(i);
				String silent = silentElement.getTextContent();
				if (silent.equals("true")) {
					return true;
				}
			}
		}
		
		return false;
	}

	/**
	 * Reads the content of text tags and returns them as string.
	 */
	public String readText(Node textNode) throws XMLParserException, ParameterException {
		Validate.notNull(textNode);

		if (textNode.getNodeType() != Node.ELEMENT_NODE)
			throw new XMLParserException(de.invation.code.toval.parser.XMLParserException.ErrorCode.TAGSTRUCTURE);

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
	 * Gets a tokenposition node and reads its x and y attributes. Returns a {@link Position}.
	 */
	public Position readTokenPosition(Element tokenPositionNode) throws ParameterException {
		Validate.notNull(tokenPositionNode);

		Position tokenPosition = new Position();

		// read and set x and y values
		Attr posXAttr = tokenPositionNode.getAttributeNode("x");
		if (posXAttr != null) {
			String posXStr = posXAttr.getValue();
			if (posXStr != null && posXStr.length() > 0) {
				double posX = Double.parseDouble(posXStr);
				tokenPosition.setX(posX);
			}
		}

		Attr posYAttr = tokenPositionNode.getAttributeNode("y");
		if (posYAttr != null) {
			String posYStr = posYAttr.getValue();
			if (posYStr != null && posYStr.length() > 0) {
				double posY = Double.parseDouble(posYStr);
				tokenPosition.setY(posY);
			}
		}

		return tokenPosition;
	}

	public void setGraphics(G graphics) throws ParameterException {
		Validate.notNull(graphics);

		this.graphics = graphics;
	}

	public void setNet(N net) throws ParameterException {
		Validate.notNull(graphics);

		this.net = net;
	}
}
