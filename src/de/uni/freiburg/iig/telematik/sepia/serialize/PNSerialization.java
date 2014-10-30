package de.uni.freiburg.iig.telematik.sepia.serialize;

import java.io.File;
import java.io.IOException;

import de.invation.code.toval.file.FileUtils;
import de.invation.code.toval.validate.ParameterException;
import de.invation.code.toval.validate.ParameterException.ErrorCode;
import de.invation.code.toval.validate.Validate;
import de.uni.freiburg.iig.telematik.sepia.graphic.AbstractGraphicalCPN;
import de.uni.freiburg.iig.telematik.sepia.graphic.AbstractGraphicalIFNet;
import de.uni.freiburg.iig.telematik.sepia.graphic.AbstractGraphicalPN;
import de.uni.freiburg.iig.telematik.sepia.graphic.AbstractGraphicalPTNet;
import de.uni.freiburg.iig.telematik.sepia.graphic.GraphicalCPN;
import de.uni.freiburg.iig.telematik.sepia.graphic.GraphicalIFNet;
import de.uni.freiburg.iig.telematik.sepia.graphic.GraphicalPTNet;
import de.uni.freiburg.iig.telematik.sepia.graphic.netgraphics.AbstractPNGraphics;
import de.uni.freiburg.iig.telematik.sepia.mg.abstr.AbstractMarkingGraphRelation;
import de.uni.freiburg.iig.telematik.sepia.mg.abstr.AbstractMarkingGraphState;
import de.uni.freiburg.iig.telematik.sepia.petrinet.AbstractFlowRelation;
import de.uni.freiburg.iig.telematik.sepia.petrinet.AbstractMarking;
import de.uni.freiburg.iig.telematik.sepia.petrinet.AbstractPetriNet;
import de.uni.freiburg.iig.telematik.sepia.petrinet.AbstractPlace;
import de.uni.freiburg.iig.telematik.sepia.petrinet.AbstractTransition;
import de.uni.freiburg.iig.telematik.sepia.petrinet.cpn.abstr.AbstractCPN;
import de.uni.freiburg.iig.telematik.sepia.petrinet.ifnet.abstr.AbstractIFNet;
import de.uni.freiburg.iig.telematik.sepia.petrinet.pt.abstr.AbstractPTNet;
import de.uni.freiburg.iig.telematik.sepia.serialize.formats.PNSerializationFormat;
import de.uni.freiburg.iig.telematik.sepia.serialize.serializer.PNMLCPNSerializer;
import de.uni.freiburg.iig.telematik.sepia.serialize.serializer.PNMLIFNetSerializer;
import de.uni.freiburg.iig.telematik.sepia.serialize.serializer.PNMLPTNetSerializer;
import de.uni.freiburg.iig.telematik.sepia.serialize.serializer.PetrifyPTNetSerializer;

public class PNSerialization {

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public static <P extends AbstractPlace<F, S>, T extends AbstractTransition<F, S>, F extends AbstractFlowRelation<P, T, S>, M extends AbstractMarking<S>, S extends Object, X extends AbstractMarkingGraphState<M, S>, Y extends AbstractMarkingGraphRelation<M, X, S>, N extends AbstractPetriNet<P, T, F, M, S, X, Y>, G extends AbstractPNGraphics<P, T, F, M, S>>

	PNSerializer<P, T, F, M, S, X, Y, N, G>

	getSerializer(AbstractGraphicalPN<P, T, F, M, S, X, Y, N, G> net, PNSerializationFormat format) throws SerializationException {

		// ugly unbounded wildcards as work-around for bug JDK-6932571
		Object serializer = null;
		Object netObject = net;

		switch (format) {
		case PNML:
			if (netObject instanceof GraphicalIFNet) {
				serializer = new PNMLIFNetSerializer((AbstractGraphicalIFNet) net);
			}
			if (netObject instanceof GraphicalCPN) {
				serializer = new PNMLCPNSerializer((AbstractGraphicalCPN) net);
			}
			if (netObject instanceof GraphicalPTNet) {
				serializer = new PNMLPTNetSerializer((AbstractGraphicalPTNet) net);
			}
			break;
		case PETRIFY:
			if (netObject instanceof AbstractGraphicalPTNet)
				serializer = new PetrifyPTNetSerializer((AbstractGraphicalPTNet) net);
			break;
		default:
			throw new SerializationException(de.uni.freiburg.iig.telematik.sepia.serialize.SerializationException.ErrorCode.UNSUPPORTED_FORMAT, format);
		}

		if (serializer != null)
			return (PNSerializer<P, T, F, M, S, X, Y, N, G>) serializer;
		else
			throw new SerializationException(de.uni.freiburg.iig.telematik.sepia.serialize.SerializationException.ErrorCode.UNSUPPORTED_NET_TYPE, net.getClass());
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public static <P extends AbstractPlace<F, S>, T extends AbstractTransition<F, S>, F extends AbstractFlowRelation<P, T, S>, M extends AbstractMarking<S>, S extends Object, X extends AbstractMarkingGraphState<M, S>, Y extends AbstractMarkingGraphRelation<M, X, S>, N extends AbstractPetriNet<P, T, F, M, S, X, Y>, G extends AbstractPNGraphics<P, T, F, M, S>>

	PNSerializer<P, T, F, M, S, X, Y, N, G>

	getSerializer(N net, PNSerializationFormat format) throws SerializationException {

		// ugly unbounded wildcard as work-around for bug JDK-6932571
		Object netObject = net;

		switch (format) {
		case PNML:
			if (netObject instanceof AbstractIFNet) {
				return new PNMLIFNetSerializer((AbstractIFNet) net);
			}
			if (netObject instanceof AbstractCPN) {
				// CWNs fall into this category.
				return new PNMLCPNSerializer((AbstractCPN) net);
			}
			if (netObject instanceof AbstractPTNet) {
				return new PNMLPTNetSerializer((AbstractPTNet) net);

			}
			throw new SerializationException(de.uni.freiburg.iig.telematik.sepia.serialize.SerializationException.ErrorCode.UNSUPPORTED_NET_TYPE, net.getClass());
		case PETRIFY:
			if (net instanceof AbstractPTNet)
				return new PetrifyPTNetSerializer((AbstractPTNet) net);
			throw new SerializationException(de.uni.freiburg.iig.telematik.sepia.serialize.SerializationException.ErrorCode.UNSUPPORTED_NET_TYPE, net.getClass());
		default:
			throw new SerializationException(de.uni.freiburg.iig.telematik.sepia.serialize.SerializationException.ErrorCode.UNSUPPORTED_FORMAT, format);

		}
	}

	// public static <P extends AbstractPlace<F,S>,
	// T extends AbstractTransition<F,S>,
	// F extends AbstractFlowRelation<P,T,S>,
	// M extends AbstractMarking<S>,
	// S extends Object>
	//
	// PNSerializer<P,T,F,M,S>
	//
	// getSerializer(NetType netType, SerializationFormat format) throws ParameterException{
	//
	// switch(netType){
	// case PTNet:
	// switch(format){
	// case PNML:
	// return new PTSerializer_PNML();
	// case SOLE_CARMONA:
	// return null;
	// default:
	// return null;
	// }
	// case CPN:
	// switch(format){
	// case PNML:
	// return new PTSerializer_PNML_Old();
	// case SOLE_CARMONA:
	// throw new ParameterException(ErrorCode.INCOMPATIBILITY, String.format(incompatibilityFormat, SerializationFormat.SOLE_CARMONA, NetType.CPN));
	// default:
	// return null;
	// }
	// case CWN:
	// break;
	// case IFNet:
	// break;
	// default:
	// return null;
	// }
	// return null;
	// }

	public static <P extends AbstractPlace<F, S>, T extends AbstractTransition<F, S>, F extends AbstractFlowRelation<P, T, S>, M extends AbstractMarking<S>, S extends Object, X extends AbstractMarkingGraphState<M, S>, Y extends AbstractMarkingGraphRelation<M, X, S>, N extends AbstractPetriNet<P, T, F, M, S, X, Y>, G extends AbstractPNGraphics<P, T, F, M, S>>

	String

	serialize(AbstractGraphicalPN<P, T, F, M, S, X, Y, N, G> net, PNSerializationFormat format) throws SerializationException {

		Validate.notNull(net);
		Validate.notNull(format);

		StringBuilder builder = new StringBuilder();
		builder.append(format.getFileFormat().getFileHeader());
		builder.append(getSerializer(net, format).serialize());
		builder.append(format.getFileFormat().getFileFooter());

		return builder.toString();
	}

	public static <P extends AbstractPlace<F, S>, T extends AbstractTransition<F, S>, F extends AbstractFlowRelation<P, T, S>, M extends AbstractMarking<S>, S extends Object, X extends AbstractMarkingGraphState<M, S>, Y extends AbstractMarkingGraphRelation<M, X, S>, N extends AbstractPetriNet<P, T, F, M, S, X, Y>, G extends AbstractPNGraphics<P, T, F, M, S>>

	void

	serialize(AbstractGraphicalPN<P, T, F, M, S, X, Y, N, G> net, PNSerializationFormat format, String path, String fileName) throws SerializationException, IOException {

		Validate.notNull(net);
		Validate.notNull(format);

		PNSerializer<P, T, F, M, S, X, Y, N, G> serializer = getSerializer(net, format);
		serializer.serialize(path, fileName);
	}

	public static <P extends AbstractPlace<F, S>, T extends AbstractTransition<F, S>, F extends AbstractFlowRelation<P, T, S>, M extends AbstractMarking<S>, S extends Object, X extends AbstractMarkingGraphState<M, S>, Y extends AbstractMarkingGraphRelation<M, X, S>, N extends AbstractPetriNet<P, T, F, M, S, X, Y>, G extends AbstractPNGraphics<P, T, F, M, S>>

	void

	serialize(AbstractGraphicalPN<P, T, F, M, S, X, Y, N, G> net, PNSerializationFormat format, String fileName) throws SerializationException, IOException {

		Validate.notNull(fileName);
		File file = new File(fileName);
		serialize(net, format, FileUtils.getPath(file), FileUtils.getName(file));
	}

	public static <P extends AbstractPlace<F, S>, T extends AbstractTransition<F, S>, F extends AbstractFlowRelation<P, T, S>, M extends AbstractMarking<S>, S extends Object, X extends AbstractMarkingGraphState<M, S>, Y extends AbstractMarkingGraphRelation<M, X, S>, N extends AbstractPetriNet<P, T, F, M, S, X, Y>, G extends AbstractPNGraphics<P, T, F, M, S>>

	String

	serialize(N net, PNSerializationFormat format) throws SerializationException {

		Validate.notNull(net);
		Validate.notNull(format);

		StringBuilder builder = new StringBuilder();
		builder.append(format.getFileFormat().getFileHeader());
		builder.append(getSerializer(net, format).serialize());
		builder.append(format.getFileFormat().getFileFooter());

		return builder.toString();
	}

	public static <P extends AbstractPlace<F, S>, T extends AbstractTransition<F, S>, F extends AbstractFlowRelation<P, T, S>, M extends AbstractMarking<S>, S extends Object, X extends AbstractMarkingGraphState<M, S>, Y extends AbstractMarkingGraphRelation<M, X, S>, N extends AbstractPetriNet<P, T, F, M, S, X, Y>, G extends AbstractPNGraphics<P, T, F, M, S>>

	void

	serialize(N net, PNSerializationFormat format, String path, String fileName) throws SerializationException, IOException {

		Validate.notNull(net);
		Validate.notNull(format);
		Validate.notNull(path);
		Validate.fileName(fileName);

		// Check if path and file name are valid
		File cPath = new File(path);
		if (!cPath.exists())
			cPath.mkdirs();
		if (!cPath.isDirectory())
			throw new IOException(path + " is not a valid path!");
		if (fileName.isEmpty())
			throw new ParameterException(ErrorCode.EMPTY);

		PNSerializer<P, T, F, M, S, X, Y, N, G> serializer = getSerializer(net, format);

		serializer.serialize(path, fileName);
	}

	public static <P extends AbstractPlace<F,S>, 
				   T extends AbstractTransition<F,S>, 
				   F extends AbstractFlowRelation<P,T,S>, 
				   M extends AbstractMarking<S>, 
				   S extends Object, 
				   X extends AbstractMarkingGraphState<M,S>, 
				   Y extends AbstractMarkingGraphRelation<M,X,S>, 
				   N extends AbstractPetriNet<P,T,F,M,S,X,Y>, 
				   G extends AbstractPNGraphics<P,T,F,M,S>>
	
	void

	serialize(N net, PNSerializationFormat format, String fileName) throws SerializationException, IOException {
		Validate.notNull(fileName);
		File file = new File(fileName);
		serialize(net, format, FileUtils.getPath(file), FileUtils.getName(file));
	}
	
	public static void main(String[] args) throws SerializationException, IOException {
		GraphicalPTNet net = new GraphicalPTNet();
		net.getPetriNet().setName("gerd");
		System.out.println(net.getPetriNet().getName());
		PNSerialization.serialize(net, PNSerializationFormat.PNML, "/Users/stocker/Desktop/test.pnml");
	}
}
