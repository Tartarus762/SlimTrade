package github.zmilla93.gui.managers;

import github.zmilla93.App;
import github.zmilla93.core.managers.SaveManager;
import github.zmilla93.gui.windows.VisibilityDialog;

import javax.swing.*;
import java.util.ArrayList;

/**
 * Shows/Hides overlay windows.
 */
public class VisibilityManager {

    private static final ArrayList<VisibilityDialog> frameList = new ArrayList<>();
    private static boolean show = true;

    public static void addFrame(VisibilityDialog frame) {
        frameList.add(frame);
    }

    public static void removeFrame(VisibilityDialog frame) {
        frameList.remove(frame);
    }

    public static void hideOverlay() {
        if (!show || !SaveManager.settingsSaveFile.data.hideWhenPOENotFocused || App.debugUIAlwaysOnTop) return;
        SwingUtilities.invokeLater(() -> {
            for (VisibilityDialog frame : frameList) {
                frame.hideOverlay();
            }
            show = false;
        });
    }

    public static void showOverlay() {
        if (show) return;
        SwingUtilities.invokeLater(() -> {
            for (VisibilityDialog frame : frameList) {
                frame.showOverlay();
            }
            show = true;
        });
    }

    public static void hideAllFrames() {
        for (VisibilityDialog dialog : frameList) {
            dialog.setVisible(false);
        }
    }

}
