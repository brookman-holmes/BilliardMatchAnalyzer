package com.brookmanholmes.billiards.player;

/**
 * Created by Brookman Holmes on 1/12/2016.
 */
public class StraightPoolPlayer extends AbstractPlayer {
    private int highRun = 0;
    private int seriousFouls = 0;

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
            this.seriousFouls += seriousFouls;
        }
    }

    public void addSeriousFoul() {
        seriousFouls++;
    }

    public int getPoints() {
        return shootingBallsMade - shootingFouls - safetyFouls - getBreakingFoulPoints() - getSeriousFoulsPoints();
    }

    private int getBreakingFoulPoints() {
        return breakFouls * 2;
    }

    private int getSeriousFoulsPoints() {
        return seriousFouls * 15;
    }

    @Override
    public String toString() {
        return "StraightPoolPlayer{" +
                "highRun=" + highRun +
                ", seriousFouls=" + seriousFouls +
                "} " + super.toString();
    }
}
