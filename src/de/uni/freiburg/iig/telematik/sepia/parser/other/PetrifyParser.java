package de.uni.freiburg.iig.telematik.sepia.parser.other;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

import de.invation.code.toval.file.FileReader;
import de.invation.code.toval.parser.ParserException;
import de.invation.code.toval.validate.ParameterException;
import de.invation.code.toval.validate.Validate;
import de.uni.freiburg.iig.telematik.sepia.graphic.GraphicalPTNet;
import de.uni.freiburg.iig.telematik.sepia.parser.Parser;
import de.uni.freiburg.iig.telematik.sepia.parser.ParserInterface;
import de.uni.freiburg.iig.telematik.sepia.parser.ParsingFormat;
import de.uni.freiburg.iig.telematik.sepia.petrinet.AbstractPetriNet;
import de.uni.freiburg.iig.telematik.sepia.petrinet.pt.PTMarking;
import de.uni.freiburg.iig.telematik.sepia.petrinet.pt.PTNet;
import de.uni.freiburg.iig.telematik.sepia.serialize.PNSerialization;
import de.uni.freiburg.iig.telematik.sepia.serialize.formats.PNSerializationFormat;
import de.uni.freiburg.iig.telematik.sepia.traversal.PNTraversalUtils;

public class PetrifyParser implements ParserInterface{
	
	private static final String PREFIX_OUTPUTS = ".outputs ";
	private static final String PREFIX_GRAPH = ".graph";
	private static final String PREFIX_END = ".end";
	private static final String PREFIX_CAPACITIES = ".capacity ";
	private static final String PREFIX_MARKING = ".marking ";

	@Override
	public GraphicalPTNet parse(File file) throws IOException, ParserException, ParameterException {
		GraphicalPTNet net = new GraphicalPTNet();
		FileReader reader = new FileReader(file.getAbsolutePath());
		String nextLine = null;
		while((nextLine = reader.readLine()) != null){
			String lineContent = null;
			if(nextLine.startsWith(PREFIX_OUTPUTS)){
				lineContent = nextLine.replace(PREFIX_OUTPUTS, "");
				insertTransitions(net.getPetriNet(), lineContent);
			} else if(nextLine.isEmpty() || nextLine.startsWith(PREFIX_GRAPH) || nextLine.startsWith(PREFIX_END)){
				// Do nothing
			} else if(nextLine.startsWith(PREFIX_CAPACITIES)){
				lineContent = nextLine.replace(PREFIX_CAPACITIES, "");
				setCapacities(net.getPetriNet(), lineContent);
			} else if(nextLine.startsWith(PREFIX_MARKING)){
				lineContent = nextLine.replace(PREFIX_MARKING, "");
				lineContent = lineContent.replace("{", "");
				lineContent = lineContent.replace("}", "");
				setMarking(net.getPetriNet(), lineContent);
			} else {
				addFlowRelation(net.getPetriNet(), nextLine);
			}
		}
		return net;
	}

	private void addFlowRelation(PTNet net, String lineContent) throws ParameterException {
		String sourceName = lineContent.substring(0, lineContent.indexOf(" "));
		String targetName = lineContent.substring(lineContent.indexOf(" ") + 1, lineContent.indexOf("("));
		String weightString = lineContent.substring(lineContent.indexOf("(") + 1, lineContent.indexOf(")"));
		Validate.positiveInteger(weightString);
		if(net.containsTransition(sourceName)){
			ensurePlace(net, targetName);
			net.addFlowRelationTP(sourceName, targetName, Integer.parseInt(weightString));
		} else {
			ensurePlace(net, sourceName);
			net.addFlowRelationPT(sourceName, targetName, Integer.parseInt(weightString));
		}
	}

	private void ensurePlace(PTNet net, String placeName) throws ParameterException {
		if(!net.containsPlace(placeName)){
			net.addPlace(placeName);
		}
	}

	private void setMarking(PTNet net, String lineContent) throws ParameterException {
		PTMarking marking = new PTMarking();
		String placeName = null;
		String capacityString = null;
		for(String token: getTokens(lineContent)){
			placeName = token.substring(0, token.indexOf("="));
			capacityString = token.substring(token.indexOf("=")+1);
			Validate.notNegativeInteger(capacityString);
			if(!net.containsPlace(placeName))
				throw new ParameterException("Unknown place: " + placeName);
			marking.set(placeName, Integer.parseInt(capacityString));
		}
		net.setInitialMarking(marking);
	}

	private void setCapacities(PTNet net, String lineContent) throws ParameterException {
		String placeName = null;
		String capacityString = null;
		for(String token: getTokens(lineContent)){
			placeName = token.substring(0, token.indexOf("="));
			capacityString = token.substring(token.indexOf("=")+1);
			Validate.notNegativeInteger(capacityString);
			if(!net.containsPlace(placeName))
				throw new ParameterException("Unknown place: " + placeName);
			net.getPlace(placeName).setCapacity(Integer.parseInt(capacityString));
		}
	}

	private void insertTransitions(PTNet net, String lineContent) throws ParameterException {
		for(String token: getTokens(lineContent)){
			net.addTransition(token);
		}
	}
	
	public List<String> getTokens(String lineContent){
		List<String> result = new ArrayList<String>();
		StringTokenizer tokenizer = new StringTokenizer(lineContent, " ");
		while(tokenizer.hasMoreTokens()){
			result.add(tokenizer.nextToken());
		}
		return result;
	}
	
	public static void main(String[] args) throws IOException, ParserException, ParameterException {
		AbstractPetriNet net = Parser.parse("/Users/stocker/Desktop/kmg4_2.g", ParsingFormat.PETRIFY).getPetriNet();
		PNTraversalUtils.testTraces(net, 1, 30, true, false);
		PNSerialization.serialize(net, PNSerializationFormat.PNML, "/Users/stocker/Desktop/", "kmg4_2.g");
	}


}
