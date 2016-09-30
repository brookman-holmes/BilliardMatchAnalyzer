package com.brookmanholmes.bma.ui.addturnwizard.model;

import com.brookmanholmes.billiards.turn.AdvStats;
import com.brookmanholmes.bma.wizard.model.ModelCallbacks;

/**
 * Created by Brookman Holmes on 4/1/2016.
 */
class SafetyErrorPage extends HowMissPage {
    SafetyErrorPage(ModelCallbacks callbacks, String title) {
        super(callbacks, title);
    }

    @Override
    public void updateTurnInfo(AddTurnWizardModel model) {
        model.setShotType(AdvStats.ShotType.SAFETY_ERROR);

        super.updateTurnInfo(model);
    }
}
