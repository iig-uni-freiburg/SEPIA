package de.uni.freiburg.iig.telematik.sepia.graphic;

import java.util.HashMap;
import java.util.Map;

import de.invation.code.toval.types.Multiset;
import de.uni.freiburg.iig.telematik.sepia.graphic.attributes.Offset;
import de.uni.freiburg.iig.telematik.sepia.petrinet.ifnet.AbstractIFNetTransition;
import de.uni.freiburg.iig.telematik.sepia.petrinet.ifnet.IFNetFlowRelation;
import de.uni.freiburg.iig.telematik.sepia.petrinet.ifnet.IFNetMarking;
import de.uni.freiburg.iig.telematik.sepia.petrinet.ifnet.IFNetPlace;

public class IFNetGraphics extends PNGraphics<IFNetPlace, AbstractIFNetTransition, IFNetFlowRelation, IFNetMarking, Multiset<String>>{
	
	private Map<IFNetPlace, Offset> subjectGraphics = new HashMap<IFNetPlace, Offset>();
	private Map<AbstractIFNetTransition, Offset> accessFunctionGraphics = new HashMap<AbstractIFNetTransition, Offset>();
	
	
	public Map<IFNetPlace, Offset> getSubjectGraphics() {
		return subjectGraphics;
	}
	
	public void setSubjectGraphics(Map<IFNetPlace, Offset> subjectGraphics) {
		this.subjectGraphics = subjectGraphics;
	}

	public Map<AbstractIFNetTransition, Offset> getAccessFunctionGraphics() {
		return accessFunctionGraphics;
	}

	public void setAccessFunctionGraphics(
			Map<AbstractIFNetTransition, Offset> accessFunctionGraphics) {
		this.accessFunctionGraphics = accessFunctionGraphics;
	}
	
	

}
