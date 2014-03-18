package de.uni.freiburg.iig.telematik.sepia.util.mg.cwn;

import de.uni.freiburg.iig.telematik.sepia.petrinet.cwn.CWNMarking;

public class CWNMarkingGraphState extends AbstractCWNMarkingGraphState<CWNMarking>{

	protected CWNMarkingGraphState(String name) {
		super(name);
	}

	public CWNMarkingGraphState(String name, CWNMarking element) {
		super(name, element);
	}
	
}
