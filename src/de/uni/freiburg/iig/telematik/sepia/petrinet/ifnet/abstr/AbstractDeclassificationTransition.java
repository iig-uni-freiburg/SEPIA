package de.uni.freiburg.iig.telematik.sepia.petrinet.ifnet.abstr;

import java.util.HashSet;
import java.util.Set;

import de.invation.code.toval.misc.SetUtils;
import de.uni.freiburg.iig.telematik.sepia.exception.PNValidationException;

public abstract class AbstractDeclassificationTransition<E extends AbstractIFNetFlowRelation<? extends AbstractIFNetPlace<E>, 
			 									? extends AbstractIFNetTransition<E>>>  extends AbstractIFNetTransition<E>{
	
	private static final long serialVersionUID = 6265290456894007048L;

//	protected AbstractDeclassificationTransition(){
//		super(TransitionType.REGULAR);
//	}
	
	public AbstractDeclassificationTransition(String name, String label, boolean isSilent) {
		super(TransitionType.DECLASSIFICATION, name, label, isSilent);
	}

	public AbstractDeclassificationTransition(String name, boolean isSilent) {
		this(name, name, isSilent);
	}

	public AbstractDeclassificationTransition(String name, String label) {
		this(name, label, false);
	}

	public AbstractDeclassificationTransition(String name) {
		this(name, name, false);
	}
	
	
	@Override
	public boolean isDeclassificator() {
		return true;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public void checkValidity() throws PNValidationException{
		super.checkValidity();
		
		// Property 1: Only one input and output place
		if(getIncomingRelations().size() > 1)
			throw new PNValidationException("Declassificators can only have one input place.");
		if(getOutgoingRelations().size() > 1)
			throw new PNValidationException("Declassificators can only have one output place.");
		
		// Property 2: The declassification transition must be effective, i.e. 
		//             consume at least one colored token (which is not the control flow token).
		Set<String> consumedAttributes = getConsumedAttributes();
		if(consumedAttributes.isEmpty())
			throw new PNValidationException("Declassificators must consume at least one colored token (which is not the control flow token).");
		
		// Property 3: The sets of consumed colors and produced colors have no elements in common
		
		if(!SetUtils.intersection(getConsumedAttributes(), getProducedAttributes()).isEmpty())
			throw new PNValidationException("The sets of consumed and produced token colors must not have common elements.");
		
		// Property 4: No other net transition creates a token with the same color than any of the produced colors of this transition.
		//      (Either as regular transition with CREATE mode or as declassification transition)
		//---> This property is checked by the enclosing net in checkValidity().
		
		
		// Property 5: Token color constraints
		consumedAttributes = getConsumedAttributes();
		Set<String> producedAttributes = getProducedAttributes();
		// 4 a) For each input token color there is one output token color.
		if(consumedAttributes.size() != producedAttributes.size())
			throw new PNValidationException("The number of consumed token colors does not match the number of produced token colors.");
		// 4 b) The number of consumed tokens for each color matches the number of produced tokens for another color.
		Set<String> assignedAttributes = new HashSet<String>();
		for(String consumedAttribute: consumedAttributes){
			for(String producedAttribute: producedAttributes){
				if(!assignedAttributes.contains(producedAttribute) && getConsumedTokens(consumedAttribute) == getProducedTokens(producedAttribute)){
					assignedAttributes.add(consumedAttribute);
					break;
				}
			}
		}
		if(!assignedAttributes.containsAll(consumedAttributes))
			throw new PNValidationException("For at least one input token color, there is no output token color where the number of produced/consumed tokens equals.");
		
	}  

}
