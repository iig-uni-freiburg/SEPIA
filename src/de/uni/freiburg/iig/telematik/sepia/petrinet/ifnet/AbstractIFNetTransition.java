package de.uni.freiburg.iig.telematik.sepia.petrinet.ifnet;

import java.util.Set;

import de.invation.code.toval.validate.ParameterException;
import de.uni.freiburg.iig.telematik.sepia.petrinet.cwn.abstr.AbstractCWN;
import de.uni.freiburg.iig.telematik.sepia.petrinet.cwn.abstr.AbstractCWNTransition;


public abstract class AbstractIFNetTransition extends AbstractCWNTransition<IFNetFlowRelation> {
	
	protected AbstractIFNetTransition(){
		super();
	}

	public AbstractIFNetTransition(String name, boolean isEmpty) throws ParameterException {
		super(name, isEmpty);
	}

	public AbstractIFNetTransition(String name, String label, boolean isEmpty) throws ParameterException {
		super(name, label, isEmpty);
	}

	public AbstractIFNetTransition(String name, String label) throws ParameterException {
		super(name, label);
	}

	public AbstractIFNetTransition(String name) throws ParameterException {
		super(name);
	}
	
	public Set<String> getConsumedAttributes(){
		Set<String> consumedColors = super.getConsumedColors();
		consumedColors.remove(AbstractCWN.CONTROL_FLOW_TOKEN_COLOR);
		return consumedColors;
	}
	
	public Set<String> getProducedAttributes(){
		Set<String> producedColors = super.getProducedColors();
		producedColors.remove(AbstractCWN.CONTROL_FLOW_TOKEN_COLOR);
		return producedColors;
	}
	
	public Set<String> getProcessedAttributes(){
		Set<String> processedColors = super.getProcessedColors();
		processedColors.remove(AbstractCWN.CONTROL_FLOW_TOKEN_COLOR);
		return processedColors;
	}

	public abstract boolean isDeclassificator();

	@Override
	public AbstractIFNetTransition clone() {
		return (AbstractIFNetTransition) super.clone();
	}
	
	
	
}
