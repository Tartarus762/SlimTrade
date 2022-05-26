package com.slimtrade.gui.options.general;

import com.slimtrade.core.managers.SaveManager;
import com.slimtrade.core.utility.ZUtil;
import com.slimtrade.modules.saving.ISavable;

import javax.swing.*;
import java.awt.*;

public class EnableFeaturesPanel extends JPanel implements ISavable {

    GridBagConstraints gc = ZUtil.getGC();

    private final JCheckBox incomingMessages = new JCheckBox();
    private final JCheckBox outgoingMessages = new JCheckBox();
    private final JCheckBox itemHighlighter = new JCheckBox();
    private final JCheckBox menubarButton = new JCheckBox();
    private final JCheckBox autoUpdateCheckbox = new JCheckBox();
    private final JCheckBox hideWhenPOENotFocusedCheckbox = new JCheckBox();

    public EnableFeaturesPanel() {
        setLayout(new GridBagLayout());
        gc.fill = GridBagConstraints.BOTH;
        gc.weightx = 1;

        addRow("Incoming Messages", incomingMessages);
        addRow("Outgoing Messages", outgoingMessages);
        addRow("Item Highlighter", itemHighlighter);
        addRow("Menubar Button", menubarButton);
        addRow("Update Automatically", autoUpdateCheckbox);
        addRow("Hide overlay when POE is not focused", hideWhenPOENotFocusedCheckbox);
    }

    private void addRow(String title, JComponent component) {
        gc.insets = new Insets(0, 10, 2, 0);
        add(component, gc);
        gc.gridx++;
        add(new JLabel(title), gc);
        gc.gridx = 0;
        gc.gridy++;
    }

    @Override
    public void save() {
        SaveManager.settingsSaveFile.data.enableIncomingMessages = incomingMessages.isSelected();
        SaveManager.settingsSaveFile.data.enableOutgoingMessages = outgoingMessages.isSelected();
        SaveManager.settingsSaveFile.data.enableItemHighlighter = itemHighlighter.isSelected();
        SaveManager.settingsSaveFile.data.enableMenuBar = menubarButton.isSelected();
        SaveManager.settingsSaveFile.data.enableAutomaticUpdate = autoUpdateCheckbox.isSelected();
        SaveManager.settingsSaveFile.data.hideWhenPOENotFocused = hideWhenPOENotFocusedCheckbox.isSelected();
    }

    @Override
    public void load() {
        incomingMessages.setSelected(SaveManager.settingsSaveFile.data.enableIncomingMessages);
        outgoingMessages.setSelected(SaveManager.settingsSaveFile.data.enableOutgoingMessages);
        itemHighlighter.setSelected(SaveManager.settingsSaveFile.data.enableItemHighlighter);
        menubarButton.setSelected(SaveManager.settingsSaveFile.data.enableMenuBar);
        autoUpdateCheckbox.setSelected(SaveManager.settingsSaveFile.data.enableAutomaticUpdate);
        hideWhenPOENotFocusedCheckbox.setSelected(SaveManager.settingsSaveFile.data.hideWhenPOENotFocused);
        hideWhenPOENotFocusedCheckbox.setSelected(SaveManager.settingsSaveFile.data.hideWhenPOENotFocused);
    }
}
