package de.uni.freiburg.iig.telematik.sepia.petrinet.ifnet.concepts.graphic;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Window;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

import javax.swing.Box;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.SpringLayout;

import de.invation.code.toval.graphic.component.EnumComboBox;
import de.invation.code.toval.graphic.dialog.AbstractEditCreateDialog;
import de.invation.code.toval.graphic.util.SpringUtilities;
import de.invation.code.toval.misc.soabase.SOABase;
import de.invation.code.toval.validate.ParameterException;
import de.uni.freiburg.iig.telematik.sepia.petrinet.ifnet.concepts.AnalysisContext;
import de.uni.freiburg.iig.telematik.sepia.petrinet.ifnet.concepts.Labeling;
import de.uni.freiburg.iig.telematik.sepia.petrinet.ifnet.concepts.SecurityLevel;
import de.uni.freiburg.iig.telematik.sewol.accesscontrol.acl.ACLModel;

public class LabelingDialog extends AbstractEditCreateDialog<Labeling> {

	private static final long serialVersionUID = 3854524414609450308L;
	
	private AnalysisContext analysisContext;
	private String[] typeOptions = {"Activity Classification", "Attribute Classification", "Subject Clearance"};
	private JComboBox comboType;
	private JPanel activityClassificationsPanel;
	private JPanel attributeClassificationsPanel;
	private JPanel subjectClearancePanel;
	private JScrollPane centerPanel;
	private JTextField txtName;

	protected LabelingDialog(Window owner, Labeling labeling) {
		super(owner, labeling);
		this.analysisContext = labeling.getAnalysisContext();
	}

	protected LabelingDialog(Window owner, AnalysisContext analysisContext) {
		super(owner, analysisContext);
		this.analysisContext = analysisContext;
	}

	@Override
	protected Labeling newDialogObject(Object... parameters) {
		return new Labeling((AnalysisContext) parameters[0]);
	}

	@Override
	protected void addComponents() throws Exception {
		mainPanel().setLayout(new BorderLayout());
		JPanel topPanel = new JPanel(new BorderLayout());
		JPanel namePanel = new JPanel(new FlowLayout());
		txtName = new JTextField();
		txtName.setText(getDialogObject().getName());
		txtName.setPreferredSize(new Dimension(200,24));
		namePanel.add(new JLabel("Name:"));
		namePanel.add(txtName);
		topPanel.add(namePanel, BorderLayout.NORTH);
		topPanel.add(getComboType(), BorderLayout.CENTER);
		mainPanel().add(topPanel, BorderLayout.NORTH);
		centerPanel = new JScrollPane();
		centerPanel.setPreferredSize(new Dimension(300,300));
		centerPanel.setViewportView(getActivityClassificationPanel());
		mainPanel().add(centerPanel, BorderLayout.CENTER);
	}
	
	private JPanel getActivityClassificationPanel(){
		if(activityClassificationsPanel == null){
			activityClassificationsPanel = new JPanel(new SpringLayout());
			for(String activity: analysisContext.getACModel().getContext().getActivities()){
				SecurityLevelBox box = new SecurityLevelBox(LabelingSettingType.ACTIVITY_CLASSIFICATION, activity);
				box.setSelectedItem(getDialogObject().getActivityClassification(activity));
				activityClassificationsPanel.add(box);
				activityClassificationsPanel.add(new JLabel(activity));
				activityClassificationsPanel.add(Box.createHorizontalGlue());
			}
			SpringUtilities.makeCompactGrid(activityClassificationsPanel, analysisContext.getACModel().getContext().getActivities().size(), 3, 0, 0, 0, 0);
		}
		return activityClassificationsPanel;
	}
	
	private JPanel getAttributeClassificationPanel(){
		if(attributeClassificationsPanel == null){
			attributeClassificationsPanel = new JPanel(new SpringLayout());
			for(String attribute: analysisContext.getACModel().getContext().getObjects()){
				SecurityLevelBox box = new SecurityLevelBox(LabelingSettingType.ATTRIBUTE_CLASSIFICATION, attribute);
				box.setSelectedItem(getDialogObject().getAttributeClassification(attribute));
				attributeClassificationsPanel.add(box);
				attributeClassificationsPanel.add(new JLabel(attribute));
				attributeClassificationsPanel.add(Box.createHorizontalGlue());
			}
			SpringUtilities.makeCompactGrid(attributeClassificationsPanel, analysisContext.getACModel().getContext().getObjects().size(), 3, 0, 0, 0, 0);
		}
		return attributeClassificationsPanel;
	}
	
	private JPanel getSubjectClearancePanel(){
		if(subjectClearancePanel == null){
			subjectClearancePanel = new JPanel(new SpringLayout());
			for(String subject: analysisContext.getACModel().getContext().getSubjects()){
				SecurityLevelBox box = new SecurityLevelBox(LabelingSettingType.SUBJECT_CLEARANCE, subject);
				box.setSelectedItem(getDialogObject().getSubjectClearance(subject));
				subjectClearancePanel.add(box);
				subjectClearancePanel.add(new JLabel(subject));
				subjectClearancePanel.add(Box.createHorizontalGlue());
			}
			SpringUtilities.makeCompactGrid(subjectClearancePanel, analysisContext.getACModel().getContext().getSubjects().size(), 3, 0, 0, 0, 0);
		}
		return subjectClearancePanel;
	}
	
	
	private JComboBox getComboType(){
		if(comboType == null){
			comboType = new JComboBox();
			comboType.setModel(new DefaultComboBoxModel(typeOptions));
			comboType.addItemListener(new ItemListener() {
				
				@Override
				public void itemStateChanged(ItemEvent e) {
					if(e.getStateChange() == ItemEvent.SELECTED){
						updatePanels();
					}
				}
			});
		}
		return comboType;
	}
	
	private String getSelectedType(){
		return (String) getComboType().getSelectedItem();
	}
	
	private void updatePanels() {
		if(getSelectedType().equals(typeOptions[0])){
			centerPanel.setViewportView(getActivityClassificationPanel());
		} else if(getSelectedType().equals(typeOptions[1])){
			centerPanel.setViewportView(getAttributeClassificationPanel());
		} else if(getSelectedType().equals(typeOptions[2])){
			centerPanel.setViewportView(getSubjectClearancePanel());
		}
		mainPanel().repaint();
	}

	@Override
	protected void prepareEditing() throws Exception {
		txtName.setName(getDialogObject().getName());
	}

	@Override
	protected void validateAndSetFieldValues() throws Exception {
		if(txtName.getText() == null || txtName.getText().isEmpty())
			throw new ParameterException("Empty labeling name.");
		getDialogObject().setName(txtName.getText());
	}

	@Override
	protected void setTitle() {
		if(editMode()){
			setTitle("Edit Labeling");
		} else {
			setTitle("Create Labeling");
		}
	}
	
	private class SecurityLevelBox extends EnumComboBox<SecurityLevel> {
		
		private static final long serialVersionUID = -2604130463794944173L;
		
		private static final int PREFERRED_WIDTH = 90;
		private LabelingSettingType type;
		private String name;

		public SecurityLevelBox(LabelingSettingType type, String name) {
			super(SecurityLevel.class);
			this.type = type;
			this.name = name;
			
			addItemListener(new ItemListener() {
				
				@Override
				public void itemStateChanged(ItemEvent e) {
					if(e.getStateChange() == ItemEvent.SELECTED){
						switch(SecurityLevelBox.this.type){
						case ACTIVITY_CLASSIFICATION:
							getDialogObject().setActivityClassification(SecurityLevelBox.this.name, getSelectedItem());
							break;
						case ATTRIBUTE_CLASSIFICATION:
							getDialogObject().setAttributeClassification(SecurityLevelBox.this.name, getSelectedItem());
							break;
						case SUBJECT_CLEARANCE:
							getDialogObject().setSubjectClearance(SecurityLevelBox.this.name, getSelectedItem());
							break;
						default:
							break;
						}
					}
				}
			});
		}
		
		@Override
		public Dimension getPreferredSize(){
			return new Dimension(PREFERRED_WIDTH, super.getPreferredSize().height);
		}
		
		@Override
		public Dimension getMaximumSize(){
			return getPreferredSize();
		}
	}
	
	private enum LabelingSettingType {
		ACTIVITY_CLASSIFICATION,
		ATTRIBUTE_CLASSIFICATION,
		SUBJECT_CLEARANCE;
	}
	
	public static Labeling showDialog(AnalysisContext analysisContext) throws Exception{
		return showDialog(null, analysisContext);
	}
	
	public static Labeling showDialog(Window owner, AnalysisContext analysisContext) throws Exception{
		LabelingDialog dialog = new LabelingDialog(owner, analysisContext);
		dialog.setUpGUI();
		return dialog.getDialogObject();
	}
	
	public static void showDialog(Labeling labeling) throws Exception{
		showDialog(null, labeling);
	}
	
	public static void showDialog(Window owner, Labeling labeling) throws Exception{
		LabelingDialog dialog = new LabelingDialog(owner, labeling);
		dialog.setUpGUI();
	}

	public static void main(String[] args) throws Exception{
		ACLModel acl = new ACLModel("acl1", SOABase.createSOABase("base", 5, 5, 5));
		AnalysisContext analysisContext = new AnalysisContext("AnalysisContext1", acl, false);
		Labeling testLabeling = new Labeling(analysisContext);
		testLabeling.setActivityClassification("act_1", SecurityLevel.HIGH);
		System.out.println(testLabeling);
		LabelingDialog.showDialog(testLabeling);
		System.out.println(testLabeling);
	}

}
