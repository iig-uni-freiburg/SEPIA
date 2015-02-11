package de.uni.freiburg.iig.telematik.sepia.petrinet.ifnet;

import de.invation.code.toval.validate.Validate;
import de.uni.freiburg.iig.telematik.sepia.mg.pt.AbstractPTMarkingGraphRelation;
import de.uni.freiburg.iig.telematik.sepia.mg.pt.AbstractPTMarkingGraphState;
import de.uni.freiburg.iig.telematik.sepia.petrinet.cpn.CPNTransformation;
import de.uni.freiburg.iig.telematik.sepia.petrinet.pt.abstr.AbstractPTFlowRelation;
import de.uni.freiburg.iig.telematik.sepia.petrinet.pt.abstr.AbstractPTMarking;
import de.uni.freiburg.iig.telematik.sepia.petrinet.pt.abstr.AbstractPTNet;
import de.uni.freiburg.iig.telematik.sepia.petrinet.pt.abstr.AbstractPTPlace;
import de.uni.freiburg.iig.telematik.sepia.petrinet.pt.abstr.AbstractPTTransition;

public class IFNetTransformation extends CPNTransformation {

	public static <P extends AbstractPTPlace<F>, 
				   T extends AbstractPTTransition<F>, 
				   F extends AbstractPTFlowRelation<P, T>, 
				   M extends AbstractPTMarking, 
				   X extends AbstractPTMarkingGraphState<M>, 
				   Y extends AbstractPTMarkingGraphRelation<M, X>>

	IFNet transformToIFNet(AbstractPTNet<P, T, F, M, X, Y> ptNet) {
		Validate.notNull(ptNet);
		IFNet ifNet = new IFNet();
		transform(ptNet, ifNet);
		return ifNet;
	}

}
