package com.brookmanholmes.billiardmatchanalyzer.ui.addturnwizard.model;

import com.brookmanholmes.billiardmatchanalyzer.wizard.model.ModelCallbacks;

/**
 * Created by Brookman Holmes on 4/1/2016.
 */
public class BreakErrorPage extends HowMissPage {
    public BreakErrorPage(ModelCallbacks callbacks, String title) {
        super(callbacks, title);
    }

    @Override public void updateTurnInfo(TurnBuilder turnBuilder) {
        turnBuilder.advStats.shotType("Break shot");
        turnBuilder.advStats.clearAngle();
        turnBuilder.advStats.clearSubType();
        turnBuilder.advStats.clearWhyTypes();

        super.updateTurnInfo(turnBuilder);
    }
}
