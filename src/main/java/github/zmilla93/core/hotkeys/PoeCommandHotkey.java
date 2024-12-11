package github.zmilla93.core.hotkeys;

import github.zmilla93.core.utility.POEInterface;

/**
 * Runs a POE chat command.
 */
public class PoeCommandHotkey implements IHotkeyAction {

    private final String command;

    public PoeCommandHotkey(String command) {
        this.command = command;
    }

    @Override
    public void execute() {
        if (!POEInterface.isGameFocused()) return;
        POEInterface.paste(command);
    }

}
