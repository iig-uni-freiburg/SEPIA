package de.uni.freiburg.iig.telematik.sepia.petrinet;

import java.net.MalformedURLException;
import java.net.URL;

import de.invation.code.toval.validate.Validate;
import de.uni.freiburg.iig.telematik.sepia.petrinet.cpn.abstr.AbstractCPN;
import de.uni.freiburg.iig.telematik.sepia.petrinet.ifnet.IFNet;
import de.uni.freiburg.iig.telematik.sepia.petrinet.pt.abstr.AbstractPTNet;
import de.uni.freiburg.iig.telematik.sepia.petrinet.ptc.PTCNet;
import de.uni.freiburg.iig.telematik.sepia.petrinet.timedNet.TimedNet;

public enum NetType {

        PTNet, CPN, IFNet, RTPTnet, PTCNet, Unknown;

        public static final String OfficialPTNetURI = "http://www.pnml.org/version-2009/grammar/ptnet";
        public static final String PTNetURI = "http://ifnml.process-security.de/grammar/v1.0/ptnet";
        public static final String CPNURI = "http://ifnml.process-security.de/grammar/v1.0/cpnet";
        public static final String IFNetURI = "http://ifnml.process-security.de/grammar/v1.0/ifnet";
        public static final String RTPTnetURI = "http://ifnml.process-security.de/grammar/v1.0/rtpnet";
        public static final String PTCNetURI = "http://ifnml.process-security.de/grammar/v1.0/ptcnet";

        public static NetType getNetType(String uri) {
                Validate.notNull(uri);

		switch (uri) {
			case OfficialPTNetURI:
				return PTNet;
			case PTNetURI:
				return PTNet;
			case CPNURI:
				return CPN;
			case IFNetURI:
				return IFNet;
			case RTPTnetURI:
				return RTPTnet;
			case PTCNetURI:
				return PTCNet;
			default:
				break;
		}

                return Unknown;
        }

        public static String getURL(NetType type) {
                Validate.notNull(type);
                switch (type) {
                        case PTNet:
                                return PTNetURI;
                        case CPN:
                                return CPNURI;
                        case IFNet:
                                return IFNetURI;
                        case RTPTnet:
                                return RTPTnetURI;
            			case PTCNet:
            					return PTCNetURI;
                        default:
                                return null;
                }
        }

        public static URL getVerificationURL(NetType type) {
                Validate.notNull(type);
                try {
                        switch (type) {
                                case PTNet:
                                        return new URL(PTNetURI + ".pntd");
                                case CPN:
                                        return new URL(CPNURI + ".pntd");
                                case IFNet:
                                        return new URL(IFNetURI + ".pntd");
                                case RTPTnet:
                                		return new URL(RTPTnetURI+".pntd");
                                case PTCNet:
                            		return new URL(PTCNetURI+".pntd");
                                default:
                                        return null;
                        }
                } catch (MalformedURLException e) {
                        throw new RuntimeException(e);
                }
        }

        public static Class<?> getClassType(NetType type) {
                Validate.notNull(type);
                switch (type) {
                        case PTNet:
                                return AbstractPTNet.class;
                        case CPN:
                                return AbstractCPN.class;
                        case IFNet:
                                return IFNet.class;
                        case RTPTnet:
                                return TimedNet.class;
                        case PTCNet:
                            return PTCNet.class;
                        default:
                                return null;
                }
        }
}
