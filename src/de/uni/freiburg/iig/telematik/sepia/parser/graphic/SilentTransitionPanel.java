package de.uni.freiburg.iig.telematik.sepia.parser.graphic;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.swing.BorderFactory;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.ScrollPaneConstants;
import javax.swing.table.AbstractTableModel;

import de.invation.code.toval.validate.ParameterException;
import de.uni.freiburg.iig.telematik.sepia.petrinet.AbstractPetriNet;
import de.uni.freiburg.iig.telematik.sepia.util.PNUtils;

public class SilentTransitionPanel extends JPanel {
	
	private static final long serialVersionUID = 4926262262767599934L;
	private static final Dimension PREFERRED_SIZE = new Dimension(300,300);
	private static final int MIN_SIZE_TRANSITION_NAME = 60;
	private static final int MIN_SIZE_TRANSITION_LABEL = 180;
	private static final int MIN_SIZE_CHECKBOX = 40;
	
	private JTable transitionTable = null;
	
	public SilentTransitionPanel(){
		super(new BorderLayout());
		setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
		JScrollPane scroll = new JScrollPane();
		scroll.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
		scroll.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		scroll.setViewportView(getTransitionTable());
		add(scroll, BorderLayout.CENTER);
	}
	
	@Override
	public Dimension getPreferredSize() {
		return PREFERRED_SIZE;
	}

	@SuppressWarnings("rawtypes")
	public void update(AbstractPetriNet net){
		transitionTable.setModel(new SilentTransitionTableModel(net));
		transitionTable.setRowHeight(30);
	
		transitionTable.getColumnModel().getColumn(0).setWidth(MIN_SIZE_TRANSITION_NAME);
		transitionTable.getColumnModel().getColumn(0).setMinWidth(MIN_SIZE_TRANSITION_NAME);
		transitionTable.getColumnModel().getColumn(0).setPreferredWidth(MIN_SIZE_TRANSITION_NAME);
		transitionTable.getColumnModel().getColumn(0).setHeaderValue("Name");
		
		transitionTable.getColumnModel().getColumn(1).setWidth(MIN_SIZE_TRANSITION_LABEL);
		transitionTable.getColumnModel().getColumn(1).setMinWidth(MIN_SIZE_TRANSITION_LABEL);
		transitionTable.getColumnModel().getColumn(1).setPreferredWidth(MIN_SIZE_TRANSITION_LABEL);
		transitionTable.getColumnModel().getColumn(1).setHeaderValue("Label");
		
		transitionTable.getColumnModel().getColumn(2).setWidth(MIN_SIZE_CHECKBOX);
		transitionTable.getColumnModel().getColumn(2).setMinWidth(MIN_SIZE_CHECKBOX);
		transitionTable.getColumnModel().getColumn(2).setMaxWidth(MIN_SIZE_CHECKBOX);
		transitionTable.getColumnModel().getColumn(2).setPreferredWidth(MIN_SIZE_CHECKBOX);
		transitionTable.getColumnModel().getColumn(2).setHeaderValue("Silent");
	}

	private JTable getTransitionTable(){
		if(transitionTable == null){
			transitionTable = new JTable();
			transitionTable.getSelectionModel().setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
			transitionTable.setBorder(BorderFactory.createLineBorder(Color.black));
		}
		return transitionTable;
	}
	
	public Set<String> getSilentTransitions(){
		return ((SilentTransitionTableModel) getTransitionTable().getModel()).getSilentTransitions();
	}
	
	private class SilentTransitionTableModel extends AbstractTableModel {

		private static final long serialVersionUID = 1419785199076203719L;
		private List<JLabel> labelList = new ArrayList<JLabel>();
		private List<JLabel> nameLabelList = new ArrayList<JLabel>();
		private List<JCheckBox> checkBoxList = new ArrayList<JCheckBox>();
		
		@SuppressWarnings({ "rawtypes", "unchecked" })
		public SilentTransitionTableModel(AbstractPetriNet net){
			List<String> transitionLabelList = new ArrayList<String>();
			try {
				transitionLabelList.addAll(PNUtils.getLabelSetFromTransitions(net.getTransitions(), true));
			} catch (ParameterException e) {
				e.printStackTrace();
			}
			Collections.sort(transitionLabelList);
			for(String transitionLabel: transitionLabelList){
				Set<String> transitionNames = null;
				try {
					transitionNames = PNUtils.getNameSetFromTransitions(net.getTransitions(transitionLabel), true);
				} catch (ParameterException e) {
					e.printStackTrace();
				}
				for(String transitionName: transitionNames){
					labelList.add(new JLabel(transitionLabel));
					nameLabelList.add(new JLabel(transitionName));
					JCheckBox checkBox = new JCheckBox("silent");
					if(net.getTransition(transitionName).isSilent() || transitionLabel.startsWith("_"))
						checkBox.setSelected(true);
					checkBoxList.add(checkBox);
				}
			}
		}

		@Override
		public boolean isCellEditable(int rowIndex, int columnIndex) {
			return columnIndex == 2;
//			return true;
		}

		@Override
		public int getRowCount() {
			return labelList.size();
		}

		@Override
		public int getColumnCount() {
			return 3;
		}
		
		

		@Override
		public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
			if(columnIndex == 2){
				checkBoxList.get(rowIndex).setSelected((Boolean) aValue);
			} else {
				super.setValueAt(aValue, rowIndex, columnIndex);
			}
		}

		@Override
		public Object getValueAt(int rowIndex, int columnIndex) {
			if(columnIndex == 0){
				return nameLabelList.get(rowIndex).getText();
			} if(columnIndex == 1){
				return labelList.get(rowIndex).getText();
			} else if(columnIndex == 2){
				return checkBoxList.get(rowIndex).isSelected();
			}
			return null;
		}


		@Override
		@SuppressWarnings({ "unchecked", "rawtypes" })
		public Class getColumnClass(int c) {
			if(c == 2)
				return Boolean.class;
			return super.getColumnClass(c);
	    }
		
		public Set<String> getSilentTransitions(){
			Set<String> result = new HashSet<String>();
			for(int i = 0; i<getRowCount(); i++){
				if(checkBoxList.get(i).isSelected()){
					result.add(nameLabelList.get(i).getText());
				}
			}
			return result;
		}
		
	}

	
}
