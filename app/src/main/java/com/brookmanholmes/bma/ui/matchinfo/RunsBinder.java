package com.brookmanholmes.bma.ui.matchinfo;

import android.support.annotation.Nullable;

import com.brookmanholmes.billiards.game.GameStatus;
import com.brookmanholmes.billiards.game.GameType;
import com.brookmanholmes.billiards.player.Player;
import com.brookmanholmes.bma.R;

/**
 * Created by Brookman Holmes on 9/22/2016.
 */

public class RunsBinder extends BindingAdapter {
    public String playerBreakRuns = "0", opponentBreakRuns = "0";

    public String playerTableRuns = "0", opponentTableRuns = "0";

    public String playerFiveBallRuns = "0", opponentFiveBallRuns = "0";

    public String playerEarlyWins = "0", opponentEarlyWins = "0";
    boolean showEarlyWins;

    RunsBinder(String title, boolean expanded, GameType gameType) {
        super(title, expanded, !gameType.isStraightPool());
        helpLayout = R.layout.dialog_help_runs;
        showEarlyWins = gameType.isWinEarly();
    }

    @Override
    public void update(Player player, Player opponent, @Nullable GameStatus gameStatus) {
        playerBreakRuns = player.getBreakAndRuns() + "";
        opponentBreakRuns = opponent.getBreakAndRuns() + "";

        playerTableRuns = player.getTableRuns() + "";
        opponentTableRuns = opponent.getTableRuns() + "";

        playerFiveBallRuns = player.getFiveBallRun() + "";
        opponentFiveBallRuns = opponent.getFiveBallRun() + "";

        if (player.getGameType().isWinEarly()) {
            playerEarlyWins = player.getEarlyWins() + "";
            opponentEarlyWins = opponent.getEarlyWins() + "";
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
