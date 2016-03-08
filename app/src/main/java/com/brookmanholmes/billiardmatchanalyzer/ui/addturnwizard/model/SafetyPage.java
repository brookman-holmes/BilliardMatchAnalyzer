package com.brookmanholmes.billiardmatchanalyzer.ui.addturnwizard.model;

import android.support.v4.app.Fragment;

import com.brookmanholmes.billiardmatchanalyzer.ui.addturnwizard.fragments.AddTurnSingleChoiceFragment;
import com.brookmanholmes.billiardmatchanalyzer.wizard.model.ModelCallbacks;
import com.brookmanholmes.billiardmatchanalyzer.wizard.model.SingleFixedChoicePage;

/**
 * Created by Brookman Holmes on 3/7/2016.
 */
public class SafetyPage extends SingleFixedChoicePage {
    private static final String[] safetyTypes = {"Full hook", "Partial hook", "Long T", "Short T"};

    public SafetyPage(ModelCallbacks callbacks) {
        super(callbacks, "What type of safety did you make?");

        setChoices(safetyTypes);
        setRequired(true);
    }

    @Override
    public Fragment createFragment() {
        return AddTurnSingleChoiceFragment.create(getKey());
    }
}
