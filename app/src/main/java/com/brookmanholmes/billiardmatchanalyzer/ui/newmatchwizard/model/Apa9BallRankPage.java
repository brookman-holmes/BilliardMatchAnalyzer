package com.brookmanholmes.billiardmatchanalyzer.ui.newmatchwizard.model;

import com.brookmanholmes.billiardmatchanalyzer.wizard.model.ModelCallbacks;
import com.brookmanholmes.billiardmatchanalyzer.wizard.model.Page;
import com.brookmanholmes.billiardmatchanalyzer.wizard.model.ReviewItem;
import com.brookmanholmes.billiards.game.util.ApaRaceToHelper;

import java.util.ArrayList;

/**
 * Created by Brookman Holmes on 1/7/2016.
 */
public class Apa9BallRankPage extends RankPage {
    public Apa9BallRankPage(ModelCallbacks callbacks, int playerNumber) {
        super(callbacks, playerNumber);
    }

    @Override
    public void getReviewItems(ArrayList<ReviewItem> dest) {
        dest.add(new ReviewItem(getTitle(), data.getString(Page.SIMPLE_DATA_KEY) + ", wins with " + getRaceTo() + " points", getKey()));
    }

    private String getRaceTo() {
        return String.valueOf(ApaRaceToHelper.apa9BallRaceTo(Integer.valueOf(data.getString(Page.SIMPLE_DATA_KEY, "0"))));
    }

    @Override
    protected void setChoices() {
        choices.add("1");
        choices.add("2");
        choices.add("3");
        choices.add("4");
        choices.add("5");
        choices.add("6");
        choices.add("7");
        choices.add("8");
        choices.add("9");
    }
}
