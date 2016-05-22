package com.brookmanholmes.billiardmatchanalyzer.ui.addturnwizard.model;

import android.support.v4.app.Fragment;

import com.brookmanholmes.billiardmatchanalyzer.wizard.model.ModelCallbacks;
import com.brookmanholmes.billiardmatchanalyzer.wizard.model.MultipleFixedChoicePage;
import com.brookmanholmes.billiardmatchanalyzer.wizard.ui.MultipleChoiceFragment;

/**
 * Created by Brookman Holmes on 3/7/2016.
 */
public class BankPage extends MultipleFixedChoicePage implements UpdatesTurnInfo{
    public BankPage(ModelCallbacks callbacks, String title) {
        super(callbacks, title);
    }

    @Override public void updateTurnInfo(AddTurnWizardModel model) {
        model.getAdvStats().clearAngle();
        if (data.getStringArrayList(SIMPLE_DATA_KEY) != null)
            model.getAdvStats().angle(data.getStringArrayList(SIMPLE_DATA_KEY));
    }

    @Override public Fragment createFragment() {
        return MultipleChoiceFragment.create(getKey(), 1);
    }
}
