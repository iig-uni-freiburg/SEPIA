package de.uni.freiburg.iig.telematik.sepia.mg.ifnet;

import de.uni.freiburg.iig.telematik.sepia.petrinet.ifnet.IFNetMarking;

public class IFNetMarkingGraphState extends AbstractIFNetMarkingGraphState<IFNetMarking>{

	private static final long serialVersionUID = -1234436337252542904L;

	protected IFNetMarkingGraphState(String name) {
		super(name);
	}
	
	public IFNetMarkingGraphState(String name, IFNetMarking element) {
		super(name, element);
	}
	
}
