package github.zmilla93.gui.options;

import github.zmilla93.App;
import github.zmilla93.core.data.PlayerMessage;
import github.zmilla93.core.jna.JnaAwtEvent;
import github.zmilla93.core.managers.FontManager;
import github.zmilla93.core.managers.SaveManager;
import github.zmilla93.core.poe.Game;
import github.zmilla93.core.trading.TradeOffer;
import github.zmilla93.core.trading.TradeOfferType;
import github.zmilla93.core.utility.ZUtil;
import github.zmilla93.gui.chatscanner.ChatScannerEntry;
import github.zmilla93.gui.components.ComponentPanel;
import github.zmilla93.gui.components.HotkeyButton;
import github.zmilla93.gui.components.StyledLabel;
import github.zmilla93.gui.managers.FrameManager;
import github.zmilla93.modules.theme.ThemeManager;

import javax.swing.*;
import java.awt.*;

public class DebugOptionPanel extends AbstractOptionPanel {

    private static final boolean GROUP_FONTS_BY_FAMILY = true;

    private final HotkeyButton chatHotkeyButton = new HotkeyButton();

    private final JButton incomingMessageButtonPoe1 = new JButton("Incoming Trade POE1");
    private final JButton incomingMessageButtonPoe2 = new JButton("Incoming Trade POE2");
    private final JButton outgoingMessageButtonPoe1 = new JButton("Outgoing Trade POE1");
    private final JButton outgoingMessageButtonPoe2 = new JButton("Outgoing Trade POE2");
    private final JButton scannerMessageButton = new JButton("Scanner Message");
    private final JButton updateMessageButton = new JButton("Update Message");
    private final JButton createBackupButton = new JButton("Create New Backup");
    private final JButton loadBackupButton = new JButton("Load Backup");
    private final JButton clientButtonPoe1 = new JButton("Open Client.txt POE1");
    private final JButton clientButtonPoe2 = new JButton("Open Client.txt POE2");
    private final JButton uiDumpButton = new JButton("Dump UIManager to Clipboard");
    private final JComboBox<String> fontCombo = new JComboBox<>();

    private static final String slashString = "Slash - // \\\\";
    private final JLabel chineseLabel = new JLabel(FontManager.CHINESE_EXAMPLE_TEXT);
    private final JLabel japaneseLabel = new JLabel(FontManager.JAPANESE_EXAMPLE_TEXT);
    private final JLabel koreanLabel = new JLabel(FontManager.KOREAN_EXAMPLE_TEXT);
    private final JLabel russianLabel = new JLabel(FontManager.RUSSIAN_EXAMPLE_TEXT);
    private final JLabel thaiLabel = new JLabel(FontManager.THAI_EXAMPLE_TEXT);
    private final JLabel slashLabel = new JLabel(slashString);
    private final JLabel[] translationLabels = new JLabel[]{chineseLabel, japaneseLabel, koreanLabel, russianLabel, thaiLabel, slashLabel};

    public DebugOptionPanel() {
        chatHotkeyButton.addHotkeyChangeListener(e -> {
            if (chatHotkeyButton.getData() != null) {
                System.out.println(chatHotkeyButton.getData().toString());
                JnaAwtEvent.hotkeyToEvent(chatHotkeyButton.getData());
            }
        });

        addHeader("Debug Tools");
        addComponent(incomingMessageButtonPoe1);
        addComponent(incomingMessageButtonPoe2);
        addComponent(outgoingMessageButtonPoe1);
        addComponent(outgoingMessageButtonPoe2);
        addComponent(scannerMessageButton);
        addComponent(updateMessageButton);
        addComponent(clientButtonPoe1);
        addComponent(clientButtonPoe2);
        addComponent(uiDumpButton);
        addComponent(new ComponentPanel(createBackupButton, loadBackupButton));
        addVerticalStrut();
        addHeader("Hotkey Test");
        addComponent(chatHotkeyButton);
        addVerticalStrut();
        addHeader("Font Test");
        addComponent(new StyledLabel("Almost before we knew it, we had left the ground."));
        addComponent(new StyledLabel("The quick brown fox jumped over the lazy dogs.").bold());
        addComponent(new StyledLabel("You are captured, stupid beast!"));
        addComponent(new StyledLabel("You are captured, stupid beast!").bold());

        // Font Translations
        addComponent(fontCombo);
        GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
        if (GROUP_FONTS_BY_FAMILY) for (String s : ge.getAvailableFontFamilyNames()) fontCombo.addItem(s);
        else for (Font font : ge.getAllFonts()) fontCombo.addItem(font.getName());
        addComponent(createFontTranslationPanel());

        addListeners();
    }

    private void addListeners() {
        incomingMessageButtonPoe1.addActionListener(e -> FrameManager.messageManager.addMessage(TradeOffer.getExampleTrade(TradeOfferType.INCOMING_TRADE)));
        incomingMessageButtonPoe2.addActionListener(e -> FrameManager.messageManager.addMessage(TradeOffer.getExampleTrade(TradeOfferType.INCOMING_TRADE, Game.PATH_OF_EXILE_2)));
        outgoingMessageButtonPoe1.addActionListener(e -> FrameManager.messageManager.addMessage(TradeOffer.getExampleTrade(TradeOfferType.OUTGOING_TRADE)));
        outgoingMessageButtonPoe2.addActionListener(e -> FrameManager.messageManager.addMessage(TradeOffer.getExampleTrade(TradeOfferType.OUTGOING_TRADE, Game.PATH_OF_EXILE_2)));
        scannerMessageButton.addActionListener(e -> FrameManager.messageManager.addScannerMessage(new ChatScannerEntry("alch"), new PlayerMessage("CoolTrader123", "wtb alch for chaos")));
        updateMessageButton.addActionListener(e -> FrameManager.messageManager.addUpdateMessage(true, App.getAppInfo().appVersion.toString()));
        uiDumpButton.addActionListener(e -> ThemeManager.debugKeyValueDump());
        clientButtonPoe1.addActionListener(e -> ZUtil.openFile(SaveManager.settingsSaveFile.data.settingsPoe1.installFolder));
        clientButtonPoe2.addActionListener(e -> ZUtil.openFile(SaveManager.settingsSaveFile.data.settingsPoe2.installFolder));
        fontCombo.addActionListener(e -> {
            String s = (String) fontCombo.getSelectedItem();
            Font font = new Font(s, Font.PLAIN, 12);
            for (JLabel label : translationLabels) {
                label.setFont(font);
            }
            revalidate();
            repaint();
        });
        createBackupButton.addActionListener(e -> {
            int response = JOptionPane.showConfirmDialog(DebugOptionPanel.this, "Are you sure you want to create a new backup of all app settings?\nThis will delete the existing backup.", "Create Backup", JOptionPane.YES_NO_OPTION);
            if (response == JOptionPane.OK_OPTION) SaveManager.createBackup();
        });
        loadBackupButton.addActionListener(e -> {
            int response = JOptionPane.showConfirmDialog(DebugOptionPanel.this, "Are you sure you want to load the existing settings backup?\nThis requires a program restart after.", "Load Backup", JOptionPane.YES_NO_OPTION);
            if (response == JOptionPane.OK_OPTION) SaveManager.loadBackup();
        });
    }

    private JPanel createFontTranslationPanel() {
        JPanel panel = new JPanel();

        AbstractOptionPanel p1 = new AbstractOptionPanel();
        p1.addHeader("DEFAULT FONT");
        p1.addComponent(new JLabel(FontManager.CHINESE_EXAMPLE_TEXT));
        p1.addComponent(new JLabel(FontManager.JAPANESE_EXAMPLE_TEXT));
        p1.addComponent(new JLabel(FontManager.KOREAN_EXAMPLE_TEXT));
        p1.addComponent(new JLabel(FontManager.THAI_EXAMPLE_TEXT));
        p1.addComponent(new JLabel(FontManager.RUSSIAN_EXAMPLE_TEXT));
        p1.addComponent(new JLabel(slashString));

        AbstractOptionPanel p2 = new AbstractOptionPanel();
        p2.addHeader("TRANSLATED FONTS");
        p2.addComponent(FontManager.applyFont(new JLabel(FontManager.CHINESE_EXAMPLE_TEXT)));
        p2.addComponent(FontManager.applyFont(new JLabel(FontManager.JAPANESE_EXAMPLE_TEXT)));
        p2.addComponent(FontManager.applyFont(new JLabel(FontManager.KOREAN_EXAMPLE_TEXT)));
        p2.addComponent(FontManager.applyFont(new JLabel(FontManager.THAI_EXAMPLE_TEXT)));
        p2.addComponent(FontManager.applyFont(new JLabel(FontManager.RUSSIAN_EXAMPLE_TEXT)));
        p2.addComponent(new JLabel(slashString));

        AbstractOptionPanel p3 = new AbstractOptionPanel();
        p3.addHeader("FONT PREVIEW");
        p3.addComponent(chineseLabel);
        p3.addComponent(japaneseLabel);
        p3.addComponent(koreanLabel);
        p3.addComponent(russianLabel);
        p3.addComponent(thaiLabel);
        p3.addComponent(slashLabel);

        panel.setLayout(new GridBagLayout());
//        panel.setLayout(new FlowLayout(FlowLayout.LEFT, 6, 0));
        GridBagConstraints gc = ZUtil.getGC();
        gc.fill = GridBagConstraints.VERTICAL;
        gc.weighty = 1;
        panel.add(p1, gc);
        gc.gridx++;
        panel.add(p2, gc);
        gc.gridx++;
        panel.add(p3, gc);

        return panel;
    }

}
