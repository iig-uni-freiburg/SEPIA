package de.uni.freiburg.iig.telematik.sepia.graphic.netgraphics;

import java.awt.Color;
import java.util.HashMap;
import java.util.Map;

import de.invation.code.toval.types.Multiset;
import de.uni.freiburg.iig.telematik.sepia.petrinet.cpn.abstr.AbstractCPNFlowRelation;
import de.uni.freiburg.iig.telematik.sepia.petrinet.cpn.abstr.AbstractCPNMarking;
import de.uni.freiburg.iig.telematik.sepia.petrinet.cpn.abstr.AbstractCPNPlace;
import de.uni.freiburg.iig.telematik.sepia.petrinet.cpn.abstr.AbstractCPNTransition;

/**
 * {@link AbstractPNGraphics} implementation for the CPNs.
 * 
 * @author Thomas Stocker
 * @author Adrian Lange
 */
public abstract class AbstractCPNGraphics<P extends AbstractCPNPlace<F>,
						 		  T extends AbstractCPNTransition<F>, 
						 		  F extends AbstractCPNFlowRelation<P,T>, 
						 		  M extends AbstractCPNMarking> extends  AbstractPNGraphics<P, T, F, M, Multiset<String>> {

	public AbstractCPNGraphics() {
		super();
	}

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