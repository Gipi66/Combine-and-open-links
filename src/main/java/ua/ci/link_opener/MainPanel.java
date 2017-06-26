package ua.ci.link_opener;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Desktop;
import java.awt.GridLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Properties;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.SwingConstants;
import javax.swing.UIManager;

public class MainPanel extends JPanel {
	/**
	 * 
	 */
	private static final long serialVersionUID = 2824552039451407154L;
	final String propsPath = "config.ini";
	Properties props = new Properties();

	private File file;

	/**
	 * Create the panel.
	 */
	public MainPanel() {
		loadProps(props);

		setBackground(Color.LIGHT_GRAY);
		setLayout(new BorderLayout(0, 0));

		JPanel panelCenter = new JPanel();
		add(panelCenter, BorderLayout.CENTER);
		panelCenter.setLayout(new BorderLayout(0, 0));

		JPanel panelHelpTop = new JPanel();
		panelHelpTop.setBackground(new Color(255, 255, 255));
		panelCenter.add(panelHelpTop, BorderLayout.NORTH);
		panelHelpTop.setLayout(new GridLayout(0, 3, 4, 4));

		JLabel lblNewLabel = new JLabel("Слева");
		lblNewLabel.setBackground(UIManager.getColor("CheckBox.background"));
		panelHelpTop.add(lblNewLabel);

		JLabel lblNewLabel_1 = new JLabel("плюс");
		lblNewLabel_1.setBackground(UIManager.getColor("Button.highlight"));
		lblNewLabel_1.setHorizontalAlignment(SwingConstants.CENTER);
		panelHelpTop.add(lblNewLabel_1);

		JLabel lblNewLabel_2 = new JLabel("справа");
		lblNewLabel_2.setBackground(UIManager.getColor("CheckBox.background"));
		lblNewLabel_2.setHorizontalAlignment(SwingConstants.RIGHT);
		panelHelpTop.add(lblNewLabel_2);

		JPanel panelText = new JPanel();
		panelCenter.add(panelText, BorderLayout.CENTER);
		panelText.setLayout(new GridLayout(0, 2, 10, 0));

		JTextArea textAreaLeft = new JTextArea();
		textAreaLeft.setText(props.getProperty("left"));

		JScrollPane scrollPaneLeft = new JScrollPane(textAreaLeft);
		panelText.add(scrollPaneLeft);

		JTextArea textAreaRight = new JTextArea();
		textAreaRight.setText(props.getProperty("right"));

		JScrollPane scrollPaneRight = new JScrollPane(textAreaRight);
		panelText.add(scrollPaneRight);

		JPanel panelSouth = new JPanel();
		add(panelSouth, BorderLayout.SOUTH);
		panelSouth.setLayout(new GridLayout(0, 1, 1, 1));

		JButton btnNewButton = new JButton("Открыть в браузере");

		btnNewButton.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				props.put("left", textAreaLeft.getText());
				props.put("right", textAreaRight.getText());

				saveProps(props);

				for (String rowLeft : textAreaLeft.getText().split("\n")) {
					if (!rowLeft.isEmpty()) {
						for (String rowRight : textAreaRight.getText().split("\n")) {
							if (!rowRight.isEmpty()) {
								System.out.println(rowLeft + rowRight);
								if (Desktop.isDesktopSupported()) {
									try {
										Desktop.getDesktop().browse(new URI(rowLeft + rowRight));
									} catch (IOException e1) {
										// TODO Auto-generated catch block
										e1.printStackTrace();
									} catch (URISyntaxException e1) {
										// TODO Auto-generated catch block
										e1.printStackTrace();
									}
								}
							}
						}
					}
				}

			}
		});
		btnNewButton.setBackground(UIManager.getColor("Button.select"));
		panelSouth.add(btnNewButton);

	}

	private File openIniFile() {
		file = new File(propsPath);
		try {
			file.createNewFile();

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return file;
	}

	private boolean loadProps(Properties props) {

		InputStream is = null;

		// First try loading from the current directory
		try {
			is = new FileInputStream(openIniFile());
		} catch (Exception e) {
			is = null;
		}

		try {
			if (is == null) {
				// Try loading from classpath
				is = getClass().getResourceAsStream("server.properties");
			}

			// Try loading properties from the file (if found)
			props.load(is);
		} catch (Exception e) {
		}

		return true;
	}

	private boolean saveProps(Properties props) {
		boolean results = false;

		OutputStream out;
		try {
			out = new FileOutputStream(openIniFile());
			props.store(out, "Saved links");
			results = true;
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return results;

	}
}
