package gui;

import java.awt.BorderLayout;

import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JProgressBar;

public class Progress extends JDialog {

	JProgressBar dpb;
	JLabel lblProgress;
	JLabel lblStatus;
	
	public Progress(JFrame parentFrame, int maxCount){
		
	    super(parentFrame, "Progress Dialog", true);
	    dpb = new JProgressBar(0, maxCount);
	    lblProgress = new JLabel("Progress...");
	    lblStatus = new JLabel("Status...");
	    this.add(BorderLayout.CENTER, dpb);
	    this.add(BorderLayout.NORTH, lblProgress);
	    this.add(BorderLayout.SOUTH, lblStatus);
	    this.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
	    this.setSize(300, 120);
	    this.setLocationRelativeTo(parentFrame);

	}	
}
