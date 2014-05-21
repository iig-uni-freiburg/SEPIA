package de.uni.freiburg.iig.telematik.sepia.petrinet.ifnet.abstr;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import de.invation.code.toval.constraint.AbstractConstraint;
import de.invation.code.toval.validate.ParameterException;
import de.invation.code.toval.validate.ParameterException.ErrorCode;
import de.invation.code.toval.validate.Validate;
import de.uni.freiburg.iig.telematik.sepia.event.TransitionEvent;
import de.uni.freiburg.iig.telematik.sepia.exception.PNValidationException;
import de.uni.freiburg.iig.telematik.sepia.petrinet.cwn.abstr.AbstractCWN;
import de.uni.freiburg.iig.telematik.sepia.petrinet.ifnet.concepts.AccessMode;
import de.uni.freiburg.iig.telematik.sepia.petrinet.ifnet.concepts.GuardDataContainer;

public abstract class AbstractRegularIFNetTransition<E extends AbstractIFNetFlowRelation<? extends AbstractIFNetPlace<E>, ? extends AbstractIFNetTransition<E>>>  
													 extends AbstractIFNetTransition<E>{
	
	protected Map<String, Set<AccessMode>> accessModes = new HashMap<String, Set<AccessMode>>();
	protected GuardDataContainer dataContainer = null;
	protected Set<AbstractConstraint<?>> guards = new HashSet<AbstractConstraint<?>>();
	
	protected AbstractRegularIFNetTransition() {
		super(TransitionType.REGULAR);
	}

	public AbstractRegularIFNetTransition(String name, boolean isSilent) {
		super(TransitionType.REGULAR, name, isSilent);
	}

	public AbstractRegularIFNetTransition(String name, String label, boolean isSilent) {
		super(TransitionType.REGULAR, name, label, isSilent);
	}

	public AbstractRegularIFNetTransition(String name, String label) {
		super(TransitionType.REGULAR, name, label);
	}

	public AbstractRegularIFNetTransition(String name) {
		super(TransitionType.REGULAR, name);
	}
	
	public GuardDataContainer getGuardDataContainer() {
		return dataContainer;
	}

	@SuppressWarnings("unchecked")
	public void setGuardDataContainer(GuardDataContainer dataContainer) {
		Validate.notNull(dataContainer);

		// Check if data container provides values for all processed data elements (colors)
		if (!dataContainer.getAttributes().containsAll(getProcessedColors()))
			throw new ParameterException(ErrorCode.INCOMPATIBILITY, "Data container must provide values for all data elements processed by this transition.");

		// Check if the type of generated values matches the type of guard attributes.
		for (AbstractConstraint<?> guard : guards) {
			if (!dataContainer.getAttributeValueClass(guard.getElement()).isAssignableFrom(guard.getParameterClass()))
				throw new ParameterException(ErrorCode.INCOMPATIBILITY, "Type of generated values for attribute \"" + guard.getElement() + "\" does not match the expected value type of constraint \"" + guard + "\".");
		}
		this.dataContainer = dataContainer;
	}

	@Override
	public void checkState() {
		boolean oldEnabledState = isEnabled();

		enabled = true;
		// Check if there are enough tokens in all input places.
		if (!enoughTokensInInputPlaces()) {
			enabled = false;
		}

		// Check if there is enough room in output places.
		if (!enoughSpaceInOutputPlaces()) {
			enabled = false;
		}

		// Check if all guards evaluate to true.
		for (AbstractConstraint<?> guard : guards) {
			Object attributeValue = null;
			try {
				attributeValue = dataContainer.getValueForAttribute(guard.getElement());
			} catch (Exception e1) {
				// Value generation Exception
				e1.printStackTrace();
			}

			try {
				if (!guard.validate(attributeValue)) {
					enabled = false;
				}
			} catch (ParameterException e) {
				// Cannot happen, since the case data container generates values of correct type.
			}
		}

		// Check if enabled state changed.
		if (enabled && !oldEnabledState) {
			listenerSupport.notifyEnabling(new TransitionEvent<AbstractRegularIFNetTransition<E>>(this));
		} else if (!enabled && oldEnabledState) {
			listenerSupport.notifyDisabling(new TransitionEvent<AbstractRegularIFNetTransition<E>>(this));
		}
	}

	public void setAccessMode(String tokenColor, Collection<AccessMode> colorAccessModes) {
		Validate.notNull(tokenColor);
		if (tokenColor.equals(AbstractCWN.CONTROL_FLOW_TOKEN_COLOR))
			throw new ParameterException(ErrorCode.INCOMPATIBILITY, "Cannot set access mode for control flow token color.");
		Validate.notNull(colorAccessModes);
		Validate.noNullElements(colorAccessModes);

		accessModes.put(tokenColor, new HashSet<AccessMode>(colorAccessModes));
	}

	public void setAccessMode(String tokenColor, AccessMode... accessModes) {
		Validate.notNull(accessModes);
		setAccessMode(tokenColor, Arrays.asList(accessModes));
	}

	@SuppressWarnings("unchecked")
	public boolean addGuard(AbstractConstraint<?> guard) {
		if (dataContainer == null)
			throw new ParameterException(ErrorCode.INCOMPATIBILITY, "Cannot add guard. Please set guard data container first.");
		if (!getProcessedColors().contains(guard.getElement()))
			throw new ParameterException(ErrorCode.INCOMPATIBILITY, "Cannot add constraint for attribute which is not processed by the transition");
		if (!dataContainer.getAttributes().contains(guard.getElement()))
			throw new ParameterException(ErrorCode.INCOMPATIBILITY, "Cannot add constraint for attribute for which the data container does not produce values.");
		if (!guard.getParameterClass().isAssignableFrom(dataContainer.getAttributeValueClass(guard.getElement())))
			throw new ParameterException(ErrorCode.INCOMPATIBILITY, "Type mismatch for guard element and produced values within the assigned data container.\n" + "Guard requires values of type \"" + guard.getParameters()[0].getClass() + "\"\n" + "Generated values are of type \"" + dataContainer.getAttributeValueClass(guard.getElement()) + "\"");
		return guards.add(guard);
	}

	public boolean removeGuard(AbstractConstraint<?> guard) {
		return guards.remove(guard);
	}

	public Set<AbstractConstraint<?>> getGuards() {
		return Collections.unmodifiableSet(guards);
	}

	public Map<String, Set<AccessMode>> getAccessModes() {
		return Collections.unmodifiableMap(accessModes);
	}

	public Set<AccessMode> getAccessModes(String color) {
		Validate.notNull(color);
		if (!accessModes.containsKey(color))
			return new HashSet<AccessMode>();
		return new HashSet<AccessMode>(accessModes.get(color));
	}

	public boolean addAccessMode(String color, AccessMode... accessModes) {
		Validate.notNull(accessModes);
		return addAccessMode(color, Arrays.asList(accessModes));
	}

	public boolean addAccessMode(String color, Collection<AccessMode> accessModes) {
		Validate.notNull(color);
		Validate.notNull(accessModes);
		Validate.notEmpty(accessModes);
		Validate.noNullElements(accessModes);
		if (!this.accessModes.containsKey(color)) {
			this.accessModes.put(color, new HashSet<AccessMode>());
		}
		return this.accessModes.get(color).addAll(accessModes);
	}

	public boolean removeAccessMode(String color, AccessMode... accessModes) {
		return removeAccessMode(color, Arrays.asList(accessModes));
	}

	public boolean removeAccessMode(String color, Collection<AccessMode> accessModes) {
		Validate.notNull(color);
		Validate.notNull(accessModes);
		Validate.noNullElements(accessModes);
		if (accessModes.isEmpty())
			return false;
		if (!this.accessModes.containsKey(color))
			return false;
		return this.accessModes.get(color).removeAll(accessModes);
	}

	public boolean removeAccessModes(String color) {
		Validate.notNull(color);
		if (!accessModes.containsKey(color))
			return false;
		return accessModes.remove(color) != null;
	}

	@Override
	public boolean isDeclassificator() {
		return false;
	}

	public Set<String> getProcessedAttributes(AccessMode... accessModes) {
		Validate.notNull(accessModes);
		return getProcessedAttributes(Arrays.asList(accessModes));
	}

	public Set<String> getProcessedAttributes(Collection<AccessMode> accessModes) {
		Validate.notNull(accessModes);
		Set<String> result = new HashSet<String>();
		for (String processedColor : getProcessedAttributes()) {
			if (getAccessModes(processedColor).containsAll(accessModes)) {
				result.add(processedColor);
			}
		}
		return result;
	}

	public Set<String> getConsumedAttributes(AccessMode... accessModes) {
		Validate.notNull(accessModes);
		return getConsumedAttributes(Arrays.asList(accessModes));
	}

	public Set<String> getConsumedAttributes(Collection<AccessMode> accessModes) {
		Validate.notNull(accessModes);
		Set<String> result = new HashSet<String>();
		for (String consumedColor : getConsumedAttributes()) {
			if (getAccessModes(consumedColor).containsAll(accessModes)) {
				result.add(consumedColor);
			}
		}
		return result;
	}

	public Set<String> getProducedAttributes(AccessMode... accessModes) {
		Validate.notNull(accessModes);
		return getProducedAttributes(Arrays.asList(accessModes));
	}

	public Set<String> getProducedAttributes(Collection<AccessMode> accessModes) {
		Validate.notNull(accessModes);
		Set<String> result = new HashSet<String>();
		for (String producedColor : getProducedAttributes()) {
			if (getAccessModes(producedColor).containsAll(accessModes)) {
				result.add(producedColor);
			}
		}
		return result;
	}

	@Override
	public void checkValidity() throws PNValidationException {
		super.checkValidity();

		Set<AccessMode> colorAccessModes;
		boolean containsCreateMode;
		boolean containsDeleteMode;
		boolean tokenColorIsProduced = false;
		boolean tokenColorIsConsumed = false;

		for (String color : accessModes.keySet()) {
			colorAccessModes = accessModes.get(color);
			containsCreateMode = colorAccessModes.contains(AccessMode.CREATE);
			containsDeleteMode = colorAccessModes.contains(AccessMode.DELETE);

			if (containsCreateMode && containsDeleteMode)
				throw new PNValidationException("Transition cannot create and delete a token at the same time.");

			tokenColorIsConsumed = consumesColor(color);
			tokenColorIsProduced = producesColor(color);

			if (containsCreateMode) {
				if (tokenColorIsConsumed)
					throw new PNValidationException("Created tokens must not be consumed by a transition, only produced.");
				if (!tokenColorIsProduced)
					throw new PNValidationException("Created tokens must be produced in at least one output place.");

			} else if (containsDeleteMode) {
				if (tokenColorIsProduced)
					throw new PNValidationException("Deleted tokens must not be produced by a transition, only consumed.");
				if (!tokenColorIsConsumed)
					throw new PNValidationException("Deleted tokens must be consumed in at least one input place.");

			} else {
				if (!tokenColorIsProduced || !tokenColorIsConsumed)
					throw new PNValidationException("Tokens which are neither created, nor deleted must be produced and consumed.");
			}
		}
	}

}
