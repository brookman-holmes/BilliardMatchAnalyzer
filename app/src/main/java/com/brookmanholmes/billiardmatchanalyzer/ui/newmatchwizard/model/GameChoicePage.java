package com.brookmanholmes.billiardmatchanalyzer.ui.newmatchwizard.model;

import com.brookmanholmes.billiardmatchanalyzer.wizard.model.BranchPage;
import com.brookmanholmes.billiardmatchanalyzer.wizard.model.ModelCallbacks;
import com.brookmanholmes.billiardmatchanalyzer.wizard.model.Page;
import com.brookmanholmes.billiardmatchanalyzer.wizard.model.ReviewItem;

import java.util.ArrayList;

/**
 * Created by Brookman Holmes on 1/8/2016.
 */
public class GameChoicePage extends BranchPage implements RequiresPlayerNames {
    public GameChoicePage(ModelCallbacks callbacks) {
        super(callbacks, "Game");
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

        if (data.getString(SIMPLE_DATA_KEY, "").equals("American Rotation"))
            dest.add(new ReviewItem("The break", "Alternate", getKey()));
        else if (data.getString(SIMPLE_DATA_KEY, "").startsWith("APA"))
            dest.add(new ReviewItem("The break", "Winner", getKey()));
    }

    @Override
    public void setPlayerNames(String playerName, String opponentName) {
        for (Branch branch : branches) {
            for (Page page : branch.childPageList) {
                if (page instanceof RequiresPlayerNames) {
                    ((RequiresPlayerNames) page).setPlayerNames(playerName, opponentName);
                }
            }
        }
    }
}
