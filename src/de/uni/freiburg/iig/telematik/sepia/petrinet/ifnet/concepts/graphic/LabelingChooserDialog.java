package de.uni.freiburg.iig.telematik.sepia.petrinet.ifnet.concepts.graphic;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

import de.invation.code.toval.graphic.dialog.AbstractDialog;
import de.invation.code.toval.graphic.renderer.AlternatingRowColorListCellRenderer;
import de.invation.code.toval.misc.soabase.SOABase;
import de.invation.code.toval.validate.Validate;
import de.uni.freiburg.iig.telematik.sepia.petrinet.ifnet.concepts.AnalysisContext;
import de.uni.freiburg.iig.telematik.sepia.petrinet.ifnet.concepts.Labeling;
import de.uni.freiburg.iig.telematik.sewol.accesscontrol.acl.ACLModel;

public class LabelingChooserDialog extends AbstractDialog<Labeling> {

	private static final long serialVersionUID = -2786732882426789817L;

	private static final Dimension PREFERRED_SIZE = new Dimension(400,400);
	
	private JComboBox comboLabeling;
	private DefaultComboBoxModel comboLabelingModel = new DefaultComboBoxModel();
	private Set<Labeling> labelings;
	private JButton btnAddLabeling;
	private JButton btnEditLabeling;
	private JTextArea areaPreview;
	private AnalysisContext analysisContext;

	public LabelingChooserDialog(Window owner, AnalysisContext analysisContext, Collection<Labeling> labelings) {
		super(owner);
		Validate.notNull(analysisContext);
		Validate.notNull(labelings);
		Validate.noNullElements(labelings);
		this.analysisContext = analysisContext;
		this.labelings = new HashSet<Labeling>(labelings);
		setPreferredSize(PREFERRED_SIZE);
	}

	@Override
	protected void addComponents() throws Exception {
		mainPanel().setLayout(new BorderLayout());
		
		JPanel topPanel = new JPanel(new FlowLayout());
        topPanel.add(new JLabel("Labeling:"));
        comboLabeling = new JComboBox(comboLabelingModel);
        comboLabeling.setRenderer(new LabelingCellRenderer());
        comboLabeling.addItemListener(new ItemListener() {
			@Override
			public void itemStateChanged(ItemEvent e) {
				if(e.getStateChange() == ItemEvent.SELECTED) {
					updatePreview();
                }
			}
		});
		topPanel.add(comboLabeling);
		mainPanel().add(topPanel, BorderLayout.PAGE_START);
		
		areaPreview = new JTextArea();
		mainPanel().add(new JScrollPane(areaPreview), BorderLayout.CENTER);
		
		mainPanel().add(getLabelingButtonPanel(), BorderLayout.PAGE_END);
		
		updateModelCombo();
		updatePreview();
	}
	
	private JPanel getLabelingButtonPanel() {
		JPanel panelButtons = new JPanel();
		BoxLayout l = new BoxLayout(panelButtons, BoxLayout.LINE_AXIS);
		panelButtons.setLayout(l);
		
		panelButtons.add(getButtonAddLabeling());
		panelButtons.add(getButtonEditModel());
		panelButtons.add(Box.createHorizontalGlue());
		return panelButtons;
	}

	@Override
	protected void setTitle() {
		setTitle("Choose labeling");
	}

	@Override
	protected void okProcedure() {
		setDialogObject(getSelectedLabeling());
		super.okProcedure();
	}

	private JButton getButtonAddLabeling(){
		if(btnAddLabeling == null){
			btnAddLabeling = new JButton("Add");
			btnAddLabeling.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					Labeling newLabeling = null;
					try {
						newLabeling = LabelingDialog.showDialog(LabelingChooserDialog.this, analysisContext);
					} catch(Exception ex){
						JOptionPane.showMessageDialog(LabelingChooserDialog.this, "Cannot launch LabelingDialog: " + ex.getMessage(), "Internal Error", JOptionPane.ERROR_MESSAGE);
						return;
					}
					if(newLabeling == null)
						return;
					
					if(ensureValidLabeling(newLabeling)){
						addNewLabeling(newLabeling);
					}
				}
			});
		}
		return btnAddLabeling;
	}
	
	private boolean ensureValidLabeling(Labeling newLabeling) {
		boolean cont = true;
		while(cont){
			try{
				validateNewLabeling(newLabeling);
				cont = false;
			} catch(Exception ex){
				int result = JOptionPane.showConfirmDialog(LabelingChooserDialog.this, "Cannot add new labeling: " + ex.getMessage() + "\nEdit labeling?", "Invalid Parameter", JOptionPane.YES_NO_OPTION);
				if(result != JOptionPane.YES_OPTION){
					return false;
				}
				try {
					LabelingDialog.showDialog(LabelingChooserDialog.this, newLabeling);
				} catch (Exception e2) {
					JOptionPane.showMessageDialog(LabelingChooserDialog.this, "Cannot launch LabelingDialog: " + e2.getMessage(), "Internal Error", JOptionPane.ERROR_MESSAGE);
					return false;
				}
			}
		}
		return true;
	}
	
	protected void addNewLabeling(Labeling newLabeling) {
		labelings.add(newLabeling);
		updateModelCombo();
		comboLabeling.setSelectedItem(newLabeling);
		updatePreview();
	}
	
	private JButton getButtonEditModel(){
		if(btnEditLabeling == null){
			btnEditLabeling = new JButton("Edit");
			btnEditLabeling.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					try{
						LabelingDialog.showDialog(LabelingChooserDialog.this, getSelectedLabeling());
					} catch(Exception ex){
						JOptionPane.showMessageDialog(LabelingChooserDialog.this, "Cannot launch ACModelDialog: " + ex.getMessage(), "Internal Error", JOptionPane.ERROR_MESSAGE);
						return;
					}
					if(ensureValidLabeling(getSelectedLabeling()))
						updatePreview();
				}
			});
		}
		return btnEditLabeling;
	}
	
	private Labeling getSelectedLabeling(){
		Object selectedObject = comboLabeling.getSelectedItem();
		if(selectedObject == null)
			return null;
		return (Labeling) selectedObject;
	}
	
	private void updateModelCombo() {
		comboLabeling.removeAllItems();
		for(Labeling labeling: labelings){
			comboLabelingModel.addElement(labeling);
		}
		comboLabeling.setEnabled(comboLabeling.getItemCount() > 0);
	}
	
	private void updatePreview(){
		areaPreview.setText("");
		if(comboLabeling.getSelectedItem() != null){
			Labeling selectedModel = (Labeling) comboLabeling.getSelectedItem();
			areaPreview.setText(selectedModel.toString());
		}
	}
	
	protected void validateNewLabeling(Labeling newModel) throws Exception {}
	
	private class LabelingCellRenderer extends AlternatingRowColorListCellRenderer {

		private static final long serialVersionUID = 7856449069936955758L;

		@Override
		protected String getText(Object value) {
			return ((Labeling) value).getName();
		}

		@Override
		protected String getTooltip(Object value) {
			return ((Labeling) value).getName();
		}
		
	}

	public static Labeling showDialog(Window owner, AnalysisContext analysisContext, Collection<Labeling> labelings) throws Exception{
		LabelingChooserDialog dialog = new LabelingChooserDialog(owner, analysisContext, labelings);
		dialog.setUpGUI();
		return dialog.getDialogObject();
	}
	
	public static void main(String[] args) throws Exception{
		SOABase base1 = SOABase.createSOABase("base1", 10, 10, 10);
		ACLModel m1 = new ACLModel("m1", base1);
		AnalysisContext aContext = new AnalysisContext("aContext1", m1, false);
		Labeling l1 = new Labeling("l1", aContext);
		Labeling l2 = new Labeling("l2", aContext);
		
		List<Labeling> list = new ArrayList<Labeling>();
		list.add(l1);
		list.add(l2);
		LabelingChooserDialog.showDialog(null, aContext, list);
	}
}
