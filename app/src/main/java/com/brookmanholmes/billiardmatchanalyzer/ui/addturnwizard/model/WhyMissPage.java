package com.brookmanholmes.billiardmatchanalyzer.ui.addturnwizard.model;

import android.support.v4.app.Fragment;

import com.brookmanholmes.billiardmatchanalyzer.ui.addturnwizard.fragments.AddTurnMultipleChoiceFragment;
import com.brookmanholmes.billiardmatchanalyzer.wizard.model.ModelCallbacks;
import com.brookmanholmes.billiardmatchanalyzer.wizard.model.MultipleFixedChoicePage;

import java.util.Arrays;

/**
 * Created by Brookman Holmes on 3/7/2016.
 */
public class WhyMissPage extends MultipleFixedChoicePage implements UpdatesTurnInfo{
    public WhyMissPage(ModelCallbacks callbacks, String[] choices, String parentKey) {
        super(callbacks, "Why did you miss?");

        Arrays.sort(choices);
        setParentKey(parentKey);
        setChoices(choices);
        setRequired(true);
    }

    @Override
    public void updateTurnInfo(TurnBuilder turnBuilder) {
        if (parentKey.equals("break miss why") || parentKey.equals("illegal break why")) {
            turnBuilder.advStats.shotType("Break shot");
            turnBuilder.advStats.clearHowTypes();
            turnBuilder.advStats.clearWhyTypes();
            turnBuilder.advStats.clearAngle();
            turnBuilder.advStats.clearSubType();
        }

        turnBuilder.advStats.clearWhyTypes();
        if (data.getStringArrayList(SIMPLE_DATA_KEY) != null)
            turnBuilder.advStats.whyTypes(data.getStringArrayList(SIMPLE_DATA_KEY));
    }



    @Override
    public Fragment createFragment() {
        return AddTurnMultipleChoiceFragment.create(getKey());
    }
}
