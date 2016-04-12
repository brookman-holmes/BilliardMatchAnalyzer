package com.brookmanholmes.billiardmatchanalyzer.ui.addturnwizard.model;

import android.os.Bundle;
import android.support.v4.app.Fragment;

import com.brookmanholmes.billiardmatchanalyzer.ui.addturnwizard.fragments.BreakFragment;
import com.brookmanholmes.billiardmatchanalyzer.utils.MatchDialogHelperUtils;
import com.brookmanholmes.billiardmatchanalyzer.wizard.model.BranchPage;
import com.brookmanholmes.billiardmatchanalyzer.wizard.model.ModelCallbacks;
import com.brookmanholmes.billiardmatchanalyzer.wizard.model.ReviewItem;
import com.brookmanholmes.billiards.game.util.BallStatus;
import com.brookmanholmes.billiards.game.util.GameType;
import com.brookmanholmes.billiards.turn.TableStatus;

import java.util.ArrayList;

/**
 * Created by Brookman Holmes on 2/20/2016.
 */
public class BreakPage extends BranchPage implements UpdatesTurnInfo {
    private static final String showShotPage = "show shot page";
    TableStatus tableStatus;
    GameType gameType;

    public BreakPage(ModelCallbacks callbacks, String title, String title2, Bundle matchData) {
        super(callbacks, title);

        data.putAll(matchData);
        gameType = MatchDialogHelperUtils.createGameStatusFromBundle(matchData).gameType;
        tableStatus = TableStatus.newTable(gameType);

        addBranch(showShotPage, new ShotPage(callbacks, title2, matchData));
    }

    @Override public Fragment createFragment() {
        return BreakFragment.create(getKey(), getData());
    }

    @Override public void getReviewItems(ArrayList<ReviewItem> dest) {
    }

    @Override public void updateTurnInfo(TurnBuilder turnBuilder) {
        for (int ball = 1; ball <= tableStatus.size(); ball++) {
            turnBuilder.tableStatus.setBallTo(tableStatus.getBallStatus(ball), ball);
        }
    }

    public BallStatus updateBallStatus(int ball) {
        BallStatus ballStatus = tableStatus.getBallStatus(ball);

        BallStatus newBallStatus = incrementBallStatus(ballStatus, ball);
        tableStatus.setBallTo(newBallStatus, ball);

        data.putString(SIMPLE_DATA_KEY, showShotPage());
        notifyDataChanged();

        return newBallStatus;
    }

    private BallStatus incrementBallStatus(BallStatus ballStatus, int ball) {
        int gameBall = 0;
        if (gameType == GameType.BCA_EIGHT_BALL) {
            gameBall = 8;
        } else if (gameType == GameType.BCA_TEN_BALL) {
            gameBall = 10;
        }

        switch (ballStatus) {
            case ON_TABLE:
                if (ball == gameBall)
                    return BallStatus.GAME_BALL_MADE_ON_BREAK;
                else
                    return BallStatus.MADE_ON_BREAK;
            case MADE_ON_BREAK:
                return BallStatus.DEAD_ON_BREAK;
            case DEAD_ON_BREAK:
                return BallStatus.ON_TABLE;
            case GAME_BALL_MADE_ON_BREAK:
                return BallStatus.DEAD_ON_BREAK;
            default:
                return ballStatus;
        }
    }

    private String showShotPage() {
        if (tableStatus.getBreakBallsMade() > 0 && gameNotWonOnBreak()) {
            return showShotPage;
        } else return "";
    }

    private boolean gameNotWonOnBreak() {
        return !(gameBallMadeOnBreak() && canWinOnBreak());
    }

    private boolean gameBallMadeOnBreak() {
        return tableStatus.getGameBallMadeOnBreak();
    }

    private boolean canWinOnBreak() {
        return gameType == GameType.APA_EIGHT_BALL || gameType == GameType.APA_NINE_BALL || gameType == GameType.BCA_NINE_BALL;
    }
}