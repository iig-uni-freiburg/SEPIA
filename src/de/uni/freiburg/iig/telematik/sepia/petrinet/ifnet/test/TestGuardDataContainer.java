package de.uni.freiburg.iig.telematik.sepia.petrinet.ifnet.test;

import java.util.Collection;
import java.util.HashMap;
import java.util.Set;

import de.invation.code.toval.validate.ParameterException;
import de.uni.freiburg.iig.telematik.sepia.petrinet.ifnet.GuardDataContainer;

/**
 * An implemetation of the interface GuardDataContainer used to test SNets.
 * 
 * @author boehr
 */
public class TestGuardDataContainer implements GuardDataContainer {

	// A map between data values and attributes
	HashMap<String, Integer> data = new HashMap<String, Integer>();

	// The class of the values
	@SuppressWarnings("rawtypes")
	Class valueClass = null;

	/*
	 * (non-Javadoc)
	 * 
	 * @see petrinet.snet.GuardDataContainer#getAttributes()
	 */
	@Override
	public Set<String> getAttributes() {
		return data.keySet();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see petrinet.snet.GuardDataContainer#getValueForAttribute(java.lang.String)
	 */
	@Override
	public Object getValueForAttribute(String attribute) throws Exception {
		return data.get(attribute);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see petrinet.snet.GuardDataContainer#getAttributeValueType(java.lang.String)
	 */
	@SuppressWarnings("rawtypes")
	@Override
	public Class getAttributeValueClass(String attribute) throws ParameterException {

		// It is a container of integers
		return valueClass;
	}

	public void setAttribute(String attrib, Integer value) {
		data.put(attrib, value);
	}

	public void removeAttribute(String attrib) {
		data.remove(attrib);
	}

	public TestGuardDataContainer(Collection<String> attribs) {
		this(attribs, Number.class);
	}

	public TestGuardDataContainer(Collection<String> attribs, @SuppressWarnings("rawtypes") Class valueClass) {
		this.valueClass = valueClass;
		for (String attrib : attribs) {
			data.put(attrib, 0);
		}
	}
}
