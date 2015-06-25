/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.uni.freiburg.iig.telematik.sepia.graphic.container;

import de.invation.code.toval.debug.SimpleDebugger;
import de.uni.freiburg.iig.telematik.sepia.graphic.GraphicalPTNet;
import de.uni.freiburg.iig.telematik.sepia.graphic.netgraphics.PTGraphics;
import de.uni.freiburg.iig.telematik.sepia.petrinet.pt.PTFlowRelation;
import de.uni.freiburg.iig.telematik.sepia.petrinet.pt.PTMarking;
import de.uni.freiburg.iig.telematik.sepia.petrinet.pt.PTNet;
import de.uni.freiburg.iig.telematik.sepia.petrinet.pt.PTPlace;
import de.uni.freiburg.iig.telematik.sepia.petrinet.pt.PTTransition;

/**
 *
 * @author stocker
 */
public class GraphicalPTNetContainer extends AbstractGraphicalPTNetContainer<PTPlace, PTTransition, PTFlowRelation, PTMarking, PTNet, PTGraphics, GraphicalPTNet>{

    public GraphicalPTNetContainer(String serializationPath) {
        super(serializationPath);
    }

    public GraphicalPTNetContainer(String serializationPath, SimpleDebugger debugger) {
        super(serializationPath, debugger);
    }

}
