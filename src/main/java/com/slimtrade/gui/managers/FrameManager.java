package com.slimtrade.gui.managers;

import com.slimtrade.App;
import com.slimtrade.core.data.CheatSheetData;
import com.slimtrade.core.enums.AppState;
import com.slimtrade.core.enums.MenubarStyle;
import com.slimtrade.core.managers.SaveManager;
import com.slimtrade.gui.chatscanner.ChatScannerWindow;
import com.slimtrade.gui.development.DesignerConfigWindow;
import com.slimtrade.gui.development.StashAlignmentDesignerWindow;
import com.slimtrade.gui.listening.IDefaultSizeAndLocation;
import com.slimtrade.gui.menubar.MenubarButtonDialog;
import com.slimtrade.gui.menubar.MenubarDialog;
import com.slimtrade.gui.ninja.NinjaWindow;
import com.slimtrade.gui.options.ignore.ItemIgnoreWindow;
import com.slimtrade.gui.options.searching.StashSearchGroupData;
import com.slimtrade.gui.options.searching.StashSearchWindow;
import com.slimtrade.gui.options.searching.StashSearchWindowMode;
import com.slimtrade.gui.overlays.MenubarOverlay;
import com.slimtrade.gui.overlays.MessageOverlay;
import com.slimtrade.gui.overlays.OverlayInfoDialog;
import com.slimtrade.gui.pinning.IPinnable;
import com.slimtrade.gui.pinning.PinManager;
import com.slimtrade.gui.setup.SetupWindow;
import com.slimtrade.gui.stash.StashHelperContainer;
import com.slimtrade.gui.windows.*;
import com.slimtrade.gui.windows.test.MessageTestWindow;

import javax.swing.*;
import java.awt.*;
import java.util.HashMap;
import java.util.HashSet;

public class FrameManager {

    // Windows
    public static DebugWindow debugWindow;
    public static MessageManager messageManager;
    public static OptionsWindow optionsWindow;
    public static HistoryWindow historyWindow;
    public static ChatScannerWindow chatScannerWindow;
    public static ItemIgnoreWindow itemIgnoreWindow;
    public static KalguurHelperWindow kalguurHelperWindow;
    public static NinjaWindow ninjaWindow;
    public static TutorialWindow tutorialWindow;
    public static StashAlignmentDesignerWindow stashAlignmentDesignerWindow;
    public static DesignerConfigWindow designerConfigWindow;
    public static PatchNotesWindow patchNotesWindow;
    public static HashMap<String, CheatSheetWindow> cheatSheetWindows = new HashMap<>();
    public static HashMap<String, StashSearchWindow> searchWindows = new HashMap<>();
    public static StashSearchWindow combinedSearchWindow;
    public static SetupWindow setupWindow;
    public static UpdateProgressWindow updateProgressWindow;

    // Debug Windows
    public static MessageTestWindow debugMessageWindow;

    // Overlays
    public static DummyWindow dummyWindow;
    public static OverlayInfoDialog overlayInfoWindow;
    public static MessageOverlay messageOverlay;
    public static MenubarOverlay menubarOverlay;
    public static StashGridWindow stashGridWindow;
    public static StashHelperContainer stashHelperContainer;

    // Menubar
    public static MenubarButtonDialog menubarIcon;
    public static MenubarDialog menubarDialog;

    private static final HashMap<AppState, Window[]> windowMap = new HashMap<>();
    private static final HashMap<AppState, Boolean[]> windowVisibilityMap = new HashMap<>();
    private static IDefaultSizeAndLocation[] defaultSizeAndLocationWindows;

    private static boolean menubarExpanded = false;
    private static boolean initialized = false;

    public static void init() {
        // Windows
        // FIXME : Remove debug window
        debugWindow = new DebugWindow();
        stashHelperContainer = new StashHelperContainer();
        messageManager = new MessageManager();
        optionsWindow = new OptionsWindow();
        historyWindow = new HistoryWindow();
        chatScannerWindow = new ChatScannerWindow();
        itemIgnoreWindow = new ItemIgnoreWindow();
        kalguurHelperWindow = new KalguurHelperWindow();
        ninjaWindow = new NinjaWindow();
        tutorialWindow = new TutorialWindow();
        patchNotesWindow = new PatchNotesWindow();
        if (App.debug) {
            stashAlignmentDesignerWindow = new StashAlignmentDesignerWindow();
            designerConfigWindow = new DesignerConfigWindow();
        }

        setupWindow = new SetupWindow();

        // Overlays
        overlayInfoWindow = new OverlayInfoDialog();
        messageOverlay = new MessageOverlay();
        menubarOverlay = new MenubarOverlay();
        stashGridWindow = new StashGridWindow();

        dummyWindow = new DummyWindow(); // Omitted from visibility list

        // Menubar
        menubarIcon = new MenubarButtonDialog();
        menubarDialog = new MenubarDialog();

        buildCheatSheetWindows();
        buildSearchWindows();

        // FIXME : Add all windows
        // FIXME: Check CheatSheet windows and StashSearch windows.
        // Group windows that need to be shown/hidden during state changes
        Window[] runningWindows = new Window[]{messageManager, optionsWindow, historyWindow, chatScannerWindow, menubarIcon, menubarDialog, stashHelperContainer, tutorialWindow, patchNotesWindow};
        Window[] stashWindows = new Window[]{stashGridWindow};
        Window[] setupWindows = new Window[]{setupWindow};
        Window[] overlayWindows = new Window[]{overlayInfoWindow, messageOverlay, menubarOverlay};
        defaultSizeAndLocationWindows = new IDefaultSizeAndLocation[]{optionsWindow, historyWindow, chatScannerWindow, tutorialWindow, patchNotesWindow};

        // Matching boolean array so running remember previous visibility.
        Boolean[] runningWindowsVisibility = new Boolean[runningWindows.length];

        // Throw the data into maps for ease of use
        windowMap.put(AppState.RUNNING, runningWindows);
        windowMap.put(AppState.EDIT_OVERLAY, overlayWindows);
        windowMap.put(AppState.EDIT_STASH, stashWindows);
        windowMap.put(AppState.SETUP, setupWindows);
        windowVisibilityMap.put(AppState.RUNNING, runningWindowsVisibility);
        for (IDefaultSizeAndLocation window : defaultSizeAndLocationWindows) {
            window.applyDefaultSizeAndLocation();
        }
        // Debug
        if (App.messageUITest) debugMessageWindow = new MessageTestWindow();
        initialized = true;
    }

    public static boolean hasBeenInitialized() {
        return initialized;
    }

    public static void showAppFrames() {
        // FIXME: Show proper windows
        if (App.showOptionsOnLaunch) optionsWindow.setVisible(true);
        messageManager.setVisible(true);
        updateMenubarVisibility();
        // FIXME : Temp
//        kalguurCalculatorWindow.setVisible(true);
    }

    public static void setWindowVisibility(AppState newState) {
        assert (SwingUtilities.isEventDispatchThread());
        Window[] windows = windowMap.get(App.getState());
        Boolean[] windowVisibility = windowVisibilityMap.get(App.getState());
        // Hide current Windows
        if (windows != null) {
            for (int i = 0; i < windows.length; i++) {
                if (windowVisibility != null) {
                    windowVisibility[i] = windows[i].isVisible();
                }
                windows[i].setVisible(false);
            }
        }
        // Show Windows
        windows = windowMap.get(newState);
        windowVisibility = windowVisibilityMap.get(newState);
        if (windows != null) {
            for (int i = 0; i < windows.length; i++) {
                if (windowVisibility != null) {
                    windows[i].setVisible(windowVisibility[i]);
                } else {
                    windows[i].setVisible(true);
                }
            }
        }
        if (newState == AppState.RUNNING) SystemTrayManager.showDefault();
        else SystemTrayManager.showSimple();
        App.setState(newState);
    }

    public static void buildCheatSheetWindows() {
        HashSet<String> openWindows = new HashSet<>();
        for (CheatSheetWindow window : cheatSheetWindows.values()) {
            if (window.isVisible()) openWindows.add(window.getPinTitle());
            PinManager.removePinnable(window);
            window.dispose();
        }
        cheatSheetWindows.clear();
        for (CheatSheetData data : SaveManager.settingsSaveFile.data.cheatSheets) {
            CheatSheetWindow window = CheatSheetWindow.createCheatSheet(data);
            if (window != null) cheatSheetWindows.put(data.title(), window);
        }
        PinManager.applyCheatSheetPins();
        for (CheatSheetWindow window : cheatSheetWindows.values()) {
            if (openWindows.contains(window.getPinTitle())) window.setVisible(true);
        }
    }

    public static void buildSearchWindows() {
        assert (SwingUtilities.isEventDispatchThread());
        // Dispose of existing windows
        HashSet<String> openWindows = new HashSet<>();
        for (StashSearchWindow window : searchWindows.values()) {
            if (window.isVisible()) openWindows.add(window.getPinTitle());
            window.dispose();
        }
        if (combinedSearchWindow != null) {
            if (combinedSearchWindow.isVisible()) openWindows.add(combinedSearchWindow.getPinTitle());
            combinedSearchWindow.dispose();
        }
        searchWindows.clear();
        // Build new window(s)
        StashSearchWindowMode windowMode = SaveManager.settingsSaveFile.data.stashSearchWindowMode;
        if (windowMode == StashSearchWindowMode.COMBINED) {
            combinedSearchWindow = new StashSearchWindow(SaveManager.settingsSaveFile.data.stashSearchData);
        } else if (windowMode == StashSearchWindowMode.SEPARATE) {
            for (StashSearchGroupData group : SaveManager.settingsSaveFile.data.stashSearchData) {
                StashSearchWindow window = new StashSearchWindow(group);
                searchWindows.put(group.getPinTitle(), window);
            }
        }
        // Apply pins and visibility
        PinManager.applySearchWindowPins();
        if (windowMode == StashSearchWindowMode.COMBINED) {
            if (openWindows.contains(combinedSearchWindow.getPinTitle())) combinedSearchWindow.setVisible(true);
        } else if (windowMode == StashSearchWindowMode.SEPARATE) {
            for (StashSearchWindow window : searchWindows.values()) {
                if (openWindows.contains(window.getPinTitle())) window.setVisible(true);
            }
        }
    }

    public static void checkMenubarVisibility(Point point) {
        if (SaveManager.settingsSaveFile.data.menubarStyle == MenubarStyle.DISABLED) return;
        if (menubarExpanded) {
            if (!menubarDialog.getBufferedBounds().contains(point)) {
                menubarExpanded = false;
                updateMenubarVisibility();
            }
        } else {
            if (menubarIcon.getBufferedBounds().contains(point)) {
                menubarExpanded = true;
                updateMenubarVisibility();
            }
        }
    }

    public static void updateMenubarVisibility() {
        SwingUtilities.invokeLater(() -> {
            if (SaveManager.settingsSaveFile.data.menubarStyle == MenubarStyle.DISABLED) {
                menubarDialog.setVisible(false);
                menubarIcon.setVisible(false);
            } else if (SaveManager.settingsSaveFile.data.menubarAlwaysExpanded || menubarExpanded) {
                menubarDialog.setVisible(true);
                menubarIcon.setVisible(false);
            } else {
                menubarIcon.setVisible(true);
                menubarDialog.setVisible(false);
            }
        });
    }

    public static void displayUpdateAvailable() {
        SwingUtilities.invokeLater(() -> {
            FrameManager.optionsWindow.showUpdateButton();
            FrameManager.messageManager.addUpdateMessage(true);
        });
    }

    public static void requestRestoreUIDefaults() {
        assert SwingUtilities.isEventDispatchThread();
        int result = JOptionPane.showConfirmDialog(optionsWindow,
                "Are you sure you want to reset the UI to its default state?\n" +
                        "This will clear all pins and reset the size and location of most windows.",
                "Reset SlimTrade UI", JOptionPane.YES_NO_OPTION);
        if (result == JOptionPane.OK_OPTION) {
            restoreUIDefaults();
        }
    }

    private static void restoreUIDefaults() {
        assert SwingUtilities.isEventDispatchThread();
        for (IDefaultSizeAndLocation window : defaultSizeAndLocationWindows) {
            if (window instanceof IPinnable) {
                ((IPinnable) window).unpin();
            }
            window.applyDefaultSizeAndLocation();
        }
        for (CheatSheetWindow window : cheatSheetWindows.values()) {
            window.unpin();
            window.applyDefaultSizeAndLocation();
        }
        if (combinedSearchWindow != null) {
            combinedSearchWindow.unpin();
            combinedSearchWindow.applyDefaultSizeAndLocation();
        }
        for (StashSearchWindow window : searchWindows.values()) {
            window.unpin();
            window.applyDefaultSizeAndLocation();
        }
        overlayInfoWindow.restoreDefaults();
        SaveManager.overlaySaveFile.saveToDisk();
    }

}
