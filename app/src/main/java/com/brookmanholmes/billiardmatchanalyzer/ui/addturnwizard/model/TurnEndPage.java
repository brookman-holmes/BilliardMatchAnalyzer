package com.brookmanholmes.billiardmatchanalyzer.ui.addturnwizard.model;

import android.os.Bundle;
import android.support.v4.app.Fragment;

import com.brookmanholmes.billiardmatchanalyzer.ui.addturnwizard.fragments.TurnEndFragment;
import com.brookmanholmes.billiardmatchanalyzer.utils.MatchDialogHelperUtils;
import com.brookmanholmes.billiardmatchanalyzer.wizard.model.BranchPage;
import com.brookmanholmes.billiardmatchanalyzer.wizard.model.ModelCallbacks;
import com.brookmanholmes.billiards.game.util.GameType;
import com.brookmanholmes.billiards.game.util.PlayerTurn;
import com.brookmanholmes.billiards.match.Match;
import com.brookmanholmes.billiards.turn.TableStatus;
import com.brookmanholmes.billiards.turn.TurnEnd;
import com.brookmanholmes.billiards.turn.TurnEndOptions;
import com.brookmanholmes.billiards.turn.helpers.TurnEndHelper;

import java.util.ArrayList;

import static com.brookmanholmes.billiardmatchanalyzer.utils.MatchDialogHelperUtils.STATS_LEVEL_KEY;

/**
 * Created by Brookman Holmes on 2/20/2016.
 */
public class TurnEndPage extends BranchPage implements RequiresUpdatedTurnInfo, UpdatesTurnInfo {
    TurnEndHelper turnEndHelper;
    TurnEndFragment fragment;

    public TurnEndPage(ModelCallbacks callbacks, String title, Bundle matchData) {
        super(callbacks, title);
        data.putAll(matchData);
        setRequired(true);

        turnEndHelper = TurnEndHelper.newTurnEndHelper(GameType.valueOf(data.getString(MatchDialogHelperUtils.GAME_TYPE_KEY)));
    }

    @Override public Fragment createFragment() {
        TurnEndOptions options = turnEndHelper.create(MatchDialogHelperUtils.createGameStatusFromBundle(data),
                TableStatus.newTable(GameType.valueOf(data.getString(MatchDialogHelperUtils.GAME_TYPE_KEY)), data.getIntegerArrayList(MatchDialogHelperUtils.BALLS_ON_TABLE_KEY)));
        ArrayList<String> stringList = new ArrayList<>();
        for (TurnEnd ending : options.possibleEndings) {
            stringList.add(ending.name());
        }

        return TurnEndFragment.create(getKey(), stringList, options.defaultCheck.name());
    }

    @Override public void getNewTurnInfo(TurnBuilder turnBuilder) {
        TurnEndOptions options = turnEndHelper.create(MatchDialogHelperUtils.createGameStatusFromBundle(data),
                turnBuilder.tableStatus);
        updateFragment(options);
    }

    @Override public void updateTurnInfo(TurnBuilder turnBuilder) {
        turnBuilder.turnEnd = data.getString(SIMPLE_DATA_KEY);
        String temp = data.getString(SIMPLE_DATA_KEY, "");
        if ((temp.equals("Miss on break shot")
                || temp.equals("Safety")
                || temp.equals("Unsuccessful safety")
                || temp.equals("Miss")) && currentPlayerTurnAndAdvancedStats())
            turnBuilder.advStats.use(true);
        else turnBuilder.advStats.use(false);

    }

    private boolean currentPlayerTurnAndAdvancedStats() {
        PlayerTurn turn = PlayerTurn.valueOf(data.getString(MatchDialogHelperUtils.TURN_KEY));
        Match.StatsDetail detail = Match.StatsDetail.valueOf(data.getString(STATS_LEVEL_KEY));
        if (turn == PlayerTurn.PLAYER && detail == Match.StatsDetail.ADVANCED_PLAYER)
            return true;
        else if (turn == PlayerTurn.OPPONENT && detail == Match.StatsDetail.ADVANCED_OPPONENT)
            return true;
        else return detail == Match.StatsDetail.ADVANCED;
    }

    public void registerListener(TurnEndFragment fragment) {
        this.fragment = fragment;
    }

    public void unregisterListener() {
        fragment = null;
    }

    public void updateFragment(TurnEndOptions options) {
        if (fragment != null) {

            fragment.updateOptions(options.possibleEndings, options.defaultCheck);
        }
    }
}
