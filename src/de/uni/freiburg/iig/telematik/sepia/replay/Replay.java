package de.uni.freiburg.iig.telematik.sepia.replay;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import de.invation.code.toval.thread.ExecutorListener;
import de.uni.freiburg.iig.telematik.sepia.mg.abstr.AbstractMarkingGraphRelation;
import de.uni.freiburg.iig.telematik.sepia.mg.abstr.AbstractMarkingGraphState;
import de.uni.freiburg.iig.telematik.sepia.petrinet.AbstractFlowRelation;
import de.uni.freiburg.iig.telematik.sepia.petrinet.AbstractMarking;
import de.uni.freiburg.iig.telematik.sepia.petrinet.AbstractPetriNet;
import de.uni.freiburg.iig.telematik.sepia.petrinet.AbstractPlace;
import de.uni.freiburg.iig.telematik.sepia.petrinet.AbstractTransition;
import de.uni.freiburg.iig.telematik.sepia.replay.ReplayingCallable.TerminationCriteria;
import de.uni.freiburg.iig.telematik.sewol.log.LogEntry;
import de.uni.freiburg.iig.telematik.sewol.log.LogTrace;

public class Replay {
	
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
					X extends AbstractMarkingGraphState<M,S>,
					Y extends AbstractMarkingGraphRelation<M,X,S>,
					E extends LogEntry>

		void 
					
		initiateTraceReplay(AbstractPetriNet<P,T,F,M,S,X,Y> petriNet, 
							Collection<LogTrace<E>> logTraces, 
							Map<String, String> transitionLabelRelation, 
							TerminationCriteria terminationCriteria,
							ExecutorListener listener)
												
		throws ReplayException {

		ThreadedReplayer<P,T,F,M,S,X,Y,E> calculator = new ThreadedReplayer<P,T,F,M,S,X,Y,E>(petriNet, transitionLabelRelation, terminationCriteria);
		calculator.setLogTraces(logTraces);
		calculator.addExecutorListener(listener);
		calculator.runCalculation();
	}
	
	public static <	P extends AbstractPlace<F,S>, 
					T extends AbstractTransition<F,S>, 
					F extends AbstractFlowRelation<P,T,S>, 
					M extends AbstractMarking<S>, 
					S extends Object,
					X extends AbstractMarkingGraphState<M,S>,
					Y extends AbstractMarkingGraphRelation<M,X,S>,
					E extends LogEntry>

		void 
	
		initiateTraceReplay(AbstractPetriNet<P,T,F,M,S,X,Y> petriNet, 
							Collection<LogTrace<E>> logTraces,
							TerminationCriteria terminationCriteria,
							ExecutorListener listener)
								
		throws ReplayException {

		ThreadedReplayer<P,T,F,M,S,X,Y,E> calculator = new ThreadedReplayer<P,T,F,M,S,X,Y,E>(petriNet, terminationCriteria);
		calculator.setLogTraces(logTraces);
		calculator.addExecutorListener(listener);
		calculator.runCalculation();
	}
	
	public static <	P extends AbstractPlace<F,S>, 
					T extends AbstractTransition<F,S>, 
					F extends AbstractFlowRelation<P,T,S>, 
					M extends AbstractMarking<S>, 
					S extends Object,
					X extends AbstractMarkingGraphState<M,S>,
					Y extends AbstractMarkingGraphRelation<M,X,S>,
					E extends LogEntry>

		void 
					
		initiateTraceReplay(AbstractPetriNet<P,T,F,M,S,X,Y> petriNet, 
							Collection<LogTrace<E>> logTraces, 
							Map<String, String> transitionLabelRelation, 
							ExecutorListener listener)
	
		throws ReplayException {

		ThreadedReplayer<P,T,F,M,S,X,Y,E> calculator = new ThreadedReplayer<P,T,F,M,S,X,Y,E>(petriNet, transitionLabelRelation);
		calculator.setLogTraces(logTraces);
		calculator.addExecutorListener(listener);
		calculator.runCalculation();
	}
	
	public static <	P extends AbstractPlace<F,S>, 
					T extends AbstractTransition<F,S>, 
					F extends AbstractFlowRelation<P,T,S>, 
					M extends AbstractMarking<S>, 
					S extends Object,
					X extends AbstractMarkingGraphState<M,S>,
					Y extends AbstractMarkingGraphRelation<M,X,S>,
					E extends LogEntry>

		void 
					
		initiateTraceReplay(AbstractPetriNet<P,T,F,M,S,X,Y> petriNet, 
							Collection<LogTrace<E>> logTraces, 
							ExecutorListener listener)
	
		throws ReplayException {
		
		ThreadedReplayer<P,T,F,M,S,X,Y,E> calculator = new ThreadedReplayer<P,T,F,M,S,X,Y,E>(petriNet);
		calculator.setLogTraces(logTraces);
		calculator.addExecutorListener(listener);
		calculator.runCalculation();
	}
	
	public static <	P extends AbstractPlace<F,S>, 
					T extends AbstractTransition<F,S>, 
					F extends AbstractFlowRelation<P,T,S>, 
					M extends AbstractMarking<S>, 
					S extends Object,
					X extends AbstractMarkingGraphState<M,S>,
					Y extends AbstractMarkingGraphRelation<M,X,S>,
					E extends LogEntry>

		void 
	
		initiateSequenceReplay(	AbstractPetriNet<P,T,F,M,S,X,Y> petriNet, 
								Collection<List<String>> logSequences, 
								Map<String, String> transitionLabelRelation, 
								TerminationCriteria terminationCriteria,
								ExecutorListener listener)
								
		throws ReplayException {

		ThreadedReplayer<P,T,F,M,S,X,Y,E> calculator = new ThreadedReplayer<P,T,F,M,S,X,Y,E>(petriNet, transitionLabelRelation, terminationCriteria);
		calculator.setLogSequences(logSequences);
		calculator.addExecutorListener(listener);
		calculator.runCalculation();
	}

	public static <	P extends AbstractPlace<F,S>, 
					T extends AbstractTransition<F,S>, 
					F extends AbstractFlowRelation<P,T,S>, 
					M extends AbstractMarking<S>, 
					S extends Object,
					X extends AbstractMarkingGraphState<M,S>,
					Y extends AbstractMarkingGraphRelation<M,X,S>,
					E extends LogEntry>
	
		void 

		initiateSequenceReplay(	AbstractPetriNet<P,T,F,M,S,X,Y> petriNet, 
								Collection<List<String>> logSequences, 
								TerminationCriteria terminationCriteria,
								ExecutorListener listener)
				
		throws ReplayException {

		ThreadedReplayer<P,T,F,M,S,X,Y,E> calculator = new ThreadedReplayer<P,T,F,M,S,X,Y,E>(petriNet, terminationCriteria);
		calculator.setLogSequences(logSequences);
		calculator.addExecutorListener(listener);
		calculator.runCalculation();
	}

	public static <	P extends AbstractPlace<F,S>, 
					T extends AbstractTransition<F,S>, 
					F extends AbstractFlowRelation<P,T,S>, 
					M extends AbstractMarking<S>, 
					S extends Object,
					X extends AbstractMarkingGraphState<M,S>,
					Y extends AbstractMarkingGraphRelation<M,X,S>,
					E extends LogEntry>

	void 
	
	initiateSequenceReplay(	AbstractPetriNet<P,T,F,M,S,X,Y> petriNet, 
							Collection<List<String>> logSequences, 
							Map<String, String> transitionLabelRelation, 
							ExecutorListener listener)

	throws ReplayException {

	ThreadedReplayer<P,T,F,M,S,X,Y,E> calculator = new ThreadedReplayer<P,T,F,M,S,X,Y,E>(petriNet, transitionLabelRelation);
	calculator.setLogSequences(logSequences);
	calculator.addExecutorListener(listener);
	calculator.runCalculation();
	}

	public static <	P extends AbstractPlace<F,S>, 
					T extends AbstractTransition<F,S>, 
					F extends AbstractFlowRelation<P,T,S>, 
					M extends AbstractMarking<S>, 
					S extends Object,
					X extends AbstractMarkingGraphState<M,S>,
					Y extends AbstractMarkingGraphRelation<M,X,S>,
					E extends LogEntry>

		void 
	
		initiateSequenceReplay(	AbstractPetriNet<P,T,F,M,S,X,Y> petriNet, 
								Collection<List<String>> logSequences, 
								ExecutorListener listener)

		throws ReplayException {

		ThreadedReplayer<P,T,F,M,S,X,Y,E> calculator = new ThreadedReplayer<P,T,F,M,S,X,Y,E>(petriNet);
		calculator.setLogSequences(logSequences);
		calculator.addExecutorListener(listener);
		calculator.runCalculation();
	}
	
	public static <	P extends AbstractPlace<F,S>, 
					T extends AbstractTransition<F,S>, 
					F extends AbstractFlowRelation<P,T,S>, 
					M extends AbstractMarking<S>, 
					S extends Object,
					X extends AbstractMarkingGraphState<M,S>,
					Y extends AbstractMarkingGraphRelation<M,X,S>,
					E extends LogEntry>

		ReplayResult<E> 
	
		replayTraces(	AbstractPetriNet<P,T,F,M,S,X,Y> petriNet, 
						Collection<LogTrace<E>> logTraces)
						
		throws ReplayException {

		ThreadedReplayer<P,T,F,M,S,X,Y,E> calculator = new ThreadedReplayer<P,T,F,M,S,X,Y,E>(petriNet);
		calculator.setLogTraces(logTraces);
		calculator.runCalculation();
		
		return calculator.getReplayResult();
	}
	
	public static <	P extends AbstractPlace<F,S>, 
					T extends AbstractTransition<F,S>, 
					F extends AbstractFlowRelation<P,T,S>, 
					M extends AbstractMarking<S>, 
					S extends Object,
					X extends AbstractMarkingGraphState<M,S>,
					Y extends AbstractMarkingGraphRelation<M,X,S>,
					E extends LogEntry>

		ReplayResult<E> 

		replayTraces(	AbstractPetriNet<P,T,F,M,S,X,Y> petriNet, 
						Collection<LogTrace<E>> logTraces,
						Map<String, String> transitionLabelRelation)
		
		throws ReplayException {

		ThreadedReplayer<P,T,F,M,S,X,Y,E> calculator = new ThreadedReplayer<P,T,F,M,S,X,Y,E>(petriNet, transitionLabelRelation);
		calculator.setLogTraces(logTraces);
		calculator.runCalculation();

		return calculator.getReplayResult();
	}
	
	public static <	P extends AbstractPlace<F,S>, 
					T extends AbstractTransition<F,S>, 
					F extends AbstractFlowRelation<P,T,S>, 
					M extends AbstractMarking<S>, 
					S extends Object,
					X extends AbstractMarkingGraphState<M,S>,
					Y extends AbstractMarkingGraphRelation<M,X,S>,
					E extends LogEntry>

		ReplayResult<E> 

		replayTraces(	AbstractPetriNet<P,T,F,M,S,X,Y> petriNet, 
						Collection<LogTrace<E>> logTraces,
						TerminationCriteria terminationCriteria)

		throws ReplayException {

		ThreadedReplayer<P,T,F,M,S,X,Y,E> calculator = new ThreadedReplayer<P,T,F,M,S,X,Y,E>(petriNet, terminationCriteria);
		calculator.setLogTraces(logTraces);
		calculator.runCalculation();
		
		return calculator.getReplayResult();
	}
	
	public static <	P extends AbstractPlace<F,S>, 
					T extends AbstractTransition<F,S>, 
					F extends AbstractFlowRelation<P,T,S>, 
					M extends AbstractMarking<S>, 
					S extends Object,
					X extends AbstractMarkingGraphState<M,S>,
					Y extends AbstractMarkingGraphRelation<M,X,S>,
					E extends LogEntry>

		ReplayResult<E> 

		replayTraces(	AbstractPetriNet<P,T,F,M,S,X,Y> petriNet, 
						Collection<LogTrace<E>> logTraces,
						Map<String, String> transitionLabelRelation,
						TerminationCriteria terminationCriteria)

		throws ReplayException {

		ThreadedReplayer<P,T,F,M,S,X,Y,E> calculator = new ThreadedReplayer<P,T,F,M,S,X,Y,E>(petriNet, transitionLabelRelation, terminationCriteria);
		calculator.setLogTraces(logTraces);
		calculator.runCalculation();
		
		return calculator.getReplayResult();
	}
	
	public static <	P extends AbstractPlace<F,S>, 
					T extends AbstractTransition<F,S>, 
					F extends AbstractFlowRelation<P,T,S>, 
					M extends AbstractMarking<S>, 
					S extends Object,
					X extends AbstractMarkingGraphState<M,S>,
					Y extends AbstractMarkingGraphRelation<M,X,S>,
					E extends LogEntry>

		ReplayResult<E> 

		replaySequences(AbstractPetriNet<P,T,F,M,S,X,Y> petriNet, 
						Collection<List<String>> logSequences)
		
		throws ReplayException {

		ThreadedReplayer<P,T,F,M,S,X,Y,E> calculator = new ThreadedReplayer<P,T,F,M,S,X,Y,E>(petriNet);
		calculator.setLogSequences(logSequences);
		calculator.runCalculation();
		
		return calculator.getReplayResult();
	}

	public static <	P extends AbstractPlace<F,S>, 
					T extends AbstractTransition<F,S>, 
					F extends AbstractFlowRelation<P,T,S>, 
					M extends AbstractMarking<S>, 
					S extends Object,
					X extends AbstractMarkingGraphState<M,S>,
					Y extends AbstractMarkingGraphRelation<M,X,S>,
					E extends LogEntry>

		ReplayResult<E> 

		replaySequences(AbstractPetriNet<P,T,F,M,S,X,Y> petriNet, 
						Collection<List<String>> logSequences,
						Map<String, String> transitionLabelRelation)

		throws ReplayException {

		ThreadedReplayer<P,T,F,M,S,X,Y,E> calculator = new ThreadedReplayer<P,T,F,M,S,X,Y,E>(petriNet, transitionLabelRelation);
		calculator.setLogSequences(logSequences);
		calculator.runCalculation();
		
		return calculator.getReplayResult();
	}

	public static <	P extends AbstractPlace<F,S>, 
					T extends AbstractTransition<F,S>, 
					F extends AbstractFlowRelation<P,T,S>, 
					M extends AbstractMarking<S>, 
					S extends Object,
					X extends AbstractMarkingGraphState<M,S>,
					Y extends AbstractMarkingGraphRelation<M,X,S>,
					E extends LogEntry>
	
		ReplayResult<E> 

		replaySequences(AbstractPetriNet<P,T,F,M,S,X,Y> petriNet, 
						Collection<List<String>> logSequences,
						TerminationCriteria terminationCriteria)

		throws ReplayException {

		ThreadedReplayer<P,T,F,M,S,X,Y,E> calculator = new ThreadedReplayer<P,T,F,M,S,X,Y,E>(petriNet, terminationCriteria);
		calculator.setLogSequences(logSequences);
		calculator.runCalculation();
		
		return calculator.getReplayResult();
	}

	public static <	P extends AbstractPlace<F,S>, 
					T extends AbstractTransition<F,S>, 
					F extends AbstractFlowRelation<P,T,S>, 
					M extends AbstractMarking<S>, 
					S extends Object,
					X extends AbstractMarkingGraphState<M,S>,
					Y extends AbstractMarkingGraphRelation<M,X,S>,
					E extends LogEntry>
	
		ReplayResult<E> 

		replaySequences(AbstractPetriNet<P,T,F,M,S,X,Y> petriNet, 
						Collection<List<String>> logSequences,
						Map<String, String> transitionLabelRelation,
						TerminationCriteria terminationCriteria)

		throws ReplayException {

		ThreadedReplayer<P,T,F,M,S,X,Y,E> calculator = new ThreadedReplayer<P,T,F,M,S,X,Y,E>(petriNet, transitionLabelRelation, terminationCriteria);
		calculator.setLogSequences(logSequences);
		calculator.runCalculation();
		
		return calculator.getReplayResult();
	}
}
