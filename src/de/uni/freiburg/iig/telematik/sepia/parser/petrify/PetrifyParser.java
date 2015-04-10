package de.uni.freiburg.iig.telematik.sepia.parser.petrify;

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
import de.uni.freiburg.iig.telematik.sepia.parser.PNParserInterface;
import de.uni.freiburg.iig.telematik.sepia.parser.PNParsingFormat;
import de.uni.freiburg.iig.telematik.sepia.petrinet.pt.PTMarking;
import de.uni.freiburg.iig.telematik.sepia.petrinet.pt.PTNet;

public class PetrifyParser implements PNParserInterface{
	
	private static final String PREFIX_OUTPUTS = ".outputs ";
	private static final String PREFIX_GRAPH = ".graph";
	private static final String PREFIX_END = ".end";
	private static final String PREFIX_CAPACITIES = ".capacity ";
	private static final String PREFIX_MARKING = ".marking ";
	private static final String PREFIX_COMMENT = "#";

	@SuppressWarnings("unchecked")
	@Override
	public GraphicalPTNet parse(File file) throws IOException, ParserException {
		GraphicalPTNet net = new GraphicalPTNet();
		FileReader reader = new FileReader(file.getAbsolutePath());
		String nextLine = null;
		while((nextLine = reader.readLine()) != null){
			String lineContent = null;
			if(nextLine.startsWith(PREFIX_COMMENT)){
				// Do nothing
			} else if(nextLine.startsWith(PREFIX_OUTPUTS)){
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
		reader.closeFile();
		return net;
	}

	private void addFlowRelation(PTNet net, String lineContent) {
		String sourceName = lineContent.substring(0, lineContent.indexOf(" "));
		String targetName = null;
		int weight = 1;
		if(lineContent.contains("(")){
			targetName = lineContent.substring(lineContent.indexOf(" ") + 1, lineContent.indexOf("("));
			String weightString = lineContent.substring(lineContent.indexOf("(") + 1, lineContent.indexOf(")"));
			Validate.positiveInteger(weightString);
			weight = Integer.parseInt(weightString);
		} else {
			targetName = lineContent.substring(lineContent.indexOf(" ") + 1);
		}
		if(net.containsTransition(sourceName)){
			ensurePlace(net, targetName);
			net.addFlowRelationTP(sourceName, targetName, weight);
		} else {
			ensurePlace(net, sourceName);
			net.addFlowRelationPT(sourceName, targetName, weight);
		}
	}

	private void ensurePlace(PTNet net, String placeName) {
		if(!net.containsPlace(placeName)){
			net.addPlace(placeName);
		}
	}

	private void setMarking(PTNet net, String lineContent) {
		PTMarking marking = new PTMarking();
		String placeName = null;
		String multiplicityString = null;
		for(String token: getTokens(lineContent)){
			int multiplicity = 0;
			if(token.contains("=")){
				placeName = token.substring(0, token.indexOf("="));
				multiplicityString = token.substring(token.indexOf("=")+1);
				Validate.notNegativeInteger(multiplicityString);
				multiplicity = Integer.parseInt(multiplicityString);
			} else {
				placeName = token;
				multiplicity = 1;
			}
			if(!net.containsPlace(placeName))
				throw new ParameterException("Unknown place: " + placeName);
			marking.set(placeName, multiplicity);
		}
		net.setInitialMarking(marking);
	}

	private void setCapacities(PTNet net, String lineContent) {
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

	private void insertTransitions(PTNet net, String lineContent) {
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

	@Override
	public PetrifyParser getInstance() {
		return this;
	}

	@Override
	public PNParsingFormat getParsingFormat() {
		return PNParsingFormat.PETRIFY;
	}

}
