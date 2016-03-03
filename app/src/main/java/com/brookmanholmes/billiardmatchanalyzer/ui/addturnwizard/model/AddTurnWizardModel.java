package com.brookmanholmes.billiardmatchanalyzer.ui.addturnwizard.model;

import android.content.Context;
import android.os.Bundle;

import com.brookmanholmes.billiardmatchanalyzer.utils.MatchDialogHelperUtils;
import com.brookmanholmes.billiardmatchanalyzer.wizard.model.AbstractWizardModel;
import com.brookmanholmes.billiardmatchanalyzer.wizard.model.Page;
import com.brookmanholmes.billiardmatchanalyzer.wizard.model.PageList;
import com.brookmanholmes.billiards.game.util.GameType;

/**
 * Created by Brookman Holmes on 2/20/2016.
 */
public class AddTurnWizardModel extends AbstractWizardModel {
    Bundle matchData;
    TurnBuilder turnBuilder;

    public AddTurnWizardModel(Context context, Bundle matchData) {
        super(context);

        this.matchData = matchData;
        rootPageList = onNewRootPageList();
        turnBuilder = new TurnBuilder(GameType.valueOf(matchData.getString(MatchDialogHelperUtils.GAME_TYPE_KEY)));
    }

    @Override
    public void onPageDataChanged(Page page) {
        super.onPageDataChanged(page);

        if (page instanceof UpdatesTurnInfo) {
            ((UpdatesTurnInfo) page).updateTurnInfo(turnBuilder);
        }

        updatePagesWithTurnInfo();
    }

    @Override
    public void onPageTreeChanged() {
        super.onPageTreeChanged();

        updatePagesWithTurnInfo();
    }

    public void updatePagesWithTurnInfo() {
        for (Page page : getCurrentPageSequence()) {
            if (page instanceof RequiresUpdatedTurnInfo) {
                ((RequiresUpdatedTurnInfo) page).getNewTurnInfo(turnBuilder);
            }
        }
    }

    @Override
    protected PageList onNewRootPageList() {
        return new PageList(
                new BreakPage(this, matchData),
                new TurnEndPage(this, matchData)
        );
    }
}
