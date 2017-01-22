package gui;

import java.awt.EventQueue;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.ArrayList;
import java.util.Collections;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComponent;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

import com.jgoodies.forms.layout.ColumnSpec;
import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.forms.layout.FormSpecs;
import com.jgoodies.forms.layout.RowSpec;
//import /ui.ProgressBar;

import services.RenameService;

public class MainWindow {

	JFrame frame;
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
	private boolean progressFlag;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (ClassNotFoundException | InstantiationException | IllegalAccessException
				| UnsupportedLookAndFeelException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		;
		setUIFont(new javax.swing.plaf.FontUIResource(new JLabel("").getFont().getFontName(), Font.PLAIN, 16));
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					MainWindow window = new MainWindow();
					window.frame.setVisible(true);

					// ProgressBar pBar = new ProgressBar();
					// pBar.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	public static void setUIFont(javax.swing.plaf.FontUIResource f) {
		java.util.Enumeration keys = UIManager.getDefaults().keys();
		while (keys.hasMoreElements()) {
			Object key = keys.nextElement();
			Object value = UIManager.get(key);
			if (value != null && value instanceof javax.swing.plaf.FontUIResource)
				UIManager.put(key, f);
		}
	}

	/**
	 * Create the application.
	 */
	public MainWindow() {
		initialize();
		runThread();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		progressFlag = false;
		frame = new JFrame();
		frame.setTitle("TravPic - See where you took that picture");
		frame.setBounds(100, 100, 873, 430);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane()
				.setLayout(new FormLayout(
						new ColumnSpec[] { FormSpecs.RELATED_GAP_COLSPEC, FormSpecs.DEFAULT_COLSPEC,
								FormSpecs.RELATED_GAP_COLSPEC, FormSpecs.DEFAULT_COLSPEC, FormSpecs.RELATED_GAP_COLSPEC,
								ColumnSpec.decode("default:grow"), },
						new RowSpec[] { FormSpecs.RELATED_GAP_ROWSPEC, FormSpecs.DEFAULT_ROWSPEC,
								FormSpecs.RELATED_GAP_ROWSPEC, FormSpecs.DEFAULT_ROWSPEC, FormSpecs.RELATED_GAP_ROWSPEC,
								FormSpecs.DEFAULT_ROWSPEC, FormSpecs.RELATED_GAP_ROWSPEC, FormSpecs.DEFAULT_ROWSPEC,
								FormSpecs.RELATED_GAP_ROWSPEC, FormSpecs.DEFAULT_ROWSPEC, FormSpecs.RELATED_GAP_ROWSPEC,
								FormSpecs.DEFAULT_ROWSPEC, FormSpecs.RELATED_GAP_ROWSPEC, FormSpecs.DEFAULT_ROWSPEC,
								FormSpecs.RELATED_GAP_ROWSPEC, FormSpecs.DEFAULT_ROWSPEC, FormSpecs.RELATED_GAP_ROWSPEC,
								FormSpecs.DEFAULT_ROWSPEC, FormSpecs.RELATED_GAP_ROWSPEC, FormSpecs.DEFAULT_ROWSPEC,
								FormSpecs.RELATED_GAP_ROWSPEC, FormSpecs.DEFAULT_ROWSPEC, }));

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
				setProgressFlag();
			}
		});
		frame.getContentPane().add(btnStart, "6, 22");
	}


	private void runThread() {
		Thread t = new Thread(new Runnable() {
			public void run() {
				while (true) {
					System.out.println();
					if (progressFlag) {
						startProcess();
					}
				}
			}
		});
		t.start();
	}

	private void setProgressFlag() {
		progressFlag = true;

	}

	protected void browse() {
		chooser = new JFileChooser();
		chooser.setCurrentDirectory(new java.io.File("."));
		chooser.setDialogTitle(choosertitle);
		chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);

		chooser.setAcceptAllFileFilterUsed(false);

		if (chooser.showOpenDialog(chooser) == JFileChooser.APPROVE_OPTION) {
			imagesFolder = chooser.getSelectedFile().getAbsolutePath();
			files = new ArrayList<File>();
			listf(imagesFolder, files);
		} else {
			imagesFolder = null;
		}
		Collections.sort(files);
		lblFileCount.setText("You have selected " + imagesFolder + " which contains " + files.size() + " files.");
	}

	private void startProcess() {
		if (files.size() > 0) {
			progressFlag = false;
			Progress2 dlg = new Progress2(frame, files.size() - 1);
			Thread t = new Thread(new Runnable() {
				public void run() {
					dlg.setVisible(true);
				}
			});
			t.start();
			dlg.lblStatus.setText("In progress...");
			for (int i = 0; i <= files.size() - 1; i++) {
				dlg.lblProgress.setText("Processing file " + (i + 1) + " of " + files.size() + " ...");
				processImage(files.get(i));

				dlg.dpb.setValue(i);
				if (dlg.dpb.getValue() == files.size() - 1) {
					dlg.setVisible(false);
					dlg.lblProgress.setText(" ");
					dlg.lblStatus.setText("Completed!");
				}
				try {
					Thread.sleep(25);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
			dlg.setVisible(true);
		}
	}

	public void processImage(File file) {
		RenameService.start(
				file.getAbsolutePath().substring(0, file.getAbsolutePath().length() - file.getName().length()),
				file.getName(), txtPrefix.getText(), txtPostfix.getText(), cbDate.isSelected(), cbTime.isSelected(),
				cbLocation.isSelected());
	}

	protected void clearForm() {
		lblFileCount.setText("");
		txtPostfix.setText("");
		txtPrefix.setText("");
		cbDate.setSelected(false);
		cbTime.setSelected(false);
		cbLocation.setSelected(false);
	}

	private void listf(String directoryName, ArrayList<File> files) {
		File directory = new File(directoryName);

		// get all the files from a directory
		File[] fList = directory.listFiles();
		for (File file : fList) {
			if (file.isFile()) {
				files.add(file);
			} else if (file.isDirectory()) {
				// newFolder = file.getAbsolutePath();
				// listf(file.getAbsolutePath(), files);
			}
		}
	}

}
