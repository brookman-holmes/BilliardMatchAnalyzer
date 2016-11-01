package com.brookmanholmes.bma.ui.matchinfo;

import com.brookmanholmes.billiards.player.AbstractPlayer;
import com.brookmanholmes.billiards.player.ApaEightBallPlayer;
import com.brookmanholmes.billiards.player.ApaNineBallPlayer;
import com.brookmanholmes.billiards.player.IWinsOnBreak;
import com.brookmanholmes.billiards.player.NineBallPlayer;
import com.brookmanholmes.bma.R;

/**
 * Created by Brookman Holmes on 9/22/2016.
 */

public class BreaksBinder extends BindingAdapter {
    public int playerBallOnBreak, opponentBallOnBreak;
    public int playerBreaks, opponentBreaks;

    public String playerAvg, opponentAvg;

    public String playerContinuation, opponentContinuation;

    public String playerFouls, opponentFouls;

    public String playerWinOnBreak = "0", opponentWinOnBreak = "0";
    public String breakBall;
    boolean showWinOnBreak = false;

    BreaksBinder(AbstractPlayer player, AbstractPlayer opponent, String title, boolean expanded) {
        super(expanded);
        this.title = title;
        helpLayout = R.layout.dialog_help_breaks;

        playerBallOnBreak = player.getBreakSuccesses();
        opponentBallOnBreak = opponent.getBreakSuccesses();
        playerBreaks = player.getBreakAttempts();
        opponentBreaks = opponent.getBreakAttempts();

        playerAvg = player.getAvgBallsBreak();
        opponentAvg = opponent.getAvgBallsBreak();

        playerContinuation = player.getBreakContinuations() + "";
        opponentContinuation = opponent.getBreakContinuations() + "";

        playerFouls = player.getBreakFouls() + "";
        opponentFouls = opponent.getBreakFouls() + "";

        if (player instanceof IWinsOnBreak && opponent instanceof IWinsOnBreak) {
            showWinOnBreak = true;
            playerWinOnBreak = ((IWinsOnBreak) player).getWinsOnBreak() + "";
            opponentWinOnBreak = ((IWinsOnBreak) opponent).getWinsOnBreak() + "";
        }

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

        playerAvg = player.getAvgBallsBreak();
        opponentAvg = opponent.getAvgBallsBreak();

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
        return showWinOnBreak && visible;
    }

    public boolean playerAvgHigher() {
        return Double.compare(
                Double.parseDouble(playerAvg),
                Double.parseDouble(opponentAvg)
        ) > 0;
    }

    public boolean opponentAvgHigher() {
        return Double.compare(
                Double.parseDouble(playerAvg),
                Double.parseDouble(opponentAvg)
        ) < 0;
    }

    public boolean playerFoulsLower() {
        return Integer.parseInt(playerFouls) < Integer.parseInt(opponentFouls);
    }

    public boolean opponentFoulsLower() {
        return Integer.parseInt(playerFouls) > Integer.parseInt(opponentFouls);
    }

    public boolean playerContinuationsHigher() {
        return Integer.parseInt(playerContinuation) > Integer.parseInt(opponentContinuation);
    }

    public boolean opponentContinuationsHigher() {
        return Integer.parseInt(playerContinuation) < Integer.parseInt(opponentContinuation);
    }

    public boolean playerWinsOnBreakMore() {
        return Integer.parseInt(playerWinOnBreak) > Integer.parseInt(opponentWinOnBreak);
    }

    public boolean opponentWinsOnBreakMore() {
        return Integer.parseInt(playerWinOnBreak) < Integer.parseInt(opponentWinOnBreak);
    }
}
