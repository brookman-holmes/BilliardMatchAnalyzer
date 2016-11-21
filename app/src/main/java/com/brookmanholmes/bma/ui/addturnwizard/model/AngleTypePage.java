package com.brookmanholmes.bma.ui.addturnwizard.model;

import android.support.v4.app.Fragment;

import com.brookmanholmes.bma.wizard.model.ModelCallbacks;
import com.brookmanholmes.bma.wizard.model.SingleFixedChoicePage;
import com.brookmanholmes.bma.wizard.ui.SingleChoiceFragment;

/**
 * Created by Brookman Holmes on 3/7/2016.
 */
class AngleTypePage extends SingleFixedChoicePage implements UpdatesTurnInfo {
    boolean sortPage;

    AngleTypePage(ModelCallbacks callbacks, String title, boolean sortPage) {
        super(callbacks, title);
        this.sortPage = sortPage;
    }

    @Override
    public void updateTurnInfo(AddTurnWizardModel model) {
        model.setAngles(data.getString(SIMPLE_DATA_KEY));
    }

    @Override
    public Fragment createFragment() {
        return SingleChoiceFragment.create(getKey(), 1, sortPage);
    }
}
