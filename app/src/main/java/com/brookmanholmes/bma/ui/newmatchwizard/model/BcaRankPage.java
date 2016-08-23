package com.brookmanholmes.bma.ui.newmatchwizard.model;

import android.support.v4.app.Fragment;

import com.brookmanholmes.billiards.game.util.PlayerTurn;
import com.brookmanholmes.bma.wizard.model.ModelCallbacks;
import com.brookmanholmes.bma.wizard.ui.SingleChoiceFragment;

/**
 * Created by Brookman Holmes on 8/21/2016.
 */
public class BcaRankPage extends RankPage {
    BcaRankPage(ModelCallbacks callbacks, String title, PlayerTurn playerTurn) {
        super(callbacks, title, playerTurn);
    }

    @Override protected void setChoices() {
        for (int i = 1; i < 21; i++) {
            choices.add(String.valueOf(i));
        }
    }

    @Override public Fragment createFragment() {
        return SingleChoiceFragment.create(getKey(), false);
    }
}