package com.slimtrade.gui.options;

import com.slimtrade.core.CommonText;
import com.slimtrade.core.data.PlayerMessage;
import com.slimtrade.core.trading.TradeOffer;
import com.slimtrade.core.trading.TradeOfferType;
import com.slimtrade.core.utility.GUIReferences;
import com.slimtrade.core.utility.MacroButton;
import com.slimtrade.core.utility.ZUtil;
import com.slimtrade.gui.chatscanner.ChatScannerEntry;
import com.slimtrade.gui.components.AddRemoveContainer;
import com.slimtrade.gui.components.StyledLabel;
import com.slimtrade.gui.messaging.ChatScannerMessagePanel;
import com.slimtrade.gui.messaging.TradeMessagePanel;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.util.ArrayList;

public class AbstractMacroOptionPanel extends AbstractOptionPanel {

    private final TradeOfferType messageType;
    protected final AddRemoveContainer<MacroCustomizerPanel> macroContainer = new AddRemoveContainer<>();

    private final JPanel exampleTradeContainer = new JPanel(new GridBagLayout());
    private GridBagConstraints gc = new GridBagConstraints();

    // Examples
    private final JButton exampleButton = new JButton("Show Examples");
    private final Component exampleHeaderPanel;
    private final Component examplePanel;
    private final Component exampleSeparator;
    private boolean showExamples;

    public AbstractMacroOptionPanel(TradeOfferType messageType) {
        this.messageType = messageType;
        gc.gridx = 0;
        gc.gridy = 0;
        gc.insets = new Insets(0, 0, 2, 0);
        macroContainer.setSpacing(4);
        // FIXME : Need to make a matching drag border
//        macroContainer.setUseDragBorder(false);
        Border border = UIManager.getBorder("TextField.border");
//        setBorder(border);
//        System.out.println(((FlatTextBorder)border).());

        JButton addMacroButton = new JButton("Add New Macro");
        addMacroButton.addActionListener(e -> {
            macroContainer.add(new MacroCustomizerPanel());
            gc.gridy++;
            macroContainer.revalidate();
            macroContainer.repaint();
        });

        addHeader("Inbuilt Macros");
        addComponent(inbuiltMacroPanel("Player Name", "/whois {player}", "Open empty whisper message"));
        addVerticalStrutSmall();
        if (messageType == TradeOfferType.INCOMING_TRADE) {
            addComponent(inbuiltMacroPanel("Item Name", "Open Stash Helper", "Ignore Item"));
            addVerticalStrut();
        }
        if (messageType == TradeOfferType.INCOMING_TRADE) {
            addComponent(inbuiltMacroPanel("Close Button", "Close Message", "Close all incoming messages with the same item name"));
            addVerticalStrut();
        } else if (messageType == TradeOfferType.OUTGOING_TRADE) {
            addComponent(inbuiltMacroPanel("Close Button", "Close Message", "Close all outgoing messages EXCEPT the clicked message"));
            addVerticalStrut();
        }
        addHeader("Custom Macro Info");
        addComponent(new JLabel("Run one or more commands using {player}, {item}, {price}, {zone}, and {message} for text replacement."));
        addComponent(new JLabel("Commands that don't start with / or @ will have '@{player}' added automatically."));
        addComponent(new JLabel("Use / or @ to start a new command, and they will be chained together."));
        addComponent(new JLabel("Hotkeys use the left click of the oldest trade, or the selected trade when using tabs."));
        addComponent(new JLabel("Use escape to clear a hotkey."));
        addVerticalStrutSmall();
        addComponent(exampleButton);

        addVerticalStrut();
        exampleHeaderPanel = addHeader("Examples");
        examplePanel = addComponent(createExamplePanel());
        exampleSeparator = addVerticalStrut();
        addHeader("Macro Preview");
        addComponent(exampleTradeContainer);
        addVerticalStrut();

        addHeader("Custom Macros");
        addComponent(addMacroButton);
        addVerticalStrutSmall();
        addComponent(macroContainer);
        hideExamples();
        addListeners();
    }

    private void addListeners() {
        exampleButton.addActionListener(e -> {
            showExamples = !showExamples;
            if (showExamples) showExamples();
            else hideExamples();
        });
    }

    private JPanel inbuiltMacroPanel(String text, String lmb, String rmb) {
        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints gc = ZUtil.getGC();
        gc.weightx = 1;
        gc.fill = GridBagConstraints.HORIZONTAL;
        panel.add(new JLabel(text), gc);
        gc.gridx++;
//        gc.gridy++;
        gc.insets.left = 20;
        panel.add(new JLabel("Left Mouse"), gc);
        gc.gridy++;
        panel.add(new JLabel("Right Mouse"), gc);
        gc.gridx++;
        gc.gridy--;
        panel.add(new JLabel(lmb), gc);
        gc.gridy++;
        panel.add(new JLabel(rmb), gc);
        return panel;
    }

    private JPanel createExamplePanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints gc = ZUtil.getGC();
        gc.weightx = 1;
        gc.fill = GridBagConstraints.HORIZONTAL;

        panel.add(new StyledLabel("thanks").bold(), gc);
        gc.gridx++;
        gc.insets.left = GUIReferences.INSET;
        panel.add(new JLabel("Whisper thanks (shorthand for \"@{player} thanks\")"), gc);
        gc.insets.left = 0;
        gc.gridx = 0;
        gc.gridy++;

        panel.add(new StyledLabel("thanks /kick {player}").bold(), gc);
        gc.gridx++;
        gc.insets.left = GUIReferences.INSET;
        panel.add(new JLabel("Whisper thanks, then kick the other player."), gc);
        gc.insets.left = 0;
        gc.gridx = 0;
        gc.gridy++;

        panel.add(new StyledLabel("thanks /leave /hideout").bold(), gc);
        gc.gridx++;
        gc.insets.left = GUIReferences.INSET;
        panel.add(new JLabel("Whisper thanks, leave the party, then warp to your own hideout."), gc);
        gc.insets.left = 0;
        gc.gridx = 0;
        gc.gridy++;

        panel.add(new StyledLabel("hold on, in {zone}").bold(), gc);
        gc.gridx++;
        gc.insets.left = GUIReferences.INSET;
        panel.add(new JLabel("Let a player know what zone you are in."), gc);
        gc.insets.left = 0;
        gc.gridx = 0;
        gc.gridy++;

        panel.add(new StyledLabel("{message}").bold(), gc);
        gc.gridx++;
        gc.insets.left = GUIReferences.INSET;
        panel.add(new JLabel("Resend an outgoing trade message."), gc);
        gc.insets.left = 0;
        gc.gridx = 0;
        gc.gridy++;
        return panel;
    }

    private void showExamples() {
        exampleButton.setText("Hide Examples");
        exampleHeaderPanel.setVisible(true);
        examplePanel.setVisible(true);
        exampleSeparator.setVisible(true);
    }

    private void hideExamples() {
        exampleButton.setText("Show Examples");
        exampleHeaderPanel.setVisible(false);
        examplePanel.setVisible(false);
        exampleSeparator.setVisible(false);
    }

    public void reloadExampleTrade() {
        exampleTradeContainer.removeAll();
        TradeMessagePanel panel = null;
        switch (messageType) {
            case INCOMING_TRADE:
                panel = new TradeMessagePanel(TradeOffer.getExampleTrade(TradeOfferType.INCOMING_TRADE), false);
                break;
            case OUTGOING_TRADE:
                panel = new TradeMessagePanel(TradeOffer.getExampleTrade(TradeOfferType.OUTGOING_TRADE), false);
                break;
        }
        assert panel != null;
        exampleTradeContainer.add(panel);
        exampleTradeContainer.revalidate();
        exampleTradeContainer.repaint();
    }

    public void reloadExampleTrade(ChatScannerEntry chatScannerEntry) {
        exampleTradeContainer.removeAll();
        ChatScannerMessagePanel panel = new ChatScannerMessagePanel(chatScannerEntry, new PlayerMessage("ScannerPlayer789", CommonText.LOREM_IPSUM_SHORT), false);
        exampleTradeContainer.add(panel);
        exampleTradeContainer.revalidate();
        exampleTradeContainer.repaint();
    }

    public void clearMacros() {
        macroContainer.removeAll();
        gc.gridy = 0;
    }

    public void setMacros(ArrayList<MacroButton> macros) {
        clearMacros();
        for (MacroButton macro : macros) {
            addMacro(macro);
        }
    }

    public void addMacro(MacroButton macro) {
        MacroCustomizerPanel macroPanel = new MacroCustomizerPanel();
        macroPanel.setMacro(macro);
        macroContainer.add(macroPanel);
        gc.gridy++;
    }

    public ArrayList<MacroButton> getMacros() {
        ArrayList<MacroButton> macros = new ArrayList<>(macroContainer.getComponentCount());
        for (MacroCustomizerPanel panel : macroContainer.getComponentsTyped()) {
            MacroButton macro = panel.getMacroButton();
            macros.add(macro);
        }
        return macros;
    }

}
