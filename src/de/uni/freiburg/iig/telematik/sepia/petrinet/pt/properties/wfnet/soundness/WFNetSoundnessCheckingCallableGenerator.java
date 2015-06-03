package de.uni.freiburg.iig.telematik.sepia.petrinet.pt.properties.wfnet.soundness;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import de.invation.code.toval.validate.Validate;
import de.uni.freiburg.iig.telematik.sepia.mg.abstr.AbstractMarkingGraph;
import de.uni.freiburg.iig.telematik.sepia.petrinet.properties.threaded.AbstractCallableGenerator;
import de.uni.freiburg.iig.telematik.sepia.petrinet.pt.abstr.AbstractPTFlowRelation;
import de.uni.freiburg.iig.telematik.sepia.petrinet.pt.abstr.AbstractPTMarking;
import de.uni.freiburg.iig.telematik.sepia.petrinet.pt.abstr.AbstractPTNet;
import de.uni.freiburg.iig.telematik.sepia.petrinet.pt.abstr.AbstractPTPlace;
import de.uni.freiburg.iig.telematik.sepia.petrinet.pt.abstr.AbstractPTTransition;

public class WFNetSoundnessCheckingCallableGenerator<P extends AbstractPTPlace<F>,
										  			 T extends AbstractPTTransition<F>, 
										  			 F extends AbstractPTFlowRelation<P,T>, 
										  			 M extends AbstractPTMarking> extends AbstractCallableGenerator<P,T,F,M,Integer> {

	public static final boolean DEFAULT_CHECK_WFNET_STRUCTURE = true;
	public static final boolean DEFAULT_CHECK_BOUNDEDNESS = true;
	
	private boolean checkWFNetStructure = DEFAULT_CHECK_WFNET_STRUCTURE;
	private boolean checkBoundedness = DEFAULT_CHECK_BOUNDEDNESS;
	
	private Set<WFNetSoundnessPropertyFlag> propertyFlags = new HashSet<WFNetSoundnessPropertyFlag>();
	private AbstractMarkingGraph<M,Integer,?,?> markingGraph = null;
	
	public WFNetSoundnessCheckingCallableGenerator(AbstractPTNet<P,T,F,M> ptNet) {
		super(ptNet);
	}

	public boolean isCheckCWNStructure() {
		return checkWFNetStructure;
	}

	public void setCheckCWNStructure(boolean checkCWNStructure) {
		this.checkWFNetStructure = checkCWNStructure;
	}

	@Override
	public AbstractPTNet<P,T,F,M> getPetriNet() {
		return (AbstractPTNet<P,T,F,M>) super.getPetriNet();
	}

	public boolean isCheckBoundedness() {
		return checkBoundedness;
	}

	public void setCheckBoundedness(boolean checkBoundedness) {
		this.checkBoundedness = checkBoundedness;
	}
	
	public void addPropertyFlag(WFNetSoundnessPropertyFlag flag){
		Validate.notNull(flag);
		propertyFlags.add(flag);
	}
	
	public void removePropertyFlag(WFNetSoundnessPropertyFlag flag){
		Validate.notNull(flag);
		propertyFlags.remove(flag);
	}
	
	public boolean containsPropertyFlag(WFNetSoundnessPropertyFlag flag){
		Validate.notNull(flag);
		return propertyFlags.contains(flag);
	}
	
	public AbstractMarkingGraph<M,Integer,?,?> getMarkingGraph() {
		return markingGraph;
	}
	
	public void setMarkingGraph(AbstractMarkingGraph<M,Integer,?,?> markingGraph) {
		this.markingGraph = markingGraph;
	}
	
	public boolean containsPropertyFlags(){
		return !propertyFlags.isEmpty();
	}
	
	public Set<WFNetSoundnessPropertyFlag> getPropertyFlags(){
		return Collections.unmodifiableSet(propertyFlags);
	}
	
}
