package com.brookmanholmes.bma.ui.newmatchwizard.model;

import android.content.Context;

import com.brookmanholmes.bma.R;
import com.brookmanholmes.bma.wizard.model.BranchPage;
import com.brookmanholmes.bma.wizard.model.ModelCallbacks;
import com.brookmanholmes.bma.wizard.model.Page;
import com.brookmanholmes.bma.wizard.model.ReviewItem;

import java.util.ArrayList;

/**
 * Created by Brookman Holmes on 1/8/2016.
 */
class GameChoicePage extends BranchPage implements RequiresPlayerNames, UpdatesMatchBuilder {
    private final String theBreak;
    private final String alternateBreak;
    private final String winnerBreak;
    private final String americanRotation;
    private final String apa8Ball;
    private final String apa9Ball;
    private final String game;

    GameChoicePage(ModelCallbacks callbacks, String title, Context context, String parentKey) {
        super(callbacks, title);

        theBreak = context.getString(R.string.title_page_break);
        alternateBreak = context.getString(R.string.break_alternate);
        winnerBreak = context.getString(R.string.break_winner);
        americanRotation = context.getString(R.string.game_american_rotation);
        apa8Ball = context.getString(R.string.game_apa_eight);
        apa9Ball = context.getString(R.string.game_apa_nine);
        game = context.getString(R.string.prompt_game);
        this.parentKey = parentKey;
    }

    @Override
    public void getReviewItems(ArrayList<ReviewItem> dest) {
        dest.add(new ReviewItem(game, data.getString(SIMPLE_DATA_KEY), getKey()));
        if (data.getString(SIMPLE_DATA_KEY, "").equals(americanRotation))
            dest.add(new ReviewItem(theBreak, alternateBreak, getKey()));
        else if (data.getString(SIMPLE_DATA_KEY, "").equals(apa8Ball) || data.getString(SIMPLE_DATA_KEY, "").equals(apa9Ball))
            dest.add(new ReviewItem(theBreak, winnerBreak, getKey()));
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

    @Override
    public void updateMatchBuilder(CreateNewMatchWizardModel model) {
        model.setGameType(data.getString(SIMPLE_DATA_KEY, ""));
    }
}
