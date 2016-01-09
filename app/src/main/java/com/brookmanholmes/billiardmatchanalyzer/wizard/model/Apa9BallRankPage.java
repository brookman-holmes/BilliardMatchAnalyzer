package com.brookmanholmes.billiardmatchanalyzer.wizard.model;

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
        dest.add(new ReviewItem(getTitle(), mData.getString(SIMPLE_DATA_KEY) + ", races to " + getRaceTo(), getKey()));
    }


    private String getRaceTo() {
        switch (Integer.valueOf(mData.getString(SIMPLE_DATA_KEY, "0"))) {
            case 1:
                return "14";
            case 2:
                return "19";
            case 3:
                return "25";
            case 4:
                return "31";
            case 5:
                return "38";
            case 6:
                return "46";
            case 7:
                return "55";
            case 8:
                return "65";
            case 9:
                return "75";
            default:
                return "0";
        }
    }
}
