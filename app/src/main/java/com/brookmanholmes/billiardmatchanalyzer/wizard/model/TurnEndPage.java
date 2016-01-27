package com.brookmanholmes.billiardmatchanalyzer.wizard.model;

import android.os.Bundle;
import android.support.v4.app.Fragment;

import com.brookmanholmes.billiardmatchanalyzer.ui.dialogs.SelectTurnEndDialog;

import java.util.ArrayList;

/**
 * Created by Brookman Holmes on 1/23/2016.
 */
public class TurnEndPage extends Page {
    Bundle args;

    public TurnEndPage(ModelCallbacks callbacks, Bundle args) {
        super(callbacks, "where does this show");
        this.args = args;
    }

    @Override
    public Fragment createFragment() {
        return SelectTurnEndDialog.create(args);
    }

    @Override
    public void getReviewItems(ArrayList<ReviewItem> dest) {

    }

    @Override
    public void setPlayerNames(String player, String opponent) {

    }
}
