package de.uni.freiburg.iig.telematik.sepia.petrinet.ifnet;

import de.invation.code.toval.constraint.AbstractConstraint;
import de.invation.code.toval.validate.ParameterException;
import de.uni.freiburg.iig.telematik.sepia.petrinet.ifnet.abstr.AbstractRegularIFNetTransition;

public class RegularIFNetTransition extends AbstractRegularIFNetTransition<IFNetFlowRelation> {

	protected RegularIFNetTransition() {
		super();
	}

	public RegularIFNetTransition(String name, boolean isEmpty) {
		super(name, isEmpty);
	}

	public RegularIFNetTransition(String name, String label, boolean isEmpty) {
		super(name, label, isEmpty);
	}

	public RegularIFNetTransition(String name, String label) {
		super(name, label);
	}

	public RegularIFNetTransition(String name) {
		super(name);
	}

	@Override
	public RegularIFNetTransition clone() {
		RegularIFNetTransition result = (RegularIFNetTransition) super.clone();
		try {
			if (dataContainer != null)
				result.setGuardDataContainer(dataContainer);
			for (AbstractConstraint<?> guard : guards) {
				result.addGuard(guard.clone());
			}

			for (String color : accessModes.keySet()) {
				result.setAccessMode(color, getAccessModes(color));
			}
		} catch (ParameterException e) {
			e.printStackTrace();
		}
		return result;
	}

	@Override
	protected RegularIFNetTransition newInstance() {
		return new RegularIFNetTransition();
	}
}
