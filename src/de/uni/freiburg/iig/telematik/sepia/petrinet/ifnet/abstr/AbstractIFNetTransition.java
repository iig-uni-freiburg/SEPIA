package de.uni.freiburg.iig.telematik.sepia.petrinet.ifnet.abstr;

import java.util.Set;

import de.uni.freiburg.iig.telematik.sepia.petrinet.cpn.abstr.AbstractCPNTransition;


public abstract class AbstractIFNetTransition<E extends AbstractIFNetFlowRelation<? extends AbstractIFNetPlace<E>, 
		  									  ? extends AbstractIFNetTransition<E>>> 

												extends AbstractCPNTransition<E> {
	
	private static final long serialVersionUID = 478252467742688294L;
	
	private TransitionType type = null;
	
//	protected AbstractIFNetTransition(TransitionType type){
//		super();
//		this.type = type;
//	}

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
		consumedColors.remove(AbstractIFNet.CONTROL_FLOW_TOKEN_COLOR);
		return consumedColors;
	}
	
	public Set<String> getProducedAttributes(){
		Set<String> producedColors = super.getProducedColors();
		producedColors.remove(AbstractIFNet.CONTROL_FLOW_TOKEN_COLOR);
		return producedColors;
	}
	
	public Set<String> getProcessedAttributes(){
		Set<String> processedColors = super.getProcessedColors();
		processedColors.remove(AbstractIFNet.CONTROL_FLOW_TOKEN_COLOR);
		return processedColors;
	}

	public abstract boolean isDeclassificator();

	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + ((type == null) ? 0 : type.hashCode());
		return result;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (!super.equals(obj)) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		@SuppressWarnings("rawtypes")
		AbstractIFNetTransition other = (AbstractIFNetTransition) obj;
		if (type != other.type) {
			return false;
		}
		return true;
	}
	
}
