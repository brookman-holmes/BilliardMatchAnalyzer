package com.brookmanholmes.billiardmatchanalyzer.wizard.model;

import android.support.v4.app.Fragment;

import com.brookmanholmes.billiardmatchanalyzer.ui.dialogs.SelectTurnEndDialog;

import java.util.ArrayList;

/**
 * Created by Brookman Holmes on 1/23/2016.
 */
public class TurnEndPage extends SelectBallsPage {
    public TurnEndPage(ModelCallbacks callbacks, String playerName) {
        super(callbacks, playerName);
    }

    @Override
    public Fragment createFragment() {
        return SelectTurnEndDialog.create(getKey());
    }

    @Override
    public void getReviewItems(ArrayList<ReviewItem> dest) {

    }

    @Override
    public void setPlayerNames(String player, String opponent) {

    }
}
