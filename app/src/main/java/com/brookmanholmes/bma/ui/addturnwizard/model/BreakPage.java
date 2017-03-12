package com.brookmanholmes.bma.ui.addturnwizard.model;

import android.os.Bundle;
import android.support.v4.app.Fragment;

import com.brookmanholmes.billiards.game.BallStatus;
import com.brookmanholmes.billiards.game.GameStatus;
import com.brookmanholmes.billiards.game.GameType;
import com.brookmanholmes.billiards.turn.TableStatus;
import com.brookmanholmes.bma.ui.addturnwizard.fragments.BreakFragment;
import com.brookmanholmes.bma.wizard.model.FragmentDependentBranch;
import com.brookmanholmes.bma.wizard.model.ModelCallbacks;
import com.brookmanholmes.bma.wizard.model.ReviewItem;

import java.util.ArrayList;

import static com.brookmanholmes.billiards.game.BallStatus.DEAD_ON_BREAK;
import static com.brookmanholmes.billiards.game.BallStatus.GAME_BALL_DEAD_ON_BREAK;
import static com.brookmanholmes.billiards.game.BallStatus.GAME_BALL_MADE_ON_BREAK;
import static com.brookmanholmes.billiards.game.BallStatus.MADE_ON_BREAK;
import static com.brookmanholmes.billiards.game.BallStatus.ON_TABLE;
import static com.brookmanholmes.bma.utils.MatchDialogHelperUtils.GAME_STATUS_KEY;

/**
 * Created by Brookman Holmes on 2/20/2016.
 */
public class BreakPage extends FragmentDependentBranch<BreakFragment> implements UpdatesTurnInfo {
    static final String showShotPage = "show shot page";
    private static final String TAG = "BreakPage";
    private static final String TABLE_STATUS_KEY = "table_status";
    final GameType gameType;
    TableStatus tableStatus;

    BreakPage(ModelCallbacks callbacks, String title, String title2, Bundle matchData) {
        super(callbacks, title);

        data.putAll(matchData);
        gameType = ((GameStatus) matchData.getSerializable(GAME_STATUS_KEY)).gameType;
        tableStatus = TableStatus.newTable(gameType);
        data.putSerializable(TABLE_STATUS_KEY, tableStatus);

        addBranch(showShotPage, new ShotPage(callbacks, title2, matchData));
    }

    @Override
    public Fragment createFragment() {
        return BreakFragment.create(getKey(), getData());
    }

    @Override
    public void updateFragment() {
        if (modelCallbacks instanceof AddTurnWizardModel)
            fragment.updateView(((AddTurnWizardModel) modelCallbacks).getTableStatus().getBallStatuses());
    }

    @Override
    public void getReviewItems(ArrayList<ReviewItem> dest) {
    }

    @Override
    public void updateTurnInfo(AddTurnWizardModel model) {
        for (int ball = 1; ball <= model.getTableStatus().size(); ball++) {
            model.getTableStatus().setBallTo(tableStatus.getBallStatus(ball), ball);
        }
    }

    public BallStatus getBallStatus(int ball) {
        return tableStatus.getBallStatus(ball);
    }

    public void setBallStatus(BallStatus ballStatus, int ball) {
        tableStatus.setBallTo(ballStatus, ball);
        ballStatusUpdated();
    }

    public BallStatus updateBallStatus(int ball) {
        BallStatus ballStatus = tableStatus.getBallStatus(ball);

        BallStatus newBallStatus = incrementBallStatus(ballStatus, ball);
        tableStatus.setBallTo(newBallStatus, ball);
        ballStatusUpdated();

        return newBallStatus;
    }

    private void ballStatusUpdated() {
        data.putSerializable(TABLE_STATUS_KEY, tableStatus);
        data.putString(SIMPLE_DATA_KEY, showShotPage());
        notifyDataChanged();
    }

    @Override
    public void resetData(Bundle data) {
        tableStatus = (TableStatus) data.getSerializable(TABLE_STATUS_KEY);
        super.resetData(data);
    }

    private BallStatus incrementBallStatus(BallStatus ballStatus, int ball) {
        int gameBall = tableStatus.getGameBall();

        switch (ballStatus) {
            case ON_TABLE:
                if (ball == gameBall)
                    return GAME_BALL_MADE_ON_BREAK;
                else
                    return MADE_ON_BREAK;
            case MADE_ON_BREAK:
                return DEAD_ON_BREAK;
            case DEAD_ON_BREAK:
                return ON_TABLE;
            case GAME_BALL_MADE_ON_BREAK:
                return GAME_BALL_DEAD_ON_BREAK;
            case GAME_BALL_DEAD_ON_BREAK:
                return ON_TABLE;
            default:
                return ballStatus;
        }
    }

    String showShotPage() {
        if (tableStatus.getBreakBallsMade() > 0 && gameNotWonOnBreak()) {
            return showShotPage;
        } else return "";
    }

    boolean gameNotWonOnBreak() {
        return !(gameBallMadeOnBreak() && canWinOnBreak());
    }

    boolean gameBallMadeOnBreak() {
        return tableStatus.isGameBallMadeOnBreak();
    }

    boolean canWinOnBreak() {
        return gameType == GameType.APA_EIGHT_BALL ||
                gameType == GameType.APA_NINE_BALL ||
                gameType == GameType.BCA_NINE_BALL ||
                gameType == GameType.APA_GHOST_EIGHT_BALL ||
                gameType == GameType.APA_GHOST_NINE_BALL;
    }

    public int getGameBall() {
        return tableStatus.getGameBall();
    }
}