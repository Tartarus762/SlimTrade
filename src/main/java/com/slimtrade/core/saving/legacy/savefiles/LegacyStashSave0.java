package com.slimtrade.core.saving.legacy.savefiles;

import com.slimtrade.core.saving.savefiles.AbstractSaveFile;

public class LegacyStashSave0 extends AbstractSaveFile {

    public int gridX, gridY, gridWidth, gridHeight;

    @Override
    public int getTargetFileVersion() {
        return 0;
    }

}
