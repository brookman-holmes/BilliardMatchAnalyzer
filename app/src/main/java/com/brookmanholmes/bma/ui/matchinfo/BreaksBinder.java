package com.brookmanholmes.bma.ui.matchinfo;

import com.brookmanholmes.billiards.player.Player;
import com.brookmanholmes.bma.R;

/**
 * Created by Brookman Holmes on 9/22/2016.
 */

public class BreaksBinder extends BindingAdapter {

    private static final String TAG = "BreaksBinder";
    public int playerBallOnBreak, opponentBallOnBreak;
    public int playerBreaks, opponentBreaks;

    public String playerAvg, opponentAvg;

    public String playerContinuation, opponentContinuation;

    public String playerFouls, opponentFouls;

    public String playerWinOnBreak = "0", opponentWinOnBreak = "0";
    public String breakBall;
    private boolean showWinOnBreak = false;

    BreaksBinder(Player player, Player opponent, String title, boolean expanded) {
        super(expanded, !player.getGameType().isStraightPool());
        this.title = title;
        helpLayout = R.layout.dialog_help_breaks;

        update(player, opponent);

        if (player.getGameType().isApa8Ball()) {
            breakBall = "8";
        } else if (player.getGameType().is9Ball()) {
            breakBall = "9";
        } else {
            breakBall = "8/9";
        }
    }

    public void update(Player player, Player opponent) {
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
