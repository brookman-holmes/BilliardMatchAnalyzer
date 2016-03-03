package com.brookmanholmes.billiardmatchanalyzer.ui.addturnwizard.model;

import com.brookmanholmes.billiardmatchanalyzer.wizard.model.ModelCallbacks;
import com.brookmanholmes.billiardmatchanalyzer.wizard.model.SingleFixedChoicePage;

/**
 * Created by Brookman Holmes on 3/3/2016.
 */
public class FoulPage extends SingleFixedChoicePage implements UpdatesTurnInfo {
    public FoulPage(ModelCallbacks callbacks) {
        super(callbacks, "Did you foul?");

        setChoices("Yes, lost game", "Yes", "No");
        setValue("No");
    }

    @Override
    public void updateTurnInfo(TurnBuilder turnBuilder) {
        turnBuilder.scratch = data.getString(SIMPLE_DATA_KEY, "").startsWith("Yes");
    }
}
