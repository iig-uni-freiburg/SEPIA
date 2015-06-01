package de.uni.freiburg.iig.telematik.sepia.petrinet.ifnet.properties.validity;

import de.uni.freiburg.iig.telematik.sepia.petrinet.cpn.properties.cwn.CWNCheckingCallableGenerator;
import de.uni.freiburg.iig.telematik.sepia.petrinet.ifnet.abstr.AbstractDeclassificationTransition;
import de.uni.freiburg.iig.telematik.sepia.petrinet.ifnet.abstr.AbstractIFNet;
import de.uni.freiburg.iig.telematik.sepia.petrinet.ifnet.abstr.AbstractIFNetFlowRelation;
import de.uni.freiburg.iig.telematik.sepia.petrinet.ifnet.abstr.AbstractIFNetMarking;
import de.uni.freiburg.iig.telematik.sepia.petrinet.ifnet.abstr.AbstractIFNetPlace;
import de.uni.freiburg.iig.telematik.sepia.petrinet.ifnet.abstr.AbstractIFNetTransition;
import de.uni.freiburg.iig.telematik.sepia.petrinet.ifnet.abstr.AbstractRegularIFNetTransition;

public class IFNetValidityCheckingCallableGenerator<P extends AbstractIFNetPlace<F>,
												   T extends AbstractIFNetTransition<F>, 
												   F extends AbstractIFNetFlowRelation<P,T>, 
												   M extends AbstractIFNetMarking,
												   R extends AbstractRegularIFNetTransition<F>,
												   D extends AbstractDeclassificationTransition<F>> extends CWNCheckingCallableGenerator<P,T,F,M> {

	public IFNetValidityCheckingCallableGenerator(AbstractIFNet<P,T,F,M,R,D> ifnet) {
		super(ifnet);
	}

	@SuppressWarnings("unchecked")
	@Override
	public AbstractIFNet<P,T,F,M,R,D> getPetriNet() {
		return (AbstractIFNet<P,T,F,M,R,D>) super.getPetriNet();
	}
	
	

}
