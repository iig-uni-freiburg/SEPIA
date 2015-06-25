/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.uni.freiburg.iig.telematik.sepia.graphic.container;

import de.invation.code.toval.debug.SimpleDebugger;
import de.uni.freiburg.iig.telematik.sepia.graphic.AbstractGraphicalPTNet;
import de.uni.freiburg.iig.telematik.sepia.graphic.netgraphics.AbstractPTGraphics;
import de.uni.freiburg.iig.telematik.sepia.petrinet.NetType;
import de.uni.freiburg.iig.telematik.sepia.petrinet.pt.abstr.AbstractPTFlowRelation;
import de.uni.freiburg.iig.telematik.sepia.petrinet.pt.abstr.AbstractPTMarking;
import de.uni.freiburg.iig.telematik.sepia.petrinet.pt.abstr.AbstractPTNet;
import de.uni.freiburg.iig.telematik.sepia.petrinet.pt.abstr.AbstractPTPlace;
import de.uni.freiburg.iig.telematik.sepia.petrinet.pt.abstr.AbstractPTTransition;

/**
 *
 * @author stocker
 * @param <P>
 */
public abstract class AbstractGraphicalPTNetContainer<P extends AbstractPTPlace<F>, 
                                    T extends AbstractPTTransition<F>, 
                                    F extends AbstractPTFlowRelation<P,T>, 
                                    M extends AbstractPTMarking,
                                    N extends AbstractPTNet<P,T,F,M>,
                                    G extends AbstractPTGraphics<P,T,F,M>,
                                    X extends AbstractGraphicalPTNet<P,T,F,M,N,G>> extends AbstractGraphicalPNContainer<P,T,F,M,Integer,N,G,X>{
    
    public static final String COMPONENT_DESCRIPTOR = "PT-Net";

    public AbstractGraphicalPTNetContainer(String serializationPath) {
        super(serializationPath);
    }

    public AbstractGraphicalPTNetContainer(String serializationPath, SimpleDebugger debugger) {
        super(serializationPath, debugger);
    }

    @Override
    public String getComponentDescriptor() {
        return COMPONENT_DESCRIPTOR;
    }
    
    @Override
    protected NetType getExpectedNetType() {
        return NetType.PTNet;
    }

}
