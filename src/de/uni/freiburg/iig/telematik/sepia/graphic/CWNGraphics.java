package de.uni.freiburg.iig.telematik.sepia.graphic;

import java.awt.Color;
import java.util.HashMap;
import java.util.Map;

import de.invation.code.toval.types.Multiset;
import de.uni.freiburg.iig.telematik.sepia.petrinet.cwn.CWNFlowRelation;
import de.uni.freiburg.iig.telematik.sepia.petrinet.cwn.CWNMarking;
import de.uni.freiburg.iig.telematik.sepia.petrinet.cwn.CWNPlace;
import de.uni.freiburg.iig.telematik.sepia.petrinet.cwn.CWNTransition;

/**
 * TODO
 * 
 * @author Thomas Stocker
 * @author Adrian Lange
 */
public class CWNGraphics extends PNGraphics<CWNPlace, CWNTransition, CWNFlowRelation, CWNMarking, Multiset<String>> {
	
	private Map<String, Color> colors = new HashMap<String, Color>();

	public Map<String, Color> getColors() {
		return colors;
	}

	public void setColors(Map<String, Color> colors) {
		this.colors = colors;
	}
}
