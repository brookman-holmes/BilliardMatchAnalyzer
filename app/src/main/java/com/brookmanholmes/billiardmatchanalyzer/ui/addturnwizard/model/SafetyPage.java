package com.brookmanholmes.billiardmatchanalyzer.ui.addturnwizard.model;

import android.support.v4.app.Fragment;

import com.brookmanholmes.billiardmatchanalyzer.ui.addturnwizard.fragments.AddTurnSingleChoiceFragment;
import com.brookmanholmes.billiardmatchanalyzer.wizard.model.ModelCallbacks;
import com.brookmanholmes.billiardmatchanalyzer.wizard.model.SingleFixedChoicePage;

/**
 * Created by Brookman Holmes on 3/7/2016.
 */
public class SafetyPage extends SingleFixedChoicePage implements UpdatesTurnInfo {
    public SafetyPage(ModelCallbacks callbacks, String title) {
        super(callbacks, title);
    }

    @Override public void updateTurnInfo(AddTurnWizardModel model) {
        model.getAdvStats().shotType("Safety");
        model.getAdvStats().subType(data.getString(SIMPLE_DATA_KEY));
        model.getAdvStats().clearAngle();
        model.getAdvStats().clearWhyTypes();
        model.getAdvStats().clearHowTypes();
    }

    @Override public Fragment createFragment() {
        return AddTurnSingleChoiceFragment.create(getKey());
    }
}
