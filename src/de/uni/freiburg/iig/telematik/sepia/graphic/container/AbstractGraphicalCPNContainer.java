/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.uni.freiburg.iig.telematik.sepia.graphic.container;

import de.invation.code.toval.debug.SimpleDebugger;
import de.invation.code.toval.types.Multiset;
import de.uni.freiburg.iig.telematik.sepia.graphic.AbstractGraphicalCPN;
import de.uni.freiburg.iig.telematik.sepia.graphic.netgraphics.AbstractCPNGraphics;
import de.uni.freiburg.iig.telematik.sepia.petrinet.NetType;
import de.uni.freiburg.iig.telematik.sepia.petrinet.cpn.abstr.AbstractCPN;
import de.uni.freiburg.iig.telematik.sepia.petrinet.cpn.abstr.AbstractCPNFlowRelation;
import de.uni.freiburg.iig.telematik.sepia.petrinet.cpn.abstr.AbstractCPNMarking;
import de.uni.freiburg.iig.telematik.sepia.petrinet.cpn.abstr.AbstractCPNPlace;
import de.uni.freiburg.iig.telematik.sepia.petrinet.cpn.abstr.AbstractCPNTransition;

/**
 *
 * @author stocker
 */
public class AbstractGraphicalCPNContainer<P extends AbstractCPNPlace<F>, 
								  T extends AbstractCPNTransition<F>, 
								  F extends AbstractCPNFlowRelation<P, T>, 
								  M extends AbstractCPNMarking,
								  N extends AbstractCPN<P,T,F,M>,
  							  	  G extends AbstractCPNGraphics<P,T,F,M>,
                                                                  X extends AbstractGraphicalCPN<P,T,F,M,N,G>> extends AbstractGraphicalPNContainer<P,T,F,M,Multiset<String>,N,G,X> {

    public static final String COMPONENT_DESCRIPTOR = "CPN";

    public AbstractGraphicalCPNContainer(String serializationPath) {
        super(serializationPath);
    }

    public AbstractGraphicalCPNContainer(String serializationPath, SimpleDebugger debugger) {
        super(serializationPath, debugger);
    }
    
    @Override
    protected NetType getExpectedNetType() {
        return NetType.CPN;
    }

    @Override
    public String getComponentDescriptor() {
        return COMPONENT_DESCRIPTOR;
    }
    
}
