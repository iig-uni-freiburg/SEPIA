package de.uni.freiburg.iig.telematik.sepia.parser;

import de.invation.code.toval.file.FileFormat;
import de.uni.freiburg.iig.telematik.sepia.serialize.formats.PNFF_PNML;
import de.uni.freiburg.iig.telematik.sepia.serialize.formats.PNFF_Petrify;


public enum ParsingFormat {
	
	PNML(new PNFF_PNML()),
	
	SOLE_CARMONA(new PNFF_Petrify());
	
	private FileFormat fileFormat = null;
	
	private ParsingFormat(FileFormat fileFormat){
		this.fileFormat = fileFormat;
	}
	
	public FileFormat getFileFormat(){
		return fileFormat;
	}

}
