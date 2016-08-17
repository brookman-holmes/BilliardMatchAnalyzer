package com.brookmanholmes.bma.ui.addturnwizard.model;

import android.support.v4.app.Fragment;

import com.brookmanholmes.bma.wizard.model.ModelCallbacks;
import com.brookmanholmes.bma.wizard.model.SingleFixedChoicePage;
import com.brookmanholmes.bma.wizard.ui.SingleChoiceFragment;

/**
 * Created by Brookman Holmes on 3/7/2016.
 */
public class CutTypePage extends SingleFixedChoicePage implements UpdatesTurnInfo {
    public CutTypePage(ModelCallbacks callbacks, String title) {
        super(callbacks, title);
    }

    @Override public void updateTurnInfo(AddTurnWizardModel model) {
        model.getAdvStats().subType(data.getString(SIMPLE_DATA_KEY));
    }

    @Override public Fragment createFragment() {
        return SingleChoiceFragment.create(getKey(), 1);
    }
}