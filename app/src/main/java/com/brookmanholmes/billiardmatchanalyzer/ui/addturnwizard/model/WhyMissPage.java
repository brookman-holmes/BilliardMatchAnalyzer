package com.brookmanholmes.billiardmatchanalyzer.ui.addturnwizard.model;

import android.support.v4.app.Fragment;

import com.brookmanholmes.billiardmatchanalyzer.wizard.model.ModelCallbacks;
import com.brookmanholmes.billiardmatchanalyzer.wizard.model.MultipleFixedChoicePage;
import com.brookmanholmes.billiardmatchanalyzer.wizard.ui.MultipleChoiceFragment;

/**
 * Created by Brookman Holmes on 3/7/2016.
 */
public class WhyMissPage extends MultipleFixedChoicePage implements UpdatesTurnInfo {
    public WhyMissPage(ModelCallbacks callbacks, String title) {
        super(callbacks, title);
    }

    @Override public void updateTurnInfo(AddTurnWizardModel model) {
        if (parentKey.equals("break miss why") || parentKey.equals("illegal break why")) {
            model.getAdvStats().shotType("Break shot");
            model.getAdvStats().clearHowTypes();
            model.getAdvStats().clearWhyTypes();
            model.getAdvStats().clearAngle();
            model.getAdvStats().clearSubType();
        }

        model.getAdvStats().clearWhyTypes();
        if (data.getStringArrayList(SIMPLE_DATA_KEY) != null)
            model.getAdvStats().whyTypes(data.getStringArrayList(SIMPLE_DATA_KEY));
    }



    @Override public Fragment createFragment() {
        return MultipleChoiceFragment.create(getKey(), 1);
    }
}
