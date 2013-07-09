package util;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import petrinet.AbstractTransition;
import petrinet.pt.PTMarking;
import petrinet.pt.PTNet;
import petrinet.pt.PTTransition;
import petrinet.pt.RandomPTTraverser;
import traversal.PNTraverser;
import de.invation.code.toval.math.MathUtils;
import de.invation.code.toval.types.HashList;
import de.invation.code.toval.validate.ParameterException;
import de.invation.code.toval.validate.Validate;
import de.uni.freiburg.iig.telematik.sepia.exception.PNException;

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
	
	public static PTNet getORFragment(Set<String> alternatives) throws ParameterException{
		PTNet ptNet = new PTNet();
		String[] alt = new String[alternatives.size()];
		alternatives.toArray(alt);
		
		int k = alternatives.size();
		
		ptNet.addPlace("in");
		ptNet.addPlace("active");
		ptNet.addPlace("fin");
		ptNet.addPlace("out");
		
		ptNet.addTransition("start", true);
		ptNet.addTransition("end", true);
		ptNet.addTransition("no_option", true);

		ptNet.addFlowRelationPT("fin", "end");
		ptNet.addFlowRelationTP("end", "out");
		ptNet.addFlowRelationTP("start", "active");
		ptNet.addFlowRelationPT("active", "end");
		ptNet.addFlowRelationPT("in", "start");
		
		boolean[][] truthTable = MathUtils.getTruthTable(k);
		
		for(int i = 1; i <= k; i++){
			String alternative = alt[i-1];
			ptNet.addPlace("p"+i);
			ptNet.addPlace("p_"+alternative);
			ptNet.addPlace("p_not_"+alternative);
			
			ptNet.addTransition(alternative);
			ptNet.addTransition("Not "+alternative, true);
			
			ptNet.addFlowRelationTP("start", "p"+i);
			ptNet.addFlowRelationPT("p"+i, alternative);
			ptNet.addFlowRelationPT("p"+i, "Not "+alternative);
			ptNet.addFlowRelationTP(alternative, "p_"+alternative);
			ptNet.addFlowRelationTP("Not "+alternative, "p_not_"+alternative);
			ptNet.addFlowRelationPT("p_not_"+alternative, "no_option");
			ptNet.addFlowRelationTP("no_option", "p"+i);
		}

		for(int j = 0; j < Math.pow(2, k) - 1; j++){
			String optionTransitionName = "Option_"+(j+1);
			ptNet.addTransition(optionTransitionName, true);
			for(int l = 0; l < k; l++){
				if(truthTable[l][j]){
					ptNet.addFlowRelationPT("p_"+alt[l], optionTransitionName);
				} else {
					ptNet.addFlowRelationPT("p_not_"+alt[l], optionTransitionName);
				}
			}
			ptNet.addFlowRelationTP(optionTransitionName, "fin");
		}
		
		PTMarking marking = new PTMarking();
		marking.set("in", 1);
		ptNet.setInitialMarking(marking);
		
		return ptNet;
	}
	
	public static void main(String[] args) throws ParameterException, PNException {
		Set<String> alternatives = new HashSet<String>(Arrays.asList("A","B","C"));
		PTNet net = getORFragment(alternatives);
		RandomPTTraverser traverser = new RandomPTTraverser(net);
		
		for(int i=1; i<= 10; i++){
			net.reset();
			while(net.hasEnabledTransitions()){
				PTTransition fireTransition = traverser.chooseNextTransition(net.getEnabledTransitions());
				if(!fireTransition.isSilent())
					System.out.println(fireTransition);
				net.fire(fireTransition.getName());
			}
			System.out.println("-------");
		}
	}

}
