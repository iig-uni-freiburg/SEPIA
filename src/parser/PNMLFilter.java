package parser;

import java.io.File;

import javax.swing.filechooser.FileFilter;

import file.FileUtils;

public class PNMLFilter extends FileFilter {

	@Override
	public boolean accept(File f) {
		if (f == null || f.isDirectory()) {
	        return false;
	    }
		String extension = FileUtils.getExtension(f);
		if(extension == null)
			return false;
		return extension.equals("pnml");
	}

	@Override
	public String getDescription() {
		return "PNML Net Descriptions";
	}

}
