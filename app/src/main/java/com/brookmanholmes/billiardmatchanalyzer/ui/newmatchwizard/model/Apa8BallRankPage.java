package com.brookmanholmes.billiardmatchanalyzer.ui.newmatchwizard.model;

import com.brookmanholmes.billiardmatchanalyzer.wizard.model.ModelCallbacks;

/**
 * Created by Brookman Holmes on 1/7/2016.
 */
public class Apa8BallRankPage extends RankPage {
    public Apa8BallRankPage(ModelCallbacks callbacks, int playerNumber) {
        super(callbacks, playerNumber);
    }

    @Override
    protected void setChoices() {
        choices.add("2");
        choices.add("3");
        choices.add("4");
        choices.add("5");
        choices.add("6");
        choices.add("7");
    }
}
