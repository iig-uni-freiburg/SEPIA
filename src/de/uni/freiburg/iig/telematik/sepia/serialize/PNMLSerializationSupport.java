package de.uni.freiburg.iig.telematik.sepia.serialize;

import java.net.URI;
import java.util.Vector;

import org.w3c.dom.Element;

import de.invation.code.toval.validate.ParameterException;
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
import de.uni.freiburg.iig.telematik.sepia.petrinet.NetType;

public class PNMLSerializationSupport extends XMLSerializationSupport{
	
	protected Element netElement = null;
	protected Element pageElement = null;
	
	public PNMLSerializationSupport(String netName, NetType netType) {
		super("pnml");
		getRootElement().setAttribute("xmlns", "http://www.pnml.org/version-2009/grammar/pnml");
		addNetAndPageElements(netName, netType);
	}
	
	protected void addNetAndPageElements(String netName, NetType netType) {
		netElement = createElement("net");
		netElement.setAttribute("id", netName);
		try {
			netElement.setAttribute("type", NetType.getURL(netType).toString());
		} catch (ParameterException e) {
			throw new ParameterException("Cannot set net type.\n Reason: " + e.getMessage());
		}
		getRootElement().appendChild(netElement);
		
		pageElement = createElement("page");
		pageElement.setAttribute("id", "top-level");
		netElement.appendChild(pageElement);
	}
	
	public Element getNetElement() {
		return netElement;
	}

	public Element getPageElement() {
		return pageElement;
	}
	
	//------- Methods for PNML-tag generation ---------------------------------------------------
	

		protected Element createToolSpecificElement(String tool, String version){
			Element toolElement = createElement("toolspecific");
			toolElement.setAttribute("tool", tool);
			toolElement.setAttribute("version", version);
			return toolElement;
		}
		
		protected Element createNameElement(String label, AnnotationGraphics graphics) {
			Element nameElement = createElement("name");
			nameElement.appendChild(createTextElement("text", label));
			if(graphics != null && graphics.hasContent()){
				Element graphicsElement = createTextGraphicsElement(graphics);
				if(graphicsElement != null)
					nameElement.appendChild(graphicsElement);
				nameElement.appendChild(createAnnotationVisibilityElement(graphics.isVisible()));
			}
			return nameElement;
		}
		
		public Element createTextGraphicsElement(AnnotationGraphics annotationGraphics){
			Element graphicsElement = createElement("graphics");
			
			Offset offset = annotationGraphics.getOffset();
			if(offset != null && offset.hasContent()){
				graphicsElement.appendChild(createOffsetElement(offset));
			}
			Fill fill = annotationGraphics.getFill();
			if(fill != null && fill.hasContent()){
				graphicsElement.appendChild(createFillElement(fill));
			}
			Line line = annotationGraphics.getLine();
			if(line != null && line.hasContent()){
				graphicsElement.appendChild(createLineElement(line));
			}
			Font font = annotationGraphics.getFont();
			if(font != null && font.hasContent()){
				graphicsElement.appendChild(createFontElement(font));
			}
			
			if(graphicsElement.getChildNodes().getLength() == 0)
				return null;
			return graphicsElement;
		}
		
		protected Element createArcGraphicsElement(ArcGraphics arcGraphics){
			Element graphicsElement = createElement("graphics");
			
			Line line = arcGraphics.getLine();
			if(line != null && line.hasContent()){
				graphicsElement.appendChild(createLineElement(line));
			}
			Vector<Position> positions = arcGraphics.getPositions();
			if(positions != null && !positions.isEmpty()){
				for(Position position: positions){
					if(position != null && position.hasContent()){
						graphicsElement.appendChild(createPositionElement(position));
					}
				}
			}
			
			if(graphicsElement.getChildNodes().getLength() == 0)
				return null;
			return graphicsElement;
		}

		protected Element createNodeGraphicsElement(NodeGraphics nodeGraphics){
			Element graphicsElement = createElement("graphics");
			
			Dimension dimension = nodeGraphics.getDimension();
			if(dimension != null && dimension.hasContent()){
				graphicsElement.appendChild(createDimensionElement(dimension));
			}
			Position position = nodeGraphics.getPosition();
			if(position != null && position.hasContent()){
				graphicsElement.appendChild(createPositionElement(position));
			}
			Fill fill = nodeGraphics.getFill();
			if(fill != null && fill.hasContent()){
				graphicsElement.appendChild(createFillElement(fill));
			}
			Line line = nodeGraphics.getLine();
			if(line != null && line.hasContent()){
				graphicsElement.appendChild(createLineElement(line));
			}
			
			if(graphicsElement.getChildNodes().getLength() == 0)
				return null;
			return graphicsElement;
		}

		protected Element createAnnotationVisibilityElement(boolean visible){
			Element toolElement = createToolSpecificElement("de.uni-freiburg.telematik.editor", "1.0");
			
			Element visibleElement = createTextElement("visible", String.valueOf(visible));
			toolElement.appendChild(visibleElement);
			
			return toolElement;
		}
		
		private Element createDimensionElement(Dimension dimension){
			Element dimensionElement = createElement("dimension");
			dimensionElement.setAttribute("x", ((Double) dimension.getX()).toString());
			dimensionElement.setAttribute("y", ((Double) dimension.getY()).toString());
			return dimensionElement;
		}
		
		public Element createPositionElement(Position position){
			Element positionElement = createElement("position");
			positionElement.setAttribute("x", ((Double) position.getX()).toString());
			positionElement.setAttribute("y", ((Double) position.getY()).toString());
			return positionElement;
		}
		
		private Element createOffsetElement(Offset offset){
			Element positionElement = createElement("offset");
			positionElement.setAttribute("x", ((Double) offset.getX()).toString());
			positionElement.setAttribute("y", ((Double) offset.getY()).toString());
			return positionElement;
		}
		
		private Element createFillElement(Fill fill){
			Element fillElement = createElement("fill");

			String color = fill.getColor();
			if(color != null){
				fillElement.setAttribute("color", color);
			}
			String gradientColor = fill.getGradientColor();
			if(gradientColor != null){
				fillElement.setAttribute("gradient-color", gradientColor);
			}
			URI image = fill.getImage();
			if(image != null){
				fillElement.setAttribute("image", image.toString());
			}
			GradientRotation gradientRotation = fill.getGradientRotation();
			if(gradientRotation != null){
				fillElement.setAttribute("gradient-rotation", gradientRotation.toString());
			}
			
			return fillElement;
		}
		
		private Element createLineElement(Line line){
			Element lineElement = createElement("line");
			
			Shape shape = line.getShape();
			if(shape != null){
				lineElement.setAttribute("shape", shape.toString());
			}
			String color = line.getColor();
			if(color != null){
				lineElement.setAttribute("color", color);
			}
			Double width = line.getWidth();
			if(width != null){
				lineElement.setAttribute("width", width.toString());
			}
			Style style = line.getStyle();
			if(style != null){
				lineElement.setAttribute("style", style.toString());
			}
			
			return lineElement;
		}
		
		private Element createFontElement(Font font){
			Element fontElement = createElement("font");

			String family = font.getFamily();
			if(family != null){
				fontElement.setAttribute("family", family);
			}
			String size = font.getSize();
			if(size != null){
				fontElement.setAttribute("size", size);
			}
			String style = font.getStyle();
			if(style != null){
				fontElement.setAttribute("style", style);
			}
			String weight = font.getWeight();
			if(weight != null){
				fontElement.setAttribute("weight", weight);
			}
			Align align = font.getAlign();
			if(align != null){
				fontElement.setAttribute("align", align.toString());
			}
			Decoration decoration = font.getDecoration();
			if(decoration != null){
				fontElement.setAttribute("decoration", decoration.toString());
			}
			Double rotation = font.getRotation();
			if(rotation != null){
				fontElement.setAttribute("rotation", rotation.toString());
			}
			
			return fontElement;
		}

}
