package com.brookmanholmes.bma.ui.addturnwizard.model;

import android.support.v4.app.Fragment;

import com.brookmanholmes.bma.wizard.model.ModelCallbacks;
import com.brookmanholmes.bma.wizard.model.MultipleFixedChoicePage;
import com.brookmanholmes.bma.wizard.ui.MultipleChoiceFragment;

/**
 * Created by Brookman Holmes on 3/7/2016.
 */
class BankPage extends MultipleFixedChoicePage implements UpdatesTurnInfo {
    BankPage(ModelCallbacks callbacks, String title) {
        super(callbacks, title);
    }

    @Override
    public void updateTurnInfo(AddTurnWizardModel model) {
        model.setAngles(data.getStringArrayList(SIMPLE_DATA_KEY));
    }

    @Override
    public Fragment createFragment() {
        return MultipleChoiceFragment.create(getKey(), 1);
    }
}
