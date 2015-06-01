package de.uni.freiburg.iig.telematik.sepia.replay;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import de.invation.code.toval.thread.ExecutorListener;
import de.uni.freiburg.iig.telematik.sepia.petrinet.abstr.AbstractFlowRelation;
import de.uni.freiburg.iig.telematik.sepia.petrinet.abstr.AbstractMarking;
import de.uni.freiburg.iig.telematik.sepia.petrinet.abstr.AbstractPetriNet;
import de.uni.freiburg.iig.telematik.sepia.petrinet.abstr.AbstractPlace;
import de.uni.freiburg.iig.telematik.sepia.petrinet.abstr.AbstractTransition;
import de.uni.freiburg.iig.telematik.sepia.replay.ReplayCallable.TerminationCriteria;
import de.uni.freiburg.iig.telematik.sepia.util.PNUtils;
import de.uni.freiburg.iig.telematik.sewol.log.LogEntry;

public class Replay {
	
	public static final TerminationCriteria DEFAULT_TERMINATION_CRITERIA = TerminationCriteria.NO_ENABLED_TRANSITIONS;
	
	
//	private static final String doneReplayformat = "done [fitting=%s, not fitting=%s] [%s]";
//
//	@SuppressWarnings("rawtypes")
//	public static ReplayResult<LogEntry> replay(AbstractPetriNet net, List<LogTrace<LogEntry>> traces, TerminationCriteria terminationCriteria, boolean printNonFitting) throws Exception{
//		System.out.print("Replaying log on model \""+net.getName()+"\"... ");
//		Replayer<LogEntry> replaying = new Replayer<LogEntry>(net, terminationCriteria);
//		long start = System.currentTimeMillis();
//		ReplayResult<LogEntry> result = replaying.replayTraces(traces);
//		TimeValue runtime = new TimeValue(System.currentTimeMillis() - start, TimeScale.MILLISECONDS);
//		runtime.adjustScale();
//		System.out.println(String.format(doneReplayformat, result.portionFitting(), result.portionNonFitting(), runtime));
//		if(printNonFitting)
//			CollectionUtils.print(result.getNonFittingTraces());
//		return result;
//	}
	
	public static <	P extends AbstractPlace<F,S>, 
					T extends AbstractTransition<F,S>, 
					F extends AbstractFlowRelation<P,T,S>, 
					M extends AbstractMarking<S>, 
					S extends Object,
					E extends LogEntry>

		void 
					
		initiateReplay(	ReplayCallableGenerator<P,T,F,M,S,E> generator,
						ExecutorListener<ReplayResult<E>> listener)
												
		throws ReplayException {

		ThreadedReplayer<P,T,F,M,S,E> calculator = new ThreadedReplayer<P,T,F,M,S,E>(generator);
		calculator.addExecutorListener(listener);
		calculator.runCalculation();
	}
	
	public static <	P extends AbstractPlace<F,S>, 
					T extends AbstractTransition<F,S>, 
					F extends AbstractFlowRelation<P,T,S>, 
					M extends AbstractMarking<S>, 
					S extends Object,
					E extends LogEntry>

		ReplayResult<E> 

		replayTraces(ReplayCallableGenerator<P,T,F,M,S,E> generator)
		
		throws ReplayException {

		ThreadedReplayer<P,T,F,M,S,E> calculator = new ThreadedReplayer<P,T,F,M,S,E>(generator);
		calculator.runCalculation();

		return calculator.getResult();
	}
	
	public static <	P extends AbstractPlace<F,S>, 
					T extends AbstractTransition<F,S>, 
					F extends AbstractFlowRelation<P,T,S>, 
					M extends AbstractMarking<S>, 
					S extends Object,
					E extends LogEntry>
	
		Map<String,String> 
	
		getDefaultTransitionLabelRelation(AbstractPetriNet<P,T,F,M,S> petriNet){
		
		Map<String,String> transitionLabelRelation = new HashMap<String,String>();
		Set<String> transitionLabels = PNUtils.getLabelSetFromTransitions(petriNet.getTransitions(), false);
		for(String transitionLabel: transitionLabels){
			transitionLabelRelation.put(transitionLabel, transitionLabel);
		}
		return transitionLabelRelation;
	}
}
