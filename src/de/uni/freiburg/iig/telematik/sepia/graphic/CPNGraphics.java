package de.uni.freiburg.iig.telematik.sepia.graphic;

import java.awt.Color;
import java.util.HashMap;
import java.util.Map;

import de.invation.code.toval.types.Multiset;
import de.uni.freiburg.iig.telematik.sepia.petrinet.cpn.CPNFlowRelation;
import de.uni.freiburg.iig.telematik.sepia.petrinet.cpn.CPNMarking;
import de.uni.freiburg.iig.telematik.sepia.petrinet.cpn.CPNPlace;
import de.uni.freiburg.iig.telematik.sepia.petrinet.cpn.CPNTransition;

/**
 * {@link AbstractPNGraphics} implementation for the CPNs.
 * 
 * @author Thomas Stocker
 * @author Adrian Lange
 */
public class CPNGraphics extends AbstractPNGraphics<CPNPlace, CPNTransition, CPNFlowRelation, CPNMarking, Multiset<String>> {

	private Map<String, Color> colors = new HashMap<String, Color>();

	public Map<String, Color> getColors() {
		return colors;
	}

	public void setColors(Map<String, Color> colors) {
		this.colors = colors;
	}

	public String toString() {
		StringBuilder str = new StringBuilder();
		str.append(super.toString());

		if (colors.size() > 0) {
			str.append("                      tokenColors# " + colors.size());
			if (colors.size() > 0)
				str.append(":\n" + map2Str(colors) + "\n");
			else
				str.append("\n");
		}

		return str.toString();
	}
}