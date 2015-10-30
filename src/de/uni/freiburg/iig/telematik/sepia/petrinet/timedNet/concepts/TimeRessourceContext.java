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
    
    public boolean isAvailable(String ressourceName);
    
    public boolean behaviorIsKnown(String activity, String... ressource);
    
    public T getTimeFor(String activity, List<String> resources);
    
    public void addRessource(String activity, String... ressources);
    
    public List<List<String>> getAllowedRessourcesFor(String activity);
    
    public List<String> getRandomAllowedRessourcesFor(String activity, boolean blockRessources);
    
    public void removeRessourceUsage(String activity, String... ressources);
    
    public String getName();
    
}
