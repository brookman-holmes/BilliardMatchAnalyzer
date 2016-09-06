package com.brookmanholmes.bma.ui.addturnwizard.model;

import com.brookmanholmes.billiards.turn.AdvStats;
import com.brookmanholmes.bma.wizard.model.ModelCallbacks;

/**
 * Created by Brookman Holmes on 4/1/2016.
 */
public class BreakErrorPage extends HowMissPage {
    public BreakErrorPage(ModelCallbacks callbacks, String title) {
        super(callbacks, title);
    }

    @Override public void updateTurnInfo(AddTurnWizardModel model) {
        model.setShotType(AdvStats.ShotType.BREAK_SHOT);

        super.updateTurnInfo(model);
    }
}
