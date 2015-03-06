package de.uni.freiburg.iig.telematik.sepia.replay;

import java.util.List;

import de.invation.code.toval.misc.CollectionUtils;
import de.invation.code.toval.time.TimeScale;
import de.invation.code.toval.time.TimeValue;
import de.uni.freiburg.iig.telematik.sepia.petrinet.AbstractPetriNet;
import de.uni.freiburg.iig.telematik.sepia.replay.Replayer.TerminationCriteria;
import de.uni.freiburg.iig.telematik.sewol.log.LogEntry;
import de.uni.freiburg.iig.telematik.sewol.log.LogTrace;

public class Replay {
	
	private static final String doneReplayformat = "done [fitting=%s, not fitting=%s] [%s]";

	@SuppressWarnings("rawtypes")
	public static ReplayResult<LogEntry> replay(AbstractPetriNet net, List<LogTrace<LogEntry>> traces, TerminationCriteria terminationCriteria, boolean printNonFitting) throws Exception{
		System.out.print("Replaying log on model \""+net.getName()+"\"... ");
		Replayer<LogEntry> replaying = new Replayer<LogEntry>(net, terminationCriteria);
		long start = System.currentTimeMillis();
		ReplayResult<LogEntry> result = replaying.replayTraces(traces);
		TimeValue runtime = new TimeValue(System.currentTimeMillis() - start, TimeScale.MILLISECONDS);
		runtime.adjustScale();
		System.out.println(String.format(doneReplayformat, result.portionFitting(), result.portionNonFitting(), runtime));
		if(printNonFitting)
			CollectionUtils.print(result.getNonFittingTraces());
		return result;
	}
}
