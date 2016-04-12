package com.brookmanholmes.billiardmatchanalyzer.ui.addturnwizard.model;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;

import com.brookmanholmes.billiardmatchanalyzer.ui.addturnwizard.fragments.ShotFragment;
import com.brookmanholmes.billiardmatchanalyzer.utils.MatchDialogHelperUtils;
import com.brookmanholmes.billiardmatchanalyzer.wizard.model.ModelCallbacks;
import com.brookmanholmes.billiardmatchanalyzer.wizard.model.Page;
import com.brookmanholmes.billiardmatchanalyzer.wizard.model.ReviewItem;
import com.brookmanholmes.billiards.game.util.BallStatus;
import com.brookmanholmes.billiards.game.util.GameType;
import com.brookmanholmes.billiards.game.util.PlayerColor;
import com.brookmanholmes.billiards.turn.TableStatus;
import com.brookmanholmes.billiards.turn.TableUtils;

import java.util.ArrayList;

/**
 * Created by Brookman Holmes on 2/20/2016.
 */
public class ShotPage extends Page implements RequiresUpdatedTurnInfo, UpdatesTurnInfo {
    ShotFragment fragment;
    TableStatus tableStatus;
    PlayerColor playerColor = PlayerColor.OPEN;

    public ShotPage(ModelCallbacks callbacks, String title, Bundle matchData) {
        super(callbacks, title);

        data.putAll(matchData);

        tableStatus = TableStatus.newTable(MatchDialogHelperUtils.createGameStatusFromBundle(matchData).gameType,
                data.getIntegerArrayList(MatchDialogHelperUtils.BALLS_ON_TABLE_KEY));
        playerColor = PlayerColor.valueOf(data.getString(MatchDialogHelperUtils.CURRENT_PLAYER_COLOR_KEY));
    }

    @Override public Fragment createFragment() {
        return ShotFragment.create(getKey(), getData());
    }

    @Override public void getReviewItems(ArrayList<ReviewItem> dest) {
    }

    @Override public void getNewTurnInfo(TurnBuilder turnBuilder) {
        for (int ball = 1; ball <= tableStatus.size(); ball++) {
            tableStatus.setBallTo(turnBuilder.tableStatus.getBallStatus(ball), ball);
        }
        updateFragment();
    }

    public BallStatus updateBallStatus(int ball) {
        BallStatus ballStatus = tableStatus.getBallStatus(ball);

        if (playerColor == PlayerColor.SOLIDS)
            if (ball >= 1 && ball <= 8)
                ballStatus = incrementBallStatus(ballStatus);
            else
                ballStatus = incrementDeadBallStatus(ballStatus);
        else if (playerColor == PlayerColor.STRIPES)
            if (ball >= 8 && ball <= 15)
                ballStatus = incrementBallStatus(ballStatus);
            else
                ballStatus = incrementDeadBallStatus(ballStatus);
        else
            ballStatus = incrementBallStatus(ballStatus);

        tableStatus.setBallTo(ballStatus, ball);

        notifyDataChanged();
        updateFragment();

        return ballStatus;
    }

    private PlayerColor setPlayerColorFromBallsMade() {
        if (TableUtils.getSolidsMade(tableStatus.getBallStatuses()) > TableUtils.getStripesMade(tableStatus.getBallStatuses()))
            return PlayerColor.SOLIDS;
        else if (TableUtils.getSolidsMade(tableStatus.getBallStatuses()) < TableUtils.getStripesMade(tableStatus.getBallStatuses()))
            return PlayerColor.STRIPES;
        else
            return PlayerColor.OPEN;
    }

    private PlayerColor setPlayerColorFromBreakBallsMade() {
        if (TableUtils.getSolidsMadeOnBreak(tableStatus.getBallStatuses()) > TableUtils.getStripesMadeOnBreak(tableStatus.getBallStatuses()))
            return PlayerColor.SOLIDS;
        else if (TableUtils.getSolidsMadeOnBreak(tableStatus.getBallStatuses()) < TableUtils.getStripesMadeOnBreak(tableStatus.getBallStatuses()))
            return PlayerColor.STRIPES;
        else return PlayerColor.OPEN;
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

    private BallStatus incrementDeadBallStatus(BallStatus ballStatus) {
        switch (ballStatus) {
            case ON_TABLE:
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

    @Override public void updateTurnInfo(TurnBuilder turnBuilder) {
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
            if (GameType.valueOf(data.getString(MatchDialogHelperUtils.GAME_TYPE_KEY)) == GameType.BCA_EIGHT_BALL) {

                if (PlayerColor.valueOf(data.getString(MatchDialogHelperUtils.CURRENT_PLAYER_COLOR_KEY)) == PlayerColor.OPEN)
                    playerColor = setPlayerColorFromBallsMade();

            } else if (GameType.valueOf(data.getString(MatchDialogHelperUtils.GAME_TYPE_KEY)) == GameType.APA_EIGHT_BALL) {

                if (data.getBoolean(MatchDialogHelperUtils.NEW_GAME_KEY) && setPlayerColorFromBreakBallsMade() != PlayerColor.OPEN)
                    playerColor = setPlayerColorFromBreakBallsMade();
                else
                    playerColor = setPlayerColorFromBallsMade();

            }

            String currentPlayerColor;
            String playerName = data.getString(MatchDialogHelperUtils.CURRENT_PLAYER_NAME_KEY);
            if (playerColor == PlayerColor.OPEN) {
                currentPlayerColor = "Table is open";
            }
            else {
                currentPlayerColor = playerName + " is " + playerColor.toString().toLowerCase();
            }

            fragment.updateView(tableStatus.getBallStatuses(), currentPlayerColor);
        }
    }
}
