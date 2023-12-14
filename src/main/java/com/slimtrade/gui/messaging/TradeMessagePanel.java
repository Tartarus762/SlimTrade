package com.slimtrade.gui.messaging;

import com.slimtrade.core.data.PasteReplacement;
import com.slimtrade.core.enums.StashTabColor;
import com.slimtrade.core.managers.SaveManager;
import com.slimtrade.core.trading.TradeOffer;
import com.slimtrade.core.trading.TradeOfferType;
import com.slimtrade.core.utility.AdvancedMouseListener;
import com.slimtrade.modules.theme.ThemeManager;
import com.slimtrade.gui.components.CurrencyLabelFactory;
import com.slimtrade.gui.managers.FrameManager;
import com.slimtrade.gui.stash.StashHelperPanel;
import com.slimtrade.gui.stash.StashHelperWrapper;
import com.slimtrade.modules.theme.components.PassThroughPanel;

import javax.swing.*;
import java.awt.event.MouseEvent;

public class TradeMessagePanel extends NotificationPanel {

    private StashHelperPanel stashHelperPanel;
    private StashHelperWrapper stashHelperWrapper;

    public TradeMessagePanel(TradeOffer offer) {
        this(offer, true);
    }

    public TradeMessagePanel(TradeOffer tradeOffer, boolean createListeners) {
        super(createListeners);
        this.tradeOffer = tradeOffer;
        this.pasteReplacement = new PasteReplacement(SaveManager.settingsSaveFile.data.characterName, tradeOffer.playerName, tradeOffer.itemName, tradeOffer.itemQuantity, tradeOffer.priceName, tradeOffer.priceQuantity);
        if (FrameManager.stashHelperContainer != null && tradeOffer.offerType == TradeOfferType.INCOMING_TRADE && createListeners) {
            if (this.tradeOffer.isBulkTrade) {
                stashHelperWrapper = new StashHelperWrapper(tradeOffer);
            } else {
                stashHelperPanel = new StashHelperPanel(tradeOffer);
            }
        }
        playerNameButton.setText(tradeOffer.playerName);
        JPanel itemPanel = new PassThroughPanel();
        itemPanel.setOpaque(false);
        CurrencyLabelFactory.applyItemToComponent(itemPanel, tradeOffer);
        itemButton.add(itemPanel);
        CurrencyLabelFactory.applyPriceToComponent(pricePanel, tradeOffer.priceName, tradeOffer.priceQuantity);
        // Message type specific stuff
        switch (tradeOffer.offerType) {
            case INCOMING_TRADE:
                topMacros = SaveManager.settingsSaveFile.data.incomingTopMacros;
                bottomMacros = SaveManager.settingsSaveFile.data.incomingBottomMacros;
                break;
            case OUTGOING_TRADE:
                topMacros = SaveManager.settingsSaveFile.data.outgoingTopMacros;
                bottomMacros = SaveManager.settingsSaveFile.data.outgoingBottomMacros;
                break;
        }
        updateUI();
        setup();
        if (createListeners) addListeners();
    }

    private void addListeners() {
        TradeMessagePanel self = this;
        addPlayerButtonListener(tradeOffer.playerName);
        switch (tradeOffer.offerType) {
            case INCOMING_TRADE:
                itemButton.addMouseListener(new AdvancedMouseListener() {
                    @Override
                    public void click(MouseEvent e) {
                        if (e.getButton() == MouseEvent.BUTTON1) {
                            if (stashHelperPanel != null)
                                stashHelperPanel.setVisible(true);
                            if (stashHelperWrapper != null)
                                stashHelperWrapper.setVisible(true);
                            FrameManager.stashHelperContainer.refresh();
                        } else if (e.getButton() == MouseEvent.BUTTON3) {
                            FrameManager.itemIgnoreWindow.setItemName(tradeOffer.itemName);
                        }
                    }
                });
                closeButton.addMouseListener(new AdvancedMouseListener() {
                    @Override
                    public void click(MouseEvent e) {
                        if (e.getButton() == MouseEvent.BUTTON3) {
                            FrameManager.messageManager.quickCloseIncoming(tradeOffer);
                        }
                    }
                });
                break;
            case OUTGOING_TRADE:
                closeButton.addMouseListener(new AdvancedMouseListener() {
                    @Override
                    public void click(MouseEvent e) {
                        if (e.getButton() == MouseEvent.BUTTON3)
                            FrameManager.messageManager.quickCloseOutgoing(self);
                    }
                });
                break;
        }
    }

    @Override
    protected void onInvite() {
        super.onInvite();
        if (stashHelperPanel != null)
            stashHelperPanel.setVisible(true);
        if (stashHelperWrapper != null) {
            stashHelperWrapper.setVisible(true);
        }
        FrameManager.stashHelperContainer.refresh();
    }

    @Override
    public void updateUI() {
        super.updateUI();
        if (tradeOffer == null) return;
        StashTabColor stashTabColor = tradeOffer.getStashTabColor();
        if (tradeOffer.offerType == TradeOfferType.INCOMING_TRADE
                && SaveManager.settingsSaveFile.data.applyStashColorToMessage
                && stashTabColor != StashTabColor.ZERO
        ) {
            messageColor = tradeOffer.getStashTabColor().getBackground();
            currencyTextColor = tradeOffer.getStashTabColor().getForeground();
        } else {
            messageColor = ThemeManager.getCurrentTheme().themeType.getColor(tradeOffer.offerType);
            currencyTextColor = null;
        }
        revalidate();
        repaint();
        applyMessageColor();
    }

    @Override
    public void cleanup() {
        super.cleanup();
        if (tradeOffer.offerType == TradeOfferType.INCOMING_TRADE) {
            if (stashHelperPanel != null) {
                FrameManager.stashHelperContainer.remove(stashHelperPanel);
                stashHelperPanel.cleanup();
            }
            if (stashHelperWrapper != null) {
                FrameManager.stashHelperContainer.remove(stashHelperWrapper);
                stashHelperWrapper.cleanup();
            }
            FrameManager.stashHelperContainer.refresh();
        }
    }

}
