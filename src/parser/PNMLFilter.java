package parser;

import java.io.File;

import javax.swing.filechooser.FileFilter;

import file.FileUtils;

public class PNMLFilter extends FileFilter {

	@Override
	public boolean accept(File f) {
		if (f.isDirectory()) {
	        return false;
	    }
		return FileUtils.getExtension(f).equals("pnml");
	}

	@Override
	public String getDescription() {
		return "PNML Net Descriptions";
	}

}
