package com.slimtrade.gui.chatscanner;

import com.slimtrade.core.utility.ZUtil;
import com.slimtrade.gui.components.CustomScrollPane;

import javax.swing.*;
import java.awt.*;

public class ChatScannerSearchingPanel extends JPanel {

    public ChatScannerSearchingPanel() {
        setLayout(new BorderLayout());
        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints gc = ZUtil.getGC();
        panel.add(new JLabel("Scanning in progress..."), gc);
        gc.gridy++;
        panel.add(new JLabel("This window can be closed."), gc);
        gc.gridy++;
        JProgressBar progressBar = new JProgressBar();
        progressBar.setIndeterminate(true);
        panel.add(progressBar, gc);
        gc.gridy++;
        JScrollPane scrollPane = new CustomScrollPane(panel);
        add(scrollPane, BorderLayout.CENTER);
    }

}
