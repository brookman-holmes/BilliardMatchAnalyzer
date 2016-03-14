package com.brookmanholmes.billiardmatchanalyzer.ui.addturnwizard.model;

import android.support.v4.app.Fragment;

import com.brookmanholmes.billiardmatchanalyzer.ui.addturnwizard.fragments.AddTurnSingleChoiceFragment;
import com.brookmanholmes.billiardmatchanalyzer.wizard.model.ModelCallbacks;
import com.brookmanholmes.billiardmatchanalyzer.wizard.model.SingleFixedChoicePage;

/**
 * Created by Brookman Holmes on 3/7/2016.
 */
public class CutTypePage extends SingleFixedChoicePage implements UpdatesTurnInfo {
    public CutTypePage(ModelCallbacks callbacks) {
        super(callbacks, "What type of cut shot?");

        setChoices("Back cut", "Down the rail", "Wing cut");
        setRequired(true);
        setValue("Down the rail");
    }

    @Override
    public void updateTurnInfo(TurnBuilder turnBuilder) {
        turnBuilder.shotSubType = data.getString(SIMPLE_DATA_KEY);
    }

    @Override
    public Fragment createFragment() {
        return AddTurnSingleChoiceFragment.create(getKey());
    }
}
