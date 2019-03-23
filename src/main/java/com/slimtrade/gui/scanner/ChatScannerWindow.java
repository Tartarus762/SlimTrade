package main.java.com.slimtrade.gui.scanner;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.logging.Level;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import main.java.com.slimtrade.core.Main;
import main.java.com.slimtrade.gui.FrameManager;
import main.java.com.slimtrade.gui.basic.AbstractResizableWindow;
import main.java.com.slimtrade.gui.enums.PreloadedImage;
import main.java.com.slimtrade.gui.options.ISaveable;
import main.java.com.slimtrade.gui.panels.BufferPanel;
import main.java.com.slimtrade.gui.panels.ContainerPanel;
import main.java.com.slimtrade.gui.panels.IconPanel;

public class ChatScannerWindow extends AbstractResizableWindow implements ISaveable {

	private static final long serialVersionUID = 1L;

	int maxSaves = 50;

	private JTextField nameInput = new JTextField(10);
	private JTextArea termsText = new JTextArea();
	private JTextArea ignoreText = new JTextArea();
	// private JTextArea activeTermsText = new JTextArea();
	// private JTextArea ActiveIgnoreText = new JTextArea();
	private JTextField lmbInput = new JTextField(10);
	private JTextField rmbInput = new JTextField(10);

	private ArrayList<ScannerMessage> messages = new ArrayList<ScannerMessage>();
	private JComboBox<ScannerMessage> messageCombo = new JComboBox<ScannerMessage>();

	private int bufferX = 20;
	private int bufferY = 10;

	private boolean searching = false;

	JButton clearButton = new JButton("Clear");
	JButton saveButton = new JButton("Save");
	JButton searchButton = new JButton("Search");
	JButton deleteButton = new JButton("Delete");

	public ChatScannerWindow() {
		super("Chat Scanner");
		this.setFocusableWindowState(true);

		// TODO : Switch to nested panels
		ContainerPanel borderPanel = new ContainerPanel();
		borderPanel.container.setLayout(new GridBagLayout());
		borderPanel.setBorder(null);

		ContainerPanel criteriaPanel = new ContainerPanel();
		JPanel topPanel = criteriaPanel.container;
		topPanel.setLayout(new GridBagLayout());

		ContainerPanel savePanel = new ContainerPanel();
		JPanel bottomPanel = savePanel.container;
		bottomPanel.setLayout(new GridBagLayout());

		JLabel nameLabel = new JLabel("Save Name");
		JLabel termsLabel = new JLabel("Search Terms");
		JLabel ignoreLabel = new JLabel("Ignore Terms");

		JLabel responseLabel = new JLabel("Response Button");
		JLabel lmbLabel = new JLabel("Left Mouse");
		JLabel rmbLabel = new JLabel("Right Mouse");

		JPanel responsePanel = new JPanel(new BorderLayout());
		IconPanel responseButton = new IconPanel(PreloadedImage.REPLY.getImage(), 20);
		responsePanel.add(responseLabel, BorderLayout.CENTER);
		responsePanel.add(responseButton, BorderLayout.EAST);
		
		JScrollPane termsScroll = new JScrollPane(termsText);
		JScrollPane ignoreScroll = new JScrollPane(ignoreText);

		messageCombo.setFocusable(false);

		clearButton.setFocusable(false);
		saveButton.setFocusable(false);
		searchButton.setFocusable(false);
		deleteButton.setFocusable(false);

		JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 40, 0));
		buttonPanel.add(clearButton);
		buttonPanel.add(saveButton);

		JPanel searchPanel = new JPanel();
		searchPanel.add(searchButton);
		searchPanel.add(messageCombo);

		// container.setLayout(new GridBagLayout());
		GridBagConstraints gc = new GridBagConstraints();
		gc.fill = GridBagConstraints.HORIZONTAL;
		gc.gridx = 0;
		gc.gridy = 0;
		gc.weightx = 0;
		gc.weighty = 0;

		topPanel.add(nameLabel, gc);
		gc.gridx = 1;
		topPanel.add(new BufferPanel(bufferX, 0), gc);
		gc.gridx = 2;
		topPanel.add(nameInput, gc);
		gc.gridx = 0;
		gc.gridy++;

		topPanel.add(new BufferPanel(0, bufferY), gc);
		gc.gridy++;

		gc.anchor = GridBagConstraints.NORTH;
		topPanel.add(termsLabel, gc);
		gc.anchor = GridBagConstraints.CENTER;
		gc.gridx = 2;
		gc.weightx = 6;
		gc.weighty = 5;
		gc.fill = GridBagConstraints.BOTH;
		topPanel.add(termsScroll, gc);
		gc.fill = GridBagConstraints.HORIZONTAL;
		gc.weightx = 0;
		gc.weighty = 0;
		gc.gridx = 0;
		gc.gridy++;

		topPanel.add(new BufferPanel(0, bufferY), gc);
		gc.gridy++;

		// gc.fill = GridBagConstraints.NONE;
		gc.anchor = GridBagConstraints.NORTH;
		topPanel.add(ignoreLabel, gc);
		gc.anchor = GridBagConstraints.CENTER;
		gc.gridx = 2;
		gc.weightx = 5;
		gc.weighty = 3;
		gc.fill = GridBagConstraints.BOTH;
		topPanel.add(ignoreScroll, gc);
		gc.fill = GridBagConstraints.HORIZONTAL;
		// gc.fill = GridBagConstraints.NONE;
		gc.weightx = 0;
		gc.weighty = 0;
		gc.gridx = 0;
		gc.gridy++;

		// gc.gridwidth = 3;
		topPanel.add(responsePanel, gc);
		// gc.gridwidth = 1;
		gc.gridy++;

		topPanel.add(lmbLabel, gc);
		gc.gridx = 2;
		topPanel.add(lmbInput, gc);
		gc.gridx = 0;
		gc.gridy++;

		topPanel.add(new BufferPanel(0, bufferY / 2), gc);
		gc.gridy++;

		topPanel.add(rmbLabel, gc);
		gc.gridx = 2;
		topPanel.add(rmbInput, gc);
		gc.gridx = 0;
		gc.gridy++;

		topPanel.add(new BufferPanel(0, bufferY), gc);
		gc.gridy++;

		gc.gridwidth = 3;
		topPanel.add(buttonPanel, gc);
		gc.gridy++;

		// Bottom Panel
		gc = new GridBagConstraints();
		gc.weightx = 2;
		gc.gridx = 0;
		gc.gridy = 0;

		gc.insets.left = 20;
		gc.insets.right = 20;
		gc.fill = GridBagConstraints.HORIZONTAL;
		gc.gridwidth = 3;
		bottomPanel.add(messageCombo, gc);
		gc.gridwidth = 1;
		gc.fill = GridBagConstraints.NONE;
		// gc.insets.left = 0;
		// gc.insets.right = 0;
		gc.gridx = 0;
		gc.gridy++;

		bottomPanel.add(new BufferPanel(0, bufferY), gc);
		gc.gridy++;

		gc.fill = GridBagConstraints.HORIZONTAL;
		bottomPanel.add(deleteButton, gc);
		gc.gridx = 1;
		gc.weightx = 1;
		bottomPanel.add(new BufferPanel(10, 0), gc);
		gc.gridx = 2;
		gc.weightx = 4;
		bottomPanel.add(searchButton, gc);
		gc.fill = GridBagConstraints.NONE;
		gc.gridx = 0;
		gc.gridy++;

		bottomPanel.add(new BufferPanel(0, bufferY), gc);
		gc.gridy++;

		// bottomPanel.add(new BufferPanel(0, bufferY), gc);
		// gc.gridy++;

		topPanel.setBackground(Color.green);
		bottomPanel.setBackground(Color.ORANGE);

		// Container
		// gc.insets.top = 40;
		gc = new GridBagConstraints();
		gc.gridx = 0;
		gc.gridy = 0;
		gc.weightx = 1;
		gc.weighty = 4;
		gc.fill = GridBagConstraints.BOTH;
		gc.insets.bottom = 20;
		borderPanel.container.add(criteriaPanel, gc);
		gc.insets.bottom = 0;
		gc.gridy++;

		gc.weighty = 1;
		borderPanel.container.add(savePanel, gc);
		gc.gridy++;

		gc = new GridBagConstraints();
		gc.weightx = 1;
		gc.weighty = 1;
		gc.fill = GridBagConstraints.BOTH;
		container.setLayout(new GridBagLayout());
		container.add(borderPanel, gc);

		clearButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				clearWindow();
			}
		});

		saveButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				save();
			}
		});

		JDialog local = this;
		messageCombo.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				updateText((ScannerMessage) messageCombo.getSelectedItem());
				local.revalidate();
				local.repaint();
			}
		});

		deleteButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				delete();
				if (messageCombo.getItemCount() == 0) {
					clearWindow();
				}
			}
		});

		searchButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				searching = !searching;
				if (messageCombo.getItemCount() == 0) {
					searching = false;
				}
				if (searching) {
					ScannerMessage msg = (ScannerMessage) messageCombo.getSelectedItem();
					Main.chatParser.setSearchName(msg.name);
					Main.chatParser.setSearchTerms(msg.searchTermsArray);
					Main.chatParser.setIgnoreTerms(msg.ignoreTermsArray);
					Main.chatParser.setResponseText(msg.clickLeft, msg.clickRight);
					System.out.println(Arrays.toString(msg.searchTermsArray));
				}
				updateSearchButton();
				Main.chatParser.setChatScannerRunning(searching);
			}
		});

		load();

		updateCombo(null);

		this.setMinimumSize(this.getPreferredSize());
		this.setPreferredSize(new Dimension(400, 450));
		this.pack();
		// System.out.println(this.getSize());
		FrameManager.centerFrame(this);

	}

	private void clearWindow() {
		nameInput.setText("");
		termsText.setText("");
		ignoreText.setText("");
		lmbInput.setText("");
		rmbInput.setText("");
	}

	private void updateCombo(ScannerMessage msg) {
		messageCombo.removeAllItems();
		for (ScannerMessage m : messages) {
			messageCombo.addItem(m);
		}
		if (msg != null) {
			for(int i = 0; i<messageCombo.getItemCount();i++){
				if(messageCombo.getItemAt(i).name.equals(msg.name)){
					messageCombo.setSelectedIndex(i);
				}
			}
//			messageCombo.setSelectedItem(msg);
		}
	}

	private void updateText(ScannerMessage msg) {
		// for (ScannerMessage msg : messages) {
		// if (msg.name.equals(name)) {
		if (msg == null) {
			return;
		}
		nameInput.setText(msg.name);
		termsText.setText(msg.searchTerms);
		ignoreText.setText(msg.ignoreTerms);
		lmbInput.setText(msg.clickLeft);
		rmbInput.setText(msg.clickRight);
		// }
		// }
	}

	private void updateSearchButton() {
		if (searching) {
			searchButton.setText("Searching \"" + messageCombo.getSelectedItem() + "\"");
		} else {
			searchButton.setText("Search");
		}
	}

	public void save() {
		String name = nameInput.getText();
		String terms = termsText.getText();
		String ignore = ignoreText.getText();
		String lmb = lmbInput.getText();
		String rmb = rmbInput.getText();
		if (name.replaceAll("\\s+", "").equals("") || terms.replaceAll("\\s+", "").equals("")) {
			return;
		}
		int i = 0;
		boolean saved = false;
		for (ScannerMessage msg : messages) {
			if (msg.name != null && msg.name.equals(name)) {
				messages.set(i, new ScannerMessage(name, terms, ignore, lmb, rmb));
				saved = true;
				// messages.add();
			}
			i++;
		}
		ScannerMessage msg = new ScannerMessage(name, terms, ignore, lmb, rmb);
		if (!saved) {
			messages.add(msg);
		}
		localSaveToDisk();
		updateCombo(msg);

	}

	private void delete() {
		// TODO : Remove this check once enable/disable is added?
		if (messageCombo.getItemCount() > 0) {
			messages.remove(messageCombo.getSelectedIndex());
			messageCombo.removeItem(messageCombo.getSelectedItem());
			localSaveToDisk();
		}
	}

	public void load() {
		for (int i = 0; i < maxSaves; i++) {
			if (Main.saveManager.hasEntry("chatScanner", "search" + i)) {
				String name = Main.saveManager.getString("chatScanner", "search" + i, "name");
				String terms = Main.saveManager.getString("chatScanner", "search" + i, "terms");
				String ignore = Main.saveManager.getString("chatScanner", "search" + i, "ignore");
				String lmb = Main.saveManager.getString("chatScanner", "search" + i, "clickLeft");
				String rmb = Main.saveManager.getString("chatScanner", "search" + i, "clickRight");
				messages.add(new ScannerMessage(name, terms, ignore, lmb, rmb));
			}
		}
	}

	private void localSaveToDisk() {
		Main.saveManager.deleteObject("chatScanner");
		// TODO : Double check this sorting code
		Collections.sort(messages, new Comparator<ScannerMessage>() {
			public int compare(final ScannerMessage obj1, final ScannerMessage obj2) {
				return obj1.getName().compareTo(obj2.getName());
			}
		});
		int i = 0;
		for (ScannerMessage msg : messages) {
			Main.logger.log(Level.INFO, "Writing to index [" + i + "]\n");
			Main.saveManager.putObject(msg.name, "chatScanner", "search" + i, "name");
			Main.saveManager.putObject(msg.searchTerms, "chatScanner", "search" + i, "terms");
			Main.saveManager.putObject(msg.ignoreTerms, "chatScanner", "search" + i, "ignore");
			Main.saveManager.putObject(msg.clickLeft, "chatScanner", "search" + i, "clickLeft");
			Main.saveManager.putObject(msg.clickRight, "chatScanner", "search" + i, "clickRight");
			i++;
		}
		Main.saveManager.saveToDisk();
	}

}