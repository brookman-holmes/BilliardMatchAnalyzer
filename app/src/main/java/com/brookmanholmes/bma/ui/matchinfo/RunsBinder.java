package com.brookmanholmes.bma.ui.matchinfo;

import com.brookmanholmes.billiards.player.AbstractPlayer;
import com.brookmanholmes.billiards.player.IEarlyWins;
import com.brookmanholmes.billiards.player.StraightPoolPlayer;
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

    RunsBinder(AbstractPlayer player, AbstractPlayer opponent, String title, boolean expanded, boolean showCard) {
        super(expanded, !(player instanceof StraightPoolPlayer) && showCard);
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
        return showEarlyWins && expanded;
    }

    public int highlightBreakRuns() {
        return compare(playerBreakRuns, opponentBreakRuns);
    }

    public int highlightTableRuns() {
        return compare(playerTableRuns, opponentTableRuns);
    }

    public int highlightFiveBallRuns() {
        return compare(playerFiveBallRuns, opponentFiveBallRuns);
    }

    public int highlightEarlyWins() {
        return compare(playerEarlyWins, opponentEarlyWins);
    }
}
