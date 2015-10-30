/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.uni.freiburg.iig.telematik.sepia.petrinet.timedNet.concepts;

import java.util.List;

import de.invation.code.toval.misc.NamedComponent;

/**
 *
 * @author richard
 */
public interface TimeRessourceContext<T extends ITimeBehaviour> extends NamedComponent {
    
	/**Time related */
	
    public T getTimeFor(String activity);
    
    public void removeTimeBehaviourFor(String activity);
    
    public void addTimeBehaviourFor(String activity);
    
    
    /**Resource related**/
    
    public void addResource(String activity, List<String> resources);
    
    public void blockResources(List<String> resources);
    
    public List<List<String>> getAllowedResourcesFor(String activity);
   
    public List<String> getRandomAllowedResourcesFor(String activity);
    
    public boolean isAvailable(String ressourceName);
    
    public boolean behaviorIsKnown(String activity, List<String> resources);
    
    
    /**Time and resource related */
    
    public T getTimeFor(String activity, List<String> resources);
    
    public void removeResourceUsage(String activity, List<String> resources);
    
    public void addTimeBehaviourFor(String activity, List<String> resources, T behaviour);
    
    public void removeTimeBehaviourFor(String activity, List<String> resource);
    

    
    public String getName();
    
}
