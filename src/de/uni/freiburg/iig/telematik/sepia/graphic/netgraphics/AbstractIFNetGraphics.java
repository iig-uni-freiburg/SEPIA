package de.uni.freiburg.iig.telematik.sepia.graphic.netgraphics;

import java.util.HashMap;
import java.util.Map;

import de.uni.freiburg.iig.telematik.sepia.graphic.netgraphics.attributes.Position;
import de.uni.freiburg.iig.telematik.sepia.petrinet.ifnet.abstr.AbstractIFNetFlowRelation;
import de.uni.freiburg.iig.telematik.sepia.petrinet.ifnet.abstr.AbstractIFNetMarking;
import de.uni.freiburg.iig.telematik.sepia.petrinet.ifnet.abstr.AbstractIFNetPlace;
import de.uni.freiburg.iig.telematik.sepia.petrinet.ifnet.abstr.AbstractIFNetTransition;

public class AbstractIFNetGraphics<P extends AbstractIFNetPlace<F>,
 								   T extends AbstractIFNetTransition<F>, 
 								   F extends AbstractIFNetFlowRelation<P,T>, 
 								   M extends AbstractIFNetMarking> extends  AbstractCWNGraphics<P, T, F, M> {
	
	private Map<String, AnnotationGraphics> accessFunctionGraphics = new HashMap<String, AnnotationGraphics>();
	private Map<String, AnnotationGraphics> subjectGraphics = new HashMap<String, AnnotationGraphics>();

	private Position clearancesPosition = new Position();
	private Position tokenLabelsPosition = new Position();

	public Map<String, AnnotationGraphics> getAccessFunctionGraphics() {
		return accessFunctionGraphics;
	}

	public Position getClearancesPosition() {
		return clearancesPosition;
	}
	
	public Map<String, AnnotationGraphics> getSubjectGraphics() {
		return subjectGraphics;
	}

	public Position getTokenLabelsPosition() {
		return tokenLabelsPosition;
	}

	public void setAccessFunctionGraphics(Map<String, AnnotationGraphics> accessFunctionGraphics) {
		this.accessFunctionGraphics = accessFunctionGraphics;
	}

	public void setClearancesPosition(Position clearancesPosition) {
		this.clearancesPosition = clearancesPosition;
	}

	public void setSubjectGraphics(Map<String, AnnotationGraphics> subjectGraphics) {
		this.subjectGraphics = subjectGraphics;
	}

	public void setTokenLabelsPosition(Position tokenLabelsPosition) {
		this.tokenLabelsPosition = tokenLabelsPosition;
	}

	public String toString() {
		StringBuilder str = new StringBuilder();
		str.append(super.toString());

		str.append("                      tokenColors# " + colors.size());
		if (colors.size() > 0)
			str.append(":\n" + map2Str(colors) + "\n");
		else
			str.append("\n");
		str.append("           accessFunctionGraphics# " + accessFunctionGraphics.size());
		if (accessFunctionGraphics.size() > 0)
			str.append(":\n" + map2Str(accessFunctionGraphics) + "\n");
		else
			str.append("\n");
		str.append("                  subjectGraphics# " + subjectGraphics.size());
		if (subjectGraphics.size() > 0)
			str.append(":\n" + map2Str(subjectGraphics) + "\n");
		else
			str.append("\n");
		if (clearancesPosition != null)
			str.append("               clearancesPosition#:   " + clearancesPosition + "\n");
		if (tokenLabelsPosition != null)
			str.append("              tokenLabelsPosition#:   " + tokenLabelsPosition + "\n");

		return str.toString();
	}

}
