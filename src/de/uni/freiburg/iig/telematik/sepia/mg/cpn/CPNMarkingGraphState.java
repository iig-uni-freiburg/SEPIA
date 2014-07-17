package de.uni.freiburg.iig.telematik.sepia.mg.cpn;

import de.uni.freiburg.iig.telematik.sepia.petrinet.cpn.CPNMarking;

public class CPNMarkingGraphState extends AbstractCPNMarkingGraphState<CPNMarking>{

	private static final long serialVersionUID = 1637142694155371817L;

	protected CPNMarkingGraphState(String name) {
		super(name);
	}
	
	public CPNMarkingGraphState(String name, CPNMarking element) {
		super(name, element);
	}
	

}
