package de.uni.freiburg.iig.telematik.sepia.serialize.serializer;

import java.util.Map;
import java.util.Set;

import org.w3c.dom.Element;

import de.invation.code.toval.types.Multiset;
import de.invation.code.toval.validate.ParameterException;
import de.uni.freiburg.iig.telematik.sepia.graphic.AbstractGraphicalPN;
import de.uni.freiburg.iig.telematik.sepia.graphic.netgraphics.AbstractIFNetGraphics;
import de.uni.freiburg.iig.telematik.sepia.graphic.netgraphics.AnnotationGraphics;
import de.uni.freiburg.iig.telematik.sepia.graphic.netgraphics.attributes.Position;
import de.uni.freiburg.iig.telematik.sepia.petrinet.AbstractPetriNet;
import de.uni.freiburg.iig.telematik.sepia.petrinet.ifnet.RegularIFNetTransition;
import de.uni.freiburg.iig.telematik.sepia.petrinet.ifnet.abstr.AbstractIFNet;
import de.uni.freiburg.iig.telematik.sepia.petrinet.ifnet.abstr.AbstractIFNetFlowRelation;
import de.uni.freiburg.iig.telematik.sepia.petrinet.ifnet.abstr.AbstractIFNetMarking;
import de.uni.freiburg.iig.telematik.sepia.petrinet.ifnet.abstr.AbstractIFNetPlace;
import de.uni.freiburg.iig.telematik.sepia.petrinet.ifnet.abstr.AbstractIFNetTransition;
import de.uni.freiburg.iig.telematik.sepia.petrinet.ifnet.abstr.TransitionType;
import de.uni.freiburg.iig.telematik.sepia.petrinet.ifnet.concepts.AccessMode;

public class IFNetSerializer_PNML<P extends AbstractIFNetPlace<F>, 
								  T extends AbstractIFNetTransition<F>, 
								  F extends AbstractIFNetFlowRelation<P,T>, 
								  M extends AbstractIFNetMarking> extends CPNSerializer_PNML<P, T, F, M> {

	public IFNetSerializer_PNML(AbstractGraphicalPN<P, T, F, M, Multiset<String>> ifNet) throws ParameterException {
		super(ifNet);
	}

	public IFNetSerializer_PNML(AbstractPetriNet<P, T, F, M, Multiset<String>> ifNet) throws ParameterException {
		super(ifNet);
	}
	
	@Override
	protected void addHeader() {
		super.addHeader();
		
		// Add classification position information
		if(hasGraphics()){
			Element classificationPositionsElement = createElement("classificationpositions");
			// Add clearance position information
			Position clearancePosition = getGraphics().getClearancesPosition();
			if(clearancePosition != null && clearancePosition.hasContent()){
				Element clearancesElement = createElement("clearances");
				Element positionElement = createPositionElement(clearancePosition);
				if(positionElement != null){
					clearancesElement.appendChild(positionElement);
				}
				classificationPositionsElement.appendChild(clearancesElement);
			}
			
			// Add token label position information
			Position tokenLabelPosition = getGraphics().getTokenLabelsPosition();
			if(tokenLabelPosition != null && tokenLabelPosition.hasContent()){
				Element tokenLabelsElement = createElement("tokenlabels");
				Element positionElement = createPositionElement(tokenLabelPosition);
				if(positionElement != null){
					tokenLabelsElement.appendChild(positionElement);
				}
				classificationPositionsElement.appendChild(tokenLabelsElement);
			}
			if(classificationPositionsElement.getChildNodes().getLength() > 0)
				netElement.appendChild(classificationPositionsElement);
		}
	}

	@Override
	protected void appendTransitionInformation(AbstractIFNetTransition transition, Element transitionElement) {
		// Add transition type information
		Element transitionTypeElement = createTextElement("transitiontype", transition.getType().toString().toLowerCase());
		transitionElement.appendChild(transitionTypeElement);
		
//		<subject>
//        <text>subjectA</text>
//        <graphics>
//          <offset x="0" y="5"/>
//        </graphics>
//      </subject>
		
		
		if(transition.getType() == TransitionType.REGULAR){
			RegularIFNetTransition regularTransition = (RegularIFNetTransition) transition;
			
			// Add subject graphics
			if(hasGraphics()){
				AnnotationGraphics subjectGraphics = getGraphics().getSubjectGraphics().get(transition.getName());
				if(subjectGraphics != null && subjectGraphics.hasContent()){
					Element subjectGraphicsRootElement = createElement("subjectgraphics");
					Element subjectGraphicsElement = createTextGraphicsElement(subjectGraphics);
					if(subjectGraphicsElement != null){
						subjectGraphicsRootElement.appendChild(subjectGraphicsElement);
						transitionElement.appendChild(subjectGraphicsRootElement);
					}
				}
			}
			
			// Add data access information
			Map<String, Set<AccessMode>> accessModes = regularTransition.getAccessModes();
			if(!accessModes.isEmpty()){
				Element accessFunctionsElement = createElement("accessfunctions");
				for(String color: accessModes.keySet()){
					if(accessModes.get(color).isEmpty())
						continue;
					
					Element accessfunctionElement = createElement("accessfunction");
					accessfunctionElement.appendChild(createTextElement("color", color));
					Element accessModesElement = createElement("accessmodes");
					for(AccessMode mode: AccessMode.values()){
						if(accessModes.get(color).contains(mode)){
							accessModesElement.appendChild(createTextElement(mode.toString().toLowerCase(), "true"));
						} else {
							accessModesElement.appendChild(createTextElement(mode.toString().toLowerCase(), "false"));
						}
					}
					accessfunctionElement.appendChild(accessModesElement);
					accessFunctionsElement.appendChild(accessfunctionElement);
				}
				
				// Add access modes graphics
				if(hasGraphics()){
					AnnotationGraphics functionGraphics = getGraphics().getAccessFunctionGraphics().get(transition.getName());
					if(functionGraphics != null && functionGraphics.hasContent()){
						Element graphicsElement = createTextGraphicsElement(functionGraphics);
						if(graphicsElement != null){
							accessFunctionsElement.appendChild(graphicsElement);
						}
					}
				}
				
				transitionElement.appendChild(accessFunctionsElement);
			}
		}
		
	}

	@Override
	public AbstractIFNet<P,T,F,M,?,?> getPetriNet() {
		return (AbstractIFNet<P,T,F,M,?,?>) super.getPetriNet();
	}

	@Override
	public AbstractIFNetGraphics<P, T, F, M> getGraphics() {
		return (AbstractIFNetGraphics<P, T, F, M>) super.getGraphics();
	}

	

}
