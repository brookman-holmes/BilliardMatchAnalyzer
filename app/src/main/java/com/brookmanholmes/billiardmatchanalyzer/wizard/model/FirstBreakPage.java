package com.brookmanholmes.billiardmatchanalyzer.wizard.model;

/**
 * Created by Brookman Holmes on 1/7/2016.
 */
public class FirstBreakPage extends SingleFixedChoicePage {
    String playerName = "Player 1-", opponentName = "Player 2-";


    public FirstBreakPage(ModelCallbacks callbacks) {
        super(callbacks, "Who breaks first?");
        mChoices.add(playerName);
        mChoices.add(opponentName);
    }

    @Override
    public boolean isRequired() {
        return true;
    }

    @Override
    public void setPlayerNames(String player, String opponent) {
        if (mData.getString(SIMPLE_DATA_KEY, "|!_)(@%!)*(!@%$!@").equals(playerName))
            mData.putString(SIMPLE_DATA_KEY, player);
        else if (mData.getString(SIMPLE_DATA_KEY, "|!_)(@%!)*(!@%$!@").equals(opponentName))
            mData.putString(SIMPLE_DATA_KEY, opponent);

        playerName = player;
        opponentName = opponent;

        mChoices.set(0, player);
        mChoices.set(1, opponent);
    }
}
