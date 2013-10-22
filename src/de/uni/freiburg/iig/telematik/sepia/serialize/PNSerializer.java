package de.uni.freiburg.iig.telematik.sepia.serialize;

import java.io.IOException;

import de.invation.code.toval.validate.ParameterException;
import de.invation.code.toval.validate.ParameterException.ErrorCode;
import de.invation.code.toval.validate.Validate;
import de.uni.freiburg.iig.telematik.sepia.graphic.AbstractGraphicalPN;
import de.uni.freiburg.iig.telematik.sepia.graphic.netgraphics.AbstractPNGraphics;
import de.uni.freiburg.iig.telematik.sepia.petrinet.AbstractFlowRelation;
import de.uni.freiburg.iig.telematik.sepia.petrinet.AbstractMarking;
import de.uni.freiburg.iig.telematik.sepia.petrinet.AbstractPetriNet;
import de.uni.freiburg.iig.telematik.sepia.petrinet.AbstractPlace;
import de.uni.freiburg.iig.telematik.sepia.petrinet.AbstractTransition;
import de.uni.freiburg.iig.telematik.sepia.petrinet.NetType;

public abstract class PNSerializer<P extends AbstractPlace<F,S>, 
   							  	   T extends AbstractTransition<F,S>, 
   							  	   F extends AbstractFlowRelation<P,T,S>, 
   							  	   M extends AbstractMarking<S>, 
   							  	   S extends Object> {
	
	protected AbstractPetriNet<P,T,F,M,S> petriNet = null;
	protected AbstractPNGraphics<P,T,F,M,S> graphics = null;
	protected SerializationSupport support = null;
	
	public PNSerializer(AbstractPetriNet<P,T,F,M,S> petriNet) throws ParameterException{
		validatePetriNet(petriNet);
		this.petriNet = petriNet;
	}
	
	public PNSerializer(AbstractGraphicalPN<P,T,F,M,S> petriNet) throws ParameterException{
		Validate.notNull(petriNet);
		validatePetriNet(petriNet.getPetriNet());
		this.petriNet = petriNet.getPetriNet();
		this.graphics = petriNet.getPetriNetGraphics();
	}
	
	private void validatePetriNet(AbstractPetriNet<P,T,F,M,S> petriNet) throws ParameterException{
		Validate.notNull(petriNet);
		Class<?> requiredClassType = NetType.getClassType(acceptedNetType());
		if(!(requiredClassType.isAssignableFrom(petriNet.getClass()))){
			throw new ParameterException(ErrorCode.INCOMPATIBILITY, "This serializer requires nets of type \""+requiredClassType+"\"\n The given net is of type \""+petriNet.getClass()+"\"");
		}
	}
	
	public AbstractPetriNet<P,T,F,M,S> getPetriNet(){
		return petriNet;
	}
	
	public AbstractPNGraphics<P,T,F,M,S> getGraphics(){
		return graphics;
	}
	
	public SerializationSupport getSupport(){
		return support;
	}
	
	protected boolean hasGraphics(){
		return graphics != null;
	}
	
	public abstract NetType acceptedNetType();
	
	public abstract String serialize() throws SerializationException;
	
	public abstract void serialize(String path, String fileName) throws ParameterException, SerializationException, IOException;
			
}
