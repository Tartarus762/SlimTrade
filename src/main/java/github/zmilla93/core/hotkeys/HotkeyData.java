package github.zmilla93.core.hotkeys;

import org.jnativehook.keyboard.NativeKeyEvent;

/**
 * Stores they key code and any modifiers for a hotkey.
 */
public class HotkeyData {

    public final int keyCode;
    public final int modifiers;

    public HotkeyData(int keyCode, int modifiers) {
        this.keyCode = keyCode;
        this.modifiers = modifiers;
    }

    public boolean isAltPressed() {
        return (modifiers & NativeKeyEvent.ALT_MASK) > 0;
    }

    public boolean isCtrlPressed() {
        return (modifiers & NativeKeyEvent.CTRL_MASK) > 0;
    }

    public boolean isShiftPressed() {
        return (modifiers & NativeKeyEvent.SHIFT_MASK) > 0;
    }

    @Override
    public String toString() {
        if (modifiers > 0) {
            return NativeKeyEvent.getModifiersText(modifiers) + "+" + NativeKeyEvent.getKeyText(keyCode);
        }
        return NativeKeyEvent.getKeyText(keyCode);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof HotkeyData) {
            HotkeyData otherData = (HotkeyData) obj;
            if (keyCode != otherData.keyCode) return false;
            if (modifiers != otherData.modifiers) return false;
            return true;
        }
        return false;
    }

    @Override
    public int hashCode() {
        int result = 17;
        result = 31 * result + keyCode;
        result = 31 * result + modifiers;
        return result;
    }

}
