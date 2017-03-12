package com.brookmanholmes.bma.ui.matchinfo;

import com.brookmanholmes.billiards.player.Player;

import java.util.ArrayList;
import java.util.Collection;

/**
 * Created by Brookman Holmes on 12/7/2016.
 */

public class GhostAttemptBinder extends RunAttemptBinder {
    private static final String TAG = "GAB";

    GhostAttemptBinder(Player player) {
        update(player, new ArrayList<Player>());
    }

    @Override
    public void update(Player player, Collection<Player> previousPlayers) {
        Player compPlayer = new Player(player);
        currentWins = Integer.toString(player.getWins());
        breakAndRuns = Integer.toString(player.getBreakAndRuns());
        breakAndRunPct = BindingAdapter.pctf.format(divide(player.getBreakAndRuns(), player.getGameTotal()));
        currentAttempts = BindingAdapter.avgf.format(divide(player.getShootingTurns(), player.getGameTotal()));
        currentMean = BindingAdapter.avgf.format(player.getAvgBallsTurn());
        avgBreakBalls = BindingAdapter.avgf.format(player.getAvgBallsBreak());
        currentGamesPlayed = Integer.toString(player.getGameTotal());

        compPlayer.addPlayerStats(previousPlayers);

        breakAndRunTotal = Integer.toString(compPlayer.getBreakAndRuns());
        breakAndRunTotalPct = BindingAdapter.pctf.format(divide(compPlayer.getBreakAndRuns(), compPlayer.getGameTotal()));
        lifetimeMean = BindingAdapter.avgf.format(divide(compPlayer.getShootingBallsMade(), compPlayer.getShootingTurns()));
        lifetimeAttempts = BindingAdapter.avgf.format(divide(compPlayer.getShootingTurns(), compPlayer.getGameTotal()));
        avgBreakBallsTotal = BindingAdapter.avgf.format(divide(compPlayer.getBreakBallsMade(), compPlayer.getBreakAttempts()));
        totalGamesPlayed = Integer.toString(compPlayer.getGameTotal());
        totalWins = Integer.toString(compPlayer.getWins());
        notifyChange();
    }

    private float divide(int top, int bottom) {
        if (bottom == 0)
            return 0f;
        else
            return (float) top / (float) bottom;
    }
}
