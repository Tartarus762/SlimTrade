package com.slimtrade.gui.managers;

import com.slimtrade.App;
import com.slimtrade.core.chatparser.ITradeListener;
import com.slimtrade.core.trading.TradeOffer;
import com.slimtrade.gui.messaging.TradeMessagePanel;

import javax.swing.*;
import java.awt.*;

public class MessageManager extends JFrame implements ITradeListener {

    public Point anchorPoint = new Point(800, 0);
    boolean expandUp = false;


    private int MESSAGE_GAP = 2;
    private Container container;

    private GridBagConstraints gc;


    public MessageManager() {
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setUndecorated(true);
        setFocusable(false);
        setFocusableWindowState(false);
        setAlwaysOnTop(true);
        setBackground(new Color(0, 0, 0, 0));

        container = getContentPane();
        setLocationRelativeTo(null);
//        WindowUtils.setWindowTransparent(this, true);

        container.setLayout(new GridBagLayout());
        gc = new GridBagConstraints();
        gc.gridx = 0;
        gc.gridy = 0;
        setLocation(anchorPoint);
        pack();
    }

    public void addMessage(TradeOffer tradeOffer) {
        assert (SwingUtilities.isEventDispatchThread());
        App.audioManager.playSoundPercent(App.saveManager.settingsSaveFile.incomingSound.sound, App.saveManager.settingsSaveFile.incomingSound.volume);
        System.out.println("MSG:" + tradeOffer.offerType);
        gc.insets = new Insets(0, 0, MESSAGE_GAP, 0);
        gc.gridy = container.getComponentCount();
        TradeMessagePanel panel = new TradeMessagePanel(tradeOffer);
        panel.startTimer();
        container.add(panel, gc);
        pack();
        adjustPosition();
        repaint();
    }

    public void removeMessage(JPanel panel) {
        assert (SwingUtilities.isEventDispatchThread());
        container.remove(panel);
        pack();
        adjustPosition();
        container.revalidate();
    }

    private void adjustPosition() {
        Point p = new Point(anchorPoint);
        if (expandUp) {
            p.y -= getHeight();
            setLocation(p);
        } else {
            setLocation(p);
        }
    }

    @Override
    public void handleTrade(TradeOffer tradeOffer) {
            SwingUtilities.invokeLater(() -> addMessage(tradeOffer));
    }
}