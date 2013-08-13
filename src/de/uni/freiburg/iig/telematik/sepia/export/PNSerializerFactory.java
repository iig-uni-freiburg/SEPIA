package de.uni.freiburg.iig.telematik.sepia.export;

import de.uni.freiburg.iig.telematik.sepia.petrinet.pt.abstr.AbstractPTFlowRelation;
import de.uni.freiburg.iig.telematik.sepia.petrinet.pt.abstr.AbstractPTMarking;
import de.uni.freiburg.iig.telematik.sepia.petrinet.pt.abstr.AbstractPTPlace;
import de.uni.freiburg.iig.telematik.sepia.petrinet.pt.abstr.AbstractPTTransition;

public class PNSerializerFactory {
	
	public static <P extends AbstractPTPlace<F>, 
				   T extends AbstractPTTransition<F>, 
				   F extends AbstractPTFlowRelation<P,T>, 
				   M extends AbstractPTMarking>
	
		   PNMLSerializerAbstractPTNet<P,T,F,M> 
	
		   getPNMLSerializerAbstractPTNet(){
		
		return new PNMLSerializerAbstractPTNet<P, T, F, M>();
	}

}
