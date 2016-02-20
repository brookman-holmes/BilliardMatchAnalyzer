package com.brookmanholmes.billiardmatchanalyzer.ui.newmatchwizard.model;

import com.brookmanholmes.billiardmatchanalyzer.ui.newmatchwizard.model.RequiresPlayerNames;
import com.brookmanholmes.billiardmatchanalyzer.wizard.model.BranchPage;
import com.brookmanholmes.billiardmatchanalyzer.wizard.model.ModelCallbacks;
import com.brookmanholmes.billiardmatchanalyzer.wizard.model.Page;
import com.brookmanholmes.billiardmatchanalyzer.wizard.model.ReviewItem;
import com.brookmanholmes.billiardmatchanalyzer.wizard.model.SingleFixedChoicePage;

import java.util.ArrayList;

/**
 * Created by Brookman Holmes on 1/7/2016.
 */
// TODO: 2/20/2016 turn into a branch page and remove FirstBreakPage from list and put into here
public class BreakTypePage extends BranchPage implements RequiresPlayerNames {
    String playerName = "Player 1-", opponentName = "Player 2-";
    String valueEnding = " always breaks";

    public BreakTypePage(ModelCallbacks callbacks) {
        super(callbacks, "The break");

        addBranch("Winner", new FirstBreakPage(callbacks));
        addBranch("Alternate", new FirstBreakPage(callbacks));
        addBranch("Loser", new FirstBreakPage(callbacks));
        addBranch(playerName + valueEnding);
        addBranch(opponentName + valueEnding);
    }

    @Override
    public boolean isRequired() {
        return true;
    }

    @Override
    public void getReviewItems(ArrayList<ReviewItem> dest) {
        super.getReviewItems(dest);

        if (data.getString(SIMPLE_DATA_KEY, "").equals(playerName + valueEnding)) {
            dest.add(new ReviewItem("Who breaks first?", playerName, getKey()));
        } else if (data.getString(SIMPLE_DATA_KEY, "").equals(opponentName + valueEnding)) {
            dest.add(new ReviewItem("Who breaks first?", opponentName, getKey()));
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

        if (data.getString(SIMPLE_DATA_KEY, "|!_)(@%!)*(!@%$!@").equals(this.playerName + valueEnding))
            data.putString(SIMPLE_DATA_KEY, playerName + valueEnding);
        else if (data.getString(SIMPLE_DATA_KEY, "|!_)(@%!)*(!@%$!@").equals(this.opponentName + valueEnding))
            data.putString(SIMPLE_DATA_KEY, opponentName + valueEnding);

        this.playerName = playerName;
        this.opponentName = opponentName;

        branches.get(3).choice = playerName + valueEnding;
        branches.get(4).choice = opponentName + valueEnding;
    }
}
