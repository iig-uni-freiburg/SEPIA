/* 
 * Copyright (c) 2015, IIG Telematics, Uni Freiburg
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted (subject to the limitations in the disclaimer
 * below) provided that the following conditions are met:
 *
 * * Redistributions of source code must retain the above copyright notice, this
 *   list of conditions and the following disclaimer.
 * * Redistributions in binary form must reproduce the above copyright notice,
 *   this list of conditions and the following disclaimer in the documentation
 *   and/or other materials provided with the distribution.
 * * Neither the name of IIG Telematics, Uni Freiburg nor the names of its
 *   contributors may be used to endorse or promote products derived from this
 *   software without specific prior written permission.
 *
 * NO EXPRESS OR IMPLIED LICENSES TO ANY PARTY'S PATENT RIGHTS ARE GRANTED BY
 * THIS LICENSE. THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND
 * CONTRIBUTORS "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT
 * NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A
 * PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR
 * CONTRIBUTORS BELIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL,
 * EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO,
 * PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS;
 * OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY,
 * WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR
 * OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF
 * ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
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
