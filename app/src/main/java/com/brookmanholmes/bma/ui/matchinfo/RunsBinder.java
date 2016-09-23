package com.brookmanholmes.bma.ui.matchinfo;

import com.brookmanholmes.billiards.player.AbstractPlayer;
import com.brookmanholmes.billiards.player.IEarlyWins;
import com.brookmanholmes.bma.R;

/**
 * Created by Brookman Holmes on 9/22/2016.
 */

public class RunsBinder extends BindingAdapter {
    public String playerBreakRuns, opponentBreakRuns;

    public String playerTableRuns, opponentTableRuns;

    public String playerFiveBallRuns, opponentFiveBallRuns;

    public String playerEarlyWins = "0", opponentEarlyWins = "0";

    boolean showEarlyWins = false;

    public RunsBinder(AbstractPlayer player, AbstractPlayer opponent, String title) {
        this.title = title;
        helpLayout = R.layout.dialog_help_runs;

        playerBreakRuns = player.getBreakAndRuns() + "";
        opponentBreakRuns = opponent.getBreakAndRuns() + "";

        playerTableRuns = player.getTableRuns() + "";
        opponentTableRuns = opponent.getTableRuns() + "";

        playerFiveBallRuns = player.getFiveBallRun() + "";
        opponentFiveBallRuns = opponent.getFiveBallRun() + "";

        if (player instanceof IEarlyWins && opponent instanceof IEarlyWins) {
            playerEarlyWins = ((IEarlyWins) player).getEarlyWins() + "";
            opponentEarlyWins = ((IEarlyWins) opponent).getEarlyWins() + "";
            showEarlyWins = true;
        }
    }

    public void update(AbstractPlayer player, AbstractPlayer opponent) {
        playerBreakRuns = player.getBreakAndRuns() + "";
        opponentBreakRuns = opponent.getBreakAndRuns() + "";

        playerTableRuns = player.getTableRuns() + "";
        opponentTableRuns = opponent.getTableRuns() + "";

        playerFiveBallRuns = player.getFiveBallRun() + "";
        opponentFiveBallRuns = opponent.getFiveBallRun() + "";

        if (player instanceof IEarlyWins && opponent instanceof IEarlyWins) {
            playerEarlyWins = ((IEarlyWins) player).getEarlyWins() + "";
            opponentEarlyWins = ((IEarlyWins) opponent).getEarlyWins() + "";
        }

        notifyChange();
    }

    public boolean isShowEarlyWins() {
        return showEarlyWins && visible;
    }

    public boolean playerBreakRunsMore() {
        return Integer.parseInt(playerBreakRuns) > Integer.parseInt(opponentBreakRuns);
    }

    public boolean opponentBreakRunsMore() {
        return Integer.parseInt(playerBreakRuns) < Integer.parseInt(opponentBreakRuns);
    }

    public boolean playerTableRunsMore() {
        return Integer.parseInt(playerTableRuns) > Integer.parseInt(opponentTableRuns);
    }

    public boolean opponentTableRunsMore() {
        return Integer.parseInt(playerTableRuns) < Integer.parseInt(opponentTableRuns);
    }

    public boolean playerFiveBallRunsMore() {
        return Integer.parseInt(playerFiveBallRuns) > Integer.parseInt(opponentFiveBallRuns);
    }

    public boolean opponentFiveBallRunsMore() {
        return Integer.parseInt(playerFiveBallRuns) < Integer.parseInt(opponentFiveBallRuns);
    }

    public boolean playerEarlyWinsMore() {
        return Integer.parseInt(playerEarlyWins) > Integer.parseInt(opponentEarlyWins);
    }

    public boolean opponentEarlyWinsMore() {
        return Integer.parseInt(playerEarlyWins) < Integer.parseInt(opponentEarlyWins);
    }
}
