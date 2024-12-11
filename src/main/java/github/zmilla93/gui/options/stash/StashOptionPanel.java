package github.zmilla93.gui.options.stash;

import github.zmilla93.core.CommonText;
import github.zmilla93.core.data.StashTabData;
import github.zmilla93.core.managers.SaveManager;
import github.zmilla93.gui.components.AddRemoveContainer;
import github.zmilla93.gui.options.AbstractOptionPanel;
import github.zmilla93.modules.saving.ISavable;

import javax.swing.*;
import java.util.ArrayList;

public class StashOptionPanel extends AbstractOptionPanel implements ISavable {

    private final JButton addButton = new JButton("Add Stash Tab");
    private final AddRemoveContainer<StashRow> tabContainer = new AddRemoveContainer<>();
    private final JCheckBox applyColorCheckbox = new JCheckBox("Also apply color to the trade notification panel.");

    public StashOptionPanel() {
        addHeader("Info");
        addComponent(new JLabel("Add stash tab names to apply a color to the item highlighter or mark quad tabs."));
        addComponent(new JLabel(CommonText.DEFAULT_WHITE_TEXT));
        addComponent(applyColorCheckbox);
        addVerticalStrut();
        addHeader("Stash Tab List");
        addComponent(addButton);
        addComponent(tabContainer);
        addListeners();
    }

    private void addListeners() {
        addButton.addActionListener(e -> {
            tabContainer.add(new StashRow());
            revalidate();
            repaint();
        });
    }

    @Override
    public void save() {
        ArrayList<StashTabData> stashTabs = new ArrayList<>();
        for (StashRow row : tabContainer.getComponentsTyped()) {
            stashTabs.add(row.getData());
        }
        SaveManager.settingsSaveFile.data.stashTabs = stashTabs;
        SaveManager.settingsSaveFile.data.applyStashColorToMessage = applyColorCheckbox.isSelected();
    }

    @Override
    public void load() {
        tabContainer.removeAll();
        for (StashTabData data : SaveManager.settingsSaveFile.data.stashTabs) {
            StashRow row = new StashRow();
            row.setData(data);
            tabContainer.add(row);
        }
        applyColorCheckbox.setSelected(SaveManager.settingsSaveFile.data.applyStashColorToMessage);
        revalidate();
        repaint();
    }

}
