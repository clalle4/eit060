package client;

import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ImageIcon;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.SwingUtilities;

/**
 * A user interface for the whole program. Designed for two cameras. Possible to
 * switch between modes with drop down list.
 */
public class GUI extends JFrame implements Runnable {
	private static final long serialVersionUID = -5959596772809263279L;
	private ImageIcon icon1;
	private ImageIcon icon2;
	private JComboBox<String> cameraBox;
	private JComboBox<String> viewBox;
	private final String[] viewOptions;
	private final String[] cameraOptions;
	private ClientMonitor mon;
	private JTextArea cameraModeJTA;
	private JTextArea viewModeJTA;
	private JTextArea infoJTA;

	/**
	 * Creates an instance of the GUI.
	 * 
	 * @param mon
	 *            - The monitor that holds the images to show.
	 */
	public GUI(ClientMonitor mon) {
		super();
		this.mon = mon;
		cameraOptions = new String[] { "Auto", "Idle", "Movie" };
		viewOptions = new String[] { "Auto", "Synchronous", "Asynchronous" };
		icon1 = new ImageIcon();
		icon2 = new ImageIcon();
	}

	/**
	 * Initializes the GUI
	 */
	private void init() {
		super.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		cameraBox = new JComboBox<>(cameraOptions);
		cameraBox.addActionListener(new cameraBoxListener());
		viewBox = new JComboBox<>(viewOptions);
		viewBox.addActionListener(new viewBoxListener());

		getContentPane().setLayout(new BorderLayout());
		JPanel namePanel = new JPanel(new GridLayout(1, 2));
		Font cameraFont = new Font("Georgia", Font.PLAIN, 25);
		JTextArea name1 = new JTextArea("Camera 1");
		name1.setFont(cameraFont);
		name1.setEditable(false);
		namePanel.add(name1);
		JTextArea name2 = new JTextArea("Camera 2");
		name2.setFont(cameraFont);
		name2.setEditable(false);
		namePanel.add(name2);
		JPanel modePanel = new JPanel(new GridLayout(3, 2));
		Font font = new Font("Georgia", Font.BOLD, 30);
		Font font2 = new Font("Georgia", Font.PLAIN, 30);
		infoJTA = new JTextArea("");
		infoJTA.setEditable(false);
		infoJTA.setFont(font);
		modePanel.add(infoJTA);
		JTextArea jta = new JTextArea("Set the mode");
		jta.setEditable(false);
		jta.setFont(font);
		modePanel.add(jta);
		cameraModeJTA = new JTextArea("Current camera mode: Idle");
		cameraModeJTA.setEditable(false);
		cameraModeJTA.setFont(font2);
		modePanel.add(cameraModeJTA);
		cameraBox.setFont(font2);
		modePanel.add(cameraBox);
		viewModeJTA = new JTextArea("Current view mode: Synchronous");
		viewModeJTA.setEditable(false);
		viewModeJTA.setFont(font2);
		modePanel.add(viewModeJTA);
		viewBox.setFont(font2);
		modePanel.add(viewBox);
		add(namePanel, BorderLayout.PAGE_START);
		add(modePanel, BorderLayout.PAGE_END);
		pack();
		setSize(1300, 720);
		setLocationRelativeTo(null);
		setVisible(true);
	}

	/**
	 * Starts the two image getting threads and builds the GUI by initializing
	 * it.
	 */
	public void run() {
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				init();
			}
		});
		UpdateCamera1Thread updateCamera1Thread = new UpdateCamera1Thread();
		updateCamera1Thread.start();
		UpdateCamera2Thread updateCamera2Thread = new UpdateCamera2Thread();
		updateCamera2Thread.start();
	}

	/**
	 * Updates the textBoxes in the GUI depending on current values on
	 * attributes in the monitor.
	 */
	private void updateText() {
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				String s = "Current camera mode: ";
				if (mon.motionDetected()) {
					s += "Movie";
					infoJTA.setText("Movie mode triggered by " + mon.getTriggeringCamera());
				} else {
					infoJTA.setText("");
					s += "Idle";
				}

				String viewMode = "Current view mode: ";
				if (mon.synchronous()) {
					viewMode += "Synchronous";
				} else {
					viewMode += "Asynchronous";
				}
				cameraModeJTA.setText(s);
				viewModeJTA.setText(viewMode);
			}
		});
	}

	/**
	 * Paints up a new image in the respective image box depending on cameraNbr.
	 * 
	 * @param img
	 * @param cameraNbr
	 */
	private void updateImage(byte[] img, int cameraNbr) {
		Image image = getToolkit().createImage(img);
		getToolkit().prepareImage(image, -1, -1, null);
		if (cameraNbr == 1) {
			icon1.setImage(image);
			icon1.paintIcon(this, this.getGraphics(), 0, 80);
		} else if (cameraNbr == 2) {
			icon2.setImage(image);
			icon2.paintIcon(this, this.getGraphics(), 640, 80);

		}
	}

	/**
	 * A listener that connects to the camera drop down list and listens for
	 * actions on this list.
	 */
	private class cameraBoxListener implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			@SuppressWarnings("unchecked")
			JComboBox<String> combo = (JComboBox<String>) e.getSource();
			int cameraMode = combo.getSelectedIndex();
			mon.setCameraMode(cameraMode);
		}
	}

	/**
	 * A listener that connects to the drop down list and listens for actions on
	 * this list.
	 */
	private class viewBoxListener implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			@SuppressWarnings("unchecked")
			JComboBox<String> combo = (JComboBox<String>) e.getSource();
			String mode = (String) combo.getSelectedItem();
			mon.setViewMode(mode);
			viewModeJTA.setText("Current view mode: " + mode);
		}
	}

	/**
	 * Retrieves images from camera1 and updates the GUI.
	 */
	private class UpdateCamera1Thread extends Thread {
		public void run() {
			while (true) {
				try {
					updateText();
					updateImage(mon.getCam1Image(), 1);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
	}

	/**
	 * Retrieves images from camera2 and updates the GUI.
	 */

	private class UpdateCamera2Thread extends Thread {
		public void run() {
			while (true) {
				try {
					updateText();
					updateImage(mon.getCam2Image(), 2);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
	}
}
