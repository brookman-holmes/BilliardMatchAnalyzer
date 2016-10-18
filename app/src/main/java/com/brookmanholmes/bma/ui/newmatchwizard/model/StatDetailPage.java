package com.brookmanholmes.bma.ui.newmatchwizard.model;

import com.brookmanholmes.bma.wizard.model.ModelCallbacks;
import com.brookmanholmes.bma.wizard.model.ReviewItem;
import com.brookmanholmes.bma.wizard.model.SingleFixedChoicePage;

import java.util.ArrayList;

/**
 * Created by Brookman Holmes on 3/9/2016.
 */
class StatDetailPage extends SingleFixedChoicePage implements RequiresPlayerNames, UpdatesMatchBuilder {
    private String playerName = "Player 1-";
    private String opponentName = "Player 2-";
    private String startValue;

    StatDetailPage(ModelCallbacks callbacks, String title) {
        super(callbacks, title);
    }

    @Override
    public void getReviewItems(ArrayList<ReviewItem> dest) {
        dest.add(new ReviewItem("Data collection", data.getString(SIMPLE_DATA_KEY), getKey()));
}

    @Override
    public void setPlayerNames(String playerName, String opponentName) {
        if (data.getString(SIMPLE_DATA_KEY, "").equals(String.format(startValue, this.playerName)))
            data.putString(SIMPLE_DATA_KEY, String.format(startValue, playerName));
        else if (data.getString(SIMPLE_DATA_KEY, "").equals(String.format(startValue, this.opponentName)))
            data.putString(SIMPLE_DATA_KEY, String.format(startValue, opponentName));

        this.playerName = playerName;
        this.opponentName = opponentName;

        choices.set(2, String.format(startValue, playerName));
        choices.set(3, String.format(startValue, opponentName));
    }

    @Override
    public void updateMatchBuilder(CreateNewMatchWizardModel model) {
        model.setStatDetail(data.getString(SIMPLE_DATA_KEY));
    }

    @Override
    public SingleFixedChoicePage setChoices(String... choices) {
        startValue = choices[3];
        return super.setChoices(choices);
    }
}
