package de.uni.freiburg.iig.telematik.sepia.graphic;

import java.util.Comparator;

@SuppressWarnings("rawtypes")
public class GraphicalPNNameComparator implements Comparator<AbstractGraphicalPN>{

	@Override
	public int compare(AbstractGraphicalPN o1, AbstractGraphicalPN o2) {
		return o1.getPetriNet().getName().compareTo(o2.getPetriNet().getName());
	}

}
