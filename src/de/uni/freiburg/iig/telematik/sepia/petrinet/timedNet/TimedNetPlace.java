/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.uni.freiburg.iig.telematik.sepia.petrinet.timedNet;

import de.uni.freiburg.iig.telematik.sepia.petrinet.abstr.AbstractPlace;
import de.uni.freiburg.iig.telematik.sepia.petrinet.pt.PTFlowRelation;
import de.uni.freiburg.iig.telematik.sepia.petrinet.pt.abstr.AbstractPTPlace;
import de.uni.freiburg.iig.telematik.sepia.petrinet.timedNet.abstr.AbstractTimedPlace;

/**
 *
 * @author richard
 */
public class TimedNetPlace extends AbstractTimedPlace<TimedFlowRelation>{
    

	public TimedNetPlace(String name) {
		super(name);
	}

	private static final long serialVersionUID = 1L;

        TimedNetPlace(String name, String label) {
             super(name,label);
    }

	@Override
	protected AbstractPlace newInstance(String name) {
		return new TimedNetPlace(name);
	}

}
