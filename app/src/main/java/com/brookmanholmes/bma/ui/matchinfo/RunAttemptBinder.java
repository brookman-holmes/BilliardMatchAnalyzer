package com.brookmanholmes.bma.ui.matchinfo;

import android.databinding.BaseObservable;

import com.brookmanholmes.billiards.player.Player;

import java.util.Collection;

/**
 * Created by Brookman Holmes on 3/2/2017.
 */

public abstract class RunAttemptBinder extends BaseObservable {
    public String currentGamesPlayed = "0", totalGamesPlayed = "0";
    public String currentWins = "0", totalWins = "0";
    public String currentMax = "0", lifetimeMax = "0";
    public String currentMean = "0.0", lifetimeMean = "0";
    public String currentMedian = "0.0", lifetimeMedian = "0";
    public String currentAttempts = "0", lifetimeAttempts = "0";
    public String breakAndRuns = "0", breakAndRunTotal = "0";
    public String breakAndRunPct = BindingAdapter.defaultPct,
            breakAndRunTotalPct = BindingAdapter.defaultPct;
    public String avgBreakBalls = BindingAdapter.defaultAvg, avgBreakBallsTotal = BindingAdapter.defaultAvg;
    float lifetimeAverage = 0f;

    public abstract void update(Player player, Collection<Player> previousPlayers);
}
