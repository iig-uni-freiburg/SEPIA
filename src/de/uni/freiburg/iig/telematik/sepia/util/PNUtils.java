package de.uni.freiburg.iig.telematik.sepia.util;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import de.invation.code.toval.math.MathUtils;
import de.invation.code.toval.types.HashList;
import de.invation.code.toval.validate.Validate;
import de.uni.freiburg.iig.telematik.jagal.graph.exception.VertexNotFoundException;
import de.uni.freiburg.iig.telematik.jagal.traverse.TraversalUtils;
import de.uni.freiburg.iig.telematik.sepia.petrinet.AbstractFlowRelation;
import de.uni.freiburg.iig.telematik.sepia.petrinet.AbstractMarking;
import de.uni.freiburg.iig.telematik.sepia.petrinet.AbstractPNNode;
import de.uni.freiburg.iig.telematik.sepia.petrinet.AbstractPetriNet;
import de.uni.freiburg.iig.telematik.sepia.petrinet.AbstractPlace;
import de.uni.freiburg.iig.telematik.sepia.petrinet.AbstractTransition;
import de.uni.freiburg.iig.telematik.sepia.petrinet.pt.PTMarking;
import de.uni.freiburg.iig.telematik.sepia.petrinet.pt.PTNet;

public class PNUtils {
	
	/**
	 * Transforms a collection of transitions into a set of activities
	 * by choosing the ID of the given transactions as activity names.
	 * @param transitions A collection of transitions.
	 * @return A set of activity names.
	 * @throws ParameterException 
	 */
	public static <T extends AbstractTransition<?,?>> Set<String> getNameSetFromTransitions(Collection<T> transitions, boolean includeSilentTransitions){
		Validate.notNull(transitions);
		Validate.noNullElements(transitions);
		Set<String> cActivities = new HashSet<String>();
		for(AbstractTransition<?,?> t: transitions){
			if(includeSilentTransitions || !t.isSilent())
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
	public static <T extends AbstractTransition<?,?>> List<String> getNameListFromTransitions(Collection<T> transitions){
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
	public static <T extends AbstractTransition<?,?>> Set<String> getLabelSetFromTransitions(Collection<T> transitions, boolean includeSilentTransitions){
		Validate.notNull(transitions);
		Validate.noNullElements(transitions);
		Set<String> cActivities = new HashSet<String>();
		for(AbstractTransition<?,?> t: transitions){
			if(includeSilentTransitions || !t.isSilent())
				cActivities.add(t.getLabel());
		}
		return cActivities;
	}
	
	public static <P extends AbstractPlace<F,S>, 
				   T extends AbstractTransition<F,S>, 
				   F extends AbstractFlowRelation<P,T,S>, 
				   M extends AbstractMarking<S>, 
				   S extends Object>

				   Map<T, Set<T>>
	
		getAllPredecessors(AbstractPetriNet<P,T,F,M,S> net){
		
		Map<T, Set<T>> predecessors = new HashMap<T, Set<T>>();
		for(T transition: net.getTransitions()){
			try {
				predecessors.put(transition, getPredecessors(net, transition));
			} catch (VertexNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		return predecessors;
	}
	
	@SuppressWarnings("unchecked")
	public static <P extends AbstractPlace<F,S>, 
	   				T extends AbstractTransition<F,S>, 
	   				F extends AbstractFlowRelation<P,T,S>, 
	   				M extends AbstractMarking<S>, 
	   				S extends Object>
					
	   				Set<T>
	
		getPredecessors(AbstractPetriNet<P,T,F,M,S> net, T transition) throws VertexNotFoundException{
		Validate.notNull(net);
		Validate.notNull(transition);

		Set<T> predecessors = new HashSet<T>();
		for(AbstractPNNode<F> predecessorNode: TraversalUtils.getPredecessorsFor(net, transition)){
			if(predecessorNode.isTransition()){
				predecessors.add((T) predecessorNode);
			}
		}
		return predecessors;
	}

	public static <T extends AbstractTransition<?,?>> Set<T> getSilentTransitions(Collection<T> transitions){
		Set<T> result = new HashSet<T>();
		for(T transition: transitions){
			if(transition.isSilent()){
				result.add(transition);
			}
		}
		return result;
	}
	
	public static <T extends AbstractTransition<?,?>> Set<T> getNonSilentTransitions(Collection<T> transitions){
		Set<T> result = new HashSet<T>();
		for(T transition: transitions){
			if(!transition.isSilent()){
				result.add(transition);
			}
		}
		return result;
	}
	
	public static PTNet getORFragment(Set<String> alternatives){
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
	
//	public static void main(String[] args) throws ParameterException, PNException, IOException, ParserException, XMLStreamException {
//		Set<String> alternatives = new HashSet<String>(Arrays.asList("A","B","C"));
//		PTNet net = getORFragment(alternatives);
//		RandomPTTraverser traverser = new RandomPTTraverser(net);
//		
//		for(int i=1; i<= 10; i++){
//			net.reset();
//			while(net.hasEnabledTransitions()){
//				PTTransition fireTransition = traverser.chooseNextTransition(net.getEnabledTransitions());
//				if(!fireTransition.isSilent())
//					System.out.println(fireTransition);
//				net.fire(fireTransition.getName());
//			}
//			System.out.println("-------");
//		}
//		
//		AbstractPetriNet net = new PNMLParser().parse("/Users/stocker/Desktop/LoanApplication.pnml", false, false).getPetriNet();
//		PTNet ptNet = OldPNMLParser.parsePNML("/Users/stocker/Desktop/LoanApplication.pnml", false);
//		System.out.println(getAllPredecessors((PTNet) net));
//	}
}
