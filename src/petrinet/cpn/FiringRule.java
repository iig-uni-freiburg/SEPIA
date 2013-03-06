package petrinet.cpn;

import java.util.HashMap;
import java.util.Map;

import validate.ParameterException;
import validate.Validate;

public class FiringRule {
	
	private Map<String,Map<String, Integer>> requiredTokens = new HashMap<String,Map<String, Integer>>();
	private Map<String,Map<String, Integer>> producedTokens = new HashMap<String,Map<String, Integer>>();
	
	
	public boolean containsRequirements(){
		return !requiredTokens.isEmpty();
	}
	
	public void addRequirement(String placeName, Map<String, Integer> requirement){
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
	
	public void addProduction(String placeName, Map<String, Integer> production){
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
