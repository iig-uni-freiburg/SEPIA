package de.uni.freiburg.iig.telematik.sepia.graphic.container;

import de.invation.code.toval.debug.SimpleDebugger;
import de.invation.code.toval.misc.wd.AbstractComponentContainer;
import de.invation.code.toval.validate.Validate;
import de.uni.freiburg.iig.telematik.sepia.graphic.AbstractGraphicalPN;
import de.uni.freiburg.iig.telematik.sepia.graphic.netgraphics.AbstractPNGraphics;
import de.uni.freiburg.iig.telematik.sepia.parser.PNParsing;
import de.uni.freiburg.iig.telematik.sepia.petrinet.NetType;
import de.uni.freiburg.iig.telematik.sepia.petrinet.abstr.AbstractFlowRelation;
import de.uni.freiburg.iig.telematik.sepia.petrinet.abstr.AbstractMarking;
import de.uni.freiburg.iig.telematik.sepia.petrinet.abstr.AbstractPetriNet;
import de.uni.freiburg.iig.telematik.sepia.petrinet.abstr.AbstractPlace;
import de.uni.freiburg.iig.telematik.sepia.petrinet.abstr.AbstractTransition;
import de.uni.freiburg.iig.telematik.sepia.serialize.PNSerialization;
import de.uni.freiburg.iig.telematik.sepia.serialize.formats.PNFF_PNML;
import de.uni.freiburg.iig.telematik.sepia.serialize.formats.PNFF_Petrify;
import de.uni.freiburg.iig.telematik.sepia.serialize.formats.PNSerializationFormat;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 *
 * @author stocker
 * @param <P>
 * @param <T>
 * @param <F>
 * @param <M>
 * @param <S>
 * @param <N>
 * @param <G>
 * @param <X>
 */
public class AbstractGraphicalPNContainer<P extends AbstractPlace<F, S>, 
                                                   T extends AbstractTransition<F, S>, 
                                                   F extends AbstractFlowRelation<P, T, S>, 
                                                   M extends AbstractMarking<S>, S extends Object, 
                                                   N extends AbstractPetriNet<P, T, F, M, S>, 
                                                   G extends AbstractPNGraphics<P, T, F, M, S>,
                                                   X extends AbstractGraphicalPN<P, T, F, M, S, N, G>> extends AbstractComponentContainer<X> {

    public static final PNSerializationFormat DEFAULT_SERIALIZATION_FORMAT = PNSerializationFormat.PNML;
    public static final boolean DEFAULT_IGNORE_INCOMPATIBLE_FILES = true;
    protected static final String PNML_FILE_ENDING = new PNFF_PNML().getFileExtension();
    protected static final String PETRIFY_FILE_ENDING = new PNFF_Petrify().getFileExtension();
    
    public static final String COMPONENT_DESCRIPTOR = "Petri net";
    
    protected PNSerializationFormat serializationFormat = DEFAULT_SERIALIZATION_FORMAT;
    protected boolean ignoreIncompatibleFiles = DEFAULT_IGNORE_INCOMPATIBLE_FILES;

    protected AbstractGraphicalPNContainer(String serializationPath) {
        super(serializationPath);
    }

    protected AbstractGraphicalPNContainer(String serializationPath, SimpleDebugger debugger) {
        super(serializationPath, debugger);
    }
    
    public void setSerializationFormat(PNSerializationFormat serializationFormat){
        Validate.notNull(serializationFormat);
        this.serializationFormat = serializationFormat;
    }
    
    public void setIgnoreIncompatibleFiles(boolean ignoreIncompatibleFiles){
        this.ignoreIncompatibleFiles = ignoreIncompatibleFiles;
    }

    @Override
    public Set<String> getAcceptedFileEndings() {
        return new HashSet<>(Arrays.asList(PNML_FILE_ENDING, PETRIFY_FILE_ENDING));
    }

    protected NetType getExpectedNetType(){
        return null;
    }

    @Override
    protected X loadComponentFromFile(String file) throws Exception {
        AbstractGraphicalPN parsedPN = PNParsing.parse(file, PNParsing.guessFormat(file));
        if (parsedPN == null) {
            throw new Exception("Unable to parse " + getComponentDescriptor() + ": NULL");
        }
        if (parsedPN.getPetriNet() == null) {
            throw new Exception("Unable to parse " + getComponentDescriptor() + ": NULL");
        }
        if ((getExpectedNetType() != null) && (parsedPN.getPetriNet().getNetType() != getExpectedNetType())) {
            if(ignoreIncompatibleFiles){
                return null;
            }
            throw new Exception("Unexpected net type, expected " + getExpectedNetType() + " but got " + parsedPN.getPetriNet().getNetType());
        }
        return (X) parsedPN;
    }
    
     @Override
    protected void serializeComponent(X component, String serializationPath, String fileName) throws Exception {
        PNSerialization.serialize(component, serializationFormat, serializationPath.concat(fileName));
    }

    @Override
    public String getComponentDescriptor() {
        return COMPONENT_DESCRIPTOR;
    }

    @Override
    protected String getFileEndingForComponent(X component) {
        return PNML_FILE_ENDING;
    }
    
    

}
