package com.brookmanholmes.billiardmatchanalyzer.ui.addturnwizard.model;

import com.brookmanholmes.billiardmatchanalyzer.wizard.model.ModelCallbacks;

/**
 * Created by Brookman Holmes on 4/1/2016.
 */
public class SafetyErrorPage extends HowMissPage {
    public SafetyErrorPage(ModelCallbacks callbacks, String title) {
        super(callbacks, title);
    }

    @Override public void updateTurnInfo(AddTurnWizardModel model) {
        model.getAdvStats().shotType("Safety error");
        model.getAdvStats().clearAngle();
        model.getAdvStats().clearSubType();
        model.getAdvStats().clearWhyTypes();

        super.updateTurnInfo(model);
    }
}
