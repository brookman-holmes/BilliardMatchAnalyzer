package com.brookmanholmes.bma.ui.matchinfo;

import android.databinding.BaseObservable;

import com.brookmanholmes.billiards.player.StraightPoolPlayer;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.math3.stat.StatUtils;

import java.text.DecimalFormat;
import java.util.List;
import java.util.Locale;

/**
 * Created by Brookman Holmes on 12/7/2016.
 */

public class HighRunAttemptBinder extends BaseObservable {
    private final static DecimalFormat avgf = new DecimalFormat("##.##");
    private final double[] lifetimeRuns;
    public String currentMax = "0", lifetimeMax = "0";
    public String currentMean = "0.0", lifetimeMean = "0";
    public String currentMedian = "0.0", lifetimeMedian = "0";
    public String currentAttempts = "0", lifetimeAttempts = "0";
    private float lifetimeAverage = 0f;

    HighRunAttemptBinder(StraightPoolPlayer player, List<Integer> lifetimeRuns) {
        this.lifetimeRuns = convertListToDoubleArray(lifetimeRuns);
        update(player);
    }

    public void update(StraightPoolPlayer player) {
        currentMax = Integer.toString(player.getHighRun());
        currentMean = player.getAverageRunLength();
        currentMedian = player.getMedianRunLength();
        currentAttempts = String.format(Locale.getDefault(), "%1$d", player.getRunLengths().size());

        setLifetimeStats(convertListToDoubleArray(player.getRunLengths()));

        notifyChange();
    }

    private void setLifetimeStats(double[] currentRuns) {
        double[] combinedRuns = ArrayUtils.addAll(lifetimeRuns, currentRuns);
        if (combinedRuns.length == 0) {
            setDefaultValues();
        } else {
            lifetimeMax = avgf.format(StatUtils.max(combinedRuns));
            lifetimeAverage = (float) StatUtils.mean(combinedRuns);
            lifetimeMean = avgf.format(lifetimeAverage);
            lifetimeMedian = avgf.format(StatUtils.percentile(combinedRuns, 50));
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
        lifetimeMax = avgf.format(0);
        lifetimeMean = avgf.format(0);
        lifetimeMedian = avgf.format(0);
        lifetimeAverage = 0f;
    }

    public float getLifetimeAverage() {
        return lifetimeAverage;
    }
}
