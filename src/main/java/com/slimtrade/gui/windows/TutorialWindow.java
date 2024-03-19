package com.slimtrade.gui.windows;

import com.slimtrade.App;
import com.slimtrade.core.trading.TradeOffer;
import com.slimtrade.core.trading.TradeOfferType;
import com.slimtrade.core.utility.ZUtil;
import com.slimtrade.gui.components.ImageLabel;
import com.slimtrade.gui.components.TutorialPanel;
import com.slimtrade.gui.managers.FrameManager;
import com.slimtrade.gui.messaging.TradeMessagePanel;

import javax.swing.*;
import java.awt.*;

public class TutorialWindow extends CustomDialog {

    private final CardLayout cardLayout = new CardLayout();
    private final JPanel cardPanel = new JPanel(cardLayout);

    private final JButton previousButton = new JButton("Previous");
    private final JButton nextButton = new JButton("Next");
    private final JLabel pageLabel = new JLabel("Page 1/1");

    private int activePanel = 1;
    private static final int IMAGE_BORDER_SIZE = 1;
    public static final int TUTORIAL_VERSION = 1;

    public TutorialWindow() {
        super("Tutorial");
        pinButton.setVisible(false);

        contentPanel.setLayout(new BorderLayout());
        JPanel borderPanel = new JPanel(new BorderLayout());
        ZUtil.addStrutsToBorderPanel(borderPanel, 10);

        // Tutorial Panels
        cardPanel.add(createFeatureOverviewPanel(), index());
        cardPanel.add(createTradeMessagePanel(), index());
        cardPanel.add(createStashHelperPanel(), index());
        cardPanel.add(createTradeHistoryPanel(), index());
        cardPanel.add(createChatScannerPanel(), index());
        cardPanel.add(createSearchPanel(), index());
        cardPanel.add(createGettingStartedPanel(), index());
        if (App.debugUIBorders >= 1) cardPanel.setBorder(BorderFactory.createLineBorder(Color.YELLOW));

        // Button Panel
        JPanel pageLabelWrapper = new JPanel(new GridBagLayout());
        pageLabelWrapper.add(pageLabel);
        JPanel buttonPanel = new JPanel(new BorderLayout());
        buttonPanel.add(previousButton, BorderLayout.WEST);
        buttonPanel.add(nextButton, BorderLayout.EAST);
        buttonPanel.add(pageLabelWrapper, BorderLayout.CENTER);

        // Content Panel
        borderPanel.add(cardPanel, BorderLayout.CENTER);
        contentPanel.add(borderPanel, BorderLayout.CENTER);
        contentPanel.add(buttonPanel, BorderLayout.SOUTH);

        setMinimumSize(null);
        pack();
        setMinimumSize(getSize());
        setLocationRelativeTo(null);
        addListeners();
        updatePageLabel();
        updateButtons();
    }

    private void addListeners() {
        previousButton.addActionListener(e -> {
            if (activePanel <= 1) return;
            activePanel--;
            cardLayout.show(cardPanel, Integer.toString(activePanel));
            updatePageLabel();
            updateButtons();
        });
        nextButton.addActionListener(e -> {
            if (activePanel >= cardPanel.getComponentCount()) return;
            activePanel++;
            cardLayout.show(cardPanel, Integer.toString(activePanel));
            updatePageLabel();
            updateButtons();
        });
        closeButton.addActionListener(e -> {
            FrameManager.optionsWindow.setVisible(true);
            FrameManager.optionsWindow.toFront();
        });
    }

    private String index() {
        return Integer.toString(cardPanel.getComponentCount() + 1);
    }

    private void updatePageLabel() {
        pageLabel.setText("Page " + activePanel + "/" + cardPanel.getComponentCount());
    }

    private void updateButtons() {
        previousButton.setEnabled(activePanel > 1);
        nextButton.setEnabled(activePanel < cardPanel.getComponentCount());
    }

    public void showFirstPanel() {
        activePanel = 1;
        cardLayout.show(cardPanel, "1");
        updatePageLabel();
        updateButtons();
    }

    ///////////////////////
    //  Tutorial Panels  //
    ///////////////////////

    private JPanel createFeatureOverviewPanel() {
        TutorialPanel panel = new TutorialPanel();
        panel.addHeader("Feature Overview");
        panel.addLabel("- Streamlined trade UI");
        panel.addLabel("- Trade history");
        panel.addLabel("- Chat scanning");
        panel.addLabel("- Macros");
        panel.addLabel("- Custom cheat sheets");
        panel.addLabel("- In game searching (stash, vendors, skill tree, etc)");
        panel.addLabel("- Snap windows to monitor edge (SHIFT while moving window)");
        panel.addLabel("- Pinnable windows");
        panel.addLabel("- 20+ color themes");
        panel.addLabel("- Incredibly customizable!").bold();
        return panel;
    }

    private JPanel createTradeMessagePanel() {
        TutorialPanel panel = new TutorialPanel();
        panel.addHeader("Trade Messages");
        panel.addLabel("Popups are created when a trade message is sent or received.");
        panel.addLabel("Response buttons are fully customizable, color blind setting for alternative colors.");
        panel.addVerticalStrut();
        panel.addComponent(new TradeMessagePanel(TradeOffer.getExampleTrade(TradeOfferType.INCOMING_TRADE), false), true);
        panel.addComponent(new TradeMessagePanel(TradeOffer.getExampleTrade(TradeOfferType.OUTGOING_TRADE), false), true);
        return panel;
    }

    private JPanel createStashHelperPanel() {
        TutorialPanel panel = new TutorialPanel();
        panel.addHeader("Stash Helper");
        panel.addLabel("Incoming trades create an info window above your stash.");
        panel.addLabel("Hover to outline an item, left click to search the name, or right click to close.");
//        panel.addLabel("Stash tab names can be assigned a color or marked as quad in the options.");
        panel.addVerticalStrut();
        panel.addComponent(new ImageLabel("/images/stash-helper.png", IMAGE_BORDER_SIZE), true);
        return panel;
    }

    private JPanel createTradeHistoryPanel() {
        TutorialPanel panel = new TutorialPanel();
        panel.addHeader("Trade History");
        panel.addLabel("The history window can be used to reopen recent trade.");
        panel.addLabel("Built using the client file, so trades show up even if the app wasn't running.");
        panel.addVerticalStrut();
        panel.addComponent(new ImageLabel("/images/history3.png", IMAGE_BORDER_SIZE), true);
        return panel;
    }

    private JPanel createChatScannerPanel() {
        TutorialPanel panel = new TutorialPanel();
        panel.addHeader("Chat Scanner");
        panel.addLabel("The chat scanner can be used to search for custom phrases in chat.");
        panel.addLabel("Create many presets with custom responses for each entry.");
        panel.addVerticalStrut();
        panel.addComponent(new TradeMessagePanel(TradeOffer.getExampleTrade(TradeOfferType.CHAT_SCANNER_MESSAGE), false), true);
        return panel;
    }

    private JPanel createSearchPanel() {
        TutorialPanel panel = new TutorialPanel();
        panel.addHeader("Searching");
        panel.addLabel("Paste search terms into any window with a search box.");
        panel.addLabel("Create multiple groups to keep things organized!");
        panel.addVerticalStrut();
        panel.addComponent(new ImageLabel("/images/search.png", IMAGE_BORDER_SIZE), true);
        return panel;
    }

    private JPanel createGettingStartedPanel() {
        TutorialPanel panel = new TutorialPanel();
        panel.addHeader("Getting Started");
        panel.addLabel("The trade UI will start working automatically.");
        panel.addLabel("Access additional features from the menubar in the top left, or from the system tray.");
        panel.addLabel("Close this window to start customizing!").bold();
        return panel;
    }

}
