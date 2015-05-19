package de.uni.freiburg.iig.telematik.sepia.replay;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import de.invation.code.toval.validate.ParameterException;
import de.invation.code.toval.validate.Validate;
import de.uni.freiburg.iig.telematik.sepia.exception.PNException;
import de.uni.freiburg.iig.telematik.sepia.mg.abstr.AbstractMarkingGraphRelation;
import de.uni.freiburg.iig.telematik.sepia.mg.abstr.AbstractMarkingGraphState;
import de.uni.freiburg.iig.telematik.sepia.petrinet.AbstractFlowRelation;
import de.uni.freiburg.iig.telematik.sepia.petrinet.AbstractMarking;
import de.uni.freiburg.iig.telematik.sepia.petrinet.AbstractPetriNet;
import de.uni.freiburg.iig.telematik.sepia.petrinet.AbstractPlace;
import de.uni.freiburg.iig.telematik.sepia.petrinet.AbstractTransition;
import de.uni.freiburg.iig.telematik.sepia.property.AbstractPNPropertyCheckerCallable;
import de.uni.freiburg.iig.telematik.sepia.util.PNUtils;
import de.uni.freiburg.iig.telematik.sewol.log.LogEntry;
import de.uni.freiburg.iig.telematik.sewol.log.LogTrace;
import de.uni.freiburg.iig.telematik.sewol.log.LogTraceUtils;

public class ReplayingCallable< P extends AbstractPlace<F,S>, 
										T extends AbstractTransition<F,S>, 
										F extends AbstractFlowRelation<P,T,S>, 
										M extends AbstractMarking<S>, 
										S extends Object,
										X extends AbstractMarkingGraphState<M,S>,
										Y extends AbstractMarkingGraphRelation<M,X,S>,
										E extends LogEntry> extends AbstractPNPropertyCheckerCallable<P,T,F,M,S,X,Y,ReplayResult<E>> {
	
	public static final TerminationCriteria DEFAULT_TERMINATION_CRITERIA = TerminationCriteria.NO_ENABLED_TRANSITIONS;
	protected Map<String, String> transitionLabelRelation = new HashMap<String, String>();
	protected TerminationCriteria terminationCriteria = DEFAULT_TERMINATION_CRITERIA;
	protected List<List<String>> activitySequences = null;
	protected List<LogTrace<E>> logTraces = null;
	
	protected ReplayingCallable(AbstractPetriNet<P,T,F,M,S,X,Y> petriNet) {
		super(petriNet);
		setDefaultTransitionLabelRelation();
	}
	
	protected ReplayingCallable(AbstractPetriNet<P,T,F,M,S,X,Y> petriNet, Map<String, String> transitionLabelRelation) {
		super(petriNet);
		setTransitionLabelRelation(transitionLabelRelation);
	}
	
	protected ReplayingCallable(AbstractPetriNet<P,T,F,M,S,X,Y> petriNet, TerminationCriteria terminationCriteria) {
		super(petriNet);
		setDefaultTransitionLabelRelation();
		setTerminationCriteria(terminationCriteria);
	}
	
	protected ReplayingCallable(AbstractPetriNet<P,T,F,M,S,X,Y> petriNet, Map<String, String> transitionLabelRelation, TerminationCriteria terminationCriteria) {
		super(petriNet);
		setTransitionLabelRelation(transitionLabelRelation);
		setTerminationCriteria(terminationCriteria);
	}
	
	private void setTerminationCriteria(TerminationCriteria terminationCriteria){
		Validate.notNull(terminationCriteria);
		this.terminationCriteria = terminationCriteria;
	}
	
	private void setDefaultTransitionLabelRelation(){
		transitionLabelRelation.clear();
		Set<String> transitionLabels = PNUtils.getLabelSetFromTransitions(petriNet.getTransitions(), false);
		for(String transitionLabel: transitionLabels){
			transitionLabelRelation.put(transitionLabel, transitionLabel);
		}
	}
	
	private void setTransitionLabelRelation(Map<String, String> transitionLabelRelation) {
		Validate.notNull(transitionLabelRelation);
		if (!transitionLabelRelation.keySet().containsAll(PNUtils.getLabelSetFromTransitions(petriNet.getTransitions(), false))) {
			throw new ParameterException("Incomplete label relation.");
		}
	}

	public void setLogTraces(Collection<LogTrace<E>> logTraces){
		Validate.notNull(logTraces);
		if(logTraces instanceof List){
			this.logTraces = (List<LogTrace<E>>) logTraces;
		} else {
			this.logTraces = new ArrayList<LogTrace<E>>(logTraces);
		}
		activitySequences = LogTraceUtils.createStringRepresentation(this.logTraces, true);
	}
	
	public void setLogSequences(Collection<List<String>> logSequences){
		Validate.notNull(logSequences);
		this.logTraces = null;
		if(logSequences instanceof List){
			this.activitySequences = (List<List<String>>) logSequences;
		} else {
			this.activitySequences = new ArrayList<List<String>>(logSequences);
		}
	}
	
	@Override
	protected ReplayResult<E> callRoutine() throws ReplayException, InterruptedException {
		if(logTraces == null && activitySequences == null)
			throw new ReplayException("Missing replaying input: activity sequences or log traces required");
		
		Collection<LogTrace<E>> fittingTraces = new ArrayList<LogTrace<E>>();
		Collection<LogTrace<E>> nonFittingTraces = new ArrayList<LogTrace<E>>();
		Collection<List<String>> fittingSequences = new ArrayList<List<String>>();
		Collection<List<String>> nonFittingSequences = new ArrayList<List<String>>();
		try {
			for (int i = 0; i < activitySequences.size(); i++) {
				if (Thread.currentThread().isInterrupted()) {
					throw new InterruptedException();
				}
				if (isReplayableRecursive(new ArrayList<String>(), activitySequences.get(i))) {
					fittingSequences.add(activitySequences.get(i));
					if (logTraces != null)
						fittingTraces.add(logTraces.get(i));
				} else {
					nonFittingSequences.add(activitySequences.get(i));
					if (logTraces != null)
						nonFittingTraces.add(logTraces.get(i));
				}
			}
		} catch (InterruptedException e) {
			throw e;
		} catch (Exception e) {
			throw new ReplayException("Exception during replay.<br>Reason: " + e.getMessage(), e);
		}
		return new ReplayResult<E>(fittingTraces, nonFittingTraces, fittingSequences, nonFittingSequences);
	}
	
	private synchronized boolean isReplayableRecursive(List<String> path, List<String> remainingActivities) throws PNException, InterruptedException{
//		System.out.println("REC: " + path + ", " + remainingActivities);
		petriNet.reset();
		for(String pathActivity: path){
			petriNet.fire(pathActivity);
			if (Thread.currentThread().isInterrupted()) {
				throw new InterruptedException();
			}
		}
		List<T> enabledTransitions = petriNet.getEnabledTransitions();
//		System.out.println("enabled transitions: " + enabledTransitions);
		if(remainingActivities.isEmpty()){
			switch(terminationCriteria){
			case POSSIBLE_FIRING_SEQUENCE: return true;
			case NO_ENABLED_TRANSITIONS: return enabledTransitions.isEmpty();
			case ESCAPABLE_WITH_SILENT_TRANSITIONS:
				if(enabledTransitions.isEmpty())
					return true;
				return escapableWithSilentTransitions(enabledTransitions, (M) petriNet.getMarking().clone());
			}
		}
		if(enabledTransitions.isEmpty()){
			return false;
		}
		
		String nextActivity = remainingActivities.get(0);
		Set<T> recTransitions = new HashSet<T>();
		for(T enabledTransition: enabledTransitions){
			if (Thread.currentThread().isInterrupted()) {
				throw new InterruptedException();
			}
			if(!enabledTransition.isSilent() && transitionLabelRelation.get(enabledTransition.getLabel()).equals(nextActivity)){
//				System.out.println("Add regular transition: " + enabledTransition);
				recTransitions.add(enabledTransition);
			}
		}
		
		Set<T> enabledSilentTransitions = PNUtils.getSilentTransitions(enabledTransitions);
		if(recTransitions.isEmpty() && enabledSilentTransitions.isEmpty()){
			return false;
		}
		for(T enabledSilentTransition: enabledSilentTransitions){
			if (Thread.currentThread().isInterrupted()) {
				throw new InterruptedException();
			}
			recTransitions.add(enabledSilentTransition);
//			System.out.println("Add silent transition: " + enabledSilentTransition);
		}
		
		for(T recTransition: recTransitions){
			if (Thread.currentThread().isInterrupted()) {
				throw new InterruptedException();
			}
			ArrayList<String> recPath = new ArrayList<String>(path);
			ArrayList<String> recRemainingActivities = new ArrayList<String>(remainingActivities);
			recPath.add(recTransition.getName());
			if(!recTransition.isSilent()){
				recRemainingActivities.remove(0);
			}
			if(isReplayableRecursive(recPath, recRemainingActivities)){
				return true;
			}
		}
		return false;
	}

	private boolean escapableWithSilentTransitions(List<T> enabledTransitions, M marking) throws PNException, InterruptedException{
		if(enabledTransitions.isEmpty())
			return true;
		
		Set<T> enabledSilentTransitions = PNUtils.getSilentTransitions(enabledTransitions);
		if(enabledSilentTransitions.isEmpty())
			return false;
		
		for(T enabledSilentTransition: enabledSilentTransitions){
			if (Thread.currentThread().isInterrupted()) {
				throw new InterruptedException();
			}
			petriNet.setMarking((M) marking.clone());
			petriNet.fire(enabledSilentTransition.getName());
			if(escapableWithSilentTransitions(petriNet.getEnabledTransitions(), (M) petriNet.getMarking().clone())){
				return true;
			}
		}
		return false;
	}

	public enum TerminationCriteria {
		POSSIBLE_FIRING_SEQUENCE,
		NO_ENABLED_TRANSITIONS,
		ESCAPABLE_WITH_SILENT_TRANSITIONS;
	}
	
}
