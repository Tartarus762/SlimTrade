package com.slimtrade.gui.windows;

import com.slimtrade.modules.theme.ThemeManager;
import com.slimtrade.core.utility.ZUtil;

import javax.swing.*;
import javax.swing.border.BevelBorder;
import java.awt.*;

public class LoadingDialog extends AbstractDialog {

    private static final int INSET_HORIZONTAL = 40;
    private static final int INSET_VERTICAL = 20;

    public LoadingDialog() {
        contentPanel.setLayout(new GridBagLayout());
        GridBagConstraints gc = ZUtil.getGC();
        gc.insets = new Insets(INSET_VERTICAL, INSET_HORIZONTAL, INSET_VERTICAL, INSET_HORIZONTAL);
        JLabel loadingLabel = new JLabel("Loading SlimTrade...");
        Font font = loadingLabel.getFont();
        loadingLabel.setFont(font.deriveFont(Font.BOLD, font.getSize()));
        contentPanel.add(loadingLabel, gc);
        contentPanel.setBorder(BorderFactory.createBevelBorder(BevelBorder.RAISED));
        pack();
        setLocationRelativeTo(null);
    }

    @Override
    public void dispose() {
        ThemeManager.removeFrame(this);
        super.dispose();
    }
}
