package de.uni.freiburg.iig.telematik.sepia.serialize;

import java.io.IOException;

import org.w3c.dom.Element;

import de.invation.code.toval.validate.ParameterException;
import de.uni.freiburg.iig.telematik.sepia.graphic.AbstractGraphicalPN;
import de.uni.freiburg.iig.telematik.sepia.petrinet.AbstractFlowRelation;
import de.uni.freiburg.iig.telematik.sepia.petrinet.AbstractMarking;
import de.uni.freiburg.iig.telematik.sepia.petrinet.AbstractPetriNet;
import de.uni.freiburg.iig.telematik.sepia.petrinet.AbstractPlace;
import de.uni.freiburg.iig.telematik.sepia.petrinet.AbstractTransition;

public abstract class PNSerializer_XML<P extends AbstractPlace<F,S>, 
	   								   T extends AbstractTransition<F,S>, 
	   								   F extends AbstractFlowRelation<P,T,S>, 
	   								   M extends AbstractMarking<S>, 
	   								   S extends Object>  extends PNSerializer<P, T, F, M, S> {
	
	public PNSerializer_XML(AbstractGraphicalPN<P, T, F, M, S> petriNet) throws ParameterException {
		super(petriNet);
		initialize();
	}

	public PNSerializer_XML(AbstractPetriNet<P, T, F, M, S> petriNet) throws ParameterException {
		super(petriNet);
		initialize();
	}
	
	protected void initialize() throws ParameterException{
		support = new XMLSerializationSupport(getRootElementName());
	}
	
	protected abstract String getRootElementName();
	
	protected Element createTextElement(String elementName, String elementText) {
		Element textElement = createElement(elementName);
		textElement.setTextContent(elementText);
		return textElement;
	}
	
	protected Element createElement(String elementName){
		return createElement(elementName);
	}
	
	protected abstract void addContent() throws SerializationException;
	
	@Override
	public String serialize() throws SerializationException {
		addContent();
		return support.serialize();
	}
	
	@Override
	public void serialize(String path, String fileName) throws ParameterException, SerializationException, IOException {
		support.serialize(path, fileName, getFileExtension());
	}
	
	protected abstract String getFileExtension();
	
}
