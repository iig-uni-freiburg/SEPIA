/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.uni.freiburg.iig.telematik.sepia.graphic.container;

import de.invation.code.toval.debug.SimpleDebugger;
import de.uni.freiburg.iig.telematik.sepia.graphic.GraphicalCPN;
import de.uni.freiburg.iig.telematik.sepia.graphic.netgraphics.CPNGraphics;
import de.uni.freiburg.iig.telematik.sepia.petrinet.cpn.CPN;
import de.uni.freiburg.iig.telematik.sepia.petrinet.cpn.CPNFlowRelation;
import de.uni.freiburg.iig.telematik.sepia.petrinet.cpn.CPNMarking;
import de.uni.freiburg.iig.telematik.sepia.petrinet.cpn.CPNPlace;
import de.uni.freiburg.iig.telematik.sepia.petrinet.cpn.CPNTransition;

/**
 *
 * @author stocker
 */
public class GraphicalCPNContainer extends AbstractGraphicalCPNContainer<CPNPlace, CPNTransition, CPNFlowRelation, CPNMarking, CPN, CPNGraphics, GraphicalCPN> {

    public GraphicalCPNContainer(String serializationPath) {
        super(serializationPath);
    }

    public GraphicalCPNContainer(String serializationPath, SimpleDebugger debugger) {
        super(serializationPath, debugger);
    }
    
}
