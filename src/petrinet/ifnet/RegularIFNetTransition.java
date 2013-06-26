package petrinet.ifnet;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import petrinet.cpn.abstr.AbstractCPN;
import validate.ParameterException;
import validate.ParameterException.ErrorCode;
import validate.Validate;
import constraint.AbstractConstraint;
import event.TransitionEvent;
import exception.PNValidationException;

public class RegularIFNetTransition extends AbstractIFNetTransition {
	
	protected Map<String, Set<AccessMode>> accessModes = new HashMap<String, Set<AccessMode>>();
	protected GuardDataContainer dataContainer = null;
	protected Set<AbstractConstraint<?>> guards = new HashSet<AbstractConstraint<?>>();
	
	protected RegularIFNetTransition(){
		super(); 
	}
	
	public RegularIFNetTransition(String name, boolean isEmpty) throws ParameterException {
		super(name, isEmpty);
	}

	public RegularIFNetTransition(String name, String label, boolean isEmpty) throws ParameterException {
		super(name, label, isEmpty);
	}

	public RegularIFNetTransition(String name, String label) throws ParameterException {
		super(name, label);
	}

	public RegularIFNetTransition(String name) throws ParameterException {
		super(name);
	}  
  
	@SuppressWarnings("unchecked")
	public void setGuardDataContainer(GuardDataContainer dataContainer) throws ParameterException{
		Validate.notNull(dataContainer);
		
		// Check if data container provides values for all processed data elements (colors)
		if(!dataContainer.getAttributes().containsAll(getProcessedColors()))
			throw new ParameterException(ErrorCode.INCOMPATIBILITY, "Data container must provide values for all data elements processed by this transition.");
		
		// Check if the type of generated values matches the type of guard attributes.
		for(AbstractConstraint<?> guard: guards){
			if(!dataContainer.getAttributeValueClass(guard.getElement()).isAssignableFrom(guard.getParameterClass()))
				throw new ParameterException(ErrorCode.INCOMPATIBILITY, "Type of generated values for attribute \""+guard.getElement()+"\" does not match the expected value type of constraint \""+ guard +"\".");
		}
		this.dataContainer = dataContainer;
	}
	
	@Override
	public void checkState() {
		boolean oldEnabledState = isEnabled();
		
		enabled = true;
		// Check if there are enough tokens in all input places.
		if(!enoughTokensInInputPlaces()){
			enabled = false;
		}
		
		// Check if there is enough room in output places.
		if(!enoughSpaceInOutputPlaces()){
			enabled = false;
		}
		
		// Check if all guards evaluate to true.
		for(AbstractConstraint<?> guard: guards){
			Object attributeValue = null;
			try {
				attributeValue = dataContainer.getValueForAttribute(guard.getElement());
			} catch (Exception e1) {
				// Value generation Exception
				e1.printStackTrace();
			}
			
			try {
				if(!guard.validate(attributeValue)){
					enabled = false;
				}
			}catch(ParameterException e){
				// Cannot happen, since the case data container generates values of correct type.
			}
		}
		
		
		// Check if enabled state changed.
		if(enabled && !oldEnabledState){
			listenerSupport.notifyEnabling(new TransitionEvent<RegularIFNetTransition>(this));
		} else if(!enabled && oldEnabledState){
			listenerSupport.notifyDisabling(new TransitionEvent<RegularIFNetTransition>(this));
		}
	}
	
	public void setAccessMode(String tokenColor, Collection<AccessMode> colorAccessModes) throws ParameterException{
		Validate.notNull(tokenColor);
		if(tokenColor.equals(AbstractCPN.CONTROL_FLOW_TOKEN_COLOR))
			throw new ParameterException(ErrorCode.INCOMPATIBILITY, "Cannot set access mode for control flow token color.");
		Validate.notNull(colorAccessModes);
		Validate.noNullElements(colorAccessModes);
		
		accessModes.put(tokenColor, new HashSet<AccessMode>(colorAccessModes));
	}
	
	public void setAccessMode(String tokenColor, AccessMode... accessModes) throws ParameterException{
		Validate.notNull(accessModes);
		setAccessMode(tokenColor, Arrays.asList(accessModes));
	}
	
	@SuppressWarnings("unchecked")
	public boolean addGuard(AbstractConstraint<?> guard) throws ParameterException{
		if(dataContainer == null)
			throw new ParameterException(ErrorCode.INCOMPATIBILITY, "Cannot add guard. Please set guard data container first.");
		if(!getProcessedColors().contains(guard.getElement()))
			throw new ParameterException(ErrorCode.INCOMPATIBILITY, "Cannot add constraint for attribute which is not processed by the transition");
		if(!dataContainer.getAttributes().contains(guard.getElement()))
			throw new ParameterException(ErrorCode.INCOMPATIBILITY, "Cannot add constraint for attribute for which the data container does not produce values.");
		if(!guard.getParameterClass().isAssignableFrom(dataContainer.getAttributeValueClass(guard.getElement())))
			throw new ParameterException(ErrorCode.INCOMPATIBILITY, "Type mismatch for guard element and produced values within the assigned data container.\n" +
																	"Guard requires values of type \""+ guard.getParameters()[0].getClass()+"\"\n" + 
																	"Generated values are of type \""+ dataContainer.getAttributeValueClass(guard.getElement()) +"\"");
		return guards.add(guard);
	}
	
	public boolean removeGuard(AbstractConstraint<?> guard) throws ParameterException{
		return guards.remove(guard);
	}
	
	public Set<AbstractConstraint<?>> getGuards(){
		return Collections.unmodifiableSet(guards);
	}
	
	public Map<String, Set<AccessMode>> getAccessModes(){
		return Collections.unmodifiableMap(accessModes);
	}
	
	public Set<AccessMode> getAccessModes(String color) throws ParameterException{
		Validate.notNull(color);
		if(!accessModes.containsKey(color))
			return new HashSet<AccessMode>();
		return new HashSet<AccessMode>(accessModes.get(color));
	}
	
	public boolean addAccessMode(String color, AccessMode... accessModes) throws ParameterException{
		Validate.notNull(accessModes);
		return addAccessMode(color, Arrays.asList(accessModes));
	}
	
	public boolean addAccessMode(String color, Collection<AccessMode> accessModes) throws ParameterException{
		Validate.notNull(color);
		Validate.notNull(accessModes);
		Validate.notEmpty(accessModes);
		Validate.noNullElements(accessModes);
		if(!this.accessModes.containsKey(color)){
			this.accessModes.put(color, new HashSet<AccessMode>());
		}
		return this.accessModes.get(color).addAll(accessModes);
	}

	public boolean removeAccessMode(String color, AccessMode... accessModes) throws ParameterException{
		return removeAccessMode(color, Arrays.asList(accessModes));
	}
	
	public boolean removeAccessMode(String color, Collection<AccessMode> accessModes) throws ParameterException{
		Validate.notNull(color);
		Validate.notNull(accessModes);
		Validate.noNullElements(accessModes);
		if(accessModes.isEmpty())
			return false;
		if(!this.accessModes.containsKey(color))
			return false;
		return this.accessModes.get(color).removeAll(accessModes);
	}
	
	public boolean removeAccessModes(String color) throws ParameterException{
		Validate.notNull(color);
		if(!accessModes.containsKey(color))
			return false;
		return accessModes.remove(color) != null;
	}
	
	@Override
	public boolean isDeclassificator() {
		return false;
	}
	
	public Set<String> getProcessedAttributes(AccessMode... accessModes) throws ParameterException{
		Validate.notNull(accessModes);
		return getProcessedAttributes(Arrays.asList(accessModes));
	}
	
	public Set<String> getProcessedAttributes(Collection<AccessMode> accessModes) throws ParameterException{
		Validate.notNull(accessModes);
		Set<String> result = new HashSet<String>();
		for(String processedColor: getProcessedAttributes()){
			if(getAccessModes(processedColor).containsAll(accessModes)){
				result.add(processedColor);
			}
		}
		return result;
	}
	
	public Set<String> getConsumedAttributes(AccessMode... accessModes) throws ParameterException{
		Validate.notNull(accessModes);
		return getConsumedAttributes(Arrays.asList(accessModes));
	}
	
	public Set<String> getConsumedAttributes(Collection<AccessMode> accessModes) throws ParameterException{
		Validate.notNull(accessModes);
		Set<String> result = new HashSet<String>();
		for(String consumedColor: getConsumedAttributes()){
			if(getAccessModes(consumedColor).containsAll(accessModes)){
				result.add(consumedColor);
			}
		}
		return result;
	}
	
	public Set<String> getProducedAttributes(AccessMode... accessModes) throws ParameterException{
		Validate.notNull(accessModes);
		return getProducedAttributes(Arrays.asList(accessModes));
	}
	
	public Set<String> getProducedAttributes(Collection<AccessMode> accessModes) throws ParameterException{
		Validate.notNull(accessModes);
		Set<String> result = new HashSet<String>();
		for(String producedColor: getProducedAttributes()){
			if(getAccessModes(producedColor).containsAll(accessModes)){
				result.add(producedColor);
			}
		}
		return result;
	}
	
	
	@Override
	public void checkValidity() throws PNValidationException{
		super.checkValidity();
		
		Set<AccessMode> colorAccessModes;
		boolean containsCreateMode;
		boolean containsDeleteMode;
		boolean tokenColorIsProduced = false;
		boolean tokenColorIsConsumed = false;
		

			for(String color: accessModes.keySet()){
				colorAccessModes = accessModes.get(color);
				containsCreateMode = colorAccessModes.contains(AccessMode.CREATE);
				containsDeleteMode = colorAccessModes.contains(AccessMode.DELETE);
				
				if(containsCreateMode && containsDeleteMode)
					throw new PNValidationException("Transition cannot create and delete a token at the same time.");
				
				tokenColorIsConsumed = consumesColor(color);
				tokenColorIsProduced = producesColor(color);
				
				if(containsCreateMode){
					if(tokenColorIsConsumed)
						throw new PNValidationException("Created tokens must not be consumed by a transition, only produced.");
					if(!tokenColorIsProduced)
						throw new PNValidationException("Created tokens must be produced in at least one output place.");
					
				} else if(containsDeleteMode){
					if(tokenColorIsProduced)
						throw new PNValidationException("Deleted tokens must not be produced by a transition, only consumed.");
					if(!tokenColorIsConsumed)
						throw new PNValidationException("Deleted tokens must be consumed in at least one input place.");
					
				} else {
					if(!tokenColorIsProduced || !tokenColorIsConsumed)
						throw new PNValidationException("Tokens which are neither created, nor deleted must be produced and consumed.");
				}
			}
	}

	@Override
	public String toPNML() {
		// TODO Auto-generated method stub
		return null;
	}
	
	

	@Override
	public RegularIFNetTransition clone() {
		RegularIFNetTransition result = (RegularIFNetTransition) super.clone();
		try {
			result.setGuardDataContainer(dataContainer);
			for(AbstractConstraint<?> guard: guards){
				result.addGuard(guard.clone());
			}
			
			for(String color: accessModes.keySet()){
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
