/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.uni.freiburg.iig.telematik.sepia.graphic.container;

import de.invation.code.toval.debug.SimpleDebugger;
import de.uni.freiburg.iig.telematik.sepia.graphic.GraphicalIFNet;
import de.uni.freiburg.iig.telematik.sepia.graphic.netgraphics.IFNetGraphics;
import de.uni.freiburg.iig.telematik.sepia.petrinet.ifnet.DeclassificationTransition;
import de.uni.freiburg.iig.telematik.sepia.petrinet.ifnet.IFNet;
import de.uni.freiburg.iig.telematik.sepia.petrinet.ifnet.IFNetFlowRelation;
import de.uni.freiburg.iig.telematik.sepia.petrinet.ifnet.IFNetMarking;
import de.uni.freiburg.iig.telematik.sepia.petrinet.ifnet.IFNetPlace;
import de.uni.freiburg.iig.telematik.sepia.petrinet.ifnet.RegularIFNetTransition;
import de.uni.freiburg.iig.telematik.sepia.petrinet.ifnet.abstr.AbstractIFNetTransition;

/**
 *
 * @author stocker
 */
public class GraphicalIFNetContainer extends AbstractGraphicalIFNetContainer<IFNetPlace, 
                                     AbstractIFNetTransition<IFNetFlowRelation>, 
                                     IFNetFlowRelation, 
				     IFNetMarking, 
				     RegularIFNetTransition, 
				     DeclassificationTransition, 
				     IFNet, 
				     IFNetGraphics,
                                     GraphicalIFNet> {

    public GraphicalIFNetContainer(String serializationPath) {
        super(serializationPath);
    }

    public GraphicalIFNetContainer(String serializationPath, SimpleDebugger debugger) {
        super(serializationPath, debugger);
    }
    
}
