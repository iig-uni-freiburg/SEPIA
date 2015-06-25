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
import de.uni.freiburg.iig.telematik.sewol.accesscontrol.AbstractACModel;
import de.uni.freiburg.iig.telematik.sewol.accesscontrol.acl.ACLModel;

public class AnalysisContextChooserDialog extends AbstractDialog<AnalysisContext> {

	private static final long serialVersionUID = -6428039749340520188L;

	private static final Dimension PREFERRED_SIZE = new Dimension(400, 400);

	private JComboBox comboAnalysisContext;
	private DefaultComboBoxModel comboAnalysisContextModel = new DefaultComboBoxModel();
	private Set<AnalysisContext> analysisContexts;
	private JButton btnAddAnalysisContext;
	private JButton btnEditAnalysisContext;
	private JTextArea areaPreview;
	private AbstractACModel<?> acModel;

	public AnalysisContextChooserDialog(Window owner, AbstractACModel<?> acModel, Collection<AnalysisContext> analysisContexts) {
		super(owner);
		Validate.notNull(acModel);
		Validate.notNull(analysisContexts);
		Validate.noNullElements(analysisContexts);
		this.acModel = acModel;
		this.analysisContexts = new HashSet<AnalysisContext>(analysisContexts);
		setPreferredSize(PREFERRED_SIZE);
	}

	@Override
	protected void addComponents() throws Exception {
		mainPanel().setLayout(new BorderLayout());

		JPanel topPanel = new JPanel(new FlowLayout());
		topPanel.add(new JLabel("Analysis context:"));
		comboAnalysisContext = new JComboBox(comboAnalysisContextModel);
		comboAnalysisContext.setRenderer(new AnalysisContextCellRenderer());
		comboAnalysisContext.addItemListener(new ItemListener() {
			@Override
			public void itemStateChanged(ItemEvent e) {
				if (e.getStateChange() == ItemEvent.SELECTED) {
					updatePreview();
				}
			}
		});
		topPanel.add(comboAnalysisContext);
		mainPanel().add(topPanel, BorderLayout.PAGE_START);

		areaPreview = new JTextArea();
		mainPanel().add(new JScrollPane(areaPreview), BorderLayout.CENTER);

		mainPanel().add(getAnalysisContextButtonPanel(), BorderLayout.PAGE_END);

		updateModelCombo();
		updatePreview();
	}

	private JPanel getAnalysisContextButtonPanel() {
		JPanel panelButtons = new JPanel();
		BoxLayout l = new BoxLayout(panelButtons, BoxLayout.LINE_AXIS);
		panelButtons.setLayout(l);

		panelButtons.add(getButtonAddAnalysisContext());
		panelButtons.add(getButtonEditModel());
		panelButtons.add(Box.createHorizontalGlue());
		return panelButtons;
	}

	@Override
	protected void setTitle() {
		setTitle("Choose analysis context");
	}

	@Override
	protected void okProcedure() {
		setDialogObject(getSelectedAnalysisContext());
		super.okProcedure();
	}

	private JButton getButtonAddAnalysisContext() {
		if (btnAddAnalysisContext == null) {
			btnAddAnalysisContext = new JButton("Add");
			btnAddAnalysisContext.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					AnalysisContext newAnalysisContext = null;
					try {
						newAnalysisContext = AnalysisContextDialog.showDialog(AnalysisContextChooserDialog.this, acModel);
					} catch (Exception ex) {
						internalException("Cannot launch AnalysisContextDialog", ex);
						return;
					}
					if (newAnalysisContext == null)
						return;

					if (ensureValidAnalysisContext(newAnalysisContext)) {
						addNewAnalysisContext(newAnalysisContext);
					}
				}
			});
		}
		return btnAddAnalysisContext;
	}

	private boolean ensureValidAnalysisContext(AnalysisContext newAnalysisContext) {
		boolean cont = true;
		while (cont) {
			try {
				validateNewAnalysisContext(newAnalysisContext);
				cont = false;
			} catch (Exception ex) {
				int result = JOptionPane.showConfirmDialog(AnalysisContextChooserDialog.this, "Cannot add new analysis context: " + ex.getMessage() + "\nEdit analysis context?", "Invalid Parameter", JOptionPane.YES_NO_OPTION);
				if (result != JOptionPane.YES_OPTION) {
					return false;
				}
				try {
					AnalysisContextDialog.showDialog(AnalysisContextChooserDialog.this, newAnalysisContext);
				} catch (Exception e2) {
					internalException("Cannot launch AnalysisContextDialog.", e2);
					return false;
				}
			}
		}
		return true;
	}

	protected void addNewAnalysisContext(AnalysisContext newAnalysisContext) {
		analysisContexts.add(newAnalysisContext);
		updateModelCombo();
		comboAnalysisContext.setSelectedItem(newAnalysisContext);
		updatePreview();
	}

	private JButton getButtonEditModel() {
		if (btnEditAnalysisContext == null) {
			btnEditAnalysisContext = new JButton("Edit");
			btnEditAnalysisContext.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					try {
						AnalysisContextDialog.showDialog(AnalysisContextChooserDialog.this, getSelectedAnalysisContext());
					} catch (Exception ex) {
						internalException("Cannot launch ACModelDialog.", ex);
						return;
					}
					if (ensureValidAnalysisContext(getSelectedAnalysisContext()))
						updatePreview();
				}
			});
		}
		return btnEditAnalysisContext;
	}

	private AnalysisContext getSelectedAnalysisContext() {
		Object selectedObject = comboAnalysisContext.getSelectedItem();
		if (selectedObject == null)
			return null;
		return (AnalysisContext) selectedObject;
	}

	@SuppressWarnings("unchecked")
	private void updateModelCombo() {
		comboAnalysisContext.removeAllItems();
		for (AnalysisContext analysisContext : analysisContexts) {
			comboAnalysisContextModel.addElement(analysisContext);
		}
		comboAnalysisContext.setEnabled(comboAnalysisContext.getItemCount() > 0);
	}

	private void updatePreview() {
		areaPreview.setText("");
		if (comboAnalysisContext.getSelectedItem() != null) {
			AnalysisContext selectedModel = (AnalysisContext) comboAnalysisContext.getSelectedItem();
			areaPreview.setText(selectedModel.toString());
		}
	}

	protected void validateNewAnalysisContext(AnalysisContext newAnalysisContext) throws Exception {
	}

	private class AnalysisContextCellRenderer extends AlternatingRowColorListCellRenderer {

		private static final long serialVersionUID = 6373356378620988440L;

		@Override
		protected String getText(Object value) {
			return ((AnalysisContext) value).getName();
		}

		@Override
		protected String getTooltip(Object value) {
			return ((AnalysisContext) value).getName();
		}

	}

	public static AnalysisContext showDialog(Collection<AnalysisContext> analysisContexts, AbstractACModel<?> acModel) throws Exception {
		return showDialog(null, analysisContexts, acModel);
	}

	public static AnalysisContext showDialog(Window owner, Collection<AnalysisContext> analysisContexts, AbstractACModel<?> acModel) throws Exception {
		AnalysisContextChooserDialog dialog = new AnalysisContextChooserDialog(owner, acModel, analysisContexts);
		dialog.setUpGUI();
		return dialog.getDialogObject();
	}

	public static void main(String[] args) throws Exception {
		SOABase base1 = SOABase.createSOABase("base1", 10, 10, 10);
		ACLModel m1 = new ACLModel("m1", base1);
		AnalysisContext aContext = new AnalysisContext("aContext1", m1, false);
		AnalysisContext bContext = new AnalysisContext("bContext1", m1, false);

		List<AnalysisContext> list = new ArrayList<AnalysisContext>();
		list.add(aContext);
		list.add(bContext);
		AnalysisContextChooserDialog.showDialog(list, m1);
	}
}
