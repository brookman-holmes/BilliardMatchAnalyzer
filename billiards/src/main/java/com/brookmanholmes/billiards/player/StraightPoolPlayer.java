package com.brookmanholmes.billiards.player;

import org.apache.commons.math3.stat.StatUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Brookman Holmes on 1/12/2016.
 */
public class StraightPoolPlayer extends AbstractPlayer implements IStraightPool {
    private int highRun = 0;
    private int seriousFouls = 0;
    private List<Integer> runLengths = new ArrayList<>();

    public StraightPoolPlayer(String name, int rank) {
        super(name, rank);
    }

    public StraightPoolPlayer(String name) {
        super(name);
    }

    @Override
    public void addPlayerStats(AbstractPlayer player) {
        super.addPlayerStats(player);

        if (player instanceof StraightPoolPlayer) {
            if (highRun < player.shootingBallsMade)
                highRun = player.shootingBallsMade;

            this.seriousFouls += ((StraightPoolPlayer) player).getSeriousFouls();

            if (player.shootingTurns > 0)
                runLengths.add(player.shootingBallsMade);
        }
    }

    @Override
    public int getHighRun() {
        return highRun;
    }

    public void addSeriousFoul() {
        seriousFouls++;
    }

    public int getPoints() {
        return shootingBallsMade - shootingFouls - safetyFouls - breakFouls - getSeriousFoulsPoints();
    }

    @Override
    public int getTotalFouls() {
        return shootingFouls + safetyFouls;
    }

    @Override
    public List<Integer> getRunLengths() {
        return new ArrayList<>(runLengths);
    }

    private int getSeriousFoulsPoints() {
        return seriousFouls * 15;
    }

    @Override
    public String getAverageRunLength() {
        if (runLengths.size() > 0) {
            double[] runLengths = new double[this.runLengths.size()];
            for (int i = 0; i < this.runLengths.size(); i++) {
                runLengths[i] = this.runLengths.get(i);
            }

            return avgf.format(StatUtils.mean(runLengths));
        } else return avgf.format(0);
    }

    @Override
    public String getMedianRunLength() {
        if (runLengths.size() > 0) {
            double[] runLengths = new double[this.runLengths.size()];
            for (int i = 0; i < this.runLengths.size(); i++) {
                runLengths[i] = this.runLengths.get(i);
            }

            return avgf.format(StatUtils.percentile(runLengths, 50));
        } else return avgf.format(0);
    }

    @Override
    public void addBreakShot(int ballsMade, boolean continuation, boolean foul) {
        if (foul)
            breakFouls++;
    }

    @Override
    public float getMatchCompletionPct() {
        return (float) getPoints() / (float) rank;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;

        StraightPoolPlayer that = (StraightPoolPlayer) o;

        if (highRun != that.highRun) return false;
        if (seriousFouls != that.seriousFouls) return false;
        return runLengths.equals(that.runLengths);

    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + highRun;
        result = 31 * result + seriousFouls;
        result = 31 * result + runLengths.hashCode();
        return result;
    }

    @Override
    public String toString() {
        return "StraightPoolPlayer{" +
                "highRun=" + highRun +
                ", seriousFouls=" + seriousFouls +
                "} " + super.toString();
    }

    public int getSeriousFouls() {
        return seriousFouls;
    }
}
