package github.zmilla93.core.saving.savefiles;

import github.zmilla93.App;
import github.zmilla93.core.audio.Sound;
import github.zmilla93.core.audio.SoundComponent;
import github.zmilla93.core.data.CheatSheetData;
import github.zmilla93.core.data.PriceThresholdData;
import github.zmilla93.core.data.StashTabData;
import github.zmilla93.core.enums.*;
import github.zmilla93.core.hotkeys.HotkeyData;
import github.zmilla93.core.poe.GameDetectionMethod;
import github.zmilla93.core.utility.MacroButton;
import github.zmilla93.gui.components.MonitorInfo;
import github.zmilla93.gui.listening.IColorBlindChangeListener;
import github.zmilla93.gui.options.searching.StashSearchGroupData;
import github.zmilla93.gui.options.searching.StashSearchTermData;
import github.zmilla93.gui.options.searching.StashSearchWindowMode;
import github.zmilla93.modules.theme.Theme;
import github.zmilla93.modules.updater.data.AppVersion;
import org.jnativehook.keyboard.NativeKeyEvent;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

/**
 * Class representation of settings.json
 */
public class SettingsSaveFile extends AbstractSaveFile {

    public String appVersionString;
    private transient AppVersion appVersion;

    // General
    public boolean showGuildName;
    public boolean folderOffset;
    public boolean initializedFolderOffset;
    public HotkeyData quickPasteHotkey;
    public ArrayList<CheatSheetData> cheatSheets = new ArrayList<>();

    // Message Popups
    public boolean fadeMessages;
    public boolean useMessageTabs;
    public boolean collapseMessages;
    public int messageCountBeforeCollapse = SpinnerRange.MESSAGES_BEFORE_COLLAPSE.START;
    public float secondsBeforeFading = SpinnerRangeFloat.SECONDS_BEFORE_FADE.START;
    public int fadedOpacity = SliderRange.FADED_OPACITY.START;

    // Display
    public Theme theme;
    public String preferredFontName;
    public int fontSize = SpinnerRange.FONT_SIZE.START;
    public int iconSize = SpinnerRange.ICON_SIZE.START;
    public boolean colorBlindMode;
    private final transient ArrayList<IColorBlindChangeListener> colorBlindChangeListeners = new ArrayList<>();

    // History
    public HistoryOrder historyOrder = HistoryOrder.NEWEST_FIRST;
    public TimeFormat historyTimeFormat = TimeFormat.H12;
    public DateFormat historyDateFormat = DateFormat.MM_DD_YY;

    // Path of Exile 1 & 2 - Install Directory
    @Deprecated
    public String clientPath;
    public boolean hasInitializedGameDirectories;
    public boolean notInstalledPoe1;
    public boolean notInstalledPoe2;
    public String installFolderPoe1;
    public String installFolderPoe2;
    // Path of Exile 1 & 2 - Using Stash Folders
    public boolean hasInitializedUsingStashFolders = false;
    public boolean usingStashFoldersPoe1;
    public boolean usingStashFoldersPoe2;
    // TODO : League is not used yet
    // Path of Exile 1 & 2 - League
//    public PathOfExileLeague leaguePoe1 = PathOfExileLeague.UNSET;
//    public PathOfExileLeague leaguePoe2 = PathOfExileLeague.UNSET;
    // Path of Exile - Shared Between Games
    public GameDetectionMethod gameDetectionMethod = GameDetectionMethod.UNSET;
    public MonitorInfo selectedMonitor;
    public HotkeyData poeChatHotkey = new HotkeyData(NativeKeyEvent.VC_ENTER, 0);

    // Enable Features
    public MenubarStyle menubarStyle = MenubarStyle.ICON;
    public boolean menubarAlwaysExpanded = true;
    public boolean enableIncomingTrades = true;
    public boolean enableOutgoingTrades = true;
    public boolean enableItemHighlighter = true;
    //    public boolean enableMenuBar = true;
    public boolean enableAutomaticUpdate = true;
    public boolean hideWhenPOENotFocused = true;
    public boolean kalguurAutoClearTimers = true;

    // Stash
    public ArrayList<StashTabData> stashTabs = new ArrayList<>();
    public boolean applyStashColorToMessage;

    // Audio
    public SoundComponent incomingSound = new SoundComponent(new Sound("Ping 1", Sound.SoundType.INBUILT), 50);
    public SoundComponent outgoingSound = new SoundComponent(new Sound("Ping 1", Sound.SoundType.INBUILT), 50);
    public SoundComponent itemIgnoredSound = new SoundComponent(new Sound("Blip 2", Sound.SoundType.INBUILT), 50);
    public SoundComponent chatScannerSound = new SoundComponent(new Sound("Ping 2", Sound.SoundType.INBUILT), 50);
    public SoundComponent kalguurSound = new SoundComponent(new Sound("Ping 3", Sound.SoundType.INBUILT), 50);
    public SoundComponent playerJoinedAreaSound = new SoundComponent(new Sound("Blip 1", Sound.SoundType.INBUILT), 50);
    public SoundComponent updateSound = new SoundComponent(new Sound("Blip 3", Sound.SoundType.INBUILT), 50);
    public ArrayList<PriceThresholdData> priceThresholds = new ArrayList<>();
    public transient final HashMap<CurrencyType, ArrayList<PriceThresholdData>> priceThresholdMap = new HashMap<>();

    // Macros
    public ArrayList<MacroButton> incomingMacroButtons = new ArrayList<>();
    public ArrayList<MacroButton> outgoingMacroButtons = new ArrayList<>();
    public transient ArrayList<MacroButton> incomingTopMacros = new ArrayList<>();
    public transient ArrayList<MacroButton> incomingBottomMacros = new ArrayList<>();
    public transient ArrayList<MacroButton> outgoingTopMacros = new ArrayList<>();
    public transient ArrayList<MacroButton> outgoingBottomMacros = new ArrayList<>();

    // SlimTrade Hotkeys
    public HotkeyData optionsHotkey;
    public HotkeyData historyHotkey;
    public HotkeyData chatScannerHotkey;
    public HotkeyData closeTradeHotkey;
    public HotkeyData previousMessageTabHotkey;
    public HotkeyData nextMessageTabHotkey;
    public HotkeyData kalguurWindowHotkey;
    public HotkeyData ninjaWindowHotkey;
    public HotkeyData designerWindowHotkey;

    // POE Hotkeys
    public HotkeyData delveHotkey;
    public HotkeyData doNotDisturbHotkey;
    public HotkeyData exitToMenuHotkey;
    public HotkeyData guildHideoutHotkey;
    public HotkeyData hideoutHotkey;
    public HotkeyData kingsmarchHotkey;
    public HotkeyData leavePartyHotkey;
    public HotkeyData menagerieHotkey;
    public HotkeyData remainingMonstersHotkey;

    // Searching
    public HotkeyData stashSearchHotkey;
    public StashSearchWindowMode stashSearchWindowMode = StashSearchWindowMode.COMBINED;
    public ArrayList<StashSearchGroupData> stashSearchData = new ArrayList<>();

    // poe.ninja
//    public boolean ninjaEnableCurrencyTab = true;
//    public boolean ninjaEnableFragmentTab = true;
//    public boolean ninjaEnableEssenceTab = true;
//    public boolean ninjaEnableDelveTab = true;
//    public boolean ninjaEnableBlightTab = true;
//    public boolean ninjaEnableDeliriumTab = true;
//    public boolean ninjaEnableUltimatumTab = true;

    public SettingsSaveFile() {
        incomingMacroButtons.add(new MacroButton(CustomIcon.REFRESH, "Hi, do you still want to buy my {item} listed for {price}?", "", ButtonRow.TOP_ROW, null, false));
        incomingMacroButtons.add(new MacroButton(CustomIcon.INVITE, "/invite {player}", "", ButtonRow.BOTTOM_ROW, null, false));
        incomingMacroButtons.add(new MacroButton(CustomIcon.CART, "/tradewith {player}", "", ButtonRow.BOTTOM_ROW, null, false));
        incomingMacroButtons.add(new MacroButton(CustomIcon.THUMB, "thanks", "", ButtonRow.BOTTOM_ROW, null, false));
        incomingMacroButtons.add(new MacroButton(CustomIcon.LEAVE, "/kick {player}", "", ButtonRow.BOTTOM_ROW, null, true));

        outgoingMacroButtons.add(new MacroButton(CustomIcon.REFRESH, "{message}", "", ButtonRow.TOP_ROW, null, false));
        outgoingMacroButtons.add(new MacroButton(CustomIcon.WARP, "/hideout {player}", "", ButtonRow.BOTTOM_ROW, null, false));
        outgoingMacroButtons.add(new MacroButton(CustomIcon.THUMB, "thanks", "", ButtonRow.BOTTOM_ROW, null, false));
        outgoingMacroButtons.add(new MacroButton(CustomIcon.LEAVE, "/leave", "", ButtonRow.BOTTOM_ROW, null, false));
        outgoingMacroButtons.add(new MacroButton(CustomIcon.HOME, "/hideout", "", ButtonRow.BOTTOM_ROW, null, true));

        ArrayList<StashSearchTermData> stashTerms = new ArrayList<>();
        stashTerms.add(new StashSearchTermData("map", "maps", 5));
        stashTerms.add(new StashSearchTermData("div", "divination card", 25));
        stashTerms.add(new StashSearchTermData("sac", "sacrifice at", 7));
        stashTerms.add(new StashSearchTermData("ess", "essence", 15));
        stashTerms.add(new StashSearchTermData("$$$", "currency", 22));
        stashTerms.add(new StashSearchTermData("belt", "mageblood", 4));
        ArrayList<StashSearchTermData> passiveTerms = new ArrayList<>();
        passiveTerms.add(new StashSearchTermData("str", "strength", 5));
        passiveTerms.add(new StashSearchTermData("dex", "dexterity", 17));
        passiveTerms.add(new StashSearchTermData("int", "intelligence", 14));
        passiveTerms.add(new StashSearchTermData("crit chance", "critical strike chance", 24));
        passiveTerms.add(new StashSearchTermData("crit mult", "critical strike multiplier", 22));
        stashSearchData.add(new StashSearchGroupData(1, "Stash", null, stashTerms));
        stashSearchData.add(new StashSearchGroupData(2, "Passives", null, passiveTerms));
    }

    // Macro Generators
    public void buildMacroCache() {
        incomingTopMacros.clear();
        incomingBottomMacros.clear();
        outgoingTopMacros.clear();
        outgoingBottomMacros.clear();
        for (MacroButton button : incomingMacroButtons) {
            if (button.row == ButtonRow.TOP_ROW) {
                incomingTopMacros.add(button);
            } else {
                incomingBottomMacros.add(button);
            }
        }
        for (MacroButton button : outgoingMacroButtons) {
            if (button.row == ButtonRow.TOP_ROW) {
                outgoingTopMacros.add(button);
            } else {
                outgoingBottomMacros.add(button);
            }
        }
    }

    public void buildThresholdMap() {
        priceThresholdMap.clear();
        for (PriceThresholdData data : priceThresholds) {
            CurrencyType currency = CurrencyType.getCurrencyType(data.currencyType.ID);
            ArrayList<PriceThresholdData> thresholds = priceThresholdMap.get(currency);
            if (thresholds == null) {
                thresholds = new ArrayList<>();
            }
            thresholds.add(data);
            priceThresholdMap.put(currency, thresholds);
        }
        for (ArrayList<PriceThresholdData> thresholds : priceThresholdMap.values()) {
            thresholds.sort(Collections.reverseOrder());
        }
    }

    public void triggerColorBlindModeChange(boolean colorBlindMode) {
        for (IColorBlindChangeListener listener : colorBlindChangeListeners) {
            listener.onColorBlindChange(colorBlindMode);
        }
    }

    public void addColorBlindListener(IColorBlindChangeListener listener) {
        if (colorBlindChangeListeners.contains(listener)) return;
        colorBlindChangeListeners.add(listener);
    }

    public void removeColorBlindListener(IColorBlindChangeListener listener) {
        colorBlindChangeListeners.remove(listener);
    }

    public AppVersion appVersion() {
        if (appVersion == null) {
            appVersion = new AppVersion(appVersionString);
            if (!appVersion.valid) appVersion = new AppVersion(App.getAppInfo().appVersion.toString());
        }
        return appVersion;
    }

    @Override
    public int getCurrentTargetVersion() {
        return 3;
    }

}
