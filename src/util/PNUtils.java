package util;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import petrinet.AbstractTransition;
import types.HashList;
import validate.ParameterException;
import validate.Validate;

public class PNUtils {
	
	/**
	 * Transforms a collection of transitions into a set of activities
	 * by choosing the ID of the given transactions as activity names.
	 * @param transitions A collection of transitions.
	 * @return A set of activity names.
	 * @throws ParameterException 
	 */
	public static <T extends AbstractTransition<?,?>> Set<String> getNameSetFromTransitions(Collection<T> transitions) throws ParameterException{
		Validate.notNull(transitions);
		Validate.noNullElements(transitions);
		Set<String> cActivities = new HashSet<String>();
		for(AbstractTransition<?,?> t: transitions){
			if(!t.isSilent())
				cActivities.add(t.getName());
		}
		return cActivities;
	}
	
	/**
	 * Transforms a collection of transitions into a list of activities
	 * by choosing the ID of the given transactions as activity names.
	 * @param transitions A collection of transitions.
	 * @return A list of activity names.
	 * @throws ParameterException 
	 */
	public static <T extends AbstractTransition<?,?>> List<String> getNameListFromTransitions(Collection<T> transitions) throws ParameterException{
		Validate.notNull(transitions);
		Validate.noNullElements(transitions);
		List<String> cActivities = new HashList<String>();
		for(AbstractTransition<?,?> t: transitions){
			if(!t.isSilent())
				cActivities.add(t.getName());
		}
		return cActivities;
	}
	
	/**
	 * Transforms a collection of transitions into a set of activities
	 * by choosing the label of the given transactions as activity names.
	 * @param transitions A collection of transitions.
	 * @return A set of activity labels.
	 * @throws ParameterException 
	 */
	public static <T extends AbstractTransition<?,?>> Set<String> getLabelSetFromTransitions(Collection<T> transitions) throws ParameterException{
		Validate.notNull(transitions);
		Validate.noNullElements(transitions);
		Set<String> cActivities = new HashSet<String>();
		for(AbstractTransition<?,?> t: transitions){
			if(!t.isSilent())
				cActivities.add(t.getLabel());
		}
		return cActivities;
	}

}
