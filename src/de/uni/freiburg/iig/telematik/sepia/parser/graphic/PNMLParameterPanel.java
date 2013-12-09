package de.uni.freiburg.iig.telematik.sepia.parser.graphic;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JCheckBox;
import javax.swing.JPanel;

public class PNMLParameterPanel extends JPanel{

	private static final long serialVersionUID = 3451964808731275078L;
	
	private JCheckBox requireNetTypeBox = null;
	private JCheckBox validationBox = null;
	
	public PNMLParameterPanel(){
		super();
		setLayout(new BoxLayout(this, BoxLayout.LINE_AXIS));
		add(Box.createHorizontalGlue());
		requireNetTypeBox = new JCheckBox("Require net type attribute");
		validationBox = new JCheckBox("Validate input net");
		add(requireNetTypeBox);
		add(validationBox);
		add(Box.createHorizontalGlue());
	}
	
	public boolean requireNetType(){
		return requireNetTypeBox.isSelected();
	}
	
	public boolean validation(){
		return validationBox.isSelected();
	}
	
}
