package petrinet.snet;

import java.util.Set;

import validate.ParameterException;


public interface GuardDataContainer {
	
	public Set<String> getAttributes();
	
	public Object getValueForAttribute(String attribute) throws Exception;
	
	@SuppressWarnings("rawtypes")
	public Class getAttributeValueType(String attribute) throws ParameterException;

}