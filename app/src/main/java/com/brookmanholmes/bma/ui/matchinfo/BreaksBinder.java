package com.brookmanholmes.bma.ui.matchinfo;

import com.brookmanholmes.billiards.player.AbstractPlayer;
import com.brookmanholmes.billiards.player.ApaEightBallPlayer;
import com.brookmanholmes.billiards.player.ApaNineBallPlayer;
import com.brookmanholmes.billiards.player.IWinsOnBreak;
import com.brookmanholmes.billiards.player.NineBallPlayer;
import com.brookmanholmes.billiards.player.StraightPoolPlayer;
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

    BreaksBinder(AbstractPlayer player, AbstractPlayer opponent, String title, boolean expanded) {
        super(expanded, !(player instanceof StraightPoolPlayer));
        this.title = title;
        helpLayout = R.layout.dialog_help_breaks;

        update(player, opponent);

        if (player instanceof ApaEightBallPlayer) {
            breakBall = "8";
        } else if (player instanceof ApaNineBallPlayer || player instanceof NineBallPlayer) {
            breakBall = "9";
        } else {
            breakBall = "8/9";
        }
    }

    public void update(AbstractPlayer player, AbstractPlayer opponent) {
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

        if (player instanceof IWinsOnBreak && opponent instanceof IWinsOnBreak) {
            showWinOnBreak = true;
            playerWinOnBreak = ((IWinsOnBreak) player).getWinsOnBreak() + "";
            opponentWinOnBreak = ((IWinsOnBreak) opponent).getWinsOnBreak() + "";
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
