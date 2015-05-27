package de.uni.freiburg.iig.telematik.sepia.replay;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import de.invation.code.toval.validate.Validate;
import de.uni.freiburg.iig.telematik.sepia.overlap.OverlapCallableGenerator;
import de.uni.freiburg.iig.telematik.sepia.petrinet.abstr.AbstractFlowRelation;
import de.uni.freiburg.iig.telematik.sepia.petrinet.abstr.AbstractMarking;
import de.uni.freiburg.iig.telematik.sepia.petrinet.abstr.AbstractPetriNet;
import de.uni.freiburg.iig.telematik.sepia.petrinet.abstr.AbstractPlace;
import de.uni.freiburg.iig.telematik.sepia.petrinet.abstr.AbstractTransition;
import de.uni.freiburg.iig.telematik.sepia.petrinet.properties.threaded.AbstractCallableGenerator;
import de.uni.freiburg.iig.telematik.sepia.replay.ReplayCallable.TerminationCriteria;
import de.uni.freiburg.iig.telematik.sewol.log.LogEntry;
import de.uni.freiburg.iig.telematik.sewol.log.LogTrace;
import de.uni.freiburg.iig.telematik.sewol.log.LogTraceUtils;

public class ReplayCallableGenerator< 	P extends AbstractPlace<F,S>, 
										T extends AbstractTransition<F,S>, 
										F extends AbstractFlowRelation<P,T,S>, 
										M extends AbstractMarking<S>, 
										S extends Object,
										E extends LogEntry> extends AbstractCallableGenerator<P,T,F,M,S>{

	private Map<String, String> transitionLabelRelation = null;
	private TerminationCriteria terminationCriteria = null;
	protected List<List<String>> activitySequences = null;
	protected List<LogTrace<E>> logTraces = null;
	
	public ReplayCallableGenerator(OverlapCallableGenerator<P,T,F,M,S,E> overlapGenerator){
		super(overlapGenerator);
		this.transitionLabelRelation = overlapGenerator.getTransitionLabelRelation();
		this.terminationCriteria = overlapGenerator.getTerminationCriteria();
	}
	
	public ReplayCallableGenerator(AbstractPetriNet<P,T,F,M,S> petriNet) {
		super(petriNet);
		this.transitionLabelRelation = Replay.getDefaultTransitionLabelRelation(petriNet);
	}

	
	public Map<String, String> getTransitionLabelRelation() {
		return transitionLabelRelation;
	}
	
	public void setTransitionLabelRelation(Map<String, String> transitionLabelRelation) {
		this.transitionLabelRelation = transitionLabelRelation;
	}
	
	public TerminationCriteria getTerminationCriteria() {
		return terminationCriteria;
	}
	
	public void setTerminationCriteria(TerminationCriteria terminationCriteria) {
		this.terminationCriteria = terminationCriteria;
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

	public List<List<String>> getActivitySequences() {
		return activitySequences;
	}

	public List<LogTrace<E>> getLogTraces() {
		return logTraces;
	}

}
