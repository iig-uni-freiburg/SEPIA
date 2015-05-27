package de.uni.freiburg.iig.telematik.sepia.petrinet.ifnet.properties.validity;

import java.util.HashSet;
import java.util.Set;

import de.invation.code.toval.types.Multiset;
import de.invation.code.toval.validate.InconsistencyException;
import de.invation.code.toval.validate.ParameterException;
import de.uni.freiburg.iig.telematik.sepia.exception.PNValidationException;
import de.uni.freiburg.iig.telematik.sepia.petrinet.cpn.properties.cwn.CWNException;
import de.uni.freiburg.iig.telematik.sepia.petrinet.cpn.properties.cwn.ThreadedCWNChecker;
import de.uni.freiburg.iig.telematik.sepia.petrinet.cpn.properties.validity.CPNValidity;
import de.uni.freiburg.iig.telematik.sepia.petrinet.ifnet.abstr.AbstractDeclassificationTransition;
import de.uni.freiburg.iig.telematik.sepia.petrinet.ifnet.abstr.AbstractIFNet;
import de.uni.freiburg.iig.telematik.sepia.petrinet.ifnet.abstr.AbstractIFNetFlowRelation;
import de.uni.freiburg.iig.telematik.sepia.petrinet.ifnet.abstr.AbstractIFNetMarking;
import de.uni.freiburg.iig.telematik.sepia.petrinet.ifnet.abstr.AbstractIFNetPlace;
import de.uni.freiburg.iig.telematik.sepia.petrinet.ifnet.abstr.AbstractIFNetTransition;
import de.uni.freiburg.iig.telematik.sepia.petrinet.ifnet.abstr.AbstractRegularIFNetTransition;
import de.uni.freiburg.iig.telematik.sepia.petrinet.ifnet.concepts.AccessMode;
import de.uni.freiburg.iig.telematik.sepia.petrinet.ifnet.concepts.SecurityLevel;
import de.uni.freiburg.iig.telematik.sepia.petrinet.properties.threaded.AbstractPNPropertyCheckerCallable;

public class IFNetValidityCheckingCallable<P extends AbstractIFNetPlace<F>,
										   T extends AbstractIFNetTransition<F>, 
										   F extends AbstractIFNetFlowRelation<P,T>, 
										   M extends AbstractIFNetMarking,
										   R extends AbstractRegularIFNetTransition<F>,
										   D extends AbstractDeclassificationTransition<F>> extends AbstractPNPropertyCheckerCallable<P, T, F, M, Multiset<String>, Boolean> {
		
	public IFNetValidityCheckingCallable(IFNetValidityCheckingCallableGenerator<P,T,F,M,R,D> generator){
		super(generator);
	}
	
	@SuppressWarnings("unchecked")
	@Override
	protected IFNetValidityCheckingCallableGenerator<P,T,F,M,R,D> getGenerator() {
		return (IFNetValidityCheckingCallableGenerator<P,T,F,M,R,D>) super.getGenerator();
	}

	@Override
	public Boolean callRoutine() throws PNValidationException, InterruptedException {
		try {
			CPNValidity.checkValidity(getGenerator().getPetriNet());
		} catch (PNValidationException e) {
			throw new PNValidationException("CPN validity requirements do not hold", e);
		}
		
		if (Thread.currentThread().isInterrupted()) {
			throw new InterruptedException();
		}
		
		ThreadedCWNChecker<P,T,F,M> cwnChecker = new ThreadedCWNChecker<P,T,F,M>(getGenerator());
		try {
			cwnChecker.runCalculation();
			cwnChecker.getCWNProperties();
		} catch (CWNException e) {
			throw new PNValidationException("Exception during CWN check", e);
		}
		
		if (Thread.currentThread().isInterrupted()) {
			throw new InterruptedException();
		}
		
		// Check property 4 for declassification transitions: 
		// For each declassification transition t, the following condition must hold:
		// No other net transition creates a token with the same color than any of the produced colors of t
		// (Either as regular transition with CREATE mode or as declassification transition)
		for (D declassificationTransition : getGenerator().getPetriNet().getDeclassificationTransitions()) {
			Set<T> otherNetTransitions = new HashSet<T>();
			otherNetTransitions.addAll(getGenerator().getPetriNet().getTransitions());
			
			if (Thread.currentThread().isInterrupted()) {
				throw new InterruptedException();
			}
			
			otherNetTransitions.remove(declassificationTransition);
			
			if (Thread.currentThread().isInterrupted()) {
				throw new InterruptedException();
			}
			
			for (T otherTransition : otherNetTransitions) {
				if (otherTransition.isDeclassificator()) {
					for (String color : declassificationTransition.getProducedAttributes()) {
						if (otherTransition.producesColor(color))
							throw new PNValidationException("There is another declassification transition which produces color \"" + color + "\"");
					}
				} else {
					for (String color : declassificationTransition.getProducedAttributes()) {
						if (otherTransition.producesColor(color) && ((R) otherTransition).getAccessModes(color).contains(AccessMode.CREATE))
							throw new PNValidationException("There is another net transition which creates tokens of color \"" + color + "\"");
					}
				}
				
				if (Thread.currentThread().isInterrupted()) {
					throw new InterruptedException();
				}
			}
		}

		checkAnalysisContextValidity();
		return true;
	}
	
	
	protected void checkAnalysisContextValidity() throws PNValidationException, InterruptedException{
		if(getGenerator().getPetriNet().getAnalysisContext() == null)
			return;
		
		if (Thread.currentThread().isInterrupted()) {
			throw new InterruptedException();
		}
		
		// Check if all token colors are contained in the analysis context in form of attributes.
		for(String tokenColor: getGenerator().getPetriNet().getTokenColors()){
			
			if (Thread.currentThread().isInterrupted()) {
				throw new InterruptedException();
			}
			
			if(tokenColor.equals(getGenerator().getPetriNet().defaultTokenColor()))
				continue;
			if(!getGenerator().getPetriNet().getAnalysisContext().getACModel().getContext().getObjects().contains(tokenColor))
				throw new PNValidationException("Analysis context does not contain attribute: " + tokenColor);
		}
		
		if (Thread.currentThread().isInterrupted()) {
			throw new InterruptedException();
		}
		
		for(T transition: getGenerator().getPetriNet().getTransitions(false)){
			if (Thread.currentThread().isInterrupted()) {
				throw new InterruptedException();
			}
			
			if(!getGenerator().getPetriNet().getAnalysisContext().getACModel().getContext().getActivities().contains(transition.getLabel()))
				throw new PNValidationException("Analysis context does not contain activity " + transition.getLabel());
		}
		
		// Check if there is a subject descriptor for every transition
		for (T transition : getGenerator().getPetriNet().getTransitions()) {
			if (Thread.currentThread().isInterrupted()) {
				throw new InterruptedException();
			}
			
			try {
				getGenerator().getPetriNet().getAnalysisContext().getSubjectDescriptor(transition.getName());
			} catch (ParameterException e) {
				throw new PNValidationException("Transition without subject descriptor: " + transition.getName());
			}
		}

		// Check security level consistency for regular transitions.
		for(String attribute: getGenerator().getPetriNet().getAnalysisContext().getACModel().getContext().getObjects()){
			
			if (Thread.currentThread().isInterrupted()) {
				throw new InterruptedException();
			}
			
			for(AbstractRegularIFNetTransition<F> transition: getGenerator().getPetriNet().getRegularTransitions()){
				if (Thread.currentThread().isInterrupted()) {
					throw new InterruptedException();
				}
				
				if(transition.processesColor(attribute)){
					// If the access modes of an activity contain CREATE for an attribute,
					// the classification of the attribute must equal the clearance of the assigned subject.
					try{
					if(transition.getAccessModes(attribute).contains(AccessMode.CREATE)){
						try {							
							if(!getGenerator().getPetriNet().getAnalysisContext().getLabeling().getAttributeClassification(attribute).equals(getGenerator().getPetriNet().getAnalysisContext().getLabeling().getSubjectClearance(getGenerator().getPetriNet().getAnalysisContext().getSubjectDescriptor(transition.getName()))))
								throw new InconsistencyException("Security level of attribute \""+attribute+"\" does not match the security level of the subject creating it.");
						} catch (ParameterException e) {
							throw new PNValidationException("Inconsistency exception in assigned analysis context:\n" + e.getMessage());
						}
					}
					}catch(ParameterException e){
						e.printStackTrace();
					}
				}
			}
		}
		
		
		// Check security level consistency for declassification transitions
		for(AbstractDeclassificationTransition<F> transition: getGenerator().getPetriNet().getDeclassificationTransitions()){
			
			if (Thread.currentThread().isInterrupted()) {
				throw new InterruptedException();
			}
			
			// Check property 6 for declassification transitions: 
			// -> All produced colors must have label LOW
			Set<String> producedColors = transition.getProducedColors();
			producedColors.remove(AbstractIFNet.CONTROL_FLOW_TOKEN_COLOR);
			for(String outputColor: producedColors){
				if(getGenerator().getPetriNet().getAnalysisContext().getLabeling().getAttributeClassification(outputColor) != SecurityLevel.LOW)
					throw new PNValidationException("Generated attributes of declassification transitions must be LOW");
			}
			
			if (Thread.currentThread().isInterrupted()) {
				throw new InterruptedException();
			}
			
			// Check property 7 for declassification transitions: 
			// -> Transition is classified HIGH
			if(getGenerator().getPetriNet().getAnalysisContext().getLabeling().getActivityClassification(transition.getName()) != SecurityLevel.HIGH)
				throw new PNValidationException("All declassification transitions must have classification HIGH.");
		}
		
	}
	

}
