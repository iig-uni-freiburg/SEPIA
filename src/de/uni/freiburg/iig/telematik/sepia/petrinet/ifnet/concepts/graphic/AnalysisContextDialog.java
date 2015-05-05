package de.uni.freiburg.iig.telematik.sepia.petrinet.ifnet.concepts.graphic;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Window;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.Arrays;

import javax.swing.Box;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.SpringLayout;

import de.invation.code.toval.graphic.dialog.AbstractEditCreateDialog;
import de.invation.code.toval.graphic.util.SpringUtilities;
import de.invation.code.toval.misc.soabase.SOABase;
import de.invation.code.toval.validate.ParameterException;
import de.uni.freiburg.iig.telematik.sepia.petrinet.ifnet.concepts.AnalysisContext;
import de.uni.freiburg.iig.telematik.sewol.accesscontrol.AbstractACModel;
import de.uni.freiburg.iig.telematik.sewol.accesscontrol.acl.ACLModel;
import de.uni.freiburg.iig.telematik.sewol.accesscontrol.graphic.ACModelDialog;

public class AnalysisContextDialog extends AbstractEditCreateDialog<AnalysisContext> {

	private static final long serialVersionUID = -6749130360294934050L;
	
	private JPanel subjectDescriptorPanel;
	private JScrollPane centerPanel;
	private JTextField txtName;

	protected AnalysisContextDialog(Window owner, AnalysisContext analysisContext) {
		super(owner, analysisContext);
	}

	@SuppressWarnings("rawtypes")
	protected AnalysisContextDialog(Window owner, AbstractACModel acModel) {
		super(owner, acModel);
	}

	@SuppressWarnings("rawtypes")
	@Override
	protected AnalysisContext newDialogObject(Object... parameters) {
		return new AnalysisContext((AbstractACModel) parameters[0], false);
	}

	@Override
	protected void addComponents() throws Exception {
		mainPanel().setLayout(new BorderLayout());
		JPanel namePanel = new JPanel(new FlowLayout());
		txtName = new JTextField();
		txtName.setText(getDialogObject().getName());
		txtName.setPreferredSize(new Dimension(200,24));
		namePanel.add(new JLabel("Name:"));
		namePanel.add(txtName);
		mainPanel().add(namePanel, BorderLayout.NORTH);
		centerPanel = new JScrollPane();
		centerPanel.setPreferredSize(new Dimension(300,300));
		centerPanel.setViewportView(getActivityClassificationPanel());
		mainPanel().add(centerPanel, BorderLayout.CENTER);
	}
	
	private JPanel getActivityClassificationPanel(){
		if(subjectDescriptorPanel == null){
			subjectDescriptorPanel = new JPanel(new SpringLayout());
			for(String activity: getDialogObject().getACModel().getContext().getActivities()){
				JLabel label = new JLabel(activity, JLabel.TRAILING);
				label.setToolTipText(activity);
				label.setPreferredSize(new Dimension(140, label.getPreferredSize().height));
				subjectDescriptorPanel.add(label);
				SubjectDescriptorBox box = new SubjectDescriptorBox(activity);
				box.setSelectedItem(getDialogObject().getSubjectDescriptor(activity));
				subjectDescriptorPanel.add(box);
				subjectDescriptorPanel.add(Box.createHorizontalGlue());
			}
			SpringUtilities.makeCompactGrid(subjectDescriptorPanel, getDialogObject().getACModel().getContext().getActivities().size(), 3, 0, 0, 0, 0);
		}
		return subjectDescriptorPanel;
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
			setTitle("Edit AnalysisContext");
		} else {
			setTitle("Create AnalysisContext");
		}
	}
	
	private class SubjectDescriptorBox extends JComboBox {
		
		private static final long serialVersionUID = -3257254776844101160L;
		
		private static final int PREFERRED_WIDTH = 140;
		private String activityName;

		public SubjectDescriptorBox(String activityName) {
			super();
			Object[] activities = getDialogObject().getACModel().getAuthorizedSubjectsForTransaction(activityName).toArray();
			Object[] objects = Arrays.copyOf(activities, activities.length + 1);
			setModel(new DefaultComboBoxModel(objects));
			this.activityName = activityName;
			
			addItemListener(new ItemListener() {
				
				@Override
				public void itemStateChanged(ItemEvent e) {
					if(e.getStateChange() == ItemEvent.SELECTED){
						Object selectedItem = getSelectedItem();
						try{
							if (selectedItem != null) {
								getDialogObject().setSubjectDescriptor(SubjectDescriptorBox.this.activityName, selectedItem.toString());
							} else {
								getDialogObject().removeSubjectDescriptor(SubjectDescriptorBox.this.activityName);
							}
						} catch(Exception ex){
							invalidFieldContentMessage("Cannot set subject descriptor.\nReason: " + ex.getMessage());
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
	
	public static void showDialog(AnalysisContext analysisContext) throws Exception{
		showDialog(null, analysisContext);
	}
	
	public static void showDialog(Window owner, AnalysisContext analysisContext) throws Exception{
		AnalysisContextDialog dialog = new AnalysisContextDialog(owner, analysisContext);
		dialog.setUpGUI();
	}
	
	@SuppressWarnings("rawtypes")
	public static AnalysisContext showDialog(AbstractACModel acModel) throws Exception{
		return showDialog(null, acModel);
	}
		
	@SuppressWarnings("rawtypes")
	public static AnalysisContext showDialog(Window owner, AbstractACModel acModel) throws Exception{
		AnalysisContextDialog dialog = new AnalysisContextDialog(owner, acModel);
		dialog.setUpGUI();
		return dialog.getDialogObject();
	}
	
	public static void main(String[] args) throws Exception{
		ACLModel acl = new ACLModel("acl1", SOABase.createSOABase("base", 5, 5, 5));
		ACModelDialog.showDialog(null, acl);
		AnalysisContext analysisContext = new AnalysisContext("AnalysisContext1", acl, false);
//		System.out.println(analysisContext);
		AnalysisContext newContext = AnalysisContextDialog.showDialog(acl);
		System.out.println(newContext);
//		System.out.println(analysisContext);
	}

}
