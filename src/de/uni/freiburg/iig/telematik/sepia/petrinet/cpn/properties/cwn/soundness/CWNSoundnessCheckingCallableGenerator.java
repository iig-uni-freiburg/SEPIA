package de.uni.freiburg.iig.telematik.sepia.petrinet.cpn.properties.cwn.soundness;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import de.invation.code.toval.types.Multiset;
import de.invation.code.toval.validate.Validate;
import de.uni.freiburg.iig.telematik.sepia.mg.abstr.AbstractMarkingGraph;
import de.uni.freiburg.iig.telematik.sepia.petrinet.cpn.abstr.AbstractCPN;
import de.uni.freiburg.iig.telematik.sepia.petrinet.cpn.abstr.AbstractCPNFlowRelation;
import de.uni.freiburg.iig.telematik.sepia.petrinet.cpn.abstr.AbstractCPNMarking;
import de.uni.freiburg.iig.telematik.sepia.petrinet.cpn.abstr.AbstractCPNPlace;
import de.uni.freiburg.iig.telematik.sepia.petrinet.cpn.abstr.AbstractCPNTransition;
import de.uni.freiburg.iig.telematik.sepia.petrinet.properties.threaded.AbstractCallableGenerator;

public class CWNSoundnessCheckingCallableGenerator<P extends AbstractCPNPlace<F>,
										  T extends AbstractCPNTransition<F>, 
										  F extends AbstractCPNFlowRelation<P,T>, 
										  M extends AbstractCPNMarking> extends AbstractCallableGenerator<P,T,F,M,Multiset<String>> {

	public static final boolean DEFAULT_CHECK_CWN_STRUCTURE = true;
	public static final boolean DEFAULT_CHECK_BOUNDEDNESS = true;
	
	private boolean checkCWNStructure = DEFAULT_CHECK_CWN_STRUCTURE;
	private boolean checkBoundedness = DEFAULT_CHECK_BOUNDEDNESS;
	private Set<CWNPropertyFlag> propertyFlags = new HashSet<CWNPropertyFlag>();
	private AbstractMarkingGraph<M,Multiset<String>,?,?> markingGraph = null;
	
	public CWNSoundnessCheckingCallableGenerator(AbstractCPN<P,T,F,M> cpn) {
		super(cpn);
	}

	public boolean isCheckCWNStructure() {
		return checkCWNStructure;
	}

	public void setCheckCWNStructure(boolean checkCWNStructure) {
		this.checkCWNStructure = checkCWNStructure;
	}

	@Override
	public AbstractCPN<P,T,F,M> getPetriNet() {
		return (AbstractCPN<P,T,F,M>) super.getPetriNet();
	}

	public boolean isCheckBoundedness() {
		return checkBoundedness;
	}

	public void setCheckBoundedness(boolean checkBoundedness) {
		this.checkBoundedness = checkBoundedness;
	}
	
	public void addPropertyFlag(CWNPropertyFlag flag){
		Validate.notNull(flag);
		propertyFlags.add(flag);
	}
	
	public void removePropertyFlag(CWNPropertyFlag flag){
		Validate.notNull(flag);
		propertyFlags.remove(flag);
	}
	
	public boolean containsPropertyFlag(CWNPropertyFlag flag){
		Validate.notNull(flag);
		return propertyFlags.contains(flag);
	}
	
	public AbstractMarkingGraph<M,Multiset<String>,?,?> getMarkingGraph() {
		return markingGraph;
	}
	
	public void setMarkingGraph(AbstractMarkingGraph<M,Multiset<String>,?,?> markingGraph) {
		this.markingGraph = markingGraph;
	}
	
	public boolean containsPropertyFlags(){
		return !propertyFlags.isEmpty();
	}
	
	public Set<CWNPropertyFlag> getPropertyFlags(){
		return Collections.unmodifiableSet(propertyFlags);
	}
	
}
