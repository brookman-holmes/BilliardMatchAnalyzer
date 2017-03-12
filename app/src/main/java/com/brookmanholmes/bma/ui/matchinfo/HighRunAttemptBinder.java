package com.brookmanholmes.bma.ui.matchinfo;

import com.brookmanholmes.billiards.player.Player;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.math3.stat.StatUtils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Locale;

/**
 * Created by Brookman Holmes on 12/7/2016.
 */

public class HighRunAttemptBinder extends RunAttemptBinder {


    HighRunAttemptBinder(Player player) {
        update(player, new ArrayList<Player>());
    }

    @Override
    public void update(Player player, Collection<Player> previousPlayers) {
        currentMax = Integer.toString(player.getHighRun());
        currentMean = BindingAdapter.avgf.format(player.getAverageRunLength());
        currentMedian = BindingAdapter.avgf.format(player.getMedianRunLength());
        currentAttempts = String.format(Locale.getDefault(), "%1$d", player.getRunLengths().size());

        List<Integer> lifeTimeRuns = new ArrayList<>();
        for (Player previousPlayer : previousPlayers) {
            lifeTimeRuns.add(previousPlayer.getShootingBallsMade());
        }
        lifeTimeRuns.addAll(player.getRunLengths());

        setLifetimeStats(convertListToDoubleArray(lifeTimeRuns));

        notifyChange();
    }

    private void setLifetimeStats(double[] currentRuns) {
        double[] combinedRuns = ArrayUtils.addAll(currentRuns);
        if (combinedRuns.length == 0) {
            setDefaultValues();
        } else {
            lifetimeMax = BindingAdapter.avgf.format(StatUtils.max(combinedRuns));
            lifetimeAverage = (float) StatUtils.mean(combinedRuns);
            lifetimeMean = BindingAdapter.avgf.format(lifetimeAverage);
            lifetimeMedian = BindingAdapter.avgf.format(StatUtils.percentile(combinedRuns, 50));
            lifetimeAttempts = String.format(Locale.getDefault(), "%1$d", combinedRuns.length);
        }
    }

    private double[] convertListToDoubleArray(List<Integer> runLengths) {
        double[] result = new double[runLengths.size()];
        for (int i = 0; i < runLengths.size(); i++)
            result[i] = runLengths.get(i);

        return result;
    }

    private void setDefaultValues() {
        lifetimeMax = BindingAdapter.avgf.format(0);
        lifetimeMean = BindingAdapter.avgf.format(0);
        lifetimeMedian = BindingAdapter.avgf.format(0);
        lifetimeAverage = 0f;
    }

    public float getLifetimeAverage() {
        return lifetimeAverage;
    }
}
