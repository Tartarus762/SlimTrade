package github.zmilla93.gui.components;

import github.zmilla93.gui.buttons.IIcon;
import github.zmilla93.modules.theme.ThemeManager;

import javax.swing.*;

/**
 * Displays an icon that will auto resize when icon size is changed.
 */
public class BasicIconLabel extends JLabel {

    private final int BORDER_SIZE;
    protected String iconPath;

    public BasicIconLabel(IIcon iconData) {
        this(iconData, 0);
    }

    public BasicIconLabel(IIcon iconData, int borderSize) {
        this.iconPath = iconData.path();
        this.BORDER_SIZE = borderSize;
        updateUI();
    }

    public void setIcon(IIcon iconData) {
        this.iconPath = iconData.path();
        updateUI();
    }

    protected void applyIcon() {
        assert SwingUtilities.isEventDispatchThread();
        setIcon(ThemeManager.getIcon(iconPath));
    }

    @Override
    public void updateUI() {
        super.updateUI();
        if (iconPath != null) applyIcon();
        if (BORDER_SIZE > 0)
            setBorder(BorderFactory.createLineBorder(UIManager.getColor("Separator.foreground"), BORDER_SIZE));
    }

}
