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
public interface ResourceContext extends NamedComponent{
    
    public abstract ResourceContext getInstance();
    
    public ResourceContext getInstance(String contextName);
    
    public boolean mayAcces(String subject, String transition);
    
    public List<String> getSubjectsFor(String transition) throws AccessContextException;
    
    public String toString();
    
    public String getName();
    
}
