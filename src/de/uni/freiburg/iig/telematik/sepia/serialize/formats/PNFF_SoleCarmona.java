package de.uni.freiburg.iig.telematik.sepia.serialize.formats;

import java.nio.charset.Charset;

import de.invation.code.toval.file.FileFormat;


public class PNFF_SoleCarmona  extends FileFormat{

	@Override
	public String getFileExtension() {
		return "pn";
	}
	
	@Override
	public String getFileFooter() {
		return ".end";
	}

	@Override
	public String getName() {
		return "SoleCarmona";
	}

	@Override
	public boolean supportsCharset(Charset charset) {
		return true;
	}

}
