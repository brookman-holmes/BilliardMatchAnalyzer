package com.brookmanholmes.billiardmatchanalyzer.ui.newmatchwizard.model;

import com.brookmanholmes.billiardmatchanalyzer.wizard.model.ModelCallbacks;
import com.brookmanholmes.billiardmatchanalyzer.wizard.model.SingleFixedChoicePage;

/**
 * Created by Brookman Holmes on 1/7/2016.
 */
public class FirstBreakPage extends SingleFixedChoicePage implements RequiresPlayerNames {
    String playerName = "Player 1-", opponentName = "Player 2-";
    String parentPage;

    public FirstBreakPage(ModelCallbacks callbacks, String parentPage) {
        super(callbacks, "Who breaks first?");

        this.parentPage = parentPage;

        choices.add(playerName);
        choices.add(opponentName);
    }

    @Override
    public boolean isRequired() {
        return true;
    }

    @Override
    public boolean isCompleted() {
        return super.isCompleted();
    }

    @Override
    public String getKey() {
        return parentPage + ":" + super.getKey();
    }

    @Override
    public void setPlayerNames(String playerName, String opponentName) {
        if (data.getString(SIMPLE_DATA_KEY, "|!_)(@%!)*(!@%$!@").equals(this.playerName))
            data.putString(SIMPLE_DATA_KEY, playerName);
        else if (data.getString(SIMPLE_DATA_KEY, "|!_)(@%!)*(!@%$!@").equals(this.opponentName))
            data.putString(SIMPLE_DATA_KEY, opponentName);

        this.playerName = playerName;
        this.opponentName = opponentName;

        choices.set(0, playerName);
        choices.set(1, opponentName);
    }
}
