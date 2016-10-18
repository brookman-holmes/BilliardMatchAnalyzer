package com.brookmanholmes.bma.ui.newmatchwizard.model;

import com.brookmanholmes.billiards.game.PlayerTurn;
import com.brookmanholmes.bma.wizard.model.ModelCallbacks;
import com.brookmanholmes.bma.wizard.model.SingleFixedChoicePage;

/**
 * Created by Brookman Holmes on 1/7/2016.
 */
class FirstBreakPage extends SingleFixedChoicePage implements RequiresPlayerNames, UpdatesMatchBuilder {
    private String playerName = "Player 1";
    private String opponentName = "Player 2";

    FirstBreakPage(ModelCallbacks callbacks, String title) {
        super(callbacks, title);

    }

    @Override
    public void setPlayerNames(String playerName, String opponentName) {
        if (data.getString(SIMPLE_DATA_KEY, "").equals(this.playerName))
            data.putString(SIMPLE_DATA_KEY, playerName);
        else if (data.getString(SIMPLE_DATA_KEY, "").equals(this.opponentName))
            data.putString(SIMPLE_DATA_KEY, opponentName);

        this.playerName = playerName;
        this.opponentName = opponentName;

        choices.set(0, playerName);
        choices.set(1, opponentName);
    }

    @Override
    public void updateMatchBuilder(CreateNewMatchWizardModel model) {
        if (playerName.equals(data.getString(SIMPLE_DATA_KEY)))
            model.setFirstBreaker(PlayerTurn.PLAYER);
        else if (opponentName.equals(data.getString(SIMPLE_DATA_KEY)))
            model.setFirstBreaker(PlayerTurn.OPPONENT);
    }
}
