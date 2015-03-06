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
import de.uni.freiburg.iig.telematik.sepia.exception.PNValidationException;
import de.uni.freiburg.iig.telematik.sepia.petrinet.AbstractMarking;
import de.uni.freiburg.iig.telematik.sepia.petrinet.AbstractPetriNet;
import de.uni.freiburg.iig.telematik.sepia.petrinet.AbstractTransition;
import de.uni.freiburg.iig.telematik.sepia.util.PNUtils;
import de.uni.freiburg.iig.telematik.sewol.log.LogEntry;
import de.uni.freiburg.iig.telematik.sewol.log.LogTrace;
import de.uni.freiburg.iig.telematik.sewol.log.LogTraceUtils;

public class Replayer<E extends LogEntry> {
	
	protected TerminationCriteria terminationCriteria = TerminationCriteria.NO_ENABLED_TRANSITIONS;
	@SuppressWarnings("rawtypes")
	protected AbstractPetriNet petriNet = null;
	protected List<LogTrace<E>> logTraces = null;
	protected List<List<String>> activitySequences = null;
	protected Map<String, String> transitionLabelRelation = new HashMap<String, String>();
	protected Map<String, String> defaultTransitionLabelRelation = new HashMap<String, String>();
	
	@SuppressWarnings("rawtypes")
	public Replayer(AbstractPetriNet petriNet, TerminationCriteria terminationCriteria) {
		Validate.notNull(petriNet);
		try {
			petriNet.checkValidity();
		} catch (PNValidationException e) {
			throw new ParameterException("Invalid Petri net.");
		}
		this.petriNet = petriNet;
		
		@SuppressWarnings("unchecked")
		Set<String> transitionLabels = PNUtils.getLabelSetFromTransitions(petriNet.getTransitions(), false);
		for(String transitionLabel: transitionLabels){
			defaultTransitionLabelRelation.put(transitionLabel, transitionLabel);
		}
		transitionLabelRelation = defaultTransitionLabelRelation;
		this.terminationCriteria = terminationCriteria;
	}
	
	public ReplayResult<E> replayTraces(Collection<LogTrace<E>> logTraces) throws PNException{
		return replayTraces(logTraces, defaultTransitionLabelRelation);
	}
	
	@SuppressWarnings("unchecked")
	public ReplayResult<E> replayTraces(Collection<LogTrace<E>> logTraces, Map<String,String> transitionLabelRelation) throws PNException{
		Validate.notNull(logTraces);
		Validate.notNull(transitionLabelRelation);
		if(!transitionLabelRelation.keySet().containsAll(PNUtils.getLabelSetFromTransitions(petriNet.getTransitions(), false))){
			throw new ParameterException("Incomplete label relation.");
		}
		if(logTraces instanceof List){
			this.logTraces = (List<LogTrace<E>>) logTraces;
		} else {
			this.logTraces = new ArrayList<LogTrace<E>>(logTraces);
		}
		activitySequences = LogTraceUtils.createStringRepresentation(this.logTraces, true);
		return replay(transitionLabelRelation);
	}
	
	public ReplayResult<E> replaySequences(Collection<List<String>> logSequences) throws PNException{
		return replaySequences(logSequences, defaultTransitionLabelRelation);
	}
	
	@SuppressWarnings("unchecked")
	public ReplayResult<E> replaySequences(Collection<List<String>> logSequences, Map<String,String> transitionLabelRelation) throws PNException{
		Validate.notNull(logSequences);
		Validate.notNull(transitionLabelRelation);
		if(!transitionLabelRelation.keySet().containsAll(PNUtils.getLabelSetFromTransitions(petriNet.getTransitions(), false))){
			throw new ParameterException("Incomplete label relation.");
		}
		if(logSequences instanceof List){
			this.activitySequences = (List<List<String>>) logSequences;
		} else {
			this.activitySequences = new ArrayList<List<String>>(logSequences);
		}
		return replay(transitionLabelRelation);
	}
	
	private ReplayResult<E> replay(Map<String,String> transitionLabelRelation) throws PNException{
		this.transitionLabelRelation = transitionLabelRelation;
		
		Collection<LogTrace<E>> fittingTraces = new ArrayList<LogTrace<E>>();
		Collection<LogTrace<E>> nonFittingTraces = new ArrayList<LogTrace<E>>();
		Collection<List<String>> fittingSequences = new ArrayList<List<String>>();
		Collection<List<String>> nonFittingSequences = new ArrayList<List<String>>();
		for(int i=0; i<activitySequences.size(); i++){
			if(isReplayableRecursive(new ArrayList<String>(), activitySequences.get(i))){
				fittingSequences.add(activitySequences.get(i));
				if(logTraces != null)
					fittingTraces.add(logTraces.get(i));
			} else {
				nonFittingSequences.add(activitySequences.get(i));
				if(logTraces != null)
					nonFittingTraces.add(logTraces.get(i));
			}
		}
		return new ReplayResult<E>(fittingTraces, nonFittingTraces, fittingSequences, nonFittingSequences);
	}
		
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	private synchronized boolean isReplayableRecursive(List<String> path, List<String> remainingActivities) throws PNException{
//		System.out.println("REC: " + path + ", " + remainingActivities);
		petriNet.reset();
		for(String pathActivity: path){
			petriNet.fire(pathActivity);
		}
		List<AbstractTransition> enabledTransitions = petriNet.getEnabledTransitions();
//		System.out.println(enabledTransitions);
		//System.out.println("enabled transitions: " + enabledTransitions);
		if(remainingActivities.isEmpty()){
			switch(terminationCriteria){
			case POSSIBLE_FIRING_SEQUENCE: return true;
			case NO_ENABLED_TRANSITIONS: return enabledTransitions.isEmpty();
			case ESCAPABLE_WITH_SILENT_TRANSITIONS:
				if(enabledTransitions.isEmpty())
					return true;
//				Set<AbstractTransition> enabledSilentTransitions = PNUtils.getSilentTransitions(enabledTransitions);
				return escapableWithSilentTransitions(enabledTransitions, petriNet.getMarking().clone());
//				if(enabledSilentTransitions.isEmpty()){
//					System.out.println("--1--");
//					System.out.println(path);
//					System.out.println(enabledTransitions);
//					System.out.println("-----");
//					return false;
//				}
//				boolean resetRequired = false;
//				for(AbstractTransition enabledSilentTransition: enabledSilentTransitions){
//					if(resetRequired){
//						petriNet.reset();
//						for (String pathActivity : path) {
//							petriNet.fire(pathActivity);
//						}
//					}
//					petriNet.fire(enabledSilentTransition.getName());
//					if(petriNet.getEnabledTransitions().isEmpty())
//						return true;
//					resetRequired = true;
//				}
//				System.out.println("-2---");
//				System.out.println(path);
//				System.out.println(enabledTransitions);
//				System.out.println("-_---");
//				return false;
			}
		}
		if(enabledTransitions.isEmpty()){
			return false;
		}
		
		String nextActivity = remainingActivities.get(0);
		Set<AbstractTransition> recTransitions = new HashSet<AbstractTransition>();
		for(AbstractTransition enabledTransition: enabledTransitions){
			if(!enabledTransition.isSilent() && transitionLabelRelation.get(enabledTransition.getLabel()).equals(nextActivity)){
				//System.out.println("Add regular transition: " + enabledTransition);
				recTransitions.add(enabledTransition);
			}
		}
		
		Set<AbstractTransition> enabledSilentTransitions = PNUtils.getSilentTransitions(enabledTransitions);
		if(recTransitions.isEmpty() && enabledSilentTransitions.isEmpty()){
			return false;
		}
		for(AbstractTransition enabledSilentTransition: enabledSilentTransitions){
			recTransitions.add(enabledSilentTransition);
			//System.out.println("Add silent transition: " + enabledSilentTransition);
		}
		
		for(AbstractTransition recTransition: recTransitions){
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

	private boolean escapableWithSilentTransitions(List<AbstractTransition> enabledTransitions, AbstractMarking marking) throws PNException{
		if(enabledTransitions.isEmpty())
			return true;
		
		Set<AbstractTransition> enabledSilentTransitions = PNUtils.getSilentTransitions(enabledTransitions);
		if(enabledSilentTransitions.isEmpty())
			return false;
		
//		boolean recPossible = false;
		for(AbstractTransition enabledSilentTransition: enabledSilentTransitions){
			petriNet.setMarking(marking.clone());
			petriNet.fire(enabledSilentTransition.getName());
			if(escapableWithSilentTransitions(petriNet.getEnabledTransitions(), petriNet.getMarking().clone())){
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
	
//	public static void main(String[] args) throws Exception{
//		try{
//		AbstractPetriNet petriNet = PNParserDialog.showPetriNetDialog(null).getPetriNet();
//		if(petriNet == null)
//			return;
//
//		LogTrace<LogEntry> trace1 = LogTraceUtils.createTraceFromActivities(1, LogEntry.class, "A","B","C");
//		LogTrace<LogEntry> trace2 = LogTraceUtils.createTraceFromActivities(2, LogEntry.class, "A","A","B");
//		LogTrace<LogEntry> trace3 = LogTraceUtils.createTraceFromActivities(3, LogEntry.class, "B","C");
//		LogTrace<LogEntry> trace4 = LogTraceUtils.createTraceFromActivities(4, LogEntry.class, "C");
//		
//		List<LogTrace<LogEntry>> log = LogTraceUtils.createTraceList(trace1, trace2, trace3, trace4);
//		
//		XESLogParser logParser = new XESLogParser();
////		Collection<Collection<LogTrace>> logs = logParser.parse("/Users/stocker/Documents/Kooperationen/Micronas/Prozessdaten/Testdaten 2/2013/Logistisch/Zahlungsziel und Diskont entfernt/Replay/Logistisch - keine AdHoc - ohne offene Rechnungen in Zahlungsfrist - ohne Zahlungsziel - nicht trivial.mxml");
//		Collection<Collection<LogTrace<LogEntry>>> logs = logParser.parse("/Users/stocker/Documents/Kooperationen/Micronas/Prozessdaten/Testdaten 2/2013/Logistisch/Zahlungsziel und Diskont entfernt/Replay/nonFitting - nicht trivial - ohne EK-Freigabe Aktivitaeten - mit Zahlungsaktivitaet.mxml");
////		Collection<Collection<LogTrace>> logs = logParser.parse("/Users/stocker/Desktop/test.mxml");
//		
//		LogWriter fittingWriter = new LogWriter(new MXMLLogFormat(), "/Users/stocker/Desktop/fitting");
//		LogWriter nonFittingWriter = new LogWriter(new MXMLLogFormat(), "/Users/stocker/Desktop/nonFitting");
//		
//		Replaying replaying = new Replaying(petriNet);
//		ReplayResult<LogEntry> result = replaying.replay(logs.iterator().next());
//		for(LogTrace<LogEntry> fittingTrace: result.getFittingTraces()){
//			fittingWriter.writeTrace(fittingTrace);
//		}
//		fittingWriter.closeFile();
//		for(LogTrace<LogEntry> nonFittingTrace: result.getNonFittingTraces()){
//			nonFittingWriter.writeTrace(nonFittingTrace);
//		}
//		nonFittingWriter.closeFile();
//		}catch(Exception e){
//			e.printStackTrace();
//		}
//	}

}
