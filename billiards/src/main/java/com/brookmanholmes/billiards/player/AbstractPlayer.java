package com.brookmanholmes.billiards.player;

import java.text.DecimalFormat;

/**
 * Created by Brookman Holmes on 10/28/2015.
 */
public abstract class AbstractPlayer {
    // formatter for percentages (e.g. .875)
    final static DecimalFormat pctf = new DecimalFormat("#.000");
    // formatter for average number of balls made per turn (e.g. 5.33)
    final static DecimalFormat avgf = new DecimalFormat("##.##");
    final static String ZERO_PERCENT = ".000";
    final static String ZERO = "0";

    String name = "";
    int safetyAttempts = 0;
    int safetySuccesses = 0;
    int safetyScratches = 0;
    int safetyReturns = 0;
    int safetyEscapes = 0;
    int safetyForcedErrors = 0;

    int breakSuccesses = 0;
    int breakAttempts = 0;
    int breakContinuations = 0;
    int breakScratches = 0;
    int breakBallsMade = 0;

    int shootingBallsMade = 0;
    int shootingTurns = 0;
    int shootingMisses = 0;
    int shootingScratches = 0;

    int runOuts = 0;
    int runTierOne = 0;
    int runTierTwo = 0;

    int gameTotal = 0;
    int gameWins = 0;

    public AbstractPlayer(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void addSafetyAttempt(boolean scratch) {
        safetyAttempts++;

        if (scratch)
            safetyScratches++;
    }

    public void addSafety(boolean opponentPlayedSuccessfulSafe) {
        addSafetyAttempt(false);
        safetySuccesses++;
        if (opponentPlayedSuccessfulSafe) safetyReturns++;
    }

    public void addShootingBallsMade(int ballsMade, boolean scratch) {
        shootingBallsMade += ballsMade;

        if (scratch)
            shootingScratches++;
    }

    public void addShootingMiss() {
        shootingMisses++;
    }

    public void addShootingTurn() {
        shootingTurns++;
    }

    public void addBreakShot(int ballsMade, boolean continuation, boolean scratch) {
        breakAttempts++;

        if (scratch)
            breakScratches++;
        else {
            if (ballsMade > 0)
                breakSuccesses++;

            if (continuation)
                breakContinuations++;
        }
    }

    public void addGameWon() {
        gameTotal++;
        gameWins++;
    }

    public void addGameLost() {
        gameTotal++;
    }

    /**
     * Combined stats functions
     */
    public String getWinPct() {
        if (gameTotal > 0) {
            return pctf.format((double) gameWins / (double) gameTotal);
        } else return ZERO_PERCENT;
    }

    public String getAvgBallsTurn() {
        if (shootingTurns > 0) {
            return avgf.format(((double) shootingBallsMade + (double) breakBallsMade) / (double) shootingTurns);
        } else return ZERO;
    }

    public String getSafetyPct() {
        if (safetyAttempts > 0) {
            return pctf.format((double) safetySuccesses / (double) safetyAttempts);
        } else return ZERO_PERCENT;
    }

    public String getShootingPct() {
        if (shootingBallsMade + shootingMisses > 0) {
            return pctf.format((double) shootingBallsMade / ((double) shootingBallsMade + (double) shootingMisses));
        } else return ZERO_PERCENT;
    }

    public int getShotsAttempted() {
        return shootingBallsMade + safetyAttempts + breakAttempts + shootingMisses;
    }

    public int getShotsSucceeded() {
        return shootingBallsMade + safetySuccesses + breakSuccesses;
    }

    public String getAvgBallsBreak() {
        if (breakAttempts > 0) {
            return avgf.format(((double) breakBallsMade / (double) breakAttempts));
        } else return ZERO;
    }

    public String getAggressivenessRating() {
        if (shootingBallsMade + shootingMisses + safetyAttempts > 0) {
            return pctf.format((((double) shootingBallsMade + (double) shootingMisses) / ((double) shootingBallsMade + (double) shootingMisses + (double) safetyAttempts)));
        } else return ZERO_PERCENT;

    }

    public String getTrueShootingPct() {
        if (getShotsAttempted() > 0) {
            return pctf.format((double) getShotsSucceeded() / (double) getShotsAttempted());
        } else return ZERO_PERCENT;
    }
}
