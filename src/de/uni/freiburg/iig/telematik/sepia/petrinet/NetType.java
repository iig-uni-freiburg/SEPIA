package de.uni.freiburg.iig.telematik.sepia.petrinet;

import java.net.MalformedURLException;
import java.net.URL;
import java.time.zone.ZoneOffsetTransitionRule.TimeDefinition;

import javax.management.relation.RelationTypeNotFoundException;

import de.invation.code.toval.validate.Validate;
import de.uni.freiburg.iig.telematik.sepia.petrinet.cpn.abstr.AbstractCPN;
import de.uni.freiburg.iig.telematik.sepia.petrinet.ifnet.IFNet;
import de.uni.freiburg.iig.telematik.sepia.petrinet.pt.abstr.AbstractPTNet;
import de.uni.freiburg.iig.telematik.sepia.petrinet.timedNet.TimedNet;

public enum NetType {

	PTNet, CPN, IFNet, RTPTnet, Unknown;

	public static final String OfficialPTNetURI = "http://www.pnml.org/version-2009/grammar/ptnet";
	public static final String PTNetURI = "http://ifnml.process-security.de/grammar/v1.0/ptnet";
	public static final String CPNURI = "http://ifnml.process-security.de/grammar/v1.0/cpnet";
	public static final String IFNetURI = "http://ifnml.process-security.de/grammar/v1.0/ifnet";
	public static final String RTPTnetURI = "http://ifnml.process-security.de/grammar/v1.0/rtpnet";

	public static NetType getNetType(String uri) {
		Validate.notNull(uri);

		if (uri.equals(OfficialPTNetURI))
			return PTNet;
		if (uri.equals(PTNetURI))
			return PTNet;
		if (uri.equals(CPNURI))
			return CPN;
		if (uri.equals(IFNetURI))
			return IFNet;
		if(uri.equals(RTPTnetURI))
			return RTPTnet;

		return Unknown;
	}
	
	public static String getURL(NetType type) {
		Validate.notNull(type);
		switch (type){
			case PTNet: return PTNetURI;
			case CPN: 	return CPNURI;
			case IFNet:	return IFNetURI;
			case RTPTnet: 	return RTPTnetURI; 
			default:	return null;
		}
	}

	public static URL getVerificationURL(NetType type) {
		Validate.notNull(type);
		try {
			switch (type){
			case PTNet: return new URL(PTNetURI + ".pntd");
			case CPN: 	return new URL(CPNURI + ".pntd");
			case IFNet:	return new URL(IFNetURI + ".pntd");
			default:	return null;
			}
		} catch (MalformedURLException e){
			e.printStackTrace();
		}
		return null;
	}
	
	public static Class<?> getClassType(NetType type) {
		Validate.notNull(type);
		switch (type){
			case PTNet: return AbstractPTNet.class;
			case CPN: 	return AbstractCPN.class;
			case IFNet:	return IFNet.class;
			case RTPTnet: return TimedNet.class;
			default:	return null;
		}
	}
}
