package com.brookmanholmes.billiardmatchanalyzer.wizard.model;

/**
 * Created by Brookman Holmes on 1/7/2016.
 */
public class BreakTypePage extends SingleFixedChoicePage {
    String playerName = "Player 1-", opponentName = "Player 2-";

    public BreakTypePage(ModelCallbacks callbacks) {
        super(callbacks, "The break");
        mChoices.add("Winner");
        mChoices.add("Alternate");
        mChoices.add("Loser");
        mChoices.add(playerName);
        mChoices.add(opponentName);
    }

    @Override
    public boolean isRequired() {
        return true;
    }

    @Override
    public void setPlayerNames(String player, String opponent) {
        mChoices.set(3, player);
        mChoices.set(4, opponent);
    }
}
