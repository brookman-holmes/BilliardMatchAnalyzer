package com.brookmanholmes.billiardmatchanalyzer.ui.newmatchwizard.model;

import com.brookmanholmes.billiardmatchanalyzer.wizard.model.ModelCallbacks;
import com.brookmanholmes.billiardmatchanalyzer.wizard.model.SingleFixedChoicePage;

/**
 * Created by helios on 2/20/2016.
 */
public abstract class RankPage extends SingleFixedChoicePage implements RequiresPlayerNames{
    protected int playerNumber;

    public RankPage(ModelCallbacks callbacks, int playerNumber) {
        super(callbacks, "Player " + playerNumber + "-'s Rank");
        this.playerNumber = playerNumber;
        setChoices();
        setRequired(true);
        setValue("5");
    }

    protected abstract void setChoices();

    @Override
    public void setPlayerNames(String playerName, String opponentName) {
        if (playerNumber == 1) {
            title = playerName + "'s Rank";
        } else {
            title = opponentName + "'s Rank";
        }
    }
}
