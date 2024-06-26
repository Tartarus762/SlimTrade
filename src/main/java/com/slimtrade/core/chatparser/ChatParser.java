package com.slimtrade.core.chatparser;

import com.slimtrade.App;
import com.slimtrade.core.References;
import com.slimtrade.core.data.IgnoreItemData;
import com.slimtrade.core.data.PlayerMessage;
import com.slimtrade.core.managers.AudioManager;
import com.slimtrade.core.managers.SaveManager;
import com.slimtrade.core.trading.LangRegex;
import com.slimtrade.core.trading.TradeOffer;
import com.slimtrade.core.trading.TradeOfferType;
import com.slimtrade.gui.chatscanner.ChatScannerEntry;
import com.slimtrade.gui.managers.FrameManager;
import com.slimtrade.modules.filetailing.FileTailer;
import com.slimtrade.modules.filetailing.FileTailerListener;
import com.slimtrade.modules.updater.ZLogger;

import javax.swing.*;
import java.io.File;
import java.util.ArrayList;
import java.util.regex.Matcher;

public class ChatParser implements FileTailerListener {

    private FileTailer tailer;
    public static final int tailerDelayMS = 250;

    // Listeners
    private final ArrayList<IParserInitListener> onInitListeners = new ArrayList<>();
    private final ArrayList<IParserLoadedListener> onLoadListeners = new ArrayList<>();
    private final ArrayList<IPreloadTradeListener> preloadTradeListeners = new ArrayList<>();
    private final ArrayList<ITradeListener> tradeListeners = new ArrayList<>();
    private final ArrayList<IJoinedAreaListener> joinedAreaListeners = new ArrayList<>();

    // State
    private String currentZone = "The Twilight Strand";
    private boolean open;
    private String path;
    private boolean end;
    private int lineCount;
    private int whisperCount;

    // FIXME: Should remove end option from chat parser (keep on file tailer)
    public void open(String path, boolean end) {
        this.end = end;
        lineCount = 0;
        whisperCount = 0;
        if (open) close();
        if (path == null) return;
        File clientFile = new File(path);
        if (clientFile.exists() && clientFile.isFile()) {
            this.path = path;
            tailer = FileTailer.createTailer(clientFile, this, tailerDelayMS, end);
            open = true;
        }
    }

    public void close() {
        tailer.stop();
        path = null;
        open = false;
    }

    public String getPath() {
        return path;
    }

    public void parseLine(String line) {
        if (!open) return;
        lineCount++;
        if (line.contains("@")) {
            whisperCount++;
            TradeOffer tradeOffer = TradeOffer.getTradeFromMessage(line);
            if (tradeOffer != null) {
                handleTradeOffer(tradeOffer);
                return;
            }
        }
        if (tailer.isLoaded()) {
            // Chat Scanner
            handleChatScanner(line);
            // Player Joined Area
            for (LangRegex lang : LangRegex.values()) {
                if (lang.joinedArea == null) continue;
                Matcher matcher = lang.joinedAreaPattern.matcher(line);
                if (matcher.matches()) {
                    String playerName = matcher.group("playerName");
                    for (IJoinedAreaListener listener : joinedAreaListeners) {
                        listener.onJoinedArea(playerName);
                    }
                    return;
                }
            }
        }
        // Zone Tracking
        for (LangRegex lang : LangRegex.values()) {
            if (lang.enteredArea == null) continue;
            Matcher matcher = lang.enteredAreaPattern.matcher(line);
            if (matcher.matches()) {
                currentZone = matcher.group("zone");
                return;
            }
        }
    }

    private void handleChatScanner(String line) {
        Matcher chatMatcher = References.chatPatten.matcher(line);
        Matcher metaMatcher = References.clientMetaPattern.matcher(line);
        String message;
        String messageType;
        String player;
        if (chatMatcher.matches()) {
            message = chatMatcher.group("message");
            messageType = chatMatcher.group("messageType");
            player = chatMatcher.group("playerName");
        } else if (metaMatcher.matches()) {
            message = metaMatcher.group("message");
            messageType = "meta";
            player = "Meta Text";
        } else {
            return;
        }
        if (App.chatInConsole)
            System.out.println(chatMatcher.group("playerName") + ": " + chatMatcher.group("message"));
        if (messageType == null) return;
        message = message.toLowerCase();
        messageType = messageType.toLowerCase();
        if (SaveManager.chatScannerSaveFile.data.searching) {
            // Iterate though all active searches and look for matching phrases
            for (ChatScannerEntry entry : SaveManager.chatScannerSaveFile.data.activeSearches) {
                if (entry.getSearchTerms() == null) continue;
                for (String term : entry.getSearchTerms()) {
                    if (message.contains(term)) {
                        boolean allow = false;
                        boolean ignore = false;
                        // Check if this message should be ignored
                        if (entry.getIgnoreTerms() != null) {
                            for (String ignoreTerm : entry.getIgnoreTerms()) {
                                if (message.contains(ignoreTerm)) {
                                    ignore = true;
                                    break;
                                }
                            }
                        }
                        // Verify the message is allowed
                        if (entry.allowGlobalAndTradeChat && (messageType.equals("#") || messageType.equals("$")))
                            allow = true;
                        if (entry.allowWhispers) {
                            for (LangRegex lang : LangRegex.values()) {
                                if (lang.messageFrom == null) continue;
                                if (messageType.contains(lang.messageFrom)) {
                                    allow = true;
                                    break;
                                }
                            }
                        }
                        if (entry.allowMetaText && messageType.equals("meta")) allow = true;
                        if (ignore || !allow) continue;
                        String finalMessage = message;
                        SwingUtilities.invokeLater(() -> FrameManager.messageManager.addScannerMessage(entry, new PlayerMessage(player, finalMessage)));
                        return;
                    }
                }
            }
        }

    }

    private void handleTradeOffer(TradeOffer offer) {
        // Check if trade should be ignored
        String itemNameLower = offer.itemName.toLowerCase();
        if (offer.offerType == TradeOfferType.INCOMING_TRADE) {
            IgnoreItemData item = SaveManager.ignoreSaveFile.data.exactMatchIgnoreMap.get(itemNameLower);
            if (item != null && !item.isExpired()) {
                handleIgnoreItem();
                return;
            }
            for (IgnoreItemData ignoreItemData : SaveManager.ignoreSaveFile.data.containsTextIgnoreList) {
                if (itemNameLower.contains(ignoreItemData.itemNameLower()) && !ignoreItemData.isExpired()) {
                    handleIgnoreItem();
                    return;
                }
            }
        }
        // Handle trade
        if (tailer.isLoaded()) {
            for (ITradeListener listener : tradeListeners) {
                listener.handleTrade(offer);
            }
        } else {
            for (IPreloadTradeListener listener : preloadTradeListeners) {
                listener.handlePreloadTrade(offer);
            }
        }
    }

    private void handleIgnoreItem() {
        if (tailer.isLoaded()) AudioManager.playSoundComponent(SaveManager.settingsSaveFile.data.itemIgnoredSound);
    }

    public String getCurrentZone() {
        return currentZone;
    }

    // Listeners
    public void addOnInitCallback(IParserInitListener listener) {
        onInitListeners.add(listener);
    }

    public void addOnLoadedCallback(IParserLoadedListener listener) {
        onLoadListeners.add(listener);
    }

    public void addPreloadTradeListener(IPreloadTradeListener listener) {
        preloadTradeListeners.add(listener);
    }

    public void addTradeListener(ITradeListener listener) {
        tradeListeners.add(listener);
    }

    public void addJoinedAreaListener(IJoinedAreaListener listener) {
        joinedAreaListeners.add(listener);
    }

    public void removeAllListeners() {
        onInitListeners.clear();
        onLoadListeners.clear();
        preloadTradeListeners.clear();
        tradeListeners.clear();
        joinedAreaListeners.clear();
    }

    // File Tailing

    @Override
    public void init(FileTailer tailer) {
        for (IParserInitListener listener : onInitListeners) listener.onParserInit();
    }

    @Override
    public void fileNotFound() {

    }

    @Override
    public void fileRotated() {

    }

    @Override
    public void onLoad() {
        ZLogger.log("Chat parser loaded. Found " + lineCount + " lines and " + whisperCount + " whispers.");
        for (IParserLoadedListener listener : onLoadListeners) listener.onParserLoaded();
    }

    @Override
    public void handle(String line) {
        parseLine(line);
    }

}
