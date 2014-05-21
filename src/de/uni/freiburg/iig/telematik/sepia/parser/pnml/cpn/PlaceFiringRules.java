package de.uni.freiburg.iig.telematik.sepia.parser.pnml.cpn;

import java.util.HashMap;
import java.util.Map;

import de.invation.code.toval.validate.Validate;

/**
 * Helper class to temporary save firing rules for a place
 * 
 * @author Adrian Lange
 */
public class PlaceFiringRules {

	private Map<String, Integer> incomingColorTokens = new HashMap<String, Integer>();
	private Map<String, Integer> outgoingColorTokens = new HashMap<String, Integer>();

	/**
	 * Adds a specified amount of color tokens to the incoming color tokens.
	 */
	public void addIncomingColorTokens(String color, int amount) {
		Validate.notNegative(amount);

		if (incomingColorTokens.containsKey(color)) {
			int oldValue = incomingColorTokens.get(color);
			incomingColorTokens.put(color, oldValue + amount);
		} else {
			incomingColorTokens.put(color, amount);
		}
	}

	/**
	 * Adds a specified amount of color tokens to the outgoing color tokens.
	 */
	public void addOutgoingColorTokens(String color, int amount) {
		Validate.notNegative(amount);

		if (outgoingColorTokens.containsKey(color)) {
			int oldValue = outgoingColorTokens.get(color);
			outgoingColorTokens.put(color, oldValue + amount);
		} else {
			outgoingColorTokens.put(color, amount);
		}
	}

	public Map<String, Integer> getIncomingColorTokens() {
		return incomingColorTokens;
	}

	public Map<String, Integer> getOutgoingColorTokens() {
		return outgoingColorTokens;
	}

	public void setIncomingColorTokens(Map<String, Integer> incomingColorTokens) {
		this.incomingColorTokens = incomingColorTokens;
	}

	public void setOutgoingColorTokens(Map<String, Integer> outgoingColorTokens) {
		this.outgoingColorTokens = outgoingColorTokens;
	}
}
