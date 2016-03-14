package com.brookmanholmes.billiardmatchanalyzer.ui.newmatchwizard.model;

import com.brookmanholmes.billiardmatchanalyzer.wizard.model.ModelCallbacks;
import com.brookmanholmes.billiardmatchanalyzer.wizard.model.SingleFixedChoicePage;

/**
 * Created by Brookman Holmes on 3/9/2016.
 */
public class StatDetailPage extends SingleFixedChoicePage implements RequiresPlayerNames {
    String playerName = "Player 1-", opponentName = "Player 2-";
    String startValue = "Advanced stats for ";

    public StatDetailPage(ModelCallbacks callbacks) {
        super(callbacks, "Select detail level for stats");

        setChoices("Simple", "Normal", "Advanced", startValue + playerName, startValue + opponentName);
        setValue("Normal");
        setRequired(true);
    }

    @Override
    public void setPlayerNames(String playerName, String opponentName) {
        if (data.getString(SIMPLE_DATA_KEY, "|!_)(@%!)*(!@%$!@").equals(startValue + this.playerName))
            data.putString(SIMPLE_DATA_KEY, startValue + playerName);
        else if (data.getString(SIMPLE_DATA_KEY, "|!_)(@%!)*(!@%$!@").equals(startValue + this.opponentName))
            data.putString(SIMPLE_DATA_KEY, startValue + opponentName);

        this.playerName = playerName;
        this.opponentName = opponentName;

        choices.set(3, startValue + playerName);
        choices.set(4, startValue + opponentName);
    }
}
