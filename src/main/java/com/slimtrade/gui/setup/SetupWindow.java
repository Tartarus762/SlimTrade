package com.slimtrade.gui.setup;

import com.slimtrade.App;
import com.slimtrade.core.enums.DefaultIcon;
import com.slimtrade.core.enums.SetupPhase;
import com.slimtrade.core.managers.SaveManager;
import com.slimtrade.core.utility.ZUtil;
import com.slimtrade.gui.managers.SetupManager;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;

public class SetupWindow extends JFrame {

    private final CardLayout cardLayout = new CardLayout();
    private final JPanel cardPanel = new JPanel(cardLayout);

    private final StartSetupPanel startPanel = new StartSetupPanel();
    private final FinishSetupPanel finishPanel = new FinishSetupPanel();

    private final JButton previousButton = new JButton("Previous");
    private final JButton nextButton = new JButton(NEXT_TEXT);

    private final ClientSetupPanel clientPanel = new ClientSetupPanel(nextButton);
    private final StashSetupPanel stashPanel = new StashSetupPanel(nextButton);
    private final StashFolderSetupPanel stashFolderPanel = new StashFolderSetupPanel(nextButton);

    private static final String NEXT_TEXT = "Next";

    private final JLabel countLabel = new JLabel("10/10");

    private final HashMap<Integer, AbstractSetupPanel> panelMap = new HashMap<>();

    private int panelIndex;

    public SetupWindow() {
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setTitle("SlimTrade Setup");
        ArrayList<Image> images = new ArrayList<>();
        images.add(new ImageIcon(Objects.requireNonNull(getClass().getResource(DefaultIcon.CHAOS_ORB.path()))).getImage());
        images.add(new ImageIcon(Objects.requireNonNull(getClass().getResource(DefaultIcon.CHAOS_ORB.path()))).getImage().getScaledInstance(32, 32, Image.SCALE_SMOOTH));
        setIconImages(images);
        JPanel contentPanel = new JPanel();
        setContentPane(contentPanel);
        previousButton.setVisible(false);

        contentPanel.setLayout(new BorderLayout());
        contentPanel.add(cardPanel, BorderLayout.CENTER);

        JPanel progressPanel = new JPanel(new GridBagLayout());
        JPanel buttonPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gc = ZUtil.getGC();
        int inset = 5;
        int buttonSpacing = 10;
        gc.insets = new Insets(inset, inset, inset, 0);
        progressPanel.add(countLabel, gc);
        buttonPanel.add(previousButton, gc);
        gc.insets.right = inset;
        gc.insets.left = buttonSpacing;
        gc.gridx++;
        buttonPanel.add(nextButton, gc);

        JPanel bufferPanel = new JPanel(new BorderLayout());
        bufferPanel.add(progressPanel, BorderLayout.WEST);
        bufferPanel.add(buttonPanel, BorderLayout.EAST);

        contentPanel.add(bufferPanel, BorderLayout.SOUTH);

        setAlwaysOnTop(true);
        setMinimumSize(new Dimension(300, 200));
        setResizable(false);
        addListeners();
        pack();
        countLabel.setText("");
        setLocationRelativeTo(null);
    }

    private void addListeners() {
        previousButton.addActionListener(e -> {
            if (panelIndex > 0) {
                panelIndex--;
                showIndexedPanel();
                nextButton.setEnabled(true);
            }
        });

        nextButton.addActionListener(e -> {
            if (panelIndex == cardPanel.getComponentCount() - 1) {
                finishSetup();
            }
            if (panelIndex < cardPanel.getComponentCount() - 1) {
                panelIndex++;
                showIndexedPanel();
            }
        });
    }

    private void finishSetup() {
        if (panelMap.values().size() > 0) {
            if (clientPanel.isSetupValid())
                SaveManager.settingsSaveFile.data.clientPath = clientPanel.getClientPath();
            if (stashFolderPanel.isSetupValid()) {
                SaveManager.settingsSaveFile.data.folderOffset = stashFolderPanel.isUsingFolders();
                SaveManager.settingsSaveFile.data.initializedFolderOffset = true;
            }
            SaveManager.settingsSaveFile.saveToDisk(false);
        }
        App.launchApp();
    }

    private void showIndexedPanel() {
        AbstractSetupPanel panel = panelMap.get(panelIndex);
        if (panel != null) panel.validateNextButton();
        cardLayout.show(cardPanel, Integer.toString(panelIndex));
        previousButton.setVisible(panelIndex != 0);
        if (panelIndex < cardPanel.getComponentCount() - 1) nextButton.setText(NEXT_TEXT);
        else nextButton.setText("Finish");
        countLabel.setText(panelIndex + "/" + (cardPanel.getComponentCount() - 2));
        countLabel.setVisible(panelIndex > 0 && panelIndex < cardPanel.getComponentCount() - 1);
    }

    public void setup() {
        cardPanel.add(startPanel, Integer.toString(cardPanel.getComponentCount()));
        for (SetupPhase phase : SetupManager.getSetupPhases()) {
            switch (phase) {
                case CLIENT_PATH:
                    panelMap.put(cardPanel.getComponentCount(), clientPanel);
                    cardPanel.add(clientPanel, Integer.toString(cardPanel.getComponentCount()));
                    break;
                case STASH_POSITION:
                    panelMap.put(cardPanel.getComponentCount(), stashPanel);
                    cardPanel.add(stashPanel, Integer.toString(cardPanel.getComponentCount()));
                    break;
                case STASH_FOLDERS:
                    panelMap.put(cardPanel.getComponentCount(), stashFolderPanel);
                    cardPanel.add(stashFolderPanel, Integer.toString(cardPanel.getComponentCount()));
                    break;
            }
        }
        cardPanel.add(finishPanel, Integer.toString(cardPanel.getComponentCount()));
        pack();
        setLocationRelativeTo(null);
    }

    public StashSetupPanel getStashPanel() {
        return stashPanel;
    }

}
