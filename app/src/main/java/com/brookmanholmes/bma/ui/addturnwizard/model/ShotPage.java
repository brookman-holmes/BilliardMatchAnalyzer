package com.brookmanholmes.bma.ui.addturnwizard.model;

import android.os.Bundle;
import android.support.v4.app.Fragment;

import com.brookmanholmes.billiards.game.BallStatus;
import com.brookmanholmes.billiards.game.GameType;
import com.brookmanholmes.billiards.game.PlayerColor;
import com.brookmanholmes.billiards.turn.TableStatus;
import com.brookmanholmes.bma.ui.addturnwizard.fragments.ShotFragment;
import com.brookmanholmes.bma.wizard.model.ModelCallbacks;
import com.brookmanholmes.bma.wizard.model.Page;
import com.brookmanholmes.bma.wizard.model.ReviewItem;

import static com.brookmanholmes.bma.utils.MatchDialogHelperUtils.*;
import static com.brookmanholmes.billiards.turn.TableUtils.*;
import static com.brookmanholmes.billiards.game.BallStatus.*;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Brookman Holmes on 2/20/2016.
 */
public class ShotPage extends Page implements RequiresUpdatedTurnInfo, UpdatesTurnInfo {
    private final TableStatus tableStatus;
    private ShotFragment fragment;
    private PlayerColor playerColor = PlayerColor.OPEN;

    ShotPage(ModelCallbacks callbacks, String title, Bundle matchData) {
        super(callbacks, title);

        data.putAll(matchData);

        tableStatus = TableStatus.newTable(getGameStatus(matchData).gameType,
                data.getIntegerArrayList(BALLS_ON_TABLE_KEY));
        playerColor = PlayerColor.valueOf(data.getString(CURRENT_PLAYER_COLOR_KEY));
    }

    @Override
    public Fragment createFragment() {
        return ShotFragment.create(getKey(), getData());
    }

    @Override
    public void getReviewItems(ArrayList<ReviewItem> dest) {
    }

    @Override
    public void updateTurnInfo(AddTurnWizardModel model) {
        for (int ball = 1; ball <= tableStatus.size(); ball++) {
            model.getTableStatus().setBallTo(tableStatus.getBallStatus(ball), ball);
        }
    }

    @Override
    public void getNewTurnInfo(AddTurnWizardModel model) {
        for (int ball = 1; ball <= tableStatus.size(); ball++) {
            tableStatus.setBallTo(model.getTableStatus().getBallStatus(ball), ball);
        }
        updateFragment();
    }

    public List<BallStatus> getBallStatuses() {
        return tableStatus.getBallStatuses();
    }

    public void updateBallStatus(int ball) {
        BallStatus ballStatus = tableStatus.getBallStatus(ball);

        if (playerColor == PlayerColor.SOLIDS)
            if (ball >= 1 && ball <= 8)
                ballStatus = incrementBallStatus(ballStatus);
            else
                ballStatus = incrementOtherPlayersBallStatus(ballStatus);
        else if (playerColor == PlayerColor.STRIPES)
            if (ball >= 8 && ball <= 15)
                ballStatus = incrementBallStatus(ballStatus);
            else
                ballStatus = incrementOtherPlayersBallStatus(ballStatus);
        else
            ballStatus = incrementBallStatus(ballStatus);

        tableStatus.setBallTo(ballStatus, ball);

        notifyDataChanged();
        updateFragment();
    }

    private PlayerColor setPlayerColorFromBallsMade() {
        if (getSolidsMade(tableStatus.getBallStatuses()) > getStripesMade(tableStatus.getBallStatuses()))
            return PlayerColor.SOLIDS;
        else if (getSolidsMade(tableStatus.getBallStatuses()) < getStripesMade(tableStatus.getBallStatuses()))
            return PlayerColor.STRIPES;
        else
            return PlayerColor.OPEN;
    }

    private PlayerColor setPlayerColorFromBreakBallsMade() {
        if (getSolidsMadeOnBreak(tableStatus.getBallStatuses()) > getStripesMadeOnBreak(tableStatus.getBallStatuses()))
            return PlayerColor.SOLIDS;
        else if (getSolidsMadeOnBreak(tableStatus.getBallStatuses()) < getStripesMadeOnBreak(tableStatus.getBallStatuses()))
            return PlayerColor.STRIPES;
        else return PlayerColor.OPEN;
    }

    private BallStatus incrementBallStatus(BallStatus ballStatus) {
        switch (ballStatus) {
            case ON_TABLE:
                return MADE;
            case MADE:
                return DEAD;
            case DEAD:
                return ON_TABLE;
            // game ball for 8/10 ball
            case GAME_BALL_MADE_ON_BREAK:
                return GAME_BALL_MADE_ON_BREAK_THEN_MADE;
            case GAME_BALL_MADE_ON_BREAK_THEN_MADE:
                return GAME_BALL_MADE_ON_BREAK_THEN_DEAD;
            case GAME_BALL_MADE_ON_BREAK_THEN_DEAD:
                return GAME_BALL_MADE_ON_BREAK;
            case GAME_BALL_DEAD_ON_BREAK:
                return GAME_BALL_DEAD_ON_BREAK_THEN_MADE;
            case GAME_BALL_DEAD_ON_BREAK_THEN_MADE:
                return GAME_BALL_DEAD_ON_BREAK_THEN_DEAD;
            case GAME_BALL_DEAD_ON_BREAK_THEN_DEAD:
                return GAME_BALL_DEAD_ON_BREAK;
            default:
                return ballStatus;
        }
    }

    private BallStatus incrementOtherPlayersBallStatus(BallStatus ballStatus) {
        switch (ballStatus) {
            case ON_TABLE:
                return DEAD;
            case DEAD:
                return ON_TABLE;
            // game ball for 8/10 ball
            case GAME_BALL_MADE_ON_BREAK:
                return GAME_BALL_MADE_ON_BREAK_THEN_MADE;
            case GAME_BALL_MADE_ON_BREAK_THEN_MADE:
                return GAME_BALL_MADE_ON_BREAK_THEN_DEAD;
            case GAME_BALL_MADE_ON_BREAK_THEN_DEAD:
                return GAME_BALL_MADE_ON_BREAK;
            case GAME_BALL_DEAD_ON_BREAK:
                return GAME_BALL_DEAD_ON_BREAK_THEN_DEAD;
            case GAME_BALL_DEAD_ON_BREAK_THEN_MADE:
                return GAME_BALL_DEAD_ON_BREAK_THEN_DEAD;
            case GAME_BALL_DEAD_ON_BREAK_THEN_DEAD:
                return GAME_BALL_DEAD_ON_BREAK;
            default:
                return ballStatus;
        }
    }

    public void registerListener(ShotFragment fragment) {
        this.fragment = fragment;
        updateFragment();
    }

    public void unregisterListener() {
        fragment = null;
    }

    private void updateFragment() {
        if (fragment != null) {
            if (GameType.valueOf(data.getString(GAME_TYPE_KEY)) == GameType.BCA_EIGHT_BALL) {

                if (PlayerColor.valueOf(data.getString(CURRENT_PLAYER_COLOR_KEY)) == PlayerColor.OPEN)
                    playerColor = setPlayerColorFromBallsMade();

            } else if (GameType.valueOf(data.getString(GAME_TYPE_KEY)) == GameType.APA_EIGHT_BALL) {

                if (data.getBoolean(NEW_GAME_KEY) && setPlayerColorFromBreakBallsMade() != PlayerColor.OPEN)
                    playerColor = setPlayerColorFromBreakBallsMade();
                else
                    playerColor = setPlayerColorFromBallsMade();

            }

            fragment.updateView(tableStatus.getBallStatuses(), playerColor);
        }
    }
}
