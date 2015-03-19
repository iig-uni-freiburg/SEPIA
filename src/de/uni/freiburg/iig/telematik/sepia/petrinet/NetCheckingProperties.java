package de.uni.freiburg.iig.telematik.sepia.petrinet;

import de.uni.freiburg.iig.telematik.sepia.petrinet.cpn.CWNChecker.PropertyCheckingResult;

public abstract class NetCheckingProperties {

	public PropertyCheckingResult isBounded = PropertyCheckingResult.UNKNOWN;
	public Exception exception = null;

}
