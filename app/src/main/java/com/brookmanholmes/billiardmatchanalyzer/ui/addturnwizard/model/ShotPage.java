package com.brookmanholmes.billiardmatchanalyzer.ui.addturnwizard.model;

import android.os.Bundle;
import android.support.v4.app.Fragment;

import com.brookmanholmes.billiardmatchanalyzer.ui.addturnwizard.fragments.ShotFragment;
import com.brookmanholmes.billiardmatchanalyzer.utils.MatchDialogHelperUtils;
import com.brookmanholmes.billiardmatchanalyzer.wizard.model.ModelCallbacks;
import com.brookmanholmes.billiardmatchanalyzer.wizard.model.Page;
import com.brookmanholmes.billiardmatchanalyzer.wizard.model.ReviewItem;
import com.brookmanholmes.billiards.game.util.BallStatus;
import com.brookmanholmes.billiards.inning.TableStatus;

import java.util.ArrayList;

/**
 * Created by Brookman Holmes on 2/20/2016.
 */
public class ShotPage extends Page implements RequiresUpdatedTurnInfo, UpdatesTurnInfo {
    ShotFragment fragment;
    TableStatus tableStatus;

    public ShotPage(ModelCallbacks callbacks, Bundle matchData) {
        super(callbacks, "Shot page");

        data.putAll(matchData);

        tableStatus = TableStatus.newTable(MatchDialogHelperUtils.createGameStatusFromBundle(matchData).gameType,
                data.getIntegerArrayList(MatchDialogHelperUtils.BALLS_ON_TABLE_KEY));
    }

    @Override
    public Fragment createFragment() {
        return ShotFragment.create(getKey(), getData());
    }

    @Override
    public void getReviewItems(ArrayList<ReviewItem> dest) {
    }

    @Override
    public void getNewTurnInfo(TurnBuilder turnBuilder) {
        for (int ball = 1; ball <= tableStatus.size(); ball++) {
            tableStatus.setBallTo(turnBuilder.tableStatus.getBallStatus(ball), ball);
        }
        updateFragment();
    }

    public BallStatus updateBallStatus(int ball) {
        BallStatus ballStatus = tableStatus.getBallStatus(ball);

        BallStatus newBallStatus = incrementBallStatus(ballStatus);
        tableStatus.setBallTo(newBallStatus, ball);

        notifyDataChanged();
        updateFragment();

        return newBallStatus;
    }

    private BallStatus incrementBallStatus(BallStatus ballStatus) {
        switch (ballStatus) {
            case ON_TABLE:
                return BallStatus.MADE;
            case MADE:
                return BallStatus.DEAD;
            case DEAD:
                return BallStatus.ON_TABLE;
            // game ball for 8/10 ball
            case GAME_BALL_MADE_ON_BREAK:
                return BallStatus.GAME_BALL_MADE_ON_BREAK_THEN_MADE;
            case GAME_BALL_MADE_ON_BREAK_THEN_MADE:
                return BallStatus.GAME_BALL_MADE_ON_BREAK_THEN_DEAD;
            case GAME_BALL_MADE_ON_BREAK_THEN_DEAD:
                return BallStatus.GAME_BALL_MADE_ON_BREAK;
            default:
                return ballStatus;
        }
    }

    @Override
    public void updateTurnInfo(TurnBuilder turnBuilder) {
        for (int ball = 1; ball <= tableStatus.size(); ball++) {
            turnBuilder.tableStatus.setBallTo(tableStatus.getBallStatus(ball), ball);
        }
    }

    public void registerListener(ShotFragment fragment) {
        this.fragment = fragment;
        updateFragment();
    }

    public void unregisterListener() {
        fragment = null;
    }

    public void updateFragment() {
        if (fragment != null) {
            fragment.updateView(tableStatus.getBallStatuses());
        }
    }
}
