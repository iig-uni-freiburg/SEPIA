package de.uni.freiburg.iig.telematik.sepia.parser.graphic;

import java.io.File;

import javax.swing.JFileChooser;
import javax.swing.filechooser.FileView;

import de.invation.code.toval.file.FileUtils;

public class PNChooser extends JFileChooser {

	private static final long serialVersionUID = 5434773911883000233L;

	public PNChooser(){
		super();
		setAcceptAllFileFilterUsed(false);
		setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
		addChoosableFileFilter(new PetrifyFileFilter());
		addChoosableFileFilter(new PNMLFileFilter());
		setFileView(new PNFileView());
	}
	
	public class PNFileView extends FileView {
		
	    public String getName(File f) {
	    	String fileName = null;
	 	    if(f != null) {
	 	    	fileName = PNChooser.this.getFileSystemView().getSystemDisplayName(f);
	 	    }
	 	    return fileName;
	    }
	 
	    public String getDescription(File f) {
	    	return f.getName();
	    }
	 
//	    public Boolean isTraversable(File f) {
//	        return null;
//	    }
	 
	    public String getTypeDescription(File f) {
	        String extension = FileUtils.getExtension(f);
	        String type = null;
	 
	        if (extension != null) {
	            if (extension.equals("pnml")) {
	                type = "PNML";
	            } else if (extension.equals("pn")){
	                type = "Petrify";
	            }
	        }
	        return type;
	    }
	}

}
