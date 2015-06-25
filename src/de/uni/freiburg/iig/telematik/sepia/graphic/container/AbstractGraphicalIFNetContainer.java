/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.uni.freiburg.iig.telematik.sepia.graphic.container;

import de.invation.code.toval.debug.SimpleDebugger;
import de.uni.freiburg.iig.telematik.sepia.graphic.AbstractGraphicalIFNet;
import static de.uni.freiburg.iig.telematik.sepia.graphic.container.AbstractGraphicalCPNContainer.COMPONENT_DESCRIPTOR;
import de.uni.freiburg.iig.telematik.sepia.graphic.netgraphics.AbstractIFNetGraphics;
import de.uni.freiburg.iig.telematik.sepia.petrinet.NetType;
import de.uni.freiburg.iig.telematik.sepia.petrinet.ifnet.abstr.AbstractDeclassificationTransition;
import de.uni.freiburg.iig.telematik.sepia.petrinet.ifnet.abstr.AbstractIFNet;
import de.uni.freiburg.iig.telematik.sepia.petrinet.ifnet.abstr.AbstractIFNetFlowRelation;
import de.uni.freiburg.iig.telematik.sepia.petrinet.ifnet.abstr.AbstractIFNetMarking;
import de.uni.freiburg.iig.telematik.sepia.petrinet.ifnet.abstr.AbstractIFNetPlace;
import de.uni.freiburg.iig.telematik.sepia.petrinet.ifnet.abstr.AbstractIFNetTransition;
import de.uni.freiburg.iig.telematik.sepia.petrinet.ifnet.abstr.AbstractRegularIFNetTransition;

/**
 *
 * @author stocker
 */
public class AbstractGraphicalIFNetContainer<P extends AbstractIFNetPlace<F>, 
									T extends AbstractIFNetTransition<F>, 
									F extends AbstractIFNetFlowRelation<P, T>, 
									M extends AbstractIFNetMarking,
									R extends AbstractRegularIFNetTransition<F>,
									D extends AbstractDeclassificationTransition<F>,
									N extends AbstractIFNet<P,T,F,M,R,D>,
	   							  	G extends AbstractIFNetGraphics<P,T,F,M>,
                                                                        X extends AbstractGraphicalIFNet<P,T,F,M,R,D,N,G>> extends AbstractGraphicalCPNContainer<P,T,F,M,N,G,X> {

    public static final String COMPONENT_DESCRIPTOR = "IFNet";
    
    public AbstractGraphicalIFNetContainer(String serializationPath) {
        super(serializationPath);
    }

    public AbstractGraphicalIFNetContainer(String serializationPath, SimpleDebugger debugger) {
        super(serializationPath, debugger);
    }
    
    @Override
    protected NetType getExpectedNetType() {
        return NetType.IFNet;
    }

    @Override
    public String getComponentDescriptor() {
        return COMPONENT_DESCRIPTOR;
    }
}
