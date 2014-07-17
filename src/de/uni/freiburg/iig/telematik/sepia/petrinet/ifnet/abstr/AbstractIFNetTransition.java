package de.uni.freiburg.iig.telematik.sepia.petrinet.ifnet.abstr;

import java.util.Set;

import de.uni.freiburg.iig.telematik.sepia.petrinet.cwn.abstr.AbstractCWN;
import de.uni.freiburg.iig.telematik.sepia.petrinet.cwn.abstr.AbstractCWNTransition;


public abstract class AbstractIFNetTransition<E extends AbstractIFNetFlowRelation<? extends AbstractIFNetPlace<E>, 
		  									  ? extends AbstractIFNetTransition<E>>> 

												extends AbstractCWNTransition<E> {
	
	private static final long serialVersionUID = 478252467742688294L;
	
	private TransitionType type = null;
	
	protected AbstractIFNetTransition(TransitionType type){
		super();
		this.type = type;
	}

	public AbstractIFNetTransition(TransitionType type, String name, boolean isEmpty) {
		super(name, isEmpty);
		this.type = type;
	}

	public AbstractIFNetTransition(TransitionType type, String name, String label, boolean isEmpty) {
		super(name, label, isEmpty);
		this.type = type;
	}

	public AbstractIFNetTransition(TransitionType type, String name, String label) {
		super(name, label);
		this.type = type;
	}

	public AbstractIFNetTransition(TransitionType type, String name) {
		super(name);
		this.type = type;
	}
	
	public TransitionType getType(){
		return type;
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
	
}
