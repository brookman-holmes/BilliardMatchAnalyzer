package com.brookmanholmes.billiardmatchanalyzer.wizard.model;

/**
 * Created by Brookman Holmes on 1/7/2016.
 */
public class Apa8BallRankPage extends SingleFixedChoicePage {
    private int playerNumber;

    public Apa8BallRankPage(ModelCallbacks callbacks, int playerNumber) {
        super(callbacks, "Player " + playerNumber + "-'s Rank");
        this.playerNumber = playerNumber;
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
}
