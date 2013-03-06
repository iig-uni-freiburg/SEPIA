package petrinet.pt.abstr;

import petrinet.AbstractTransition;
import validate.ParameterException;
import event.TransitionEvent;

public abstract class AbstractPTTransition<E extends AbstractPTFlowRelation<? extends AbstractPTPlace<E>, ? extends AbstractPTTransition<E>>> extends AbstractTransition<E,Integer>{

private final String pnmlFormat = "<transition id=\"%s\">%n <name><text>%s</text></name>%n</transition>%n";
	
	public AbstractPTTransition(String name) throws ParameterException {
		super(name);
	}
	
	public AbstractPTTransition(String name, String label) throws ParameterException {
		super(name, label);
	}
	
	public AbstractPTTransition(String name, boolean isEmpty) throws ParameterException{
		super(name, isEmpty);
	}
	
	public AbstractPTTransition(String name, String label, boolean isEmpty) throws ParameterException{
		super(name, label, isEmpty);
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
	public String toPNML() {
		return String.format(pnmlFormat, getName(), getLabel());
	}
	
}
