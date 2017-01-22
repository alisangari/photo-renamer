package gui;

import java.awt.Dialog;
import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.ArrayList;
import java.util.Collections;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextField;

import com.jgoodies.forms.layout.ColumnSpec;
import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.forms.layout.FormSpecs;
import com.jgoodies.forms.layout.RowSpec;
//import /ui.ProgressBar;

public class MainWindow {

	private JFrame frame;
	private JTextField txtPrefix;
	private JTextField txtPostfix;
	private JLabel lblFileCount;
	private JButton btnBrowse;
	private JButton btnStart;
	private JButton btnClearForm;
	private JCheckBox cbDate;
	private JCheckBox cbTime;
	private JCheckBox cbLocation;

	
	
	private JFileChooser chooser;
	private String newFolder = "";
	private boolean canceled = false;
	private ArrayList<File> files = new ArrayList<File>();
	private String choosertitle;
	private String imagesFolder;
	
	
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					MainWindow window = new MainWindow();
					window.frame.setVisible(true);
					
//					ProgressBar pBar = new ProgressBar();
//					pBar.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public MainWindow() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.setTitle("TravPic - See where you took that picture :)");
		frame.setBounds(100, 100, 669, 472);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(new FormLayout(new ColumnSpec[] {
				FormSpecs.RELATED_GAP_COLSPEC,
				FormSpecs.DEFAULT_COLSPEC,
				FormSpecs.RELATED_GAP_COLSPEC,
				FormSpecs.DEFAULT_COLSPEC,
				FormSpecs.RELATED_GAP_COLSPEC,
				ColumnSpec.decode("default:grow"),},
			new RowSpec[] {
				FormSpecs.RELATED_GAP_ROWSPEC,
				FormSpecs.DEFAULT_ROWSPEC,
				FormSpecs.RELATED_GAP_ROWSPEC,
				FormSpecs.DEFAULT_ROWSPEC,
				FormSpecs.RELATED_GAP_ROWSPEC,
				FormSpecs.DEFAULT_ROWSPEC,
				FormSpecs.RELATED_GAP_ROWSPEC,
				FormSpecs.DEFAULT_ROWSPEC,
				FormSpecs.RELATED_GAP_ROWSPEC,
				FormSpecs.DEFAULT_ROWSPEC,
				FormSpecs.RELATED_GAP_ROWSPEC,
				FormSpecs.DEFAULT_ROWSPEC,
				FormSpecs.RELATED_GAP_ROWSPEC,
				FormSpecs.DEFAULT_ROWSPEC,
				FormSpecs.RELATED_GAP_ROWSPEC,
				FormSpecs.DEFAULT_ROWSPEC,
				FormSpecs.RELATED_GAP_ROWSPEC,
				FormSpecs.DEFAULT_ROWSPEC,
				FormSpecs.RELATED_GAP_ROWSPEC,
				FormSpecs.DEFAULT_ROWSPEC,
				FormSpecs.RELATED_GAP_ROWSPEC,
				FormSpecs.DEFAULT_ROWSPEC,}));
		
		JLabel lblSelectTheFolder = new JLabel("Select the folder that contains your pictures");
		frame.getContentPane().add(lblSelectTheFolder, "2, 2");
		
		btnBrowse = new JButton("Browse...");
		btnBrowse.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				browse();
			}
		});
		frame.getContentPane().add(btnBrowse, "6, 2");
		
		lblFileCount = new JLabel("");
		frame.getContentPane().add(lblFileCount, "6, 4");
		
		JLabel lblSetAPrefix = new JLabel("Set a prefix for your pictures");
		frame.getContentPane().add(lblSetAPrefix, "2, 8");
		
		txtPrefix = new JTextField();
		frame.getContentPane().add(txtPrefix, "6, 8, fill, default");
		txtPrefix.setColumns(10);
		
		JLabel lblSetAPostfix = new JLabel("Set a post-fix for your pictures");
		frame.getContentPane().add(lblSetAPostfix, "2, 10");
		
		txtPostfix = new JTextField();
		frame.getContentPane().add(txtPostfix, "6, 10, fill, default");
		txtPostfix.setColumns(10);
		
		JLabel lblIncludeDateIn = new JLabel("Include date in picture names");
		frame.getContentPane().add(lblIncludeDateIn, "2, 12");
		
		cbDate = new JCheckBox("");
		frame.getContentPane().add(cbDate, "6, 12");
		
		JLabel lblIncludeTimeIn = new JLabel("Include time in picture names");
		frame.getContentPane().add(lblIncludeTimeIn, "2, 14");
		
		cbTime = new JCheckBox("");
		frame.getContentPane().add(cbTime, "6, 14");
		
		JLabel lblIncludeLocationIn = new JLabel("Include location in picture names");
		frame.getContentPane().add(lblIncludeLocationIn, "2, 16");
		
		cbLocation = new JCheckBox("");
		frame.getContentPane().add(cbLocation, "6, 16");
		
		JLabel lblSampleName = new JLabel("Sample file name: prefix 15-1-2017 at 10-25 Location post-fix.jpg");
		frame.getContentPane().add(lblSampleName, "6, 18");
		
		btnClearForm = new JButton("Clear form");
		btnClearForm.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				clearForm();
			}
		});
		frame.getContentPane().add(btnClearForm, "2, 22");
		
		btnStart = new JButton("Start");
		btnStart.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				startP();
			}
		});
		frame.getContentPane().add(btnStart, "6, 22");
	}













	//************ METHODS **************
		protected void browse() {
			chooser = new JFileChooser();
			chooser.setCurrentDirectory(new java.io.File("."));
			chooser.setDialogTitle(choosertitle);
			chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
			//
			// disable the "All files" option.
			//
			chooser.setAcceptAllFileFilterUsed(false);
			//
			if (chooser.showOpenDialog(chooser) == JFileChooser.APPROVE_OPTION) {
				// System.out.println("getCurrentDirectory(): " +
				// chooser.getCurrentDirectory());
				// System.out.println("getSelectedFile() : " +
				// chooser.getSelectedFile());
				imagesFolder = chooser.getSelectedFile().getAbsolutePath();
				files = new ArrayList<File>();
				listf(imagesFolder, files);
//				btnStart.setEnabled(true);
//				btnBrowse.setEnabled(false);
			} else {
				// System.out.println("No Selection ");
				imagesFolder = null;
			}
			Collections.sort(files);
			lblFileCount.setText("You have selected "+imagesFolder+" which contains "+files.size()+" files.");
//			System.out.println(files.size() + "files have been selected.");
		}

		private void startP(){
			Progress progress = new Progress(files, getPrefix(), getPostfix(),cbDate.isSelected(), cbTime.isSelected(), cbLocation.isSelected());
			progress.setLocationRelativeTo(frame);
			progress.startP();
			progress.setModal(true);
			progress.setVisible(true);
//			progress.startP();
		}
		/*protected void start() {
			ProgressBar progressBar = new ProgressBar();
			progressBar.setTotalFileCount(files.size());
			progressBar.setVisible(true);
//			btnClearForm.setEnabled(true);
//			btnBrowse.setEnabled(false);
			int c = 1;
			for (File file : files) {
//				System.out.println("file " + c++ + "("+ file.getName() +")"+ " sent for processing...");
				if (!progressBar.isCancelled()) {
					progressBar.update(c);
					Rename.start(
							file.getAbsolutePath().substring(0, file.getAbsolutePath().length() - file.getName().length()),
							file.getName(),getPrefix(), getPostfix(),cbDate.isSelected(), cbTime.isSelected(), cbLocation.isSelected());
				}
			}
//			System.out.println("Rename process of " + files.size() + " files Completed!");

		}*/

		private String getPostfix() {
			return txtPostfix.getText();
		}

		private String getPrefix() {
			return txtPrefix.getText();
		}

		protected void clearForm() {
			lblFileCount.setText("");
			txtPostfix.setText("");
			txtPrefix.setText("");
			

		}

		private void listf(String directoryName, ArrayList<File> files) {
			File directory = new File(directoryName);

			// get all the files from a directory
			File[] fList = directory.listFiles();
			for (File file : fList) {
				if (file.isFile()) {
					files.add(file);
				} else if (file.isDirectory()) {
//					newFolder = file.getAbsolutePath();
//					listf(file.getAbsolutePath(), files);
				}
			}
		}

}
