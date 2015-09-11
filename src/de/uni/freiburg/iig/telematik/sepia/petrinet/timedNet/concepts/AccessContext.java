/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.uni.freiburg.iig.telematik.sepia.petrinet.timedNet.concepts;


/**
 *
 * @author richard
 */
public interface AccessContext {
    
    public abstract AccessContext getInstance();
    
    public AccessContext getInstance(String contextName);
    
    public boolean mayAcces(String subject, String transition);
    
    public String getSubjectFor(String transition) throws AccessContextException;
    
}
