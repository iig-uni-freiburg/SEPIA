package de.uni.freiburg.iig.telematik.sepia.serialize.formats;

import java.nio.charset.Charset;

import de.invation.code.toval.file.FileFormat;

public class PNFF_PNML extends FileFormat {

	@Override
	public String getName() {
		return "PNML";
	}

	@Override
	public boolean supportsCharset(Charset charset) {
		return true;
	}

	@Override
	public String getFileExtension() {
		return "pnml";
	}

}
