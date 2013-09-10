package de.uni.freiburg.iig.telematik.sepia.serialize;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import de.invation.code.toval.validate.ParameterException;
import de.invation.code.toval.validate.ParameterException.ErrorCode;
import de.invation.code.toval.validate.Validate;
import de.uni.freiburg.iig.telematik.sepia.petrinet.AbstractFlowRelation;
import de.uni.freiburg.iig.telematik.sepia.petrinet.AbstractMarking;
import de.uni.freiburg.iig.telematik.sepia.petrinet.AbstractPetriNet;
import de.uni.freiburg.iig.telematik.sepia.petrinet.AbstractPlace;
import de.uni.freiburg.iig.telematik.sepia.petrinet.AbstractTransition;
import de.uni.freiburg.iig.telematik.sepia.petrinet.cpn.abstr.AbstractCPN;
import de.uni.freiburg.iig.telematik.sepia.petrinet.cwn.abstr.AbstractCWN;
import de.uni.freiburg.iig.telematik.sepia.petrinet.pt.PTNet;
import de.uni.freiburg.iig.telematik.sepia.petrinet.pt.abstr.AbstractPTNet;
import de.uni.freiburg.iig.telematik.sepia.serialize.formats.PNSerializationFormat;
import de.uni.freiburg.iig.telematik.sepia.serialize.serializer.PTSerializer_PNML;
import de.uni.freiburg.iig.telematik.sepia.serialize.serializer.PTSerializer_SoleCarmona;

public class PNSerialization {
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public static <P extends AbstractPlace<F,S>, 
	   			   T extends AbstractTransition<F,S>, 
	   			   F extends AbstractFlowRelation<P,T,S>, 
	   			   M extends AbstractMarking<S>, 
	   			   S extends Object> 

	PNSerializer<P,T,F,M,S> 

	getSerializer(AbstractPetriNet<P,T,F,M,S> net, PNSerializationFormat format) throws ParameterException, SerializationException{
		
		switch(format){
		case PNML:
			if(net instanceof AbstractCWN){
				
			}
			if(net instanceof AbstractCPN){
				
			}
			if(net instanceof AbstractPTNet){
				return new PTSerializer_PNML(net);
			}
			throw new SerializationException(de.uni.freiburg.iig.telematik.sepia.serialize.SerializationException.ErrorCode.UNSUPPORTED_NET_TYPE, net.getClass());
		case SOLE_CARMONA:
			if(net instanceof AbstractPTNet)
				return new PTSerializer_SoleCarmona(net);
			throw new SerializationException(de.uni.freiburg.iig.telematik.sepia.serialize.SerializationException.ErrorCode.UNSUPPORTED_NET_TYPE, net.getClass());
		default:
			throw new SerializationException(de.uni.freiburg.iig.telematik.sepia.serialize.SerializationException.ErrorCode.UNSUPPORTED_FORMAT, format);
			
		}
	}
	
//	public static <P extends AbstractPlace<F,S>, 
//	   			   T extends AbstractTransition<F,S>, 
//	   			   F extends AbstractFlowRelation<P,T,S>, 
//	   			   M extends AbstractMarking<S>, 
//	   			   S extends Object> 
//	
//		   PNSerializer<P,T,F,M,S> 
//	
//		   getSerializer(NetType netType, SerializationFormat format) throws ParameterException{
//		
//		switch(netType){
//		case PTNet: 
//			switch(format){
//			case PNML: 
//				return new PTSerializer_PNML();
//			case SOLE_CARMONA:
//				return null;
//			default:
//				return null;
//			}
//		case CPN:
//			switch(format){
//			case PNML: 
//				return new PTSerializer_PNML_Old();
//			case SOLE_CARMONA:
//				throw new ParameterException(ErrorCode.INCOMPATIBILITY, String.format(incompatibilityFormat, SerializationFormat.SOLE_CARMONA, NetType.CPN));
//			default:
//				return null;
//			}
//		case CWN:
//			break;
//		case IFNet:
//			break;
//		default:
//			return null;
//		}
//		return null;
//	}
	
	public static <P extends AbstractPlace<F,S>, 
	   T extends AbstractTransition<F,S>, 
	   F extends AbstractFlowRelation<P,T,S>, 
	   M extends AbstractMarking<S>, 
	   S extends Object> 

	String 

	serialize(AbstractPetriNet<P,T,F,M,S> net, PNSerializationFormat format) 
			throws SerializationException, ParameterException{

		Validate.notNull(net);
		Validate.notNull(format);
		
		StringBuilder builder = new StringBuilder();
		builder.append(format.getFileFormat().getFileHeader());
		builder.append(getSerializer(net, format).serialize());
		builder.append(format.getFileFormat().getFileFooter());

		return builder.toString();
	}
	
	public static <P extends AbstractPlace<F,S>, 
	   			   T extends AbstractTransition<F,S>, 
	   			   F extends AbstractFlowRelation<P,T,S>, 
	   			   M extends AbstractMarking<S>, 
	   			   S extends Object> 
	
	void 
	
	serialize(AbstractPetriNet<P,T,F,M,S> net, PNSerializationFormat format, String fileName, String path) 
			throws SerializationException, ParameterException, IOException{
		
		Validate.notNull(net);
		Validate.notNull(format);
		Validate.notNull(path);
		Validate.notNull(fileName);
		
		//Check if path and file name are valid
		File cPath = new File(path);
		if(!cPath.isDirectory())
			throw new IOException(path + " is not a valid path!");
		if(fileName.isEmpty())
			throw new ParameterException(ErrorCode.EMPTY);
		
		PNSerializer<P, T, F, M, S> serializer = getSerializer(net, format);
		File file = new File(String.format("%s%s.%s", path, fileName, format.getFileFormat().getFileExtension()));
		FileWriter output;
		if(file.exists()) 
			file.delete();
		file.createNewFile();
		output = new FileWriter(file, true);
		output.write(format.getFileFormat().getFileHeader());
		output.write(serializer.serialize());
		output.write(format.getFileFormat().getFileFooter());
		output.close();
	}
	
	public static void main(String[] args) throws Exception {
		PTNet net = new PTNet();
		net.addTransition("t1");
		net.addPlace("p1");
		net.addFlowRelationPT("p1", "t1");
		
		System.out.println(PNSerialization.getSerializer(net, PNSerializationFormat.SOLE_CARMONA).serialize());
	}

}
