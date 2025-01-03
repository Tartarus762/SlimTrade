package github.zmilla93.gui.options;

import github.zmilla93.core.data.CheatSheetData;
import github.zmilla93.core.hotkeys.HotkeyData;
import github.zmilla93.core.managers.SaveManager;
import github.zmilla93.core.utility.ZUtil;
import github.zmilla93.gui.components.ButtonWrapper;
import github.zmilla93.gui.components.ComponentPanel;
import github.zmilla93.gui.components.StyledLabel;
import github.zmilla93.gui.managers.FrameManager;
import github.zmilla93.gui.options.cheatsheets.CheatSheetComponentGroup;
import github.zmilla93.modules.saving.ISavable;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.util.ArrayList;

public class CheatSheetsOptionPanel extends AbstractOptionPanel implements ISavable {

    private final JButton browseButton = new JButton("Open Images Folder");
    private final JButton refreshButton = new JButton("Refresh");
    //    private final AddRemoveContainer<CheatSheetRow> cheatSheetContainer = new AddRemoveContainer<>();
    private final JPanel cheatSheetContainer = new JPanel();
    private final ArrayList<CheatSheetComponentGroup> rows = new ArrayList<>();
    private GridBagConstraints gc;
    private boolean forceRebuild = false;

    public CheatSheetsOptionPanel() {
        // Setup
        ComponentPanel buttonPanel = new ComponentPanel();
        buttonPanel.add(browseButton);
        buttonPanel.add(refreshButton);
        cheatSheetContainer.setLayout(new GridBagLayout());

        // Build
        addHeader("Setup");
        addComponent(new JLabel("Add images to the images folder, refresh, set a hotkey, then save."));
        addComponent(new JLabel("Hold SHIFT when moving a window to lock it to the current monitor."));
        addComponent(new StyledLabel("Supports png, jpg, and gif files.").bold());
        addComponent(buttonPanel);
        addVerticalStrut();
        addHeader("Cheat Sheets");
        addComponent(cheatSheetContainer);
        addListeners();
    }

    private void startRebuild() {
        gc = ZUtil.getGC();
        gc.weightx = 1;
        gc.fill = GridBagConstraints.HORIZONTAL;
        rows.clear();
        cheatSheetContainer.removeAll();
    }

    private void addRow(CheatSheetData data) {
        CheatSheetComponentGroup row = new CheatSheetComponentGroup(data);

        cheatSheetContainer.add(row.label, gc);
        gc.gridx++;
        cheatSheetContainer.add(Box.createHorizontalStrut(20), gc);
        gc.gridx++;
        cheatSheetContainer.add(new ButtonWrapper(row.hotkeyButton), gc);
        gc.gridx = 0;
        gc.gridy++;

        rows.add(row);
    }

    private void rebuildFromFolder() {
        startRebuild();
        File imagesFolder = SaveManager.getImagesDirectory().toFile();
        if (imagesFolder.exists() && imagesFolder.isDirectory()) {
            File[] files = imagesFolder.listFiles();
            if (files == null) return;
            for (File file : files) {
                HotkeyData hotkeyData = null;
                for (CheatSheetData existing : SaveManager.settingsSaveFile.data.cheatSheets) {
                    if (existing.fileName.equals(file.getName())) {
                        hotkeyData = existing.hotkeyData;
                        break;
                    }
                }
                CheatSheetData data = new CheatSheetData(file.getName(), hotkeyData);
                if (!data.hasImageExtension()) continue;
                addRow(data);
            }
        }
        forceRebuild = true;
        cheatSheetContainer.revalidate();
        cheatSheetContainer.repaint();
    }

    private void addListeners() {
        browseButton.addActionListener(e -> ZUtil.openExplorer(SaveManager.getImagesDirectory().toString()));
        refreshButton.addActionListener(e -> rebuildFromFolder());
    }

    @Override
    public void save() {
        ArrayList<CheatSheetData> cheatSheetData = new ArrayList<>();
        int matchingWindows = 0;
        for (CheatSheetComponentGroup row : rows) {
            CheatSheetData data = row.getData();
            cheatSheetData.add(data);
            if (FrameManager.cheatSheetWindows.containsKey(data.title())) matchingWindows++;
        }
        SaveManager.settingsSaveFile.data.cheatSheets = cheatSheetData;
        // Don't rebuild cheat sheets if files have not changed
        if (matchingWindows != FrameManager.cheatSheetWindows.size() || matchingWindows != rows.size() || forceRebuild) {
            FrameManager.rebuildCheatSheetWindows();
        }
        forceRebuild = false;
    }

    @Override
    public void load() {
        startRebuild();
        for (CheatSheetData data : SaveManager.settingsSaveFile.data.cheatSheets) {
            addRow(data);
        }
    }

}
