package de.uni.freiburg.iig.telematik.sepia.graphic.netgraphics;

import java.awt.Color;
import java.util.HashMap;
import java.util.Map;

import de.invation.code.toval.types.Multiset;
import de.uni.freiburg.iig.telematik.sepia.petrinet.cwn.abstr.AbstractCWNFlowRelation;
import de.uni.freiburg.iig.telematik.sepia.petrinet.cwn.abstr.AbstractCWNMarking;
import de.uni.freiburg.iig.telematik.sepia.petrinet.cwn.abstr.AbstractCWNPlace;
import de.uni.freiburg.iig.telematik.sepia.petrinet.cwn.abstr.AbstractCWNTransition;

/**
 * {@link AbstractPNGraphics} implementation for the CWNs.
 * 
 * @author Thomas Stocker
 * @author Adrian Lange
 */
public abstract class AbstractCWNGraphics<P extends AbstractCWNPlace<F>,
						 		  T extends AbstractCWNTransition<F>, 
						 		  F extends AbstractCWNFlowRelation<P,T>, 
						 		  M extends AbstractCWNMarking> extends AbstractPNGraphics<P, T, F, M, Multiset<String>> {

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
