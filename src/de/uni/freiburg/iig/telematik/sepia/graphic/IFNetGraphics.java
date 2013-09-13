package de.uni.freiburg.iig.telematik.sepia.graphic;

import java.awt.Color;
import java.util.HashMap;
import java.util.Map;

import de.invation.code.toval.types.Multiset;
import de.uni.freiburg.iig.telematik.sepia.petrinet.ifnet.AbstractIFNetTransition;
import de.uni.freiburg.iig.telematik.sepia.petrinet.ifnet.IFNetFlowRelation;
import de.uni.freiburg.iig.telematik.sepia.petrinet.ifnet.IFNetMarking;
import de.uni.freiburg.iig.telematik.sepia.petrinet.ifnet.IFNetPlace;

/**
 * {@link AbstractPNGraphics} implementation for the IFNets.
 * 
 * @author Thomas Stocker
 * @author Adrian Lange
 */
public class IFNetGraphics extends AbstractPNGraphics<IFNetPlace, AbstractIFNetTransition, IFNetFlowRelation, IFNetMarking, Multiset<String>> {

	private Map<AbstractIFNetTransition, AnnotationGraphics> accessFunctionGraphics = new HashMap<AbstractIFNetTransition, AnnotationGraphics>();
	private Map<String, Color> colors = new HashMap<String, Color>();
	private Map<IFNetPlace, AnnotationGraphics> subjectGraphics = new HashMap<IFNetPlace, AnnotationGraphics>();

	public Map<AbstractIFNetTransition, AnnotationGraphics> getAccessFunctionGraphics() {
		return accessFunctionGraphics;
	}

	public Map<String, Color> getColors() {
		return colors;
	}

	public Map<IFNetPlace, AnnotationGraphics> getSubjectGraphics() {
		return subjectGraphics;
	}

	public void setAccessFunctionGraphics(Map<AbstractIFNetTransition, AnnotationGraphics> accessFunctionGraphics) {
		this.accessFunctionGraphics = accessFunctionGraphics;
	}

	public void setColors(Map<String, Color> colors) {
		this.colors = colors;
	}

	public void setSubjectGraphics(Map<IFNetPlace, AnnotationGraphics> subjectGraphics) {
		this.subjectGraphics = subjectGraphics;
	}

	public String toString() {
		StringBuilder str = new StringBuilder();
		str.append(super.toString());

		str.append("            tokenColors# " + colors.size());
		if (colors.size() > 0)
			str.append(":\n" + map2Str(colors) + "\n");
		else
			str.append("\n");
		str.append(" accessFunctionGraphics# " + accessFunctionGraphics.size());
		if (accessFunctionGraphics.size() > 0)
			str.append(":\n" + map2Str(accessFunctionGraphics) + "\n");
		else
			str.append("\n");
		str.append("        subjectGraphics# " + subjectGraphics.size());
		if (subjectGraphics.size() > 0)
			str.append(":\n" + map2Str(subjectGraphics) + "\n");
		else
			str.append("\n");

		return str.toString();
	}
}
