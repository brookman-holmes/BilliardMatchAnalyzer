package com.brookmanholmes.billiardmatchanalyzer.ui.addturnwizard.model;

import android.support.v4.app.Fragment;

import com.brookmanholmes.billiardmatchanalyzer.ui.addturnwizard.fragments.AddTurnMultipleChoiceFragment;
import com.brookmanholmes.billiardmatchanalyzer.wizard.model.ModelCallbacks;
import com.brookmanholmes.billiardmatchanalyzer.wizard.model.MultipleFixedChoicePage;

/**
 * Created by Brookman Holmes on 3/7/2016.
 */
public class HowMissPage extends MultipleFixedChoicePage implements UpdatesTurnInfo{
    public HowMissPage(ModelCallbacks callbacks, String[] choices, String parentKey) {
        super(callbacks, "How did you miss?");
        setParentKey(parentKey);
        setChoices(choices);
    }

    @Override
    public void updateTurnInfo(TurnBuilder turnBuilder) {
        if (parentKey.equals("break miss how") || parentKey.equals("illegal break how"))
            turnBuilder.advStats.shotType("Break shot");

        turnBuilder.advStats.clearHowTypes();
        if (data.getStringArrayList(SIMPLE_DATA_KEY) != null)
            turnBuilder.advStats.howTypes(data.getStringArrayList(SIMPLE_DATA_KEY));
    }

    @Override
    public Fragment createFragment() {
        return AddTurnMultipleChoiceFragment.create(getKey());
    }
}
