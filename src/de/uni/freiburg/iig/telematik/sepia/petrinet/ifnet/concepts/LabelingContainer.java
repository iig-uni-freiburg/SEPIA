/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.uni.freiburg.iig.telematik.sepia.petrinet.ifnet.concepts;

import de.invation.code.toval.debug.SimpleDebugger;
import de.invation.code.toval.misc.wd.AbstractComponentContainer;
import de.invation.code.toval.validate.Validate;
import de.uni.freiburg.iig.telematik.sepia.parser.pnml.ifnet.LabelingParser;
import de.uni.freiburg.iig.telematik.sepia.serialize.LabelingSerialization;
import de.uni.freiburg.iig.telematik.sepia.serialize.LabelingSerializer;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 *
 * @author stocker
 */
public class LabelingContainer extends AbstractComponentContainer<Labeling>{
    
    public static final String LABELING_DESCRIPTOR = "Labeling";
    
    private AnalysisContext analysisContext = null;

    public LabelingContainer(String basePath, AnalysisContext analysisContext) {
        this(basePath, analysisContext, null);
    }

    public LabelingContainer(String basePath, AnalysisContext analysisContext, SimpleDebugger debugger) {
        super(basePath, debugger);
        Validate.notNull(analysisContext);
        this.analysisContext = analysisContext;
    }
    
    @Override
    public String getComponentDescriptor() {
        return LABELING_DESCRIPTOR;
    }

    @Override
    public Set<String> getAcceptedFileEndings() {
        return new HashSet<>(Arrays.asList(LabelingSerializer.LABELING_FILE_EXTENSION));
    }

    @Override
    protected Labeling loadComponentFromFile(String file) throws Exception {
        return LabelingParser.parse(file, new HashSet<>(Arrays.asList(analysisContext)));
    }

    @Override
    protected void serializeComponent(Labeling component, String basePath, String fileName) throws Exception {
        LabelingSerialization.serialize(component, basePath.concat(fileName));
    }
    
}
