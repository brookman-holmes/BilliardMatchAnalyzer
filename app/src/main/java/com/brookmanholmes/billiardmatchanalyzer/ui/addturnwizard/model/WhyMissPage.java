package com.brookmanholmes.billiardmatchanalyzer.ui.addturnwizard.model;

import android.support.v4.app.Fragment;

import com.brookmanholmes.billiardmatchanalyzer.ui.addturnwizard.fragments.AddTurnMultipleChoiceFragment;
import com.brookmanholmes.billiardmatchanalyzer.wizard.model.ModelCallbacks;
import com.brookmanholmes.billiardmatchanalyzer.wizard.model.MultipleFixedChoicePage;

/**
 * Created by Brookman Holmes on 3/7/2016.
 */
public class WhyMissPage extends MultipleFixedChoicePage {
    public WhyMissPage(ModelCallbacks callbacks, String[] choices, String parentKey) {
        super(callbacks, "Why did you miss?");

        setParentKey(parentKey);
        setChoices(choices);
        setRequired(true);
    }

    @Override
    public Fragment createFragment() {
        return AddTurnMultipleChoiceFragment.create(getKey());
    }
}