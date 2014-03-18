package de.uni.freiburg.iig.telematik.sepia.parser;

import de.invation.code.toval.file.FileFormat;
import de.uni.freiburg.iig.telematik.sepia.serialize.formats.PNFF_PNML;
import de.uni.freiburg.iig.telematik.sepia.serialize.formats.PNFF_Petrify;


public enum PNParsingFormat {
	
	PNML(new PNFF_PNML()),
	
	PETRIFY(new PNFF_Petrify());
	
	private FileFormat fileFormat = null;
	
	private PNParsingFormat(FileFormat fileFormat){
		this.fileFormat = fileFormat;
	}
	
	public FileFormat getFileFormat(){
		return fileFormat;
	}

}
