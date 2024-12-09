package com.slimtrade.gui.components.slimtrade;

import com.slimtrade.core.enums.ResultStatus;
import com.slimtrade.gui.components.StyledLabel;

//FIXME : Move to theme module once that is updated
public class ResultLabel extends StyledLabel {

    private ResultStatus status;

    public ResultLabel(ResultStatus status) {
        this(status, null);
    }

    public ResultLabel(ResultStatus status, String text) {
        this.status = status;
        setText(text);
        updateUI();
    }

    public void setText(ResultStatus status, String text) {
        this.status = status;
        setText(text);
        updateUI();
    }

    // FIXME : Implement
    @Override
    public void updateUI() {
        super.updateUI();
        if (status == null) return;
        setForeground(status.toColor());
    }

}
