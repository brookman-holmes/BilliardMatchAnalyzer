package com.brookmanholmes.bma.ui.matchinfo;

import android.support.annotation.Nullable;

import com.brookmanholmes.billiards.game.GameStatus;
import com.brookmanholmes.billiards.game.GameType;
import com.brookmanholmes.billiards.player.Player;
import com.brookmanholmes.bma.R;

/**
 * Created by Brookman Holmes on 9/22/2016.
 */

public class BreaksBinder extends BindingAdapter {

    private static final String TAG = "BreaksBinder";
    public int playerBallOnBreak = 0, opponentBallOnBreak = 0;
    public int playerBreaks = 0, opponentBreaks = 0;

    public String playerAvg = defaultAvg, opponentAvg = defaultAvg;

    public String playerContinuation = "0", opponentContinuation = "0";

    public String playerFouls = "0", opponentFouls = "0";

    public String playerWinOnBreak = "0", opponentWinOnBreak = "0";
    public String breakBall;
    private boolean showWinOnBreak = false;

    BreaksBinder(String title, boolean expanded, GameType gameType) {
        super(title, expanded, !gameType.isStraightPool());
        helpLayout = R.layout.dialog_help_breaks;

        if (gameType.isApa8Ball()) {
            breakBall = "8";
        } else if (gameType.is9Ball()) {
            breakBall = "9";
        } else {
            breakBall = "8/9";
        }
    }

    @Override
    public void update(Player player, Player opponent, @Nullable GameStatus gameStatus) {
        playerBallOnBreak = player.getBreakSuccesses();
        opponentBallOnBreak = opponent.getBreakSuccesses();
        playerBreaks = player.getBreakAttempts();
        opponentBreaks = opponent.getBreakAttempts();

        playerAvg = avgf.format(player.getAvgBallsBreak());
        opponentAvg = avgf.format(opponent.getAvgBallsBreak());

        playerContinuation = player.getBreakContinuations() + "";
        opponentContinuation = opponent.getBreakContinuations() + "";

        playerFouls = player.getBreakFouls() + "";
        opponentFouls = opponent.getBreakFouls() + "";

        if (player.getGameType().isWinOnBreak()) {
            showWinOnBreak = true;
            playerWinOnBreak = player.getWinsOnBreak() + "";
            opponentWinOnBreak = opponent.getWinsOnBreak() + "";
        }

        notifyChange();
    }

    public boolean isShowWinOnBreak() {
        return showWinOnBreak && expanded;
    }

    public int highlightAvg() {
        return compare(playerAvg, opponentAvg);
    }

    public int highlightFouls() {
        return compare(playerFouls, opponentFouls) * -1;
    }

    public int highlightContinuations() {
        return compare(playerContinuation, opponentContinuation);
    }

    public int highlightWinsOnBreak() {
        return compare(playerWinOnBreak, opponentWinOnBreak);
    }
}
