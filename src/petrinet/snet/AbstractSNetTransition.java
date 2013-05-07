package petrinet.snet;

import java.util.Set;

import petrinet.cpn.abstr.AbstractCPN;
import petrinet.cwn.abstr.AbstractCWNTransition;
import validate.ParameterException;

public abstract class AbstractSNetTransition extends AbstractCWNTransition<SNetFlowRelation> {
	
	protected AbstractSNetTransition(){
		super();
	}

	public AbstractSNetTransition(String name, boolean isEmpty) throws ParameterException {
		super(name, isEmpty);
	}

	public AbstractSNetTransition(String name, String label, boolean isEmpty) throws ParameterException {
		super(name, label, isEmpty);
	}

	public AbstractSNetTransition(String name, String label) throws ParameterException {
		super(name, label);
	}

	public AbstractSNetTransition(String name) throws ParameterException {
		super(name);
	}
	
	public Set<String> getConsumedAttributes(){
		Set<String> consumedColors = super.getConsumedColors();
		consumedColors.remove(AbstractCPN.CONTROL_FLOW_TOKEN_COLOR);
		return consumedColors;
	}
	
	public Set<String> getProducedAttributes(){
		Set<String> producedColors = super.getProducedColors();
		producedColors.remove(AbstractCPN.CONTROL_FLOW_TOKEN_COLOR);
		return producedColors;
	}
	
	public Set<String> getProcessedAttributes(){
		Set<String> processedColors = super.getProcessedColors();
		processedColors.remove(AbstractCPN.CONTROL_FLOW_TOKEN_COLOR);
		return processedColors;
	}

	public abstract boolean isDeclassificator();

	@Override
	public AbstractSNetTransition clone() {
		return (AbstractSNetTransition) super.clone();
	}
	
	
	
}
