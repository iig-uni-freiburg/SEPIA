package de.uni.freiburg.iig.telematik.sepia.mg.cpn;

import de.invation.code.toval.types.Multiset;
import de.uni.freiburg.iig.telematik.jagal.ts.Event;
import de.uni.freiburg.iig.telematik.sepia.mg.abstr.AbstractMarkingGraphRelation;
import de.uni.freiburg.iig.telematik.sepia.petrinet.cpn.abstr.AbstractCPNMarking;

public abstract class AbstractCPNMarkingGraphRelation<M extends AbstractCPNMarking, 
													  X extends AbstractCPNMarkingGraphState<M>> 

														extends AbstractMarkingGraphRelation<M,X,Multiset<String>> {

	public AbstractCPNMarkingGraphRelation(X source, X target, Event event) {
		super(source, target, event);
	}

	public AbstractCPNMarkingGraphRelation(X source, X target) {
		super(source, target);
	}

}
