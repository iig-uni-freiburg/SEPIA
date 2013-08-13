package de.uni.freiburg.iig.telematik.sepia.export;

import java.io.IOException;

import de.invation.code.toval.file.FileWriter;
import de.invation.code.toval.validate.ParameterException;
import de.uni.freiburg.iig.telematik.sepia.petrinet.AbstractFlowRelation;
import de.uni.freiburg.iig.telematik.sepia.petrinet.AbstractMarking;
import de.uni.freiburg.iig.telematik.sepia.petrinet.AbstractPetriNet;
import de.uni.freiburg.iig.telematik.sepia.petrinet.AbstractPlace;
import de.uni.freiburg.iig.telematik.sepia.petrinet.AbstractTransition;
import de.uni.freiburg.iig.telematik.sepia.petrinet.cpn.CPN;
import de.uni.freiburg.iig.telematik.sepia.petrinet.cwn.CWN;
import de.uni.freiburg.iig.telematik.sepia.petrinet.ifnet.IFNet;
import de.uni.freiburg.iig.telematik.sepia.petrinet.pt.PTNet;
import de.uni.freiburg.iig.telematik.sepia.petrinet.pt.abstr.AbstractPTFlowRelation;
import de.uni.freiburg.iig.telematik.sepia.petrinet.pt.abstr.AbstractPTMarking;
import de.uni.freiburg.iig.telematik.sepia.petrinet.pt.abstr.AbstractPTNet;
import de.uni.freiburg.iig.telematik.sepia.petrinet.pt.abstr.AbstractPTPlace;
import de.uni.freiburg.iig.telematik.sepia.petrinet.pt.abstr.AbstractPTTransition;

public class PNMLExporter {
	
	public static final String PTNET_EXTENSION = "pnml";
	
	
	public static 
	
	       <P extends AbstractPlace<F,S>, 
		    T extends AbstractTransition<F,S>, 
		    F extends AbstractFlowRelation<P,T,S>, 
			M extends AbstractMarking<S>, 
			S extends Object> 
	
		   void export(AbstractPetriNet<P,T,F,M,S> net, String path, String fileName) throws ParameterException, IOException {
		
		if(net instanceof PTNet){
			exportPTNet((AbstractPTNet<?,?,?,?>) net, path, fileName);
		} else if(net instanceof CPN){
			exportCPN((CPN) net, path, fileName);
		} else if(net instanceof CWN){
			exportCWN((CWN) net, path, fileName);
		} else if(net instanceof IFNet){
			exportIFNet((IFNet) net, path, fileName);
		}
		
	}
	
	private static <P extends AbstractPTPlace<F>, 
				    T extends AbstractPTTransition<F>, 
				    F extends AbstractPTFlowRelation<P,T>, 
				    M extends AbstractPTMarking>
	
	        void 
	        
	        exportPTNet(AbstractPTNet<P,T,F,M> net, String path, String fileName) throws ParameterException, IOException {
		
		FileWriter out = new FileWriter(path, fileName);
		out.setFileExtension(PTNET_EXTENSION);
		PNMLSerializerAbstractPTNet<P,T,F,M> serializer = PNSerializerFactory.getPNMLSerializerAbstractPTNet();
		out.write(serializer.toString(net));
		out.closeFile();
	}
	
	private static void exportCPN(CPN net, String path, String fileName){
		//TODO: Functionality
	}
	
	private static void exportCWN(CWN net, String path, String fileName){
		//TODO: Functionality
	}
	
	private static void exportIFNet(IFNet net, String path, String fileName){
		//TODO: Functionality
	}
	
}
