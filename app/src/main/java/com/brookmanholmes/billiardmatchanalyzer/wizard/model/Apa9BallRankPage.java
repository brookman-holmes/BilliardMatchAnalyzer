package com.brookmanholmes.billiardmatchanalyzer.wizard.model;

import com.brookmanholmes.billiards.game.util.ApaRaceToHelper;

import java.util.ArrayList;

/**
 * Created by Brookman Holmes on 1/7/2016.
 */
public class Apa9BallRankPage extends SingleFixedChoicePage {
    private int playerNumber;
    private String playerName;

    public Apa9BallRankPage(ModelCallbacks callbacks, int playerNumber) {
        super(callbacks, "Player " + playerNumber + "-'s Rank");
        this.playerNumber = playerNumber;
        mChoices.add("1");
        mChoices.add("2");
        mChoices.add("3");
        mChoices.add("4");
        mChoices.add("5");
        mChoices.add("6");
        mChoices.add("7");
        mChoices.add("8");
        mChoices.add("9");
    }

    @Override
    public boolean isRequired() {
        return true;
    }

    @Override
    public void setPlayerNames(String player, String opponent) {
        if (playerNumber == 1) {
            playerName = player;
        } else {
            playerName = opponent;
        }


        mTitle = playerName + "'s Rank";
    }

    @Override
    public void getReviewItems(ArrayList<ReviewItem> dest) {
        dest.add(new ReviewItem(getTitle(), mData.getString(SIMPLE_DATA_KEY) + ", wins with " + getRaceTo() + " points", getKey()));
    }


    private String getRaceTo() {
        return String.valueOf(ApaRaceToHelper.apa9BallRaceTo(Integer.valueOf(mData.getString(SIMPLE_DATA_KEY, "0"))));
    }
}
