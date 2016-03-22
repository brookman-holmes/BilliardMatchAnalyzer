package com.brookmanholmes.billiardmatchanalyzer.ui.addturnwizard.model;

import android.support.v4.app.Fragment;

import com.brookmanholmes.billiardmatchanalyzer.ui.addturnwizard.fragments.AddTurnMultipleChoiceFragment;
import com.brookmanholmes.billiardmatchanalyzer.wizard.model.ModelCallbacks;
import com.brookmanholmes.billiardmatchanalyzer.wizard.model.MultipleFixedChoicePage;

/**
 * Created by Brookman Holmes on 3/7/2016.
 */
public class BankPage extends MultipleFixedChoicePage implements UpdatesTurnInfo{
    public BankPage(ModelCallbacks callbacks) {
        super(callbacks, "What type of bank?");

        setChoices("1 rail", "2 rail", "3 rail", "4 rail", "Crossover", "Long rail", "Natural", "Short rail"); // TODO: 3/20/2016 rename long rail / short rail banks to be more descriptive
        setRequired(true);
    }

    @Override
    public void updateTurnInfo(TurnBuilder turnBuilder) {
        turnBuilder.advStats.clearAngle();
        if (data.getStringArrayList(SIMPLE_DATA_KEY) != null)
            turnBuilder.advStats.angle(data.getStringArrayList(SIMPLE_DATA_KEY));
    }

    @Override
    public Fragment createFragment() {
        return AddTurnMultipleChoiceFragment.create(getKey());
    }
}
