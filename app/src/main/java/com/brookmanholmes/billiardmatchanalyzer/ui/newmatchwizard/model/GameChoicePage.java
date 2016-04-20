package com.brookmanholmes.billiardmatchanalyzer.ui.newmatchwizard.model;

import android.content.Context;

import com.brookmanholmes.billiardmatchanalyzer.R;
import com.brookmanholmes.billiardmatchanalyzer.wizard.model.BranchPage;
import com.brookmanholmes.billiardmatchanalyzer.wizard.model.ModelCallbacks;
import com.brookmanholmes.billiardmatchanalyzer.wizard.model.Page;
import com.brookmanholmes.billiardmatchanalyzer.wizard.model.ReviewItem;

import java.util.ArrayList;

/**
 * Created by Brookman Holmes on 1/8/2016.
 */
public class GameChoicePage extends BranchPage implements RequiresPlayerNames, UpdatesMatchBuilder {
    String theBreak;
    String alternateBreak, winnerBreak;
    String americanRotation;
    String apa8Ball, apa9Ball;

    public GameChoicePage(ModelCallbacks callbacks, String title, Context context) {
        super(callbacks, title);

        theBreak = context.getString(R.string.title_page_break);
        alternateBreak = context.getString(R.string.break_alternate);
        winnerBreak = context.getString(R.string.break_winner);
        americanRotation = context.getString(R.string.game_american_rotation);
        apa8Ball = context.getString(R.string.game_apa_eight);
        apa9Ball = context.getString(R.string.game_apa_nine);
    }

    @Override public void getReviewItems(ArrayList<ReviewItem> dest) {
        super.getReviewItems(dest);

        if (data.getString(SIMPLE_DATA_KEY, "").equals(americanRotation))
            dest.add(new ReviewItem(theBreak, alternateBreak, getKey()));
        else if (data.getString(SIMPLE_DATA_KEY, "").equals(apa8Ball) || data.getString(SIMPLE_DATA_KEY, "").equals(apa9Ball))
            dest.add(new ReviewItem(theBreak, winnerBreak, getKey()));
    }

    @Override public void setPlayerNames(String playerName, String opponentName) {
        for (Branch branch : branches) {
            for (Page page : branch.childPageList) {
                if (page instanceof RequiresPlayerNames) {
                    ((RequiresPlayerNames) page).setPlayerNames(playerName, opponentName);
                }
            }
        }
    }

    @Override public void updateMatchBuilder(CreateNewMatchWizardModel model) {
        model.setGameType(data.getString(SIMPLE_DATA_KEY, ""));
    }
}
