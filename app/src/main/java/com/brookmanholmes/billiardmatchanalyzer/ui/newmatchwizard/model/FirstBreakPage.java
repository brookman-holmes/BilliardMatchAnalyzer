package com.brookmanholmes.billiardmatchanalyzer.ui.newmatchwizard.model;

import com.brookmanholmes.billiardmatchanalyzer.wizard.model.ModelCallbacks;
import com.brookmanholmes.billiardmatchanalyzer.wizard.model.SingleFixedChoicePage;
import com.brookmanholmes.billiards.game.util.PlayerTurn;

/**
 * Created by Brookman Holmes on 1/7/2016.
 */
public class FirstBreakPage extends SingleFixedChoicePage implements RequiresPlayerNames, UpdatesMatchBuilder {
    String playerName = "Player 1", opponentName = "Player 2";
    String parentPage;

    public FirstBreakPage(ModelCallbacks callbacks, String title, String parentPage) {
        super(callbacks, title);

        this.parentPage = parentPage;
    }

    @Override public boolean isCompleted() {
        return super.isCompleted();
    }

    @Override public String getKey() {
        return parentPage + ":" + super.getKey();
    }

    @Override public void setPlayerNames(String playerName, String opponentName) {
        if (data.getString(SIMPLE_DATA_KEY, "").equals(this.playerName))
            data.putString(SIMPLE_DATA_KEY, playerName);
        else if (data.getString(SIMPLE_DATA_KEY, "").equals(this.opponentName))
            data.putString(SIMPLE_DATA_KEY, opponentName);

        this.playerName = playerName;
        this.opponentName = opponentName;

        choices.set(0, playerName);
        choices.set(1, opponentName);
    }

    @Override public void updateMatchBuilder(CreateNewMatchWizardModel model) {
        if (playerName.equals(data.getString(SIMPLE_DATA_KEY)))
            model.setFirstBreaker(PlayerTurn.PLAYER);
        else if (opponentName.equals(data.getString(SIMPLE_DATA_KEY)))
            model.setFirstBreaker(PlayerTurn.OPPONENT);
    }
}
