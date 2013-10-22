package de.uni.freiburg.iig.telematik.sepia.serialize;

import java.io.File;
import java.io.IOException;

import de.invation.code.toval.file.FileUtils;
import de.invation.code.toval.validate.ParameterException;
import de.invation.code.toval.validate.Validate;
import de.uni.freiburg.iig.telematik.sepia.petrinet.ifnet.concepts.AnalysisContext;

public class ACSerialization {
	
	public static String serialize(AnalysisContext analysisContext) 
			throws ParameterException, SerializationException{
		Validate.notNull(analysisContext);
		return new ACSerializer(analysisContext).serialize();
	}
	
	public static void serialize(AnalysisContext analysisContext, String fileName) 
			throws ParameterException, SerializationException, IOException{
		Validate.noDirectory(fileName);
		
		File file = new File(fileName);
		serialize(analysisContext, FileUtils.getPath(file), FileUtils.getName(file));
	}
	
	public static void serialize(AnalysisContext analysisContext, String path, String fileName) 
			throws ParameterException, SerializationException, IOException{
		Validate.notNull(analysisContext);
		new ACSerializer(analysisContext).serialize(path, fileName);
	}

}
