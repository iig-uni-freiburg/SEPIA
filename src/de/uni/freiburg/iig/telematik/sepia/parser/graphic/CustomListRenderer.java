package de.uni.freiburg.iig.telematik.sepia.parser.graphic;

import java.awt.Color;
import java.awt.Component;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.ListCellRenderer;

	@SuppressWarnings("rawtypes")
	public class CustomListRenderer extends JLabel implements ListCellRenderer {
		public static final long serialVersionUID = 1L;
		
		public CustomListRenderer() {
			setOpaque(true);
			setHorizontalAlignment(LEFT);
			setVerticalAlignment(CENTER);
			this.setBorder(BorderFactory.createEmptyBorder(0, 10, 0, 0));
		}

		@Override
		public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {

			setText(value.toString());
			setToolTipText(value.toString());
			
			if (isSelected) {
				setBackground(new Color(10,100,200));
				setForeground(new Color(0,0,0));
			} else {
				if((index%2)==0){
					setBackground(list.getBackground());
					setForeground(list.getForeground());
				} else {
					setBackground(new Color(230,230,230));
					setForeground(list.getForeground());
				}
			}
					
			return this;

		}

	}