package de.uni.freiburg.iig.telematik.sepia.mg.cwn;

import de.uni.freiburg.iig.telematik.sepia.petrinet.cwn.CWNMarking;

public class CWNMarkingGraphState extends AbstractCWNMarkingGraphState<CWNMarking>{

	private static final long serialVersionUID = -7500946928446568174L;

	protected CWNMarkingGraphState(String name) {
		super(name);
	}

	public CWNMarkingGraphState(String name, CWNMarking element) {
		super(name, element);
	}
	
}
