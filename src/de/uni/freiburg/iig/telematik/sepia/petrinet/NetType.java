package de.uni.freiburg.iig.telematik.sepia.petrinet;

import java.net.MalformedURLException;
import java.net.URL;

import de.invation.code.toval.validate.ParameterException;
import de.invation.code.toval.validate.Validate;

public enum NetType {

	PTNet, CPN, CWN, IFNet;

	public static final String OfficialPTNetURI = "http://www.pnml.org/version-2009/grammar/ptnet";
	public static final String PTNetURI = "http://files.telematik.uni-freiburg.de/ifnml/grammar/v1.0/ptnet";
	public static final String CPNURI = "http://files.telematik.uni-freiburg.de/ifnml/grammar/v1.0/cpnet";
	public static final String CWNURI = "http://files.telematik.uni-freiburg.de/ifnml/grammar/v1.0/cwnet";
	public static final String IFNetURI = "http://files.telematik.uni-freiburg.de/ifnml/grammar/v1.0/ifnet";

	public static NetType getNetType(String uri) throws ParameterException {
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

		return null;
	}

	public static URL getURL(NetType type) throws ParameterException{
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
}
