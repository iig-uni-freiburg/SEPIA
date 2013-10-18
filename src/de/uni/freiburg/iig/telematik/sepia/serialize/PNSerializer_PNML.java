package de.uni.freiburg.iig.telematik.sepia.serialize;

import java.net.URI;
import java.util.Set;
import java.util.Vector;

import org.w3c.dom.Element;

import de.invation.code.toval.validate.ParameterException;
import de.uni.freiburg.iig.telematik.sepia.graphic.AbstractGraphicalPN;
import de.uni.freiburg.iig.telematik.sepia.graphic.netgraphics.AnnotationGraphics;
import de.uni.freiburg.iig.telematik.sepia.graphic.netgraphics.ArcGraphics;
import de.uni.freiburg.iig.telematik.sepia.graphic.netgraphics.NodeGraphics;
import de.uni.freiburg.iig.telematik.sepia.graphic.netgraphics.TokenGraphics;
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
import de.uni.freiburg.iig.telematik.sepia.petrinet.AbstractFlowRelation;
import de.uni.freiburg.iig.telematik.sepia.petrinet.AbstractMarking;
import de.uni.freiburg.iig.telematik.sepia.petrinet.AbstractPetriNet;
import de.uni.freiburg.iig.telematik.sepia.petrinet.AbstractPlace;
import de.uni.freiburg.iig.telematik.sepia.petrinet.AbstractTransition;
import de.uni.freiburg.iig.telematik.sepia.petrinet.NetType;

public abstract class PNSerializer_PNML<P extends AbstractPlace<F,S>, 
   							   T extends AbstractTransition<F,S>, 
   							   F extends AbstractFlowRelation<P,T,S>, 
   							   M extends AbstractMarking<S>, 
   							   S extends Object>  extends PNSerializer_XML<P, T, F, M, S> {
	
	protected Element netElement = null;
	protected Element pageElement = null;

	public PNSerializer_PNML(AbstractGraphicalPN<P, T, F, M, S> petriNet) throws ParameterException {
		super(petriNet);
	}

	public PNSerializer_PNML(AbstractPetriNet<P, T, F, M, S> petriNet) throws ParameterException {
		super(petriNet);
	}

	@Override
	protected Element createRootElement(){
		Element rootElement = createElement("pnml");
		rootElement.setAttribute("xmlns", "http://www.pnml.org/version-2009/grammar/pnml");
		return rootElement;
	}


	@Override
	public void addContent() throws SerializationException {
		netElement = createElement("net");
		netElement.setAttribute("id", getPetriNet().getName());
		try {
			netElement.setAttribute("type", NetType.getURL(getPetriNet().getNetType()).toString());
		} catch (ParameterException e) {
			throw new SerializationException("Cannot set net type.\n Reason: " + e.getMessage());
		}
		rootElement.appendChild(netElement);
		
		addHeader();
		
		pageElement = createElement("page");
		pageElement.setAttribute("id", "top-level");
		netElement.appendChild(pageElement);
		
		addPlaceInformation();
		addTransitionInformation();
		addArcInformation();
		
		addFooter();
	}
	
	protected void addHeader(){}
	
	protected void addFooter(){}
	
	protected void addPlaceInformation(){
		Element placeElement = null;
		for(P place: getPetriNet().getPlaces()){
			placeElement = createElement("place");
			placeElement.setAttribute("id", place.getName());
			placeElement.appendChild(createNameElement(place.getLabel(), graphics.getPlaceLabelAnnotationGraphics().get(place.getName())));
			NodeGraphics placeGraphics = graphics.getPlaceGraphics().get(place.getName());
			if(placeGraphics != null && placeGraphics.hasContent()){
				Element graphicsElement = getNodeGraphics(placeGraphics);
				if(graphicsElement != null)
					placeElement.appendChild(graphicsElement);
			}
			if(getPetriNet().getInitialMarking().contains(place.getName())){
				try {
					Element markingElement = addInitialMarking(placeElement, getPetriNet().getInitialMarking().get(place.getName()));
					Set<TokenGraphics> tokenGraphics = graphics.getTokenGraphics().get(place.getName());
					addTokenGraphics(markingElement, tokenGraphics);
				} catch (ParameterException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			pageElement.appendChild(placeElement);
		}
	}
	
	protected void addTokenGraphics(Element markingElement, Set<TokenGraphics> tokenGraphics){
		if(tokenGraphics != null && !tokenGraphics.isEmpty()){
			markingElement.appendChild(createTokenGraphicsElement(tokenGraphics));
		}
	}
	
	protected Element createTokenGraphicsElement(Set<TokenGraphics> tokenGraphics){
		Element toolElement = getToolSpecificElement();
	
		Element tokenGraphicsElement = createElement("tokengraphics");
		for(TokenGraphics graphics: tokenGraphics){
			if(!graphics.hasContent())
				continue;
			Element tokenPositionElement = createElement("tokenposition");
			tokenPositionElement.setAttribute("x", ((Double) graphics.getTokenposition().getX()).toString());
			tokenPositionElement.setAttribute("y", ((Double) graphics.getTokenposition().getY()).toString());
			tokenGraphicsElement.appendChild(tokenPositionElement);
		}
		toolElement.appendChild(tokenGraphicsElement);
		
		return toolElement;
	}
	
	protected abstract Element addInitialMarking(Element placeElement, S state);
	
	protected void addTransitionInformation(){
		Element transitionElement = null;
		for(T transition: getPetriNet().getTransitions()){
			transitionElement = createElement("transition");
			transitionElement.setAttribute("id", transition.getName());
			transitionElement.appendChild(createNameElement(transition.getLabel(), graphics.getTransitionLabelAnnotationGraphics().get(transition.getName())));
			NodeGraphics transitionGraphics = graphics.getTransitionGraphics().get(transition.getName());
			if(transitionGraphics != null && transitionGraphics.hasContent()){
				Element graphicsElement = getNodeGraphics(transitionGraphics);
				if(graphicsElement != null)
					transitionElement.appendChild(graphicsElement);
			}
			pageElement.appendChild(transitionElement);
		}
	}
	
	protected void addArcInformation(){
		Element arcElement = null;
		for(F relation: getPetriNet().getFlowRelations()){
			arcElement = createElement("arc");
			arcElement.setAttribute("id", relation.getName());
			arcElement.setAttribute("source", relation.getSource().getName());
			arcElement.setAttribute("target", relation.getTarget().getName());
			
			ArcGraphics arcGraphics = graphics.getArcGraphics().get(relation.getName());
			if(arcGraphics != null && arcGraphics.hasContent()){
				Element graphicsElement = getArcGraphics(arcGraphics);
				if(graphicsElement != null)
					arcElement.appendChild(graphicsElement);
			}
			addConstraint(arcElement, relation.getConstraint(), graphics.getArcAnnotationGraphics().get(relation.getName()));
			pageElement.appendChild(arcElement);
		}
	}
	
	protected abstract void addConstraint(Element arcElement, S constraint, AnnotationGraphics annotationGraphics);
	
	//------- Helper methods for PNML-tag generation ---------------------------------------------------
	
	protected Element getToolSpecificElement(){
		Element toolElement = createElement("toolspecific");
		toolElement.setAttribute("tool", "org.pnml.tool");
		toolElement.setAttribute("version", "1.0");
		return toolElement;
	}
	
	protected Element createNameElement(String label, AnnotationGraphics graphics) {
		Element nameElement = createElement("name");
		nameElement.appendChild(createTextElement("text", label));
		if(graphics != null && graphics.hasContent()){
			Element graphicsElement = getTextGraphics(graphics);
			if(graphicsElement != null)
				nameElement.appendChild(graphicsElement);
		}
		return nameElement;
	}
	
	protected Element getTextGraphics(AnnotationGraphics annotationGraphics){
		Element graphicsElement = createElement("graphics");
		
		Offset offset = annotationGraphics.getOffset();
		if(offset != null && offset.hasContent()){
			graphicsElement.appendChild(getOffsetElement(offset));
		}
		Fill fill = annotationGraphics.getFill();
		if(fill != null && fill.hasContent()){
			graphicsElement.appendChild(getFillElement(fill));
		}
		Line line = annotationGraphics.getLine();
		if(line != null && line.hasContent()){
			graphicsElement.appendChild(getLineElement(line));
		}
		Font font = annotationGraphics.getFont();
		if(font != null && font.hasContent()){
			graphicsElement.appendChild(getFontElement(font));
		}
		
		if(graphicsElement.getChildNodes().getLength() == 0)
			return null;
		return graphicsElement;
	}
	
	protected Element getArcGraphics(ArcGraphics arcGraphics){
		Element graphicsElement = createElement("graphics");
		
		Line line = arcGraphics.getLine();
		if(line != null && line.hasContent()){
			graphicsElement.appendChild(getLineElement(line));
		}
		Vector<Position> positions = arcGraphics.getPositions();
		if(positions != null && !positions.isEmpty()){
			for(Position position: positions){
				if(position != null && position.hasContent()){
					graphicsElement.appendChild(getPositionElement(position));
				}
			}
		}
		
		if(graphicsElement.getChildNodes().getLength() == 0)
			return null;
		return graphicsElement;
	}

	protected Element getNodeGraphics(NodeGraphics nodeGraphics){
		Element graphicsElement = createElement("graphics");
		
		Dimension dimension = nodeGraphics.getDimension();
		if(dimension != null && dimension.hasContent()){
			graphicsElement.appendChild(getDimensionElement(dimension));
		}
		Position position = nodeGraphics.getPosition();
		if(position != null && position.hasContent()){
			graphicsElement.appendChild(getPositionElement(position));
		}
		Fill fill = nodeGraphics.getFill();
		if(fill != null && fill.hasContent()){
			graphicsElement.appendChild(getFillElement(fill));
		}
		Line line = nodeGraphics.getLine();
		if(line != null && line.hasContent()){
			graphicsElement.appendChild(getLineElement(line));
		}
		
		if(graphicsElement.getChildNodes().getLength() == 0)
			return null;
		return graphicsElement;
	}
	
	private Element getDimensionElement(Dimension dimension){
		Element dimensionElement = createElement("dimension");
		dimensionElement.setAttribute("x", ((Double) dimension.getX()).toString());
		dimensionElement.setAttribute("y", ((Double) dimension.getY()).toString());
		return dimensionElement;
	}
	
	private Element getPositionElement(Position position){
		Element positionElement = createElement("position");
		positionElement.setAttribute("x", ((Double) position.getX()).toString());
		positionElement.setAttribute("y", ((Double) position.getY()).toString());
		return positionElement;
	}
	
	private Element getOffsetElement(Offset offset){
		Element positionElement = createElement("offset");
		positionElement.setAttribute("x", ((Double) offset.getX()).toString());
		positionElement.setAttribute("y", ((Double) offset.getY()).toString());
		return positionElement;
	}
	
	private Element getFillElement(Fill fill){
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
	
	private Element getLineElement(Line line){
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
	
	private Element getFontElement(Font font){
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
