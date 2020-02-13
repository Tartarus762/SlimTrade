package com.slimtrade.gui.messaging;

import com.slimtrade.App;
import com.slimtrade.core.SaveSystem.MacroButton;
import com.slimtrade.core.SaveSystem.StashTab;
import com.slimtrade.core.managers.ColorManager;
import com.slimtrade.core.observing.AdvancedMouseAdapter;
import com.slimtrade.core.observing.ButtonType;
import com.slimtrade.core.observing.improved.IColorable;
import com.slimtrade.core.utility.TradeOffer;
import com.slimtrade.core.utility.TradeUtility;
import com.slimtrade.enums.MessageType;
import com.slimtrade.enums.StashTabColor;
import com.slimtrade.enums.StashTabType;
import com.slimtrade.gui.FrameManager;
import com.slimtrade.gui.basic.ColorPanel;
import com.slimtrade.gui.buttons.IconButton;
import com.slimtrade.gui.enums.ButtonRow;
import com.slimtrade.gui.enums.PreloadedImage;
import com.slimtrade.gui.enums.PreloadedImageCustom;
import com.slimtrade.gui.panels.PricePanel;
import com.slimtrade.gui.stash.helper.StashHelper;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

public class MessagePanel extends AbstractMessagePanel implements IColorable {

    private static final long serialVersionUID = 1L;
    private JPanel topPanel = new ColorPanel(gb);
    private JPanel bottomPanel = new ColorPanel(gb);

    protected JPanel buttonPanelTop = new ColorPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
    protected JPanel buttonPanelBottom = new ColorPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));

    private StashHelper stashHelper;

    // private IconButton saveToHistoryButton;
    // private IconButton waitButton;
    private IconButton refreshButton;
    private IconButton inviteButton;
    private IconButton warpButton;
    private IconButton tradeButton;
    private IconButton thankButton;

    private IconButton homeButton;
    private IconButton replyButton;

    private ArrayList<IconButton> customButtonsTop = new ArrayList<IconButton>();
    private ArrayList<IconButton> customButtonsBottom = new ArrayList<IconButton>();

    // TODO Listeners?
    public MessagePanel(TradeOffer trade, Dimension size) {
        super(trade);
        buildPanel(trade, size, true);
    }

    public MessagePanel(TradeOffer trade, Dimension size, boolean makeListeners) {
        super(trade);
        buildPanel(trade, size, makeListeners);
    }

    private void buildPanel(TradeOffer trade, Dimension size, boolean makeListeners) {
        // TODO : move size stuff to super
        this.trade = trade;
        this.setMessageType(trade.messageType);

        if (trade.guildName != null && App.saveManager.saveFile.showGuildName) {
            namePanel.setText(trade.guildName + " " + trade.playerName);
        } else {
            namePanel.setText(trade.playerName);
        }

        switch (messageType) {
            case CHAT_SCANNER:
                itemPanel.setText(trade.searchMessage);
                pricePanel.setText(trade.searchName);
                break;
            case INCOMING_TRADE:
            case OUTGOING_TRADE:
                itemPanel.setText(TradeUtility.getFixedItemName(trade.itemName, trade.itemCount, true));
//                pricePanel.removeListener();
                pricePanel = new PricePanel(trade.priceTypeString, trade.priceCount, true);
                //TODO : PRICE PANEL
                break;
            case UNKNOWN:
                break;
            default:
                break;
        }

        calculateSizes(size);
        refreshButtons(this.getMessageType(), makeListeners);
        resizeFrames();

        timerPanel.setLayout(new BorderLayout());
        timerPanel.add(timerLabel, BorderLayout.CENTER);
        timerLabel.setHorizontalAlignment(SwingConstants.CENTER);

        container.add(topPanel, gc);
        gc.gridy = 1;
        container.add(bottomPanel, gc);
        gc.gridy = 0;
        borderPanel.add(container, gc);
        this.add(borderPanel, gc);
        // TOP PANEL
        topPanel.add(namePanel, gc);
        gc.gridx++;
        topPanel.add(pricePanel, gc);
        gc.gridx++;
        topPanel.add(buttonPanelTop, gc);

        // BOTTOM PANEL
        gc.gridx = 0;
        gc.gridy = 0;
        bottomPanel.add(timerPanel, gc);
        gc.gridx++;
        bottomPanel.add(itemPanel, gc);
        gc.gridx++;
        bottomPanel.add(buttonPanelBottom, gc);

        // Finalize
        this.startTimer();
        App.eventManager.addColorListener(this);
        this.updateColor();

    }

    // TODO : Finish this
    // public void resizeMessage(Dimension size, boolean listeners) {
    // calculateSizes(size);
    // resizeFrames(3, 5);
    // refreshButtons(this.getMessageType(), listeners);
    // this.revalidate();
    // this.repaint();
    // }

    private void calculateSizes(Dimension size) {
        if (size.width % 2 != 0) {
            size.width++;
        }
        if (size.height % 2 != 0) {
            size.height++;
        }
        messageWidth = size.width;
        messageHeight = size.height;
        rowHeight = messageHeight / 2;
        totalWidth = messageWidth + (borderSize * 4);
        totalHeight = messageHeight + (borderSize * 4);
    }

    // TODO : get max
    protected void refreshButtons(MessageType type, boolean listeners) {
        for (Component c : buttonPanelTop.getComponents()) {
            buttonPanelTop.remove(c);
            c = null;
        }
        for (Component c : buttonPanelBottom.getComponents()) {
            buttonPanelBottom.remove(c);
            c = null;
        }
        switch (type) {
            case CHAT_SCANNER:
                // respodButton =
                buttonCountTop = 2;
                buttonCountBottom = 4;

                replyButton = new IconButton(PreloadedImage.REPLY.getImage(), rowHeight);
                buttonPanelTop.add(replyButton);

                inviteButton = new IconButton(PreloadedImage.INVITE.getImage(), rowHeight);
                tradeButton = new IconButton(PreloadedImage.CART.getImage(), rowHeight);
                thankButton = new IconButton(PreloadedImage.THUMB.getImage(), rowHeight);
                kickButton = new IconButton(PreloadedImage.LEAVE.getImage(), rowHeight);

                if (listeners) {
//                    this.registerPoeInteractionButton(replyButton, ButtonType.WHISPER, trade.playerName, null, null);
                    this.registerPoeInteractionButton(inviteButton, ButtonType.INVITE);
                    this.registerPoeInteractionButton(tradeButton, ButtonType.TRADE);
                    this.registerPoeInteractionButton(thankButton, ButtonType.THANK);
                    this.registerPoeInteractionButton(kickButton, ButtonType.KICK);
                }

                buttonPanelBottom.add(inviteButton);
                buttonPanelBottom.add(tradeButton);
                buttonPanelBottom.add(thankButton);
                buttonPanelBottom.add(kickButton);
                break;
            case INCOMING_TRADE:
                buttonCountTop = 2;
                buttonCountBottom = 4;
                for (MacroButton macro : App.saveManager.saveFile.incomingMacroButtons) {
                    PreloadedImageCustom img = macro.image;
                    if(img == null) {
                        for(PreloadedImageCustom c : PreloadedImageCustom.values()) {
                            img = c;
                            break;
                        }
                    }
                    IconButton button = new IconButton(img.getImage(), rowHeight);
                    if (macro.row == ButtonRow.TOP) {
                        buttonCountTop++;
                        String lmb = macro.leftMouseResponse;
                        String rmb = macro.rightMouseResponse;
                        if (listeners) {
                            this.registerPoeInteractionButton(button, ButtonType.WHISPER, trade.playerName, lmb, rmb);
                        }
                        customButtonsTop.add(button);
                    } else if (macro.row == ButtonRow.BOTTOM) {
                        buttonCountBottom++;
                        String lmb = macro.leftMouseResponse;
                        String rmb = macro.rightMouseResponse;
                        if (listeners) {
                            this.registerPoeInteractionButton(button, ButtonType.WHISPER, trade.playerName, lmb, rmb);
                        }
                        customButtonsBottom.add(button);
                    }
                }
                refreshButton = new IconButton(PreloadedImage.REFRESH.getImage(), rowHeight);
                inviteButton = new IconButton(PreloadedImage.INVITE.getImage(), rowHeight);
                tradeButton = new IconButton(PreloadedImage.CART.getImage(), rowHeight);
                thankButton = new IconButton(PreloadedImage.THUMB.getImage(), rowHeight);
                kickButton = new IconButton(PreloadedImage.LEAVE.getImage(), rowHeight);
                if (listeners) {
                    this.registerPoeInteractionButton(refreshButton, ButtonType.REFRESH);
                    this.registerPoeInteractionButton(inviteButton, ButtonType.INVITE);
                    this.registerPoeInteractionButton(tradeButton, ButtonType.TRADE);
                    this.registerPoeInteractionButton(thankButton, ButtonType.THANK);
                    this.registerPoeInteractionButton(kickButton, ButtonType.KICK);
                    itemPanel.addMouseListener(new AdvancedMouseAdapter() {
                        public void click(MouseEvent e) {
                            if (e.getButton() == MouseEvent.BUTTON1) {
                                stashHelper.setVisible(true);
                                FrameManager.stashHelperContainer.pack();
                            } else if (e.getButton() == MouseEvent.BUTTON3) {
                                FrameManager.ignoreItemWindow.setItem(trade.itemName);
                                FrameManager.ignoreItemWindow.pack();
                                FrameManager.centerFrame(FrameManager.ignoreItemWindow);
                                FrameManager.ignoreItemWindow.setVisible(true);
                            }

                        }
                    });
                    inviteButton.addMouseListener(new AdvancedMouseAdapter() {
                        public void click(MouseEvent e) {
                            stashHelper.setVisible(true);
                            FrameManager.stashHelperContainer.pack();
                        }
                    });
                }

                for (IconButton b : customButtonsTop) {
                    buttonPanelTop.add(b);
                }
                for (IconButton b : customButtonsBottom) {
                    buttonPanelBottom.add(b);
                }
                // buttonPanelTop.add(saveToHistoryButton);
                buttonPanelTop.add(refreshButton);

                buttonPanelBottom.add(inviteButton);
                buttonPanelBottom.add(tradeButton);
                buttonPanelBottom.add(thankButton);
                buttonPanelBottom.add(kickButton);

                break;
            case OUTGOING_TRADE:
                buttonCountTop = 2;
                buttonCountBottom = 4;
                for (MacroButton macro : App.saveManager.saveFile.outgoingMacroButtons) {
                    PreloadedImageCustom img = macro.image;
                    if(img == null) {
                        for(PreloadedImageCustom c : PreloadedImageCustom.values()) {
                            img = c;
                            break;
                        }
                    }
                    IconButton button = new IconButton(img.getImage(), rowHeight);
                    if (macro.row == ButtonRow.TOP) {
                        buttonCountTop++;
                        String lmb = macro.leftMouseResponse;
                        String rmb = macro.rightMouseResponse;
                        if (listeners) {
                            this.registerPoeInteractionButton(button, ButtonType.WHISPER, trade.playerName, lmb, rmb);
                        }
                        customButtonsTop.add(button);
                    } else if (macro.row == ButtonRow.BOTTOM) {
                        buttonCountBottom++;
                        String lmb = macro.leftMouseResponse;
                        String rmb = macro.rightMouseResponse;
                        if (listeners) {
                            this.registerPoeInteractionButton(button, ButtonType.WHISPER, trade.playerName, lmb, rmb);
                        }
                        customButtonsBottom.add(button);
                    }
                }
                refreshButton = new IconButton(PreloadedImage.REFRESH.getImage(), rowHeight);
                warpButton = new IconButton(PreloadedImage.WARP.getImage(), rowHeight);
                thankButton = new IconButton(PreloadedImage.THUMB.getImage(), rowHeight);
                leaveButton = new IconButton(PreloadedImage.LEAVE.getImage(), rowHeight);
                homeButton = new IconButton(PreloadedImage.HOME.getImage(), rowHeight);

                for (IconButton b : customButtonsTop) {
                    buttonPanelTop.add(b);
                }
                for (IconButton b : customButtonsBottom) {
                    buttonPanelBottom.add(b);
                }

                buttonPanelTop.add(refreshButton);
                buttonPanelBottom.add(warpButton);
                buttonPanelBottom.add(thankButton);
                buttonPanelBottom.add(leaveButton);
                buttonPanelBottom.add(homeButton);
                if (listeners) {
                    this.registerPoeInteractionButton(refreshButton, ButtonType.REFRESH);
                    this.registerPoeInteractionButton(warpButton, ButtonType.WARP);
                    this.registerPoeInteractionButton(thankButton, ButtonType.THANK);
                    this.registerPoeInteractionButton(leaveButton, ButtonType.LEAVE);
                    this.registerPoeInteractionButton(homeButton, ButtonType.HIDEOUT);
                }
                break;
            case UNKNOWN:
                break;
            default:
                break;
        }
        if (listeners) {
            this.registerPoeInteractionButton(namePanel, ButtonType.NAME_PANEL);
        }
//        App.eventManager.addColorListener(this);
        // TODO : update force
        this.setCloseButton(rowHeight);
        buttonPanelTop.add(closeButton);

    }

    protected void resizeFrames() {
        this.setPreferredSize(new Dimension(totalWidth, totalHeight));
        borderPanel.setPreferredSize(new Dimension(messageWidth + borderSize * 2, messageHeight + borderSize * 2));
        container.setPreferredSize(new Dimension(messageWidth, messageHeight));
        Dimension s = new Dimension(messageWidth, rowHeight);
        topPanel.setPreferredSize(s);
        bottomPanel.setPreferredSize(s);
        Dimension buttonSizeTop = new Dimension(rowHeight * buttonCountTop, rowHeight);
        Dimension buttonSizeBottom = new Dimension(rowHeight * buttonCountBottom, rowHeight);
        buttonPanelTop.setPreferredSize(buttonSizeTop);
        buttonPanelTop.setMinimumSize(buttonSizeTop);
        buttonPanelBottom.setPreferredSize(buttonSizeBottom);
        buttonPanelBottom.setMaximumSize(buttonSizeBottom);
        int nameWidth = (int) ((messageWidth - buttonSizeTop.width) * 0.7);
        int priceWidth = messageWidth - nameWidth - buttonSizeTop.width;
        int timerWidth = (int) (messageWidth * timerWeight);
        int itemWidth = messageWidth - timerWidth - buttonSizeBottom.width;

        namePanel.setPreferredSize(new Dimension(nameWidth, rowHeight));
        pricePanel.setPreferredSize(new Dimension(priceWidth, rowHeight));
        timerPanel.setPreferredSize(new Dimension(timerWidth, rowHeight));
        itemPanel.setPreferredSize(new Dimension(itemWidth, rowHeight));
    }

    public JButton getCloseButton() {
        return this.closeButton;
    }

    public TradeOffer getTrade() {
        return trade;
    }

    public void setTrade(TradeOffer trade) {
        this.trade = trade;
    }

    public StashHelper getStashHelper() {
        return stashHelper;
    }

    public void setStashHelper(StashHelper stashHelper) {
        this.stashHelper = stashHelper;
    }

    @Override
    public void updateColor() {
        super.updateColor();
        Color color = null;
        Color colorText = null;
        //MUTUAL COLORS
        this.setBackground(ColorManager.PRIMARY);
        // Name Panel
        namePanel.setBackgroundColor(ColorManager.LOW_CONTRAST_1);
        namePanel.backgroundHover = ColorManager.PRIMARY;
        namePanel.borderDefault = ColorManager.LOW_CONTRAST_1;
        namePanel.borderHover = ColorManager.TEXT;
        namePanel.borderClick = ColorManager.TEXT;
        namePanel.setTextColor(ColorManager.TEXT);
        itemPanel.setBackgroundColor(ColorManager.LOW_CONTRAST_2);
        itemPanel.setBorderColor(ColorManager.LOW_CONTRAST_2);
        itemPanel.setTextColor(ColorManager.TEXT);
        pricePanel.setTextColor(ColorManager.PRIMARY);
        switch (trade.messageType) {
            case CHAT_SCANNER:
                // TODO : Custom tooltip
                itemPanel.setToolTipText(trade.searchMessage);
                borderPanel.setBackground(ColorManager.SCANNER_BACKGROUND);
                pricePanel.setBackgroundColor(ColorManager.SCANNER_BACKGROUND);
                pricePanel.setBorderColor(ColorManager.SCANNER_BACKGROUND);
                break;
            case INCOMING_TRADE:
                color = StashTabColor.TWENTYSIX.getBackground();
                colorText = StashTabColor.TWENTYSIX.getForeground();
                itemPanel.backgroundHover = ColorManager.PRIMARY;
                itemPanel.borderHover = ColorManager.TEXT;
                itemPanel.borderClick = ColorManager.TEXT;
                if (trade.stashtabName != null && !trade.stashtabName.equals("")) {
                    int i = 0;
                    for (StashTab tab : App.saveManager.saveFile.stashTabs) {
                        if (tab.name.toLowerCase().equals(trade.stashtabName.toLowerCase())) {
                            if (tab.color != StashTabColor.ZERO) {
                                StashTabColor stashColor = tab.color;
                                StashTabType type = tab.type;
                                color = stashColor.getBackground();
                                colorText = stashColor.getForeground();
                            }
                            break;
                        }
                    }
                }
                stashHelper = new StashHelper(trade, color, colorText);
                stashHelper.setVisible(false);
                FrameManager.stashHelperContainer.add(stashHelper);
                borderPanel.setBackground(ColorManager.GREEN_SALE);
                pricePanel.setBackgroundColor(ColorManager.GREEN_SALE);
                pricePanel.setBorderColor(ColorManager.GREEN_SALE);
                break;
            case OUTGOING_TRADE:
                borderPanel.setBackground(ColorManager.RED_SALE);
                pricePanel.setBackgroundColor(ColorManager.RED_SALE);
                pricePanel.setBorderColor(ColorManager.RED_SALE);
                break;
        }
    }

}
