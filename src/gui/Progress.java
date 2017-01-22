package gui;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.io.File;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.border.EmptyBorder;

import services.RenameService;

public class Progress extends JDialog {

	private final JPanel contentPanel = new JPanel();
	private JProgressBar progressBar;
	private JLabel lblStatus;

	private ArrayList<File> files;
	private String prefix;
	private String postfix;
	private boolean includeDate;
	private boolean includeTime;
	private boolean includeLocation;

	
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		try {
			Progress dialog = new Progress();
			dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
			dialog.setVisible(true);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public Progress() {
	}

	/**
	 * Create the dialog.
	 * 
	 * @param files
	 * @param d
	 * @param c
	 * @param b
	 * @param string2
	 * @param string
	 */
	public Progress(ArrayList<File> files, String prefix, String postfix, boolean includeDate, boolean includeTime,
			boolean includeLocation) {
		this.files = files;
		this.prefix=prefix;
		this.postfix=postfix;
		this.includeDate=includeDate;
		this.includeTime=includeTime;
		this.includeLocation=includeLocation;
		
		setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 450, 300);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		GridBagLayout gbl_contentPanel = new GridBagLayout();
		gbl_contentPanel.columnWidths = new int[] { 0, 0 };
		gbl_contentPanel.rowHeights = new int[] { 0, 0, 0, 0 };
		gbl_contentPanel.columnWeights = new double[] { 0.0, Double.MIN_VALUE };
		gbl_contentPanel.rowWeights = new double[] { 0.0, 0.0, 0.0, Double.MIN_VALUE };
		contentPanel.setLayout(gbl_contentPanel);
		{
			progressBar = new JProgressBar();
			GridBagConstraints gbc_progressBar = new GridBagConstraints();
			gbc_progressBar.fill = GridBagConstraints.HORIZONTAL;
			gbc_progressBar.insets = new Insets(0, 0, 5, 0);
			gbc_progressBar.gridx = 0;
			gbc_progressBar.gridy = 0;
			contentPanel.add(progressBar, gbc_progressBar);
		}
		{
			lblStatus = new JLabel("Status");
			GridBagConstraints gbc_lblNewLabel = new GridBagConstraints();
			gbc_lblNewLabel.gridx = 0;
			gbc_lblNewLabel.gridy = 2;
			contentPanel.add(lblStatus, gbc_lblNewLabel);
		}
		{
			JPanel buttonPane = new JPanel();
			buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
			getContentPane().add(buttonPane, BorderLayout.SOUTH);
			{
				JButton okButton = new JButton("OK");
				okButton.setActionCommand("OK");
				buttonPane.add(okButton);
				getRootPane().setDefaultButton(okButton);
			}
			{
				JButton cancelButton = new JButton("Cancel");
				cancelButton.setActionCommand("Cancel");
				buttonPane.add(cancelButton);
			}
		}
	}

	public void startP() {
//		progressBar.setMaximum(files.size());
		int c = 1;
		for (File file : files) {
			// System.out.println("file " + c++ + "("+ file.getName() +")"+ "
			// sent for processing...");
//			progressBar.setValue(c);
			RenameService.start(
					file.getAbsolutePath().substring(0, file.getAbsolutePath().length() - file.getName().length()),
					file.getName(), prefix, postfix, includeDate, includeTime, includeLocation);
		System.out.println(c);
		c++;
		}
		this.dispose();
	}
	// System.out.println("Rename process of " + files.size() + " files
	// Completed!");

}
