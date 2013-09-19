package de.uni.freiburg.iig.telematik.sepia.graphic;

import java.awt.Color;
import java.util.HashMap;
import java.util.Map;

import de.invation.code.toval.types.Multiset;
import de.uni.freiburg.iig.telematik.sepia.graphic.attributes.Position;
import de.uni.freiburg.iig.telematik.sepia.petrinet.ifnet.AbstractIFNetTransition;
import de.uni.freiburg.iig.telematik.sepia.petrinet.ifnet.IFNetFlowRelation;
import de.uni.freiburg.iig.telematik.sepia.petrinet.ifnet.IFNetMarking;
import de.uni.freiburg.iig.telematik.sepia.petrinet.ifnet.IFNetPlace;
import de.uni.freiburg.iig.telematik.sepia.petrinet.ifnet.RegularIFNetTransition;

/**
 * {@link AbstractPNGraphics} implementation for the IFNets.
 * 
 * @author Thomas Stocker
 * @author Adrian Lange
 */
public class IFNetGraphics extends AbstractPNGraphics<IFNetPlace, AbstractIFNetTransition, IFNetFlowRelation, IFNetMarking, Multiset<String>> {

	private Map<RegularIFNetTransition, AnnotationGraphics> accessFunctionGraphics = new HashMap<RegularIFNetTransition, AnnotationGraphics>();
	private Map<String, Color> colors = new HashMap<String, Color>();
	private Map<AbstractIFNetTransition, AnnotationGraphics> subjectGraphics = new HashMap<AbstractIFNetTransition, AnnotationGraphics>();

	private Position clearancesPosition = new Position();
	private Position tokenLabelsPosition = new Position();

	public Map<RegularIFNetTransition, AnnotationGraphics> getAccessFunctionGraphics() {
		return accessFunctionGraphics;
	}

	public Position getClearancesPosition() {
		return clearancesPosition;
	}

	public Map<String, Color> getColors() {
		return colors;
	}

	public Map<AbstractIFNetTransition, AnnotationGraphics> getSubjectGraphics() {
		return subjectGraphics;
	}

	public Position getTokenLabelsPosition() {
		return tokenLabelsPosition;
	}

	public void setAccessFunctionGraphics(Map<RegularIFNetTransition, AnnotationGraphics> accessFunctionGraphics) {
		this.accessFunctionGraphics = accessFunctionGraphics;
	}

	public void setClearancesPosition(Position clearancesPosition) {
		this.clearancesPosition = clearancesPosition;
	}

	public void setColors(Map<String, Color> colors) {
		this.colors = colors;
	}

	public void setSubjectGraphics(Map<AbstractIFNetTransition, AnnotationGraphics> subjectGraphics) {
		this.subjectGraphics = subjectGraphics;
	}

	public void setTokenLabelsPosition(Position tokenLabelsPosition) {
		this.tokenLabelsPosition = tokenLabelsPosition;
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
		if (clearancesPosition != null)
			str.append("     clearancesPosition#:   " + clearancesPosition + "\n");
		if (tokenLabelsPosition != null)
			str.append("    tokenLabelsPosition#:   " + tokenLabelsPosition + "\n");

		return str.toString();
	}
}
