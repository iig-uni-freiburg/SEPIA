/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.uni.freiburg.iig.telematik.sepia.petrinet.timedNet.concepts;

import java.util.List;

/**
 *
 * @author richard
 */
public interface TimeRessourceContext<T extends ITimeBehaviour> {
    
    public boolean isAvailable(String ressourceName);
    
    public boolean behaviorIsKnown(String activity, String... ressource);
    
    public T getTimeFor(String activity, String... ressource);
    
    public void addRessource(String activity, String... ressources);
    
    public List<List<String>> getAllowedRessourcesFor(String activity);
    
    public void removeRessourceUsage(String activity, String... ressources);
    
}
