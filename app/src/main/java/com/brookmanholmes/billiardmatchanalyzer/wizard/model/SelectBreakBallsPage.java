package com.brookmanholmes.billiardmatchanalyzer.wizard.model;

import android.support.v4.app.Fragment;

import com.brookmanholmes.billiardmatchanalyzer.ui.dialogs.SelectBreakBallsDialog;

import java.util.ArrayList;

/**
 * Created by Brookman Holmes on 1/23/2016.
 */
public class SelectBreakBallsPage extends SelectBallsPage {
    public SelectBreakBallsPage(ModelCallbacks callbacks, String playerName) {
        super(callbacks, playerName);
    }

    @Override
    public Fragment createFragment() {
        return SelectBreakBallsDialog.create(getKey());
    }

    @Override
    public void getReviewItems(ArrayList<ReviewItem> dest) {

    }

    @Override
    public void setPlayerNames(String player, String opponent) {

    }
}
