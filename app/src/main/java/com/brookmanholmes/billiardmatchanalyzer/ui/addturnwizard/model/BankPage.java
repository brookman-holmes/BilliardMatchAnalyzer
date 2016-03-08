package com.brookmanholmes.billiardmatchanalyzer.ui.addturnwizard.model;

import android.support.v4.app.Fragment;

import com.brookmanholmes.billiardmatchanalyzer.ui.addturnwizard.fragments.AddTurnSingleChoiceFragment;
import com.brookmanholmes.billiardmatchanalyzer.wizard.model.ModelCallbacks;
import com.brookmanholmes.billiardmatchanalyzer.wizard.model.SingleFixedChoicePage;

/**
 * Created by Brookman Holmes on 3/7/2016.
 */
public class BankPage extends SingleFixedChoicePage {
    public BankPage(ModelCallbacks callbacks) {
        super(callbacks, "Type of bank");

        setChoices("Natural", "Crossover", "Long rail", "Short rail", "1 rail", "2 rail", "3 rail", "4 rail");
        setRequired(true);
    }

    @Override
    public Fragment createFragment() {
        return AddTurnSingleChoiceFragment.create(getKey());
    }
}
