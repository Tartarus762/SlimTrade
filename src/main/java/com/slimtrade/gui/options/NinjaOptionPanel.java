package com.slimtrade.gui.options;

import com.slimtrade.core.enums.PathOfExileLeague;
import com.slimtrade.core.managers.SaveManager;
import com.slimtrade.gui.components.ButtonWrapper;
import com.slimtrade.gui.components.ComponentPair;
import com.slimtrade.gui.components.ErrorLabel;
import com.slimtrade.gui.components.HotkeyButton;
import com.slimtrade.modules.saving.ISavable;

import javax.swing.*;

public class NinjaOptionPanel extends AbstractOptionPanel implements ISavable {

    private final HotkeyButton windowHotkeyButton = new HotkeyButton();
    private final JComboBox<PathOfExileLeague> leagueCombo = new JComboBox<>();

    private final JCheckBox currencyCheckbox = new JCheckBox("Currency");
    private final JCheckBox fragmentCheckbox = new JCheckBox("Fragment");
    private final JCheckBox essenceCheckbox = new JCheckBox("Essence");
    private final JCheckBox delveCheckbox = new JCheckBox("Delve");
    private final JCheckBox blightCheckbox = new JCheckBox("Blight");
    private final JCheckBox deliriumCheckbox = new JCheckBox("Delirium");
    private final JCheckBox ultimatumCheckbox = new JCheckBox("Ultimatum");

    public NinjaOptionPanel() {
        for (PathOfExileLeague league : PathOfExileLeague.values()) leagueCombo.addItem(league);

        addHeader("Info");
        addComponent(new JLabel("Displays an overlay on the stash with pricing info from poe.ninja."));
        addComponent(new ErrorLabel("Set league in General > Path of Exile."));
        addComponent(new ComponentPair(new JLabel("Window Hotkey"), new ButtonWrapper(windowHotkeyButton)));
        addVerticalStrut();

        addHeader("Customize");
        addComponent(new ComponentPair(new JLabel("Path of Exile League"), leagueCombo));
        addVerticalStrut();

        addHeader("Enabled Tabs");
        addComponent(currencyCheckbox);
        addComponent(fragmentCheckbox);
        addComponent(essenceCheckbox);
        addComponent(delveCheckbox);
        addComponent(blightCheckbox);
        addComponent(deliriumCheckbox);
        addComponent(ultimatumCheckbox);

    }

    @Override
    public void save() {
        SaveManager.settingsSaveFile.data.pathOfExileLeague = (PathOfExileLeague) leagueCombo.getSelectedItem();
    }

    @Override
    public void load() {
        leagueCombo.setSelectedItem(SaveManager.settingsSaveFile.data.pathOfExileLeague);
    }

}
