package de.uni.freiburg.iig.telematik.sepia.petrinet.timedNet.concepts;

import java.util.List;

public interface IResourceContext {
	
	public String getName();
	
	public void setName();
	
    public void blockResources(List<String> resources);
    
    public void unBlockResources(List<String> resources);
    
    public List<List<String>> getAllowedResourcesFor(String activity);
   
    public List<String> getRandomAllowedResourcesFor(String activity, boolean blockResources);
    
    public boolean isAvailable(String resourceName);
    
    public IResource getResourceObject(String resourceName);

}
