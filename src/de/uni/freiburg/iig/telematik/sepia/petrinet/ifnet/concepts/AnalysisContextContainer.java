/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.uni.freiburg.iig.telematik.sepia.petrinet.ifnet.concepts;

import de.invation.code.toval.debug.SimpleDebugger;
import de.invation.code.toval.file.FileUtils;
import de.invation.code.toval.misc.wd.AbstractComponentContainer;
import de.invation.code.toval.misc.wd.ComponentListener;
import de.invation.code.toval.misc.wd.ProjectComponentException;
import de.invation.code.toval.validate.Validate;
import de.uni.freiburg.iig.telematik.sepia.parser.pnml.ifnet.AnalysisContextParser;
import de.uni.freiburg.iig.telematik.sepia.serialize.AnalysisContextSerialization;
import de.uni.freiburg.iig.telematik.sewol.accesscontrol.ACModelContainer;
import java.io.File;
import java.io.IOException;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author stocker
 */
public class AnalysisContextContainer extends AbstractComponentContainer<AnalysisContext> implements ComponentListener<AnalysisContext> {

    public static final String ANALYSIS_CONTEXT_DESCRIPTOR = "AnalysisContext";

    private final Map<String, LabelingContainer> labelingContainers = new HashMap<>();
    private ACModelContainer availableACModels = null;

    public AnalysisContextContainer(String serializationPath, ACModelContainer availableACModels) {
        this(serializationPath, availableACModels, null);
    }

    public AnalysisContextContainer(String serializationPath, ACModelContainer availableACModels, SimpleDebugger debugger) {
        super(serializationPath, debugger);
        Validate.notNull(availableACModels);
        this.availableACModels = availableACModels;
        this.addComponentListener(this);
    }

    public LabelingContainer getContainerLabelings(String aContextName) throws ProjectComponentException {
        validateComponent(aContextName);
        if (!containsLabelingContainer(aContextName)) {
            throw new ProjectComponentException("Labeling container for analysis context \"" + aContextName + "\" is NULL");
        }
        return labelingContainers.get(aContextName);
    }
    
    public boolean containsLabeling(String labelingName) {
        for (LabelingContainer labelingContainer : labelingContainers.values()) {
            if (labelingContainer.containsComponent(labelingName)) {
                return true;
            }
        }
        return false;
    }
    
    public void addLabeling(Labeling labeling, String aContextName, boolean storeToFile) throws ProjectComponentException {
        addLabeling(labeling, aContextName, storeToFile, true);
    }
    
    public void addLabeling(Labeling labeling, String aContextName, boolean storeToFile, boolean notifyListeners) throws ProjectComponentException {
        validateComponent(aContextName);
        if(containsLabeling(labeling.getName())){
            throw new ProjectComponentException("Container already contains a labeling with name \"" + labeling.getName() + "\"");
        }
        getContainerLabelings(aContextName).addComponent(labeling, storeToFile, notifyListeners);
    }

    public Labeling getLabeling(String aContextName, String labelingName) throws ProjectComponentException {
        validateComponent(aContextName);
        return getContainerLabelings(aContextName).getComponent(labelingName);
    }
    
    public Collection<Labeling> getLabelings(String aContextName) throws ProjectComponentException {
        validateComponent(aContextName);
        return getContainerLabelings(aContextName).getComponents();
    }

    public void removeLabeling(String labelingName, String aContextName, boolean removeFileFromDisk) throws ProjectComponentException {
        removeLabeling(labelingName, aContextName, removeFileFromDisk, true);
    }
    
    public void removeLabeling(String labelingName, String aContextName, boolean removeFileFromDisk, boolean notifyListeners) throws ProjectComponentException {
        validateComponent(aContextName);
        getContainerLabelings(aContextName).removeComponent(labelingName, removeFileFromDisk, notifyListeners);
    }

    public boolean containsLabelingContainer(String aContextName) {
        return labelingContainers.containsKey(aContextName);
    }

    @Override
    public void loadComponents() throws ProjectComponentException {
        super.loadComponents();
        // Analysis contexts have been added in super-method and reported to this class
        // -> Labeling containers have been created and put into the corresponding maps
        debugMessage("Load labelings of loaded nets");
        for (LabelingContainer labelingContainer : labelingContainers.values()) {
            labelingContainer.loadComponents();
        }
    }

    @Override
    public String getComponentDescriptor() {
        return ANALYSIS_CONTEXT_DESCRIPTOR;
    }

    @Override
    protected AnalysisContext loadComponentFromFile(String file) throws Exception {
        return AnalysisContextParser.parse(file, availableACModels.getComponents());
    }

    @Override
    protected void serializeComponent(AnalysisContext component, String serializationPath, String fileName) throws Exception {
        AnalysisContextSerialization.serialize(component, serializationPath.concat(fileName));
    }

    @Override
    public void componentAdded(AnalysisContext component) throws ProjectComponentException {
        labelingContainers.put(component.getName(), createNewLabelingContainer(component));
    }
    
    private LabelingContainer createNewLabelingContainer(AnalysisContext component) throws ProjectComponentException{
        debugMessage("Create labeling container for added net \"" + component.getName() + "\"");
        try {
            return new LabelingContainer(getLabelingDirectory(component.getName()), component, getDebugger());
        } catch (Exception e) {
            throw new ProjectComponentException("Cannot create labeling container");
        }
    }
    
    private String getLabelingDirectory(String aContextName) throws Exception {
        String netPath = getBasePath();
        if (!netPath.endsWith(System.getProperty("file.separator"))) {
            netPath = netPath.concat(System.getProperty("file.separator"));
        }
        netPath = netPath.concat(aContextName);
        netPath = netPath.concat(System.getProperty("file.separator"));
        return netPath;
    }

    @Override
    public void componentRemoved(AnalysisContext component) throws ProjectComponentException {
        //Try to remove all related labelings
        if (!labelingContainers.containsKey(component.getName())) {
            return;
        }
        debugMessage("Identify related labelings of removed Petri net");
        try {
            // Removal of files is not necessary/possible since PNContainer already removed the whole net directory
            labelingContainers.get(component.getName()).removeComponents(false);
            labelingContainers.remove(component.getName());
        } catch (Exception e) {
            throw new ProjectComponentException("Cannot remove related labelings of removed Petri net", e);
        }
    }

    @Override
    public void componentRenamed(AnalysisContext component, String oldName, String newName) throws ProjectComponentException {
        if (labelingContainers.containsKey(oldName)) {
            Collection<Labeling> labelings = getContainerLabelings(oldName).getComponents();
            getContainerLabelings(oldName).removeComponents(true, false);
            labelingContainers.put(newName, createNewLabelingContainer(component));
            for(Labeling labeling: labelings){
                getContainerLabelings(newName).addComponent(labeling, true, false);
            }
        }
    }

    @Override
    public void componentsChanged() throws ProjectComponentException {}
    
    public void storeLabelings(String aContextName) throws ProjectComponentException {
        validateComponent(aContextName);
        getContainerLabelings(aContextName).storeComponents();
    }

    public void storeLabeling(String aContextName, String labelingName) throws ProjectComponentException {
        validateComponent(aContextName);
        getContainerLabelings(aContextName).validateComponent(labelingName);
        getContainerLabelings(aContextName).storeComponent(labelingName);
    }

    @Override
    public void storeComponent(String componentName) throws ProjectComponentException {
        super.storeComponent(componentName);
        getContainerLabelings(componentName).storeComponents();
    }

    @Override
    public void storeComponents() throws ProjectComponentException {
        super.storeComponents();
        for(LabelingContainer labelingContainer: labelingContainers.values()){
            labelingContainer.storeComponents();
        }
    }
    
    

}
