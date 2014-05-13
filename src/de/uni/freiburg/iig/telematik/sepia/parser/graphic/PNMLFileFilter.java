package de.uni.freiburg.iig.telematik.sepia.parser.graphic;

import java.io.File;

import javax.swing.filechooser.FileFilter;

import de.invation.code.toval.file.FileUtils;

public class PNMLFileFilter extends FileFilter {

	@Override
	public boolean accept(File pathname) {
		if(pathname == null)
			return false;
		if(pathname.isDirectory())
			return true;
		String extension = FileUtils.getExtension(pathname);
		if(extension == null)
			return false;
		return extension.equals("pnml");
	}

	@Override
	public String getDescription() {
		return "PNML";
	}
	
}