package com.brookmanholmes.billiardmatchanalyzer.ui.addturnwizard.model;

import android.support.v4.app.Fragment;

import com.brookmanholmes.billiardmatchanalyzer.ui.addturnwizard.fragments.AddTurnSingleChoiceFragment;
import com.brookmanholmes.billiardmatchanalyzer.wizard.model.ModelCallbacks;
import com.brookmanholmes.billiardmatchanalyzer.wizard.model.SingleFixedChoicePage;

/**
 * Created by Brookman Holmes on 3/7/2016.
 */
public class AngleTypePage extends SingleFixedChoicePage {

    public AngleTypePage(ModelCallbacks callbacks) {
        super(callbacks, "Degree of cut");

        setChoices("15 degrees", "30 degrees", "45 degrees", "60 degrees", "75 degrees", "90 degrees");
        setRequired(true);
    }

    @Override
    public Fragment createFragment() {
        return AddTurnSingleChoiceFragment.create(getKey());
    }
}
