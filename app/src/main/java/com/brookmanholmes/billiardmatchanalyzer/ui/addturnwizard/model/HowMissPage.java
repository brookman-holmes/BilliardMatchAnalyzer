package com.brookmanholmes.billiardmatchanalyzer.ui.addturnwizard.model;

import android.support.v4.app.Fragment;

import com.brookmanholmes.billiardmatchanalyzer.ui.addturnwizard.fragments.AddTurnMultipleChoiceFragment;
import com.brookmanholmes.billiardmatchanalyzer.wizard.model.ModelCallbacks;
import com.brookmanholmes.billiardmatchanalyzer.wizard.model.MultipleFixedChoicePage;

import java.util.ArrayList;

/**
 * Created by Brookman Holmes on 3/7/2016.
 */
public class HowMissPage extends MultipleFixedChoicePage implements UpdatesTurnInfo {
    public HowMissPage(ModelCallbacks callbacks, String title) {
        super(callbacks, title);
        data.putStringArrayList(SIMPLE_DATA_KEY, new ArrayList<String>());
    }

    @Override public void updateTurnInfo(TurnBuilder turnBuilder) {
        turnBuilder.advStats.clearHowTypes();
        if (data.getStringArrayList(SIMPLE_DATA_KEY) != null)
            turnBuilder.advStats.howTypes(data.getStringArrayList(SIMPLE_DATA_KEY));
    }

    @Override
    public Fragment createFragment() {
        return AddTurnMultipleChoiceFragment.create(getKey());
    }
}
