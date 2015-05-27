package de.uni.freiburg.iig.telematik.sepia.serialize;

import java.io.IOException;

import de.uni.freiburg.iig.telematik.sepia.graphic.AbstractGraphicalPN;
import de.uni.freiburg.iig.telematik.sepia.graphic.netgraphics.AbstractPNGraphics;
import de.uni.freiburg.iig.telematik.sepia.petrinet.abstr.AbstractFlowRelation;
import de.uni.freiburg.iig.telematik.sepia.petrinet.abstr.AbstractMarking;
import de.uni.freiburg.iig.telematik.sepia.petrinet.abstr.AbstractPetriNet;
import de.uni.freiburg.iig.telematik.sepia.petrinet.abstr.AbstractPlace;
import de.uni.freiburg.iig.telematik.sepia.petrinet.abstr.AbstractTransition;

public abstract class PNSerializer_XML<P extends AbstractPlace<F,S>, 
	   								   T extends AbstractTransition<F,S>, 
	   								   F extends AbstractFlowRelation<P,T,S>, 
	   								   M extends AbstractMarking<S>, 
	   								   S extends Object,
	   								   N extends AbstractPetriNet<P,T,F,M,S>,
	   							  	   G extends AbstractPNGraphics<P,T,F,M,S>>  extends PNSerializer<P, T, F, M, S, N, G> {
	
	public PNSerializer_XML(AbstractGraphicalPN<P, T, F, M, S, N, G> petriNet) {
		super(petriNet);
		initialize();
	}

	public PNSerializer_XML(N petriNet) {
		super(petriNet);
		initialize();
	}
	
	protected void initialize() {
		support = new XMLSerializationSupport(getRootElementName());
	}
	
	protected abstract String getRootElementName();
	
	protected abstract void addContent() throws SerializationException;
	
	@Override
	public String serialize() throws SerializationException {
		addContent();
		return support.serialize();
	}
	
	@Override
	public void serialize(String path, String fileName) throws SerializationException, IOException {
		addContent();
		support.serialize(path, fileName, getFileExtension());
	}
	
	protected abstract String getFileExtension();
	
}
