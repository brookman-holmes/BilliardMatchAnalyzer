package com.brookmanholmes.billiards.player;

import org.apache.commons.math3.stat.StatUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Brookman Holmes on 5/9/2016.
 */
public class CompPlayer extends AbstractPlayer implements IWinsOnBreak, IStraightPool {
    // TODO: 8/26/2016 create tests for this whole class
    private int earlyWins = 0;
    private int winsOnBreak = 0;

    // straight pool info
    private int highRun = 0;
    private List<Integer> runLengths = new ArrayList<>();

    public CompPlayer(String name, int rank) {
        super(name, rank);
    }

    public CompPlayer(String name) {
        super(name);
    }

    @Override
    public void addPlayerStats(AbstractPlayer player) {
        super.addPlayerStats(player);

        if (player instanceof IEarlyWins) {
            earlyWins += ((IEarlyWins) player).getEarlyWins();
        }

        if (player instanceof IWinsOnBreak) {
            winsOnBreak += ((IWinsOnBreak) player).getWinsOnBreak();
        }

        if (player instanceof StraightPoolPlayer) {
            if (highRun < ((StraightPoolPlayer) player).getHighRun())
                highRun = ((StraightPoolPlayer) player).getHighRun();
            runLengths.addAll(((StraightPoolPlayer) player).getRunLengths());

            gameTotal++;

            if (((StraightPoolPlayer) player).getPoints() >= player.rank)
                gameWins++;
        }
    }

    @Override
    public int getHighRun() {
        return highRun;
    }

    @Override
    public List<Integer> getRunLengths() {
        return new ArrayList<>(runLengths);
    }

    @Override
    public double getAverageRunLength() {
        if (runLengths.size() > 0) {
            double[] runLengths = new double[this.runLengths.size()];
            for (int i = 0; i < this.runLengths.size(); i++) {
                runLengths[i] = this.runLengths.get(i);
            }

            return StatUtils.mean(runLengths);
        } else return 0;
    }

    @Override
    public double getMedianRunLength() {
        if (runLengths.size() > 0) {
            double[] runLengths = new double[this.runLengths.size()];
            for (int i = 0; i < this.runLengths.size(); i++) {
                runLengths[i] = this.runLengths.get(i);
            }

            return StatUtils.percentile(runLengths, 50);
        } else return 0;
    }

    @Override
    public void addWinOnBreak() {
        winsOnBreak++;
    }

    @Override
    public int getWinsOnBreak() {
        return winsOnBreak;
    }

    @Override
    public void addWinsOnBreak(int wins) {
        if (wins < 0)
            throw new IllegalArgumentException("Wins must be greater than or equal to 0");
        winsOnBreak += wins;
    }

    @Override
    public void addEarlyWin() {
        earlyWins++;
    }

    @Override
    public int getEarlyWins() {
        return earlyWins;
    }

    @Override
    public void addEarlyWins(int wins) {
        if (wins < 0)
            throw new IllegalArgumentException("Wins must be greater than or equal to 0");
        earlyWins += wins;
    }
}
