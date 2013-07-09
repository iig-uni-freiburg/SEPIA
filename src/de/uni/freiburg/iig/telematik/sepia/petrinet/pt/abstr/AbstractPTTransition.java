package de.uni.freiburg.iig.telematik.sepia.petrinet.pt.abstr;

import de.invation.code.toval.validate.ParameterException;
import de.uni.freiburg.iig.telematik.sepia.event.TransitionEvent;
import de.uni.freiburg.iig.telematik.sepia.petrinet.AbstractTransition;

public abstract class AbstractPTTransition<E extends AbstractPTFlowRelation<? extends AbstractPTPlace<E>, ? extends AbstractPTTransition<E>>> extends AbstractTransition<E,Integer>{

private final String pnmlFormat = "<transition id=\"%s\">%n <name><text>%s</text></name>%n</transition>%n";
	
	protected AbstractPTTransition(){
		super();
	}

	public AbstractPTTransition(String name) throws ParameterException {
		super(name);
	}
	
	public AbstractPTTransition(String name, String label) throws ParameterException {
		super(name, label);
	}
	
	public AbstractPTTransition(String name, boolean isSilent) throws ParameterException{
		super(name, isSilent);
	}
	
	public AbstractPTTransition(String name, String label, boolean isSilent) throws ParameterException{
		super(name, label, isSilent);
	}

	@Override
	public void checkState() {
		boolean oldEnabledState = enabled;
		enabled = true;
		for(E r: incomingRelations) {
			if(r.getPlace().getState() < r.getWeight()){
				enabled = false;
				break;
			}
		}
		
		if(enabled && !oldEnabledState){
			listenerSupport.notifyEnabling(new TransitionEvent<AbstractPTTransition<E>>(this));
		} else if(!enabled && oldEnabledState){
			listenerSupport.notifyDisabling(new TransitionEvent<AbstractPTTransition<E>>(this));
		}
	}
	
	@Override
	public boolean enoughTokensInInputPlaces() {
		for(E r: incomingRelations) {
			if(r.getPlace().getState() < r.getWeight()){
				return false;
			}
		}
		return true;
	}

	@Override
	public boolean enoughSpaceInOutputPlaces() {
		for(E r: outgoingRelations) {
			if(!r.getPlace().isBounded()){
				continue;
			}
			if((r.getPlace().getCapacity() - r.getPlace().getState()) < r.getWeight()){
				return false;
			}
		}
		return true;
	}
	
	@Override
	public AbstractPTTransition<E> clone() {
		AbstractPTTransition<E> result = (AbstractPTTransition<E>) newInstance();
		try {
			result.setName(getName());
			result.setLabel(getLabel());
		} catch (ParameterException e) {
			e.printStackTrace();
		}
		return result;
	}

	@Override
	public String toPNML() {
		return String.format(pnmlFormat, getName(), getLabel());
	}
	
}
