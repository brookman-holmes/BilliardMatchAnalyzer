package com.brookmanholmes.bma.ui.addturnwizard.model;

import android.support.v4.app.Fragment;

import com.brookmanholmes.bma.wizard.model.ModelCallbacks;
import com.brookmanholmes.bma.wizard.model.MultipleFixedChoicePage;
import com.brookmanholmes.bma.wizard.ui.MultipleChoiceFragment;

import java.util.ArrayList;

/**
 * Created by Brookman Holmes on 3/7/2016.
 */
class HowMissPage extends MultipleFixedChoicePage implements UpdatesTurnInfo {
    HowMissPage(ModelCallbacks callbacks, String title) {
        super(callbacks, title);
        data.putStringArrayList(SIMPLE_DATA_KEY, new ArrayList<String>());
    }

    @Override
    public void updateTurnInfo(AddTurnWizardModel model) {
        model.setHows(data.getStringArrayList(SIMPLE_DATA_KEY));
    }

    @Override
    public Fragment createFragment() {
        return MultipleChoiceFragment.create(getKey(), 1);
    }
}
