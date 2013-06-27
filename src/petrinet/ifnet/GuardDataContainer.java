package petrinet.ifnet;

import java.util.Set;

import de.invation.code.toval.validate.ParameterException;



public interface GuardDataContainer {
	
	public Set<String> getAttributes();
	
	public Object getValueForAttribute(String attribute) throws Exception;
	
	@SuppressWarnings("rawtypes")
	public Class getAttributeValueClass(String attribute) throws ParameterException;

}