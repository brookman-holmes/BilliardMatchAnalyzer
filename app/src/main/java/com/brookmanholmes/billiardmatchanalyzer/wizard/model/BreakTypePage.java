package com.brookmanholmes.billiardmatchanalyzer.wizard.model;

import java.util.ArrayList;

/**
 * Created by Brookman Holmes on 1/7/2016.
 */
public class BreakTypePage extends BranchPage {
    String playerName = "Player 1-", opponentName = "Player 2-";
    String valueEnding = " always breaks";

    public BreakTypePage(ModelCallbacks callbacks) {
        super(callbacks, "The break");
        setParentKey("BreakTypePage");
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
    public void setPlayerNames(String player, String opponent) {
        if (mData.getString(SIMPLE_DATA_KEY, "|!_)(@%!)*(!@%$!@").equals(playerName + valueEnding))
            mData.putString(SIMPLE_DATA_KEY, player + valueEnding);
        else if (mData.getString(SIMPLE_DATA_KEY, "|!_)(@%!)*(!@%$!@").equals(opponentName + valueEnding))
            mData.putString(SIMPLE_DATA_KEY, opponent + valueEnding);

        playerName = player;
        opponentName = opponent;

        mChoices.set(3, player + valueEnding);
        mChoices.set(4, opponent + valueEnding);
    }

    @Override
    public void getReviewItems(ArrayList<ReviewItem> dest) {
        super.getReviewItems(dest);

        if (mData.getString(SIMPLE_DATA_KEY, "").equals(playerName + valueEnding)) {
            dest.add(new ReviewItem("Who breaks first?", playerName, getKey()));
        } else if (mData.getString(SIMPLE_DATA_KEY, "").equals(opponentName + valueEnding)) {
            dest.add(new ReviewItem("Who breaks first?", opponentName, getKey()));
        }
    }
}
