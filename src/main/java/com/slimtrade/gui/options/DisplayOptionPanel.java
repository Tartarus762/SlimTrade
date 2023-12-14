package com.slimtrade.gui.options;

import com.slimtrade.core.managers.SaveManager;
import com.slimtrade.modules.theme.ThemeManager;
import com.slimtrade.modules.theme.Theme;
import com.slimtrade.core.utility.ZUtil;
import com.slimtrade.gui.components.LimitCombo;
import com.slimtrade.gui.options.general.DisplaySettingsPanel;
import com.slimtrade.modules.saving.ISavable;

import javax.swing.*;
import java.awt.*;

public class DisplayOptionPanel extends AbstractOptionPanel implements ISavable {

    private JComboBox<Theme> themeCombo = new LimitCombo<>();
    private JCheckBox colorBlindCheckBox = new JCheckBox("Color Blind Mode");

    public DisplayOptionPanel() {
        for (Theme theme : Theme.values()) themeCombo.addItem(theme);
        addHeader("UI Scale");
        addPanel(new DisplaySettingsPanel());
        addVerticalStrut();

        JPanel themePanel = new JPanel(new GridBagLayout());
        GridBagConstraints gc = ZUtil.getGC();
        themePanel.add(new JLabel("Color Theme"), gc);
        gc.gridx++;
        themePanel.add(themeCombo, gc);
        gc.gridx++;

        addHeader("Theme");
        addPanel(themePanel);
        addPanel(colorBlindCheckBox);
        themeCombo.addActionListener(e -> SwingUtilities.invokeLater(() -> ThemeManager.setTheme((Theme) themeCombo.getSelectedItem())));
    }

    @Override
    public void save() {
        SaveManager.settingsSaveFile.data.theme = (Theme) themeCombo.getSelectedItem();
        SaveManager.settingsSaveFile.data.colorBlindMode = colorBlindCheckBox.isSelected();
    }

    @Override
    public void load() {
        Theme theme = SaveManager.settingsSaveFile.data.theme;
        if (theme == null) theme = Theme.getDefaultColorTheme();
        themeCombo.setSelectedItem(theme);
        colorBlindCheckBox.setSelected(SaveManager.settingsSaveFile.data.colorBlindMode);
    }
}
