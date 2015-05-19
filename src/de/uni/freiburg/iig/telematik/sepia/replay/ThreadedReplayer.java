package de.uni.freiburg.iig.telematik.sepia.replay;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CancellationException;
import java.util.concurrent.ExecutionException;

import de.invation.code.toval.thread.AbstractCallable;
import de.uni.freiburg.iig.telematik.sepia.mg.abstr.AbstractMarkingGraphRelation;
import de.uni.freiburg.iig.telematik.sepia.mg.abstr.AbstractMarkingGraphState;
import de.uni.freiburg.iig.telematik.sepia.petrinet.AbstractFlowRelation;
import de.uni.freiburg.iig.telematik.sepia.petrinet.AbstractMarking;
import de.uni.freiburg.iig.telematik.sepia.petrinet.AbstractPetriNet;
import de.uni.freiburg.iig.telematik.sepia.petrinet.AbstractPlace;
import de.uni.freiburg.iig.telematik.sepia.petrinet.AbstractTransition;
import de.uni.freiburg.iig.telematik.sepia.property.AbstractThreadedPNPropertyChecker;
import de.uni.freiburg.iig.telematik.sepia.replay.ReplayingCallable.TerminationCriteria;
import de.uni.freiburg.iig.telematik.sewol.log.LogEntry;
import de.uni.freiburg.iig.telematik.sewol.log.LogTrace;

public class ThreadedReplayer<P extends AbstractPlace<F,S>, 
										T extends AbstractTransition<F,S>, 
										F extends AbstractFlowRelation<P,T,S>, 
										M extends AbstractMarking<S>, 
										S extends Object,
										X extends AbstractMarkingGraphState<M,S>,
										Y extends AbstractMarkingGraphRelation<M,X,S>,
										E extends LogEntry> extends AbstractThreadedPNPropertyChecker<P,T,F,M,S,X,Y,ReplayResult<E>>{
	
	protected Map<String, String> transitionLabelRelation = new HashMap<String, String>();
	protected TerminationCriteria terminationCriteria = ReplayingCallable.DEFAULT_TERMINATION_CRITERIA;
	protected Collection<List<String>> activitySequences = null;
	protected Collection<LogTrace<E>> logTraces = null;
	
	protected ThreadedReplayer(AbstractPetriNet<P,T,F,M,S,X,Y> petriNet){
		super(petriNet);
	}
	
	protected ThreadedReplayer(AbstractPetriNet<P,T,F,M,S,X,Y> petriNet, Map<String, String> transitionLabelRelation){
		super(petriNet);
		this.transitionLabelRelation = transitionLabelRelation;
	}
	
	protected ThreadedReplayer(AbstractPetriNet<P,T,F,M,S,X,Y> petriNet, TerminationCriteria terminationCriteria){
		super(petriNet);
		this.terminationCriteria = terminationCriteria;
	}
	
	protected ThreadedReplayer(AbstractPetriNet<P,T,F,M,S,X,Y> petriNet, Map<String, String> transitionLabelRelation, TerminationCriteria terminationCriteria){
		super(petriNet);
		this.transitionLabelRelation = transitionLabelRelation;
		this.terminationCriteria = terminationCriteria;
	}
	
	public void setLogTraces(Collection<LogTrace<E>> logTraces){
		this.logTraces = logTraces;
	}
	
	public void setLogSequences(Collection<List<String>> logSequences){
		this.activitySequences = logSequences;
	}
	
	@Override
	protected AbstractCallable<ReplayResult<E>> getCallable() {
		ReplayingCallable<P,T,F,M,S,X,Y,E> newCallable = null;
		if(transitionLabelRelation != null){
			if(terminationCriteria != null){
				newCallable = new ReplayingCallable<P,T,F,M,S,X,Y,E>(petriNet, transitionLabelRelation, terminationCriteria);
			} else {
				newCallable = new ReplayingCallable<P,T,F,M,S,X,Y,E>(petriNet, transitionLabelRelation);
			}
		} else {
			if(terminationCriteria != null){
				newCallable = new ReplayingCallable<P,T,F,M,S,X,Y,E>(petriNet, terminationCriteria);
			} else {
				newCallable = new ReplayingCallable<P,T,F,M,S,X,Y,E>(petriNet);
			}
		}
		if(logTraces != null){
			newCallable.setLogTraces(logTraces);
		} else if(activitySequences != null){
			newCallable.setLogSequences(activitySequences);
		}
		return newCallable;
	}
	
	public void runCalculation(){
		setUpAndRun();
	}
	
	public ReplayResult<E> getReplayResult() throws ReplayException{
		try {
			return getResult();
		} catch (CancellationException e) {
			throw new ReplayException("Replaying cancelled.", e);
		} catch (InterruptedException e) {
			throw new ReplayException("Replaying interrupted.", e);
		} catch (ExecutionException e) {
			Throwable cause = e.getCause();
			if(cause == null){
				throw new ReplayException("Exception during replay.\n" + e.getMessage(), e);
			}
			if(cause instanceof ReplayException){
				throw (ReplayException) cause;
			}
			throw new ReplayException("Exception during replay.\n" + e.getMessage(), e);
		} catch(Exception e){
			throw new ReplayException("Exception during replay.\n" + e.getMessage(), e);
		}
	}
}
