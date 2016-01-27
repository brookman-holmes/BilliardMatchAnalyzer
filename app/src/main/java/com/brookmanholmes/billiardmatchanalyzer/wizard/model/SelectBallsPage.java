package com.brookmanholmes.billiardmatchanalyzer.wizard.model;

import android.os.Bundle;
import android.support.v4.app.Fragment;

import com.brookmanholmes.billiardmatchanalyzer.ui.dialogs.SelectBallsDialog;

import java.util.ArrayList;

/**
 * Created by Brookman Holmes on 1/23/2016.
 */
public class SelectBallsPage extends Page {
    Bundle args;

    public SelectBallsPage(ModelCallbacks callbacks, Bundle args) {
        super(callbacks, "where does this show selectballspage");
        this.args = args;
    }

    @Override
    public Fragment createFragment() {
        return SelectBallsDialog.create(args);
    }

    @Override
    public void getReviewItems(ArrayList<ReviewItem> dest) {

    }

    @Override
    public void setPlayerNames(String player, String opponent) {

    }
}
