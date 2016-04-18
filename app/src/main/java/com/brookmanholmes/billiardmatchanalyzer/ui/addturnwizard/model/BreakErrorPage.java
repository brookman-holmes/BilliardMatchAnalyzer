package com.brookmanholmes.billiardmatchanalyzer.ui.addturnwizard.model;

import com.brookmanholmes.billiardmatchanalyzer.wizard.model.ModelCallbacks;

/**
 * Created by Brookman Holmes on 4/1/2016.
 */
public class BreakErrorPage extends HowMissPage {
    public BreakErrorPage(ModelCallbacks callbacks, String title) {
        super(callbacks, title);
    }

    @Override public void updateTurnInfo(AddTurnWizardModel model) {
        model.getAdvStats().shotType("Break shot");
        model.getAdvStats().clearAngle();
        model.getAdvStats().clearSubType();
        model.getAdvStats().clearWhyTypes();

        super.updateTurnInfo(model);
    }
}
