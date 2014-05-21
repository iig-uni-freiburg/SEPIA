package de.uni.freiburg.iig.telematik.sepia.petrinet;

import java.net.MalformedURLException;
import java.net.URL;

import de.invation.code.toval.validate.Validate;
import de.uni.freiburg.iig.telematik.sepia.petrinet.cpn.abstr.AbstractCPN;
import de.uni.freiburg.iig.telematik.sepia.petrinet.cwn.abstr.AbstractCWN;
import de.uni.freiburg.iig.telematik.sepia.petrinet.ifnet.IFNet;
import de.uni.freiburg.iig.telematik.sepia.petrinet.pt.abstr.AbstractPTNet;

public enum NetType {

	PTNet, CPN, CWN, IFNet, Unknown;

	public static final String OfficialPTNetURI = "http://www.pnml.org/version-2009/grammar/ptnet";
	public static final String PTNetURI = "http://ifnml.process-security.de/grammar/v1.0/ptnet";
	public static final String CPNURI = "http://ifnml.process-security.de/grammar/v1.0/cpnet";
	public static final String CWNURI = "http://ifnml.process-security.de/grammar/v1.0/cwnet";
	public static final String IFNetURI = "http://ifnml.process-security.de/grammar/v1.0/ifnet";

	public static NetType getNetType(String uri) {
		Validate.notNull(uri);

		if (uri.equals(OfficialPTNetURI))
			return PTNet;
		if (uri.equals(PTNetURI))
			return PTNet;
		if (uri.equals(CPNURI))
			return CPN;
		if (uri.equals(CWNURI))
			return CWN;
		if (uri.equals(IFNetURI))
			return IFNet;

		return Unknown;
	}
	
	public static String getURL(NetType type) {
		Validate.notNull(type);
		switch (type){
			case PTNet: return PTNetURI;
			case CPN: 	return CPNURI;
			case CWN:	return CWNURI;
			case IFNet:	return IFNetURI;
			default:	return null;
		}
	}

	public static URL getVerificationURL(NetType type) {
		Validate.notNull(type);
		try {
			switch (type){
			case PTNet: return new URL(PTNetURI + ".pntd");
			case CPN: 	return new URL(CPNURI + ".pntd");
			case CWN:	return new URL(CWNURI + ".pntd");
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
			case CWN:	return AbstractCWN.class;
			case IFNet:	return IFNet.class;
			default:	return null;
		}
	}
}
