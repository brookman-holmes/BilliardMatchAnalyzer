package com.brookmanholmes.billiardmatchanalyzer.ui.addturnwizard.model;

import android.support.v4.app.Fragment;

import com.brookmanholmes.billiardmatchanalyzer.ui.addturnwizard.fragments.AddTurnSingleChoiceFragment;
import com.brookmanholmes.billiardmatchanalyzer.wizard.model.ModelCallbacks;
import com.brookmanholmes.billiardmatchanalyzer.wizard.model.SingleFixedChoicePage;

/**
 * Created by Brookman Holmes on 3/7/2016.
 */
public class AngleTypePage extends SingleFixedChoicePage implements UpdatesTurnInfo {

    public AngleTypePage(ModelCallbacks callbacks) {
        super(callbacks, "What was the degree of cut?");

        setChoices("15°", "30°", "45°", "60°", "75°", "90°");
        setRequired(true);
        setValue("45°");
    }

    @Override
    public void updateTurnInfo(TurnBuilder turnBuilder) {
        turnBuilder.angleType.clear();
        turnBuilder.angleType.add(data.getString(SIMPLE_DATA_KEY));
    }

    @Override
    public Fragment createFragment() {
        return AddTurnSingleChoiceFragment.create(getKey());
    }
}
