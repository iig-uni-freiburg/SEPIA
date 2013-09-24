package de.uni.freiburg.iig.telematik.sepia.petrinet.cpn;

import java.util.HashMap;
import java.util.Map;

import de.invation.code.toval.validate.ParameterException;
import de.invation.code.toval.validate.Validate;


public class FiringRule {
	
	private Map<String,Map<String, Integer>> requiredTokens = new HashMap<String,Map<String, Integer>>();
	private Map<String,Map<String, Integer>> producedTokens = new HashMap<String,Map<String, Integer>>();
	
	
	public boolean containsRequirements(){
		return !requiredTokens.isEmpty();
	}
	
	public void addRequirement(String placeName, Map<String, Integer> requirement) throws ParameterException{
		Validate.notNull(placeName);
		Validate.notNull(requirement);
		Validate.noNullElements(requirement.keySet());
		Validate.noNullElements(requirement.values());
		
		for(Integer tokens : requirement.values()){
			Validate.bigger(tokens, 0);				
		}		
		requiredTokens.put(placeName, requirement);
	}
	
	
	public void addRequirement(String placeName, String color, Integer tokens) throws ParameterException{
		Validate.notNull(placeName);
		Validate.notNull(color);
		Validate.notNull(tokens);
		Validate.bigger(tokens, 0);
		Map<String, Integer> placeRequirements = requiredTokens.get(placeName);
		if(placeRequirements == null){
			placeRequirements = new HashMap<String, Integer>();
			requiredTokens.put(placeName, placeRequirements);
		}
		placeRequirements.put(color, tokens);
	}
	
	public boolean containsProductions(){
		return !producedTokens.isEmpty();
	}
	
	public void addProduction(String placeName, Map<String, Integer> production) throws ParameterException{
		Validate.notNull(placeName);
		Validate.notNull(production);
		Validate.noNullElements(production.keySet());
		Validate.noNullElements(production.values());
		
		for(Integer tokens : production.values()){
			Validate.bigger(tokens, 0);				
		}	
		
		producedTokens.put(placeName, production);
	}
	
	public void addProduction(String placeName, String color, Integer tokens) throws ParameterException{
		Validate.notNull(placeName);
		Validate.notNull(color);
		Validate.notNull(tokens);
		Validate.bigger(tokens, 0);
		Map<String, Integer> placeProductions = producedTokens.get(placeName);
		if(placeProductions == null){
			placeProductions = new HashMap<String, Integer>();
			producedTokens.put(placeName, placeProductions);
		}
		placeProductions.put(color, tokens);
	}
	
	public Map<String,Map<String, Integer>> getRequirements(){
		return requiredTokens;
	}
	
	public Map<String,Map<String, Integer>> getProductions(){
		return producedTokens;
	}
}
