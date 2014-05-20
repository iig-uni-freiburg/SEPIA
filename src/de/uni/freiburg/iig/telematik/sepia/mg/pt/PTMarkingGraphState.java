package de.uni.freiburg.iig.telematik.sepia.mg.pt;

import de.uni.freiburg.iig.telematik.sepia.mg.abstr.AbstractMarkingGraphState;
import de.uni.freiburg.iig.telematik.sepia.petrinet.pt.PTMarking;

public class PTMarkingGraphState extends AbstractMarkingGraphState<PTMarking, Integer>{

//	protected PTMarkingGraphState(String name) {
//		super(name);
//	}

	public PTMarkingGraphState(String name, PTMarking element) {
		super(name, element);
	}
	
}
