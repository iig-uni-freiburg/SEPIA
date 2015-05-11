package de.uni.freiburg.iig.telematik.sepia.petrinet.ifnet.concepts;

import java.util.Set;

public interface GuardDataContainer {

	public Set<String> getAttributes();

	public Object getValueForAttribute(String attribute) throws Exception;

	public Class<?> getAttributeValueClass(String attribute);
}