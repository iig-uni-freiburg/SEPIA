package de.uni.freiburg.iig.telematik.sepia.serialize;

import java.util.Set;

import org.w3c.dom.Element;

import de.invation.code.toval.validate.ParameterException;
import de.uni.freiburg.iig.telematik.sepia.graphic.AbstractGraphicalPN;
import de.uni.freiburg.iig.telematik.sepia.graphic.netgraphics.AnnotationGraphics;
import de.uni.freiburg.iig.telematik.sepia.graphic.netgraphics.ArcGraphics;
import de.uni.freiburg.iig.telematik.sepia.graphic.netgraphics.NodeGraphics;
import de.uni.freiburg.iig.telematik.sepia.graphic.netgraphics.TokenGraphics;
import de.uni.freiburg.iig.telematik.sepia.petrinet.AbstractFlowRelation;
import de.uni.freiburg.iig.telematik.sepia.petrinet.AbstractMarking;
import de.uni.freiburg.iig.telematik.sepia.petrinet.AbstractPetriNet;
import de.uni.freiburg.iig.telematik.sepia.petrinet.AbstractPlace;
import de.uni.freiburg.iig.telematik.sepia.petrinet.AbstractTransition;
import de.uni.freiburg.iig.telematik.sepia.serialize.formats.PNFF_PNML;

public abstract class PNSerializer_PNML<P extends AbstractPlace<F,S>, 
   							   T extends AbstractTransition<F,S>, 
   							   F extends AbstractFlowRelation<P,T,S>, 
   							   M extends AbstractMarking<S>, 
   							   S extends Object>  extends PNSerializer_XML<P, T, F, M, S> {

	public PNSerializer_PNML(AbstractGraphicalPN<P, T, F, M, S> petriNet) throws ParameterException {
		super(petriNet);
	}

	public PNSerializer_PNML(AbstractPetriNet<P, T, F, M, S> petriNet) throws ParameterException {
		super(petriNet);
	}
	
	@Override
	protected void initialize() throws ParameterException{
		support = new PNMLSerializationSupport(getPetriNet().getName(), getPetriNet().getNetType());
	}
	
	@Override
	protected String getRootElementName(){
		return "pnml";
	}

	@Override
	public void addContent() throws SerializationException {
		addHeader();
		
		addPlaceInformation();
		addTransitionInformation();
		addArcInformation();
		
		addFooter();
	}
	
	protected void addHeader(){}
	
	protected void addFooter(){}
	
	private void addPlaceInformation(){
		Element placeElement = null;
		for(P place: getPetriNet().getPlaces()){
			placeElement = getSupport().createElement("place");
			placeElement.setAttribute("id", place.getName());
			
			// Add label information
			AnnotationGraphics annotationGraphics = null;
			if(hasGraphics()){
				annotationGraphics = graphics.getPlaceLabelAnnotationGraphics().get(place.getName());
			}
			placeElement.appendChild(getSupport().createNameElement(place.getLabel(), annotationGraphics));
			
			// Add graphics information
			if(hasGraphics()){
				NodeGraphics placeGraphics = graphics.getPlaceGraphics().get(place.getName());
				if(placeGraphics != null && placeGraphics.hasContent()){
					Element graphicsElement = getSupport().createNodeGraphicsElement(placeGraphics);
					if(graphicsElement != null)
						placeElement.appendChild(graphicsElement);
				}
			}
			
			// Add capacity information
			addCapacity(place, placeElement);
	
			// Add initial marking information
			if(getPetriNet().getInitialMarking().contains(place.getName())){
				try {
					Element markingElement = addInitialMarking(placeElement, getPetriNet().getInitialMarking().get(place.getName()));
					if(hasGraphics()){
						Set<TokenGraphics> tokenGraphics = graphics.getTokenGraphics().get(place.getName());
						addTokenGraphics(markingElement, tokenGraphics);
					}
				} catch (ParameterException e) {
					// Should not happen, since we know, that the initial marking contains the place
					e.printStackTrace();
				}
			}
			
			// Add additional information
			appendPlaceInformation(place, placeElement);
			
			getSupport().getPageElement().appendChild(placeElement);
		}
	}
	
	protected void appendPlaceInformation(P place, Element placeElement){}
	
	protected void addCapacity(P place, Element placeElement){
		if(place.getCapacity() >= 0){
			Element capacityElement = getSupport().createTextElement("capacity", new Integer(place.getCapacity()).toString());
			if(capacityElement != null){
				placeElement.appendChild(capacityElement);
			}
		}
	}
	
	protected void addTokenGraphics(Element markingElement, Set<TokenGraphics> tokenGraphics){
		if(tokenGraphics != null && !tokenGraphics.isEmpty()){
			markingElement.appendChild(createTokenGraphicsElement(tokenGraphics));
		}
	}
	
	protected Element createTokenGraphicsElement(Set<TokenGraphics> tokenGraphics){
		Element toolElement = getSupport().createToolSpecificElement();
	
		Element tokenGraphicsElement = getSupport().createElement("tokengraphics");
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
	
	private void addTransitionInformation(){
		Element transitionElement = null;
		for(T transition: getPetriNet().getTransitions()){
			transitionElement = createElement("transition");
			transitionElement.setAttribute("id", transition.getName());
			
			// Add label information
			AnnotationGraphics annotationGraphics = null;
			if(hasGraphics()){
				annotationGraphics = graphics.getTransitionLabelAnnotationGraphics().get(transition.getName());
			}
			transitionElement.appendChild(getSupport().createNameElement(transition.getLabel(), annotationGraphics));
			
			// Add graphics information
			if(hasGraphics()){
				NodeGraphics transitionGraphics = graphics.getTransitionGraphics().get(transition.getName());
				if(transitionGraphics != null && transitionGraphics.hasContent()){
					Element graphicsElement = getSupport().createNodeGraphicsElement(transitionGraphics);
					if(graphicsElement != null)
						transitionElement.appendChild(graphicsElement);
				}
			}
			
			// Add additional information
			appendTransitionInformation(transition, transitionElement);
			
			getSupport().getPageElement().appendChild(transitionElement);
		}
	}
	
	protected void appendTransitionInformation(T transition, Element transitionElement){}
	
	private void addArcInformation(){
		Element arcElement = null;
		for(F relation: getPetriNet().getFlowRelations()){
			arcElement = createElement("arc");
			arcElement.setAttribute("id", relation.getName());
			
			// Add source and target information
			arcElement.setAttribute("source", relation.getSource().getName());
			arcElement.setAttribute("target", relation.getTarget().getName());
			
			// Add graphics information
			if(hasGraphics()){
				ArcGraphics arcGraphics = graphics.getArcGraphics().get(relation.getName());
				if(arcGraphics != null && arcGraphics.hasContent()){
					Element graphicsElement =getSupport(). createArcGraphicsElement(arcGraphics);
					if(graphicsElement != null)
						arcElement.appendChild(graphicsElement);
				}
			}
			
			// Add constraint information
			AnnotationGraphics annotationGraphics = null;
			if(hasGraphics()){
				annotationGraphics = graphics.getArcAnnotationGraphics().get(relation.getName());
			}
			addConstraint(arcElement, relation.getConstraint(), annotationGraphics);
			
			getSupport().getPageElement().appendChild(arcElement);
		}
	}
	
	protected abstract void addConstraint(Element arcElement, S constraint, AnnotationGraphics annotationGraphics);
	
	@Override
	protected String getFileExtension(){
		return new PNFF_PNML().getFileExtension();
	}
	
	@Override
	public PNMLSerializationSupport getSupport(){
		return (PNMLSerializationSupport) super.getSupport();
	}

}
