package de.uni.freiburg.iig.telematik.sepia.replay;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import de.uni.freiburg.iig.telematik.sepia.exception.PNException;
import de.uni.freiburg.iig.telematik.sepia.petrinet.AbstractFlowRelation;
import de.uni.freiburg.iig.telematik.sepia.petrinet.AbstractMarking;
import de.uni.freiburg.iig.telematik.sepia.petrinet.AbstractPlace;
import de.uni.freiburg.iig.telematik.sepia.petrinet.AbstractTransition;
import de.uni.freiburg.iig.telematik.sepia.property.AbstractPNPropertyCheckerCallable;
import de.uni.freiburg.iig.telematik.sepia.util.PNUtils;
import de.uni.freiburg.iig.telematik.sewol.log.LogEntry;
import de.uni.freiburg.iig.telematik.sewol.log.LogTrace;

public class ReplayCallable< P extends AbstractPlace<F,S>, 
										T extends AbstractTransition<F,S>, 
										F extends AbstractFlowRelation<P,T,S>, 
										M extends AbstractMarking<S>, 
										S extends Object,
										E extends LogEntry> extends AbstractPNPropertyCheckerCallable<P,T,F,M,S,ReplayResult<E>> {
	
	public ReplayCallable(ReplayCallableGenerator<P,T,F,M,S,E> generator) {
		super(generator);
	}
	
	@SuppressWarnings("unchecked")
	@Override
	protected ReplayCallableGenerator<P,T,F,M,S,E> getGenerator() {
		return (ReplayCallableGenerator<P,T,F,M,S,E>) super.getGenerator();
	}

	@Override
	public ReplayResult<E> callRoutine() throws ReplayException, InterruptedException {
		if(getGenerator().getLogTraces() == null && getGenerator().getActivitySequences() == null)
			throw new ReplayException("Missing replaying input: activity sequences or log traces required");
		
		Collection<LogTrace<E>> fittingTraces = new ArrayList<LogTrace<E>>();
		Collection<LogTrace<E>> nonFittingTraces = new ArrayList<LogTrace<E>>();
		Collection<List<String>> fittingSequences = new ArrayList<List<String>>();
		Collection<List<String>> nonFittingSequences = new ArrayList<List<String>>();
		try {
			for (int i = 0; i < getGenerator().getActivitySequences().size(); i++) {
				if (Thread.currentThread().isInterrupted()) {
					throw new InterruptedException();
				}
				if (isReplayableRecursive(new ArrayList<String>(), getGenerator().getActivitySequences().get(i))) {
					fittingSequences.add(getGenerator().getActivitySequences().get(i));
					if (getGenerator().getLogTraces() != null)
						fittingTraces.add(getGenerator().getLogTraces().get(i));
				} else {
					nonFittingSequences.add(getGenerator().getActivitySequences().get(i));
					if (getGenerator().getLogTraces() != null)
						nonFittingTraces.add(getGenerator().getLogTraces().get(i));
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
		getGenerator().getPetriNet().reset();
		for(String pathActivity: path){
			getGenerator().getPetriNet().fire(pathActivity);
			if (Thread.currentThread().isInterrupted()) {
				throw new InterruptedException();
			}
		}
		List<T> enabledTransitions = getGenerator().getPetriNet().getEnabledTransitions();
//		System.out.println("enabled transitions: " + enabledTransitions);
		if(remainingActivities.isEmpty()){
			switch(getGenerator().getTerminationCriteria()){
			case POSSIBLE_FIRING_SEQUENCE: return true;
			case NO_ENABLED_TRANSITIONS: return enabledTransitions.isEmpty();
			case ESCAPABLE_WITH_SILENT_TRANSITIONS:
				if(enabledTransitions.isEmpty())
					return true;
				return escapableWithSilentTransitions(enabledTransitions, (M) getGenerator().getPetriNet().getMarking().clone());
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
			if(!enabledTransition.isSilent() && getGenerator().getTransitionLabelRelation().get(enabledTransition.getLabel()).equals(nextActivity)){
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
			getGenerator().getPetriNet().setMarking((M) marking.clone());
			getGenerator().getPetriNet().fire(enabledSilentTransition.getName());
			if(escapableWithSilentTransitions(getGenerator().getPetriNet().getEnabledTransitions(), (M) getGenerator().getPetriNet().getMarking().clone())){
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
