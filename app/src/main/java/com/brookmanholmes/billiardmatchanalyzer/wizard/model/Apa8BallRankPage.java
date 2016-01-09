package com.brookmanholmes.billiardmatchanalyzer.wizard.model;

import com.brookmanholmes.billiards.game.util.ApaRaceToHelper;
import com.brookmanholmes.billiards.game.util.RaceTo;

import java.util.ArrayList;

/**
 * Created by Brookman Holmes on 1/7/2016.
 */
public class Apa8BallRankPage extends SingleFixedChoicePage {
    private int playerNumber;
    private RaceTo raceTo;

    public Apa8BallRankPage(ModelCallbacks callbacks, int playerNumber) {
        super(callbacks, "Player " + playerNumber + "-'s Rank");
        this.playerNumber = playerNumber;
        setParentKey("RankPage" + playerNumber);
        mChoices.add("2");
        mChoices.add("3");
        mChoices.add("4");
        mChoices.add("5");
        mChoices.add("6");
        mChoices.add("7");
    }

    @Override
    public boolean isRequired() {
        return true;
    }

    @Override
    public void setPlayerNames(String player, String opponent) {
        if (playerNumber == 1) {
            mTitle = player + "'s Rank";
        } else {
            mTitle = opponent + "'s Rank";
        }
    }

    @Override
    public void setPlayerRanks(int playerRank, int opponentRank) {
        raceTo = new RaceTo(playerRank, opponentRank);
    }

    @Override
    public void getReviewItems(ArrayList<ReviewItem> dest) {
        dest.add(new ReviewItem(getTitle(), mData.getString(SIMPLE_DATA_KEY) + ", races to " + getRaceTo() + " games", getKey()));
    }

    private int getRaceTo() {
        if (playerNumber == 1) {
            return raceTo.getPlayerRaceTo();
        } else return raceTo.getOpponentRaceTo();
    }
}
