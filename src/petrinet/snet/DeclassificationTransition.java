package petrinet.snet;

import java.util.HashSet;
import java.util.Set;

import petrinet.cpn.abstr.AbstractCPN;
import validate.ParameterException;
import exception.PNValidationException;

public class DeclassificationTransition extends AbstractSNetTransition{
	
	protected SNet sNet = null;
	
	public DeclassificationTransition(SNet sNet, String name, String label, boolean isEmpty) throws ParameterException {
		super(name, label, isEmpty);
		this.sNet = sNet;
	}

	public DeclassificationTransition(SNet sNet, String name, boolean isEmpty) throws ParameterException {
		this(sNet, name, name, isEmpty);
	}

	public DeclassificationTransition(SNet sNet, String name, String label) throws ParameterException {
		this(sNet, name, label, false);
	}

	public DeclassificationTransition(SNet sNet, String name) throws ParameterException {
		this(sNet, name, name, false);
	}
	
	@Override
	public boolean isDeclassificator() {
		return true;
	}
	
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
		Set<String> consumedColors = getConsumedColors();
		consumedColors.remove(AbstractCPN.CONTROL_FLOW_TOKEN_COLOR);
		if(consumedColors.isEmpty())
			throw new PNValidationException("Declassificators must consume at least one colored token (which is not the control flow token).");
		
		// Property 3: The sets of consumed colors and produced colors have no elements in common
		consumedColors = getConsumedColors();
		int numberOfConsumedColors = consumedColors.size();
		consumedColors.retainAll(getProducedColors());
		if(consumedColors.size() < numberOfConsumedColors)
			throw new PNValidationException("The sets of consumed and produced token colors must not have common elements.");
		
		// Property 4: No other net transition creates a token with the same color than any of the produced colors of this transition.
		//      (Either as regular transition with CREATE mode or as declassification transition)
		Set<AbstractSNetTransition> otherNetTransitions = new HashSet<AbstractSNetTransition>();
		otherNetTransitions.addAll(sNet.getTransitions());
		otherNetTransitions.remove(this);
		for(AbstractSNetTransition otherTransition: otherNetTransitions){
			if(otherTransition.isDeclassificator()){
				for(String color: getProducedColors()){
					if(otherTransition.producesColor(color))
						throw new PNValidationException("There is another declassification transition which produces color \""+color+"\"");
				}
			} else {
				try{
				for(String color: getProducedColors()){
					if(otherTransition.producesColor(color) && ((RegularSNetTransition) otherTransition).getAccessModes(color).contains(AccessMode.CREATE))
						throw new PNValidationException("There is another net transition which creates tokens of color \""+color+"\"");
				}
				}catch(ParameterException e){
					e.printStackTrace();
				}
			}
		}
		
		// Property 5: Token color constraints
		consumedColors = getConsumedColors();
		Set<String> producedColors = getProducedColors();
		// 4 a) For each input token color there is one output token color.
		if(consumedColors.size() != producedColors.size())
			throw new PNValidationException("The number of consumed token colors does not match the number of produced token colors.");
		// 4 b) The number of consumed tokens for each color matches the number of produced tokens for another color.
		Set<String> assignedColors = new HashSet<String>();
		for(String consumedColor: consumedColors){
			for(String producedColor: producedColors){
				if(!assignedColors.contains(producedColor) && getConsumedTokens(consumedColor) == getProducedTokens(producedColor)){
					assignedColors.add(consumedColor);
				}
			}
		}
		if(!assignedColors.containsAll(producedColors))
			throw new PNValidationException("For at least one input token color, there is no output token color where the number of produced/consumed tokens equals.");
		
	}

	@Override
	public String toPNML() {
		// TODO Auto-generated method stub
		return null;
	}

}
