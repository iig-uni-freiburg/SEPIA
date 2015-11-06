/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.uni.freiburg.iig.telematik.sepia.petrinet.timedNet;

import de.uni.freiburg.iig.telematik.sepia.event.TransitionEvent;
import de.uni.freiburg.iig.telematik.sepia.exception.PNException;
import de.uni.freiburg.iig.telematik.sepia.mg.pt.PTMarkingGraph;
import de.uni.freiburg.iig.telematik.sepia.petrinet.NetType;
import de.uni.freiburg.iig.telematik.sepia.petrinet.abstr.AbstractPetriNet;
import de.uni.freiburg.iig.telematik.sepia.petrinet.abstr.AbstractTransition;
import de.uni.freiburg.iig.telematik.sepia.petrinet.ifnet.IFNet;
import de.uni.freiburg.iig.telematik.sepia.petrinet.pt.PTFlowRelation;
import de.uni.freiburg.iig.telematik.sepia.petrinet.pt.PTMarking;
import de.uni.freiburg.iig.telematik.sepia.petrinet.pt.PTPlace;
import de.uni.freiburg.iig.telematik.sepia.petrinet.pt.PTTransition;
import de.uni.freiburg.iig.telematik.sepia.petrinet.pt.abstr.AbstractPTNet;
import de.uni.freiburg.iig.telematik.sepia.petrinet.timedNet.abstr.AbstractTimedNet;
import de.uni.freiburg.iig.telematik.sewol.context.process.ProcessContext;

import java.security.AccessControlContext;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @author richard
 */
public class TimedNet extends AbstractTimedNet<TimedNetPlace, TimedTransition, TimedFlowRelation, TimedMarking> {

	private static final long serialVersionUID = -2596591166620976262L;

	//private Map<Double, List<TokenConstraint>> pendingActions = new HashMap<Double, List<TokenConstraint>>();


	public TimedNet() {
		super();
		clock = 0.0;
		setAccessControl(new ProcessContext());
	}

	public static void main(String args[]) throws PNException {
		TimedNet test = new TimedNet();
	}


	@Override
	public Class<?> getMarkingGraphClass() {
		return PTMarkingGraph.class;
	}

	@Override
	protected TimedTransition createNewTransition(String name, String label, boolean isSilent) {
		return new TimedTransition(name, label, isSilent);
	}

	@Override
	protected TimedNetPlace createNewPlace(String name, String label) {
		return new TimedNetPlace(name, label);
	}

	@Override
	protected TimedFlowRelation createNewFlowRelation(TimedNetPlace place, TimedTransition transition, Integer constraint) {
		return new TimedFlowRelation(place, transition, constraint);
	}

	@Override
	protected TimedFlowRelation createNewFlowRelation(TimedTransition transition, TimedNetPlace place, Integer constraint) {
		return new TimedFlowRelation(transition, place, constraint);
	}

	@Override
	protected TimedFlowRelation createNewFlowRelation(TimedNetPlace place, TimedTransition transition) {
		return new TimedFlowRelation(place, transition);
	}

	@Override
	protected TimedFlowRelation createNewFlowRelation(TimedTransition transition, TimedNetPlace place) {
		return new TimedFlowRelation(transition, place);
	}

	@Override
	public TimedMarking createNewMarking() {
		return new TimedMarking();
	}

	@Override
	public TimedMarking fireCheck(String transitionName) throws PNException {
		TimedMarking newMarking = cloneMarking();
		TimedTransition transition = getTransition(transitionName);
		for (TimedFlowRelation relation : transition.getIncomingRelations()) {
			String inputPlaceName = relation.getPlace().getName();
			newMarking.set(inputPlaceName, newMarking.get(inputPlaceName) - relation.getWeight());
		}
		for (TimedFlowRelation relation : transition.getOutgoingRelations()) {
			String outputPlaceName = relation.getPlace().getName();
			Integer oldState = (newMarking.get(outputPlaceName) == null ? 0 : newMarking.get(outputPlaceName));
			newMarking.set(outputPlaceName, oldState + relation.getWeight());
		}
		return newMarking;
	}

	@Override
	public TimedNet newInstance() {
		return new TimedNet();
	}

//	private boolean checkPendingActions() {
//		List<Double> actionTimepoints = new ArrayList<Double>();
//		for (double pendingActionTime : pendingActions.keySet()) {
//			if (pendingActionTime <= clock) {
//				actionTimepoints.add(pendingActionTime);
//			}
//		}
//		if (actionTimepoints.isEmpty())
//			return false;
//
//		Collections.sort(actionTimepoints);
//		for (Double actionTimepoint : actionTimepoints) {
//			for (TokenConstraint constraint : pendingActions.get(actionTimepoint)) {
//				getPlace(constraint.placeName).addTokens(constraint.tokens);
//			}
//			pendingActions.remove(actionTimepoint);
//		}
//		return true;
//	}

    @Override
    public NetType getNetType() {
        return NetType.RTPTnet;
    }

//    @Override
//    public void transitionFired(TransitionEvent<? extends AbstractTransition<TimedFlowRelation, Integer>> e) {
//        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
//    }

}

