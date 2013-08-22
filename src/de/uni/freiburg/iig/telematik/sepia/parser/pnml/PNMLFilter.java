package de.uni.freiburg.iig.telematik.sepia.parser.pnml;

import java.io.File;

import javax.swing.filechooser.FileFilter;


public class PNMLFilter extends FileFilter {

	@Override
	public boolean accept(File f) {
		return f.isDirectory() || f.getName().endsWith(".pnml");
	}

	@Override
	public String getDescription() {
		return "PNML Net Descriptions";
	}

}
