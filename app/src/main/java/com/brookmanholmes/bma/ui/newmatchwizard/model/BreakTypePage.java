package com.brookmanholmes.bma.ui.newmatchwizard.model;

import android.content.Context;

import com.brookmanholmes.bma.R;
import com.brookmanholmes.bma.wizard.model.BranchPage;
import com.brookmanholmes.bma.wizard.model.ModelCallbacks;
import com.brookmanholmes.bma.wizard.model.Page;
import com.brookmanholmes.bma.wizard.model.ReviewItem;

import java.util.ArrayList;

/**
 * Created by Brookman Holmes on 1/7/2016.
 */
class BreakTypePage extends BranchPage implements RequiresPlayerNames, UpdatesMatchBuilder {
    private final String valueEnding;
    private final String firstBreak;
    private String playerName = "Player 1";
    private String opponentName = "Player 2";

    BreakTypePage(ModelCallbacks callbacks, String title, Context context) {
        super(callbacks, title);
        valueEnding = context.getString(R.string.break_player);
        firstBreak = context.getString(R.string.title_page_first_break);
    }

    @Override
    public void getReviewItems(ArrayList<ReviewItem> dest) {
        super.getReviewItems(dest);

        if (data.getString(SIMPLE_DATA_KEY, "").equals(String.format(valueEnding, playerName))) {
            dest.add(new ReviewItem(firstBreak, playerName, getKey()));
        } else if (data.getString(SIMPLE_DATA_KEY, "").equals(String.format(valueEnding, opponentName))) {
            dest.add(new ReviewItem(firstBreak, opponentName, getKey()));
        }
    }

    @Override
    public void setPlayerNames(String playerName, String opponentName) {
        for (Branch branch : branches) {
            for (Page page : branch.childPageList) {
                if (page instanceof RequiresPlayerNames) {
                    ((RequiresPlayerNames) page).setPlayerNames(playerName, opponentName);
                }
            }
        }

        if (data.getString(SIMPLE_DATA_KEY, "").equals(String.format(valueEnding, this.playerName)))
            data.putString(SIMPLE_DATA_KEY, String.format(valueEnding, playerName));
        else if (data.getString(SIMPLE_DATA_KEY, "").equals(String.format(valueEnding, this.opponentName)))
            data.putString(SIMPLE_DATA_KEY, String.format(valueEnding, opponentName));

        this.playerName = playerName;
        this.opponentName = opponentName;

        branches.get(3).choice = String.format(valueEnding, playerName);
        branches.get(4).choice = String.format(valueEnding, opponentName);
    }

    @Override
    public void updateMatchBuilder(CreateNewMatchWizardModel model) {
        model.setBreakType(data.getString(SIMPLE_DATA_KEY));
    }
}
