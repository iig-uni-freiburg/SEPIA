package de.uni.freiburg.iig.telematik.sepia.util.mg.pt;

import de.uni.freiburg.iig.telematik.sepia.petrinet.pt.PTMarking;
import de.uni.freiburg.iig.telematik.sepia.util.mg.abstr.AbstractMarkingGraphState;

public class PTMarkingGraphState extends AbstractMarkingGraphState<PTMarking, Integer>{

//	protected PTMarkingGraphState(String name) {
//		super(name);
//	}

	public PTMarkingGraphState(String name, PTMarking element) {
		super(name, element);
	}
	
}
