package com.brookmanholmes.billiardmatchanalyzer.ui.addturnwizard.model;

import android.support.v4.app.Fragment;

import com.brookmanholmes.billiardmatchanalyzer.ui.addturnwizard.fragments.AddTurnSingleChoiceFragment;
import com.brookmanholmes.billiardmatchanalyzer.wizard.model.ModelCallbacks;
import com.brookmanholmes.billiardmatchanalyzer.wizard.model.SingleFixedChoicePage;

/**
 * Created by Brookman Holmes on 3/7/2016.
 */
public class KickPage extends SingleFixedChoicePage implements UpdatesTurnInfo{
    public KickPage(ModelCallbacks callbacks) {
        super(callbacks, "What type of kick?");

        setChoices("1 rail", "2 rail", "3 rail", "4 rail");
        setRequired(true);
        setValue("1 rail");
    }

    @Override
    public void updateTurnInfo(TurnBuilder turnBuilder) {
        turnBuilder.advStats.clearAngle();
        turnBuilder.advStats.angle(data.getString(SIMPLE_DATA_KEY));
    }

    @Override
    public Fragment createFragment() {
        return AddTurnSingleChoiceFragment.create(getKey());
    }
}
