package com.brookmanholmes.billiardmatchanalyzer.ui.addturnwizard.model;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;

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

        turnBuilder = new TurnBuilder(GameType.valueOf(matchData.getString(MatchDialogHelperUtils.GAME_TYPE_KEY)),
                matchData.getIntegerArrayList(MatchDialogHelperUtils.BALLS_ON_TABLE_KEY));
        this.matchData = matchData;
        rootPageList = onNewRootPageList();
    }

    @Override
    public void onPageDataChanged(Page page) {
        super.onPageDataChanged(page);

        if (page instanceof UpdatesTurnInfo) {
            ((UpdatesTurnInfo) page).updateTurnInfo(turnBuilder);
        }

        Log.i("Model", turnBuilder.advData());
        updatePagesWithTurnInfo();

        Log.i("Model", getCurrentPageSequence().toString());
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
        if (matchData.getBoolean(MatchDialogHelperUtils.ALLOW_BREAK_AGAIN_KEY)) {
            return new PageList(new TurnEndPage(this, matchData));
        }

        if (matchData.getBoolean(MatchDialogHelperUtils.NEW_GAME_KEY))
            return new PageList(
                    new BreakPage(this, matchData),
                    new TurnEndPage(this, matchData)
            );
        else
            return new PageList(
                    new ShotPage(this, matchData),
                    new TurnEndPage(this, matchData)
            );
    }

    public TurnBuilder getTurnBuilder() {
        // make sure data is current
        for (Page page : getCurrentPageSequence())
            if (page instanceof UpdatesTurnInfo)
                ((UpdatesTurnInfo) page).updateTurnInfo(turnBuilder);

        Log.i("Model", turnBuilder.advData());
        return turnBuilder;
    }
}
