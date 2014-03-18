package de.uni.freiburg.iig.telematik.sepia.util.mg.ifnet;

import de.uni.freiburg.iig.telematik.sepia.petrinet.ifnet.IFNetMarking;

public class IFNetMarkingGraphState extends AbstractIFNetMarkingGraphState<IFNetMarking>{

	protected IFNetMarkingGraphState(String name) {
		super(name);
	}
	
	public IFNetMarkingGraphState(String name, IFNetMarking element) {
		super(name, element);
	}
	
}
