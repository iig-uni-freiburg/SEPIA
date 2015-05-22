package de.uni.freiburg.iig.telematik.sepia.serialize.serializer;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import de.invation.code.toval.file.FileFormat;
import de.invation.code.toval.validate.ParameterException;
import de.uni.freiburg.iig.telematik.sepia.graphic.AbstractGraphicalPTNet;
import de.uni.freiburg.iig.telematik.sepia.graphic.netgraphics.AbstractPTGraphics;
import de.uni.freiburg.iig.telematik.sepia.petrinet.NetType;
import de.uni.freiburg.iig.telematik.sepia.petrinet.pt.abstr.AbstractPTFlowRelation;
import de.uni.freiburg.iig.telematik.sepia.petrinet.pt.abstr.AbstractPTMarking;
import de.uni.freiburg.iig.telematik.sepia.petrinet.pt.abstr.AbstractPTNet;
import de.uni.freiburg.iig.telematik.sepia.petrinet.pt.abstr.AbstractPTPlace;
import de.uni.freiburg.iig.telematik.sepia.petrinet.pt.abstr.AbstractPTTransition;
import de.uni.freiburg.iig.telematik.sepia.serialize.PNSerializer;
import de.uni.freiburg.iig.telematik.sepia.serialize.SerializationException;
import de.uni.freiburg.iig.telematik.sepia.serialize.formats.PNFF_Petrify;

public class PetrifyPTNetSerializer<P extends AbstractPTPlace<F>, 
									  T extends AbstractPTTransition<F>, 
									  F extends AbstractPTFlowRelation<P,T>, 
									  M extends AbstractPTMarking,
									  N extends AbstractPTNet<P,T,F,M>,
	   							  	  G extends AbstractPTGraphics<P,T,F,M>> extends PNSerializer<P,T,F,M,Integer,N,G>{
	
	private final String RELATION_FORMAT = "%s %s";
	private final String RELATION_WEIGHTED_FORMAT = RELATION_FORMAT + "(%s)";
	private final String BOUNDED_PLACE_FORMAT = " %s=%s";
	private final String MARKING_FORMAT = " %s=%s";
	
	public PetrifyPTNetSerializer(AbstractGraphicalPTNet<P, T, F, M, N, G> petriNet) {
		super(petriNet);
	}
	
	public PetrifyPTNetSerializer(N petriNet) {
		super(petriNet);
	}

	@Override
	public String serialize() throws SerializationException {
		AbstractPTNet<P,T,F,M> net = getPetriNet();
		StringBuilder builder = new StringBuilder();
		String newLine = System.getProperty("line.separator");
		
		// Add first line: transitions
		builder.append(".outputs");
		for(T transition: net.getTransitions()){
			builder.append(' ');
			builder.append(transition.getName());
		}
		builder.append(newLine);
		
		// Add second line: graph type
		builder.append(".graph");
		builder.append(newLine);
		
		// Add relations
		for(F relation: net.getFlowRelations()){
			if(relation.getWeight() > 1){
				builder.append(String.format(RELATION_WEIGHTED_FORMAT, relation.getSource().getName(), relation.getTarget().getName(), relation.getWeight()));
			} else {
				builder.append(String.format(RELATION_FORMAT, relation.getSource().getName(), relation.getTarget().getName()));
			}
			builder.append(newLine);
		}
		
		// Add capacities
		builder.append(".capacity");
		for(P place: net.getPlaces()){
			if(place.isBounded()){
				builder.append(String.format(BOUNDED_PLACE_FORMAT, place.getName(), place.getCapacity()));
			}
		}
		builder.append(newLine);
		
		// Add marking
		builder.append(".marking");
		M initialMarking = net.getInitialMarking();
		for(P place: net.getPlaces()){
			if(initialMarking.contains(place.getName())){
				builder.append(' ');
				try {
				if(initialMarking.get(place.getName()) == 1){
					builder.append(place.getName());
				} else {
					builder.append(String.format(MARKING_FORMAT, place.getName(), initialMarking.get(place.getName())));
				}
				} catch(ParameterException e){
					//Should not happen, since we only use places of the net itself.
					e.printStackTrace();
				}
			}
		}
		builder.append(newLine);
		
		return builder.toString();
	}
	
	
	
	@Override
	public void serialize(String path, String fileName) throws SerializationException, IOException {
		
		FileFormat format = new PNFF_Petrify();
		File file = new File(String.format("%s%s.%s", path, fileName, format.getFileExtension()));
		FileWriter output;
		if(file.exists()) 
			file.delete();
		file.createNewFile();
		output = new FileWriter(file, true);
		output.write(format.getFileHeader());
		output.write(serialize());
		output.write(format.getFileFooter());
		output.close();
	}

	@Override
	public NetType acceptedNetType() {
		return NetType.PTNet;
	}

}
