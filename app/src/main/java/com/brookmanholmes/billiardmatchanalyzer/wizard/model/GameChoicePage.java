package com.brookmanholmes.billiardmatchanalyzer.wizard.model;

import java.util.ArrayList;

/**
 * Created by Brookman Holmes on 1/8/2016.
 */
public class GameChoicePage extends BranchPage {
    public GameChoicePage(ModelCallbacks callbacks) {
        super(callbacks, "Game");
        setParentKey("GameChoicePage");
        //addBranch("American Rotation");

        addBranch("APA 8 ball",
                new Apa8BallRankPage(callbacks, 1),
                new Apa8BallRankPage(callbacks, 2));

        addBranch("APA 9 ball",
                new Apa9BallRankPage(callbacks, 1),
                new Apa9BallRankPage(callbacks, 2));

        addBranch("BCA 8 ball",
                new BreakTypePage(callbacks));

        addBranch("BCA 9 ball",
                new BreakTypePage(callbacks));

        addBranch("BCA 10 ball",
                new BreakTypePage(callbacks));

        //addBranch("Straight pool", new FirstBreakPage(callbacks));
    }

    @Override
    public boolean isRequired() {
        return true;
    }

    @Override
    public void getReviewItems(ArrayList<ReviewItem> dest) {
        super.getReviewItems(dest);

        if (mData.getString(SIMPLE_DATA_KEY, "").equals("American Rotation"))
            dest.add(new ReviewItem("The break", "Alternate", getKey()));
        else if (mData.getString(SIMPLE_DATA_KEY, "").startsWith("APA"))
            dest.add(new ReviewItem("The break", "Winner", getKey()));
    }
}
