package com.brookmanholmes.bma.ui.newmatchwizard.model;

import com.brookmanholmes.billiards.game.util.PlayerTurn;
import com.brookmanholmes.bma.wizard.model.ModelCallbacks;
import com.brookmanholmes.bma.wizard.model.SingleFixedChoicePage;

/**
 * Created by helios on 2/20/2016.
 */
public abstract class RankPage extends SingleFixedChoicePage implements RequiresPlayerNames, UpdatesMatchBuilder {
    private final PlayerTurn playerTurn;
    private final String titleFormat;

    RankPage(ModelCallbacks callbacks, String title, PlayerTurn playerTurn) {
        super(callbacks, String.format(title, playerTurn.toString()));

        titleFormat = title;
        this.playerTurn = playerTurn;

        setChoices();
        setValue("5");
    }

    protected abstract void setChoices();

    @Override public void setPlayerNames(String playerName, String opponentName) {
        if (playerTurn == PlayerTurn.PLAYER) {
            title = String.format(titleFormat, playerName);
        } else {
            title = String.format(titleFormat, opponentName);
        }
    }

    @Override public void updateMatchBuilder(CreateNewMatchWizardModel model) {
        model.setPlayerRank(playerTurn, data.getString(SIMPLE_DATA_KEY, "5"));
    }
}
