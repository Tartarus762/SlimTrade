package com.slimtrade.gui.managers;

import com.slimtrade.core.data.CheatSheetData;
import com.slimtrade.core.hotkeys.*;
import com.slimtrade.core.managers.QuickPasteManager;
import com.slimtrade.core.managers.SaveManager;
import com.slimtrade.gui.options.searching.StashSearchWindowMode;
import com.slimtrade.gui.windows.CheatSheetWindow;
import org.jnativehook.keyboard.NativeKeyEvent;

import java.util.HashMap;

/**
 * To add a new hotkey type, implement IHotkeyAction then register with a HotkeyData object in loadHotkeys.
 * Notification Panel hotkeys are handled in MessageManager, but keystroke data is still sent from here.
 */
public class HotkeyManager {

    private static final HashMap<HotkeyData, IHotkeyAction> hotkeyMap = new HashMap<>();

    public static void loadHotkeys() {
        hotkeyMap.clear();
        // SlimTrade
        registerHotkey(SaveManager.settingsSaveFile.data.optionsHotkey, new AppHotkey(FrameManager.optionsWindow));
        registerHotkey(SaveManager.settingsSaveFile.data.historyHotkey, new AppHotkey(FrameManager.historyWindow));
        // TODO : Stash Search Hotkeys
//        registerHotkey(SaveManager.settingsSaveFile.data.stashSortHotkey, new AppHotkey(FrameManager.stashSortingWindow));
        registerHotkey(SaveManager.settingsSaveFile.data.chatScannerHotkey, new AppHotkey(FrameManager.chatScannerWindow));
        registerHotkey(SaveManager.settingsSaveFile.data.changeCharacterHotkey, new ChangeCharacterHotkey());
        registerHotkey(SaveManager.settingsSaveFile.data.closeTradeHotkey, new CloseOldestTradeHotkey());
        // POE
        registerHotkey(SaveManager.settingsSaveFile.data.delveHotkey, new PoeHotkey("/delve"));
        registerHotkey(SaveManager.settingsSaveFile.data.doNotDisturbHotkey, new PoeHotkey("/dnd"));
        registerHotkey(SaveManager.settingsSaveFile.data.exitToMenuHotkey, new PoeHotkey("/exit"));
        registerHotkey(SaveManager.settingsSaveFile.data.guildHideoutHotkey, new PoeHotkey("/guild"));
        registerHotkey(SaveManager.settingsSaveFile.data.hideoutHotkey, new PoeHotkey("/hideout"));
        if (SaveManager.settingsSaveFile.data.characterName != null)
            registerHotkey(SaveManager.settingsSaveFile.data.leavePartyHotkey, new PoeHotkey("/kick " + SaveManager.settingsSaveFile.data.characterName));
        registerHotkey(SaveManager.settingsSaveFile.data.menagerieHotkey, new PoeHotkey("/menagerie"));
        registerHotkey(SaveManager.settingsSaveFile.data.metamorphHotkey, new PoeHotkey("/metamorph"));
        registerHotkey(SaveManager.settingsSaveFile.data.remainingMonstersHotkey, new PoeHotkey("/remaining"));
        // Stash Searching
        if (SaveManager.settingsSaveFile.data.stashSearchWindowMode == StashSearchWindowMode.COMBINED) {
            registerHotkey(SaveManager.settingsSaveFile.data.stashSearchHotkey, new AppHotkey(FrameManager.searchWindow));
        } else {
            // TODO : Separate stash searching hotkeys
        }
        // Quick Paste
        if (SaveManager.settingsSaveFile.data.quickPasteMode == QuickPasteManager.QuickPasteMode.HOTKEY)
            registerHotkey(SaveManager.settingsSaveFile.data.quickPasteHotkey, new QuickPasteHotkey());
        // Cheat Sheets
        for (CheatSheetData cheatSheetData : SaveManager.settingsSaveFile.data.cheatSheets) {
            CheatSheetWindow window = FrameManager.cheatSheetWindows.get(cheatSheetData.title);
            registerHotkey(cheatSheetData.hotkeyData, new CheatSheetHotkey(window));
        }
    }

    private static void registerHotkey(HotkeyData hotkeyData, IHotkeyAction action) {
        if (hotkeyData == null) return;
        // FIXME : Should inform user of duplicate hotkeys
        if (hotkeyMap.containsKey(hotkeyData)) return;   // Duplicate hotkeys are ignored
        hotkeyMap.put(hotkeyData, action);
    }

    public static void processHotkey(NativeKeyEvent e) {
        HotkeyData data = new HotkeyData(e.getKeyCode(), e.getModifiers());
        IHotkeyAction hotkeyAction = hotkeyMap.get(data);
        if (hotkeyAction == null) {
            FrameManager.messageManager.checkHotkey(data);
        } else {
            hotkeyAction.execute();
        }
    }

}
