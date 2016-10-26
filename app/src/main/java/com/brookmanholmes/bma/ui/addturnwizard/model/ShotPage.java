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

import java.util.ArrayList;
import java.util.List;

import static com.brookmanholmes.billiards.game.BallStatus.DEAD;
import static com.brookmanholmes.billiards.game.BallStatus.GAME_BALL_DEAD_ON_BREAK;
import static com.brookmanholmes.billiards.game.BallStatus.GAME_BALL_DEAD_ON_BREAK_THEN_DEAD;
import static com.brookmanholmes.billiards.game.BallStatus.GAME_BALL_DEAD_ON_BREAK_THEN_MADE;
import static com.brookmanholmes.billiards.game.BallStatus.GAME_BALL_MADE_ON_BREAK;
import static com.brookmanholmes.billiards.game.BallStatus.GAME_BALL_MADE_ON_BREAK_THEN_DEAD;
import static com.brookmanholmes.billiards.game.BallStatus.GAME_BALL_MADE_ON_BREAK_THEN_MADE;
import static com.brookmanholmes.billiards.game.BallStatus.MADE;
import static com.brookmanholmes.billiards.game.BallStatus.ON_TABLE;
import static com.brookmanholmes.billiards.turn.TableUtils.getSolidsMade;
import static com.brookmanholmes.billiards.turn.TableUtils.getSolidsMadeOnBreak;
import static com.brookmanholmes.billiards.turn.TableUtils.getStripesMade;
import static com.brookmanholmes.billiards.turn.TableUtils.getStripesMadeOnBreak;
import static com.brookmanholmes.bma.utils.MatchDialogHelperUtils.BALLS_ON_TABLE_KEY;
import static com.brookmanholmes.bma.utils.MatchDialogHelperUtils.CURRENT_PLAYER_COLOR_KEY;
import static com.brookmanholmes.bma.utils.MatchDialogHelperUtils.GAME_TYPE_KEY;
import static com.brookmanholmes.bma.utils.MatchDialogHelperUtils.NEW_GAME_KEY;
import static com.brookmanholmes.bma.utils.MatchDialogHelperUtils.getGameStatus;

/**
 * Created by Brookman Holmes on 2/20/2016.
 */
public class ShotPage extends Page implements RequiresUpdatedTurnInfo, UpdatesTurnInfo {
    private static final String TAG = "ShotPage";
    private static final String TABLE_STATUS_KEY = "table_status";
    private TableStatus tableStatus;
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
                ballStatus = incrementBallStatus(ballStatus, ball);
            else
                ballStatus = incrementOtherPlayersBallStatus(ballStatus);
        else if (playerColor == PlayerColor.STRIPES)
            if (ball >= 8 && ball <= 15)
                ballStatus = incrementBallStatus(ballStatus, ball);
            else
                ballStatus = incrementOtherPlayersBallStatus(ballStatus);
        else
            ballStatus = incrementBallStatus(ballStatus, ball);

        tableStatus.setBallTo(ballStatus, ball);

        data.putSerializable(TABLE_STATUS_KEY, tableStatus);
        notifyDataChanged();
        updateFragment();
    }

    @Override
    public void resetData(Bundle data) {
        tableStatus = (TableStatus) data.getSerializable(TABLE_STATUS_KEY);
        super.resetData(data);
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

    private BallStatus incrementBallStatus(BallStatus ballStatus, int ball) {
        switch (ballStatus) {
            case ON_TABLE:
                return MADE;
            case MADE:
                return DEAD;
            case DEAD:
                return ON_TABLE;
            case MADE_ON_BREAK:
                return ballStatus;
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

    public BallStatus getGameBallStatus() {
        return tableStatus.getBallStatus(tableStatus.getGameBall());
    }

    public void registerListener(ShotFragment fragment) {
        this.fragment = fragment;
        updateFragment();
    }

    public void unregisterListener() {
        fragment = null;
    }

    private void updateFragment() {
        PlayerColor currentPlayerColor = PlayerColor.valueOf(data.getString(CURRENT_PLAYER_COLOR_KEY));
        if (fragment != null) {
            GameType gameType = GameType.valueOf(data.getString(GAME_TYPE_KEY));

            if (gameType == GameType.BCA_EIGHT_BALL || gameType == GameType.BCA_GHOST_EIGHT_BALL) {

                if (currentPlayerColor == PlayerColor.OPEN)
                    playerColor = setPlayerColorFromBallsMade();

            } else if (gameType == GameType.APA_EIGHT_BALL || gameType == GameType.APA_GHOST_EIGHT_BALL) {

                if (currentPlayerColor == PlayerColor.OPEN) {
                    if (data.getBoolean(NEW_GAME_KEY) && setPlayerColorFromBreakBallsMade() != PlayerColor.OPEN)
                        playerColor = setPlayerColorFromBreakBallsMade();
                    else
                        playerColor = setPlayerColorFromBallsMade();
                }
            }

            fragment.updateView(tableStatus.getBallStatuses(), playerColor);
        }
    }

    public int getGameBall() {
        return tableStatus.getGameBall();
    }

    public BallStatus getBallStatus(int ball) {
        return tableStatus.getBallStatus(ball);
    }
}
