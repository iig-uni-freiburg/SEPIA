package de.uni.freiburg.iig.telematik.sepia.petrinet;

import java.util.Collection;
import java.util.List;

import de.invation.code.toval.validate.ParameterException;
import de.uni.freiburg.iig.telematik.jawl.context.ProcessContext;
import de.uni.freiburg.iig.telematik.sepia.util.PNUtils;
import de.uni.freiburg.iig.telematik.seram.context.data.DUContext;

public class PNProcessUtils {

	/**
	 * Creates a new context on basis of the given Petri net transitions.<br>
	 * Transitions are converted into a list of activity names.
	 * @param transitions Petri net transitions to be used as basis for activity names.
	 * @throws Exception If activity list is <code>null</code> or empty.
	 * @see {@link #getNameListFromTransitions(Collection)}
	 */
	public static ProcessContext createProcessContext(String name, Collection<AbstractTransition<?,?>> transitions){
		ProcessContext context = new ProcessContext(name);
		context.setActivities(PNUtils.getLabelSetFromTransitions(transitions, false));
		return context;
	}

	
	/**
	 * Creates a new context using an RBAC access control model.<br>
	 * Users and permissions to execute transactions are randomly assigned to the given roles.<br>
	 * Each person is assigned to exactly one role.
	 * @param transitions A collection of Petri net transitions.
	 * @param originatorCount The number of desired originators.
	 * @param roles The roles to use.
	 * @return A new randomly generated Context.
	 * @throws ParameterException 
	 * @throws Exception 
	 */
	public static ProcessContext createRandomContext(Collection<AbstractTransition<?,?>> transitions, int originatorCount, List<String> roles){
		DUContext duContext = DUContext.createRandomContext(PNUtils.getLabelSetFromTransitions(transitions, false), originatorCount, roles);
		try {
			return new ProcessContext(duContext);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
	}
	
}
