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
            if (ballsMade > 0) {
                breakBallsMade += ballsMade;
                breakSuccesses++;
            }

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

    public int getWins() {
        return gameWins;
    }

    public int getGamesPlayed() {
        return gameTotal;
    }

    public int getShootingBallsMade() {
        return shootingBallsMade;
    }

    public int getShootingAttempts() {
        return shootingMisses + shootingBallsMade;
    }

    public int getShootingScratches() {
        return shootingScratches;
    }

    public int getSafetyAttempts() {
        return safetyAttempts;
    }

    public int getSafetySuccesses() {
        return safetySuccesses;
    }

    public int getSafetyScratches() {
        return safetyScratches;
    }

    public int getSafetyReturns() {
        return safetyReturns;
    }

    public int getSafetyEscapes() {
        return safetyEscapes;
    }

    public int getSafetyForcedErrors() {
        return safetyForcedErrors;
    }

    public int getRunOuts() {
        return runOuts;
    }

    public int getRunTierOne() {
        return runTierOne;
    }

    public int getRunTierTwo() {
        return runTierTwo;
    }

    public int getBreakSuccesses() {
        return breakSuccesses;
    }

    public int getBreakAttempts() {
        return breakAttempts;
    }

    public int getBreakContinuations() {
        return breakContinuations;
    }

    public int getBreakScratches() {
        return breakScratches;
    }

    public int getBreakBallsMade() {
        return breakBallsMade;
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
            return avgf.format(((double) shootingBallsMade) / (double) shootingTurns);
        } else return ZERO;
    }

    public String getSafetyPct() {
        if (safetyAttempts > 0) {
            return pctf.format((double) safetySuccesses / (double) safetyAttempts);
        } else return ZERO_PERCENT;
    }

    public String getShootingPct() {
        if (getShootingAttempts() > 0) {
            return pctf.format((double) shootingBallsMade / ((double) getShootingAttempts()));
        } else return ZERO_PERCENT;
    }

    public int getShotAttemptsOfAllTypes() {
        return getShootingAttempts() + getBreakAttempts() + getSafetyAttempts();
    }

    public int getShotsSucceededOfAllTypes() {
        return getShootingBallsMade() + getBreakSuccesses() + getSafetySuccesses();
    }

    public String getAvgBallsBreak() {
        if (breakAttempts > 0) {
            return avgf.format(((double) breakBallsMade / (double) breakAttempts));
        } else return ZERO;
    }

    public String getAggressivenessRating() {
        if (shootingBallsMade + shootingMisses + safetyAttempts > 0) {
            return pctf.format((((double) getShootingAttempts()) / ((double) getShootingAttempts() + (double) safetyAttempts)));
        } else return ZERO_PERCENT;

    }

    public String getTrueShootingPct() {
        if (getShotAttemptsOfAllTypes() > 0) {
            return pctf.format((double) getShotsSucceededOfAllTypes() / (double) getShotAttemptsOfAllTypes());
        } else return ZERO_PERCENT;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        AbstractPlayer that = (AbstractPlayer) o;

        if (safetyAttempts != that.safetyAttempts) return false;
        if (safetySuccesses != that.safetySuccesses) return false;
        if (safetyScratches != that.safetyScratches) return false;
        if (safetyReturns != that.safetyReturns) return false;
        if (safetyEscapes != that.safetyEscapes) return false;
        if (safetyForcedErrors != that.safetyForcedErrors) return false;
        if (breakSuccesses != that.breakSuccesses) return false;
        if (breakAttempts != that.breakAttempts) return false;
        if (breakContinuations != that.breakContinuations) return false;
        if (breakScratches != that.breakScratches) return false;
        if (breakBallsMade != that.breakBallsMade) return false;
        if (shootingBallsMade != that.shootingBallsMade) return false;
        if (shootingTurns != that.shootingTurns) return false;
        if (shootingMisses != that.shootingMisses) return false;
        if (shootingScratches != that.shootingScratches) return false;
        if (runOuts != that.runOuts) return false;
        if (runTierOne != that.runTierOne) return false;
        if (runTierTwo != that.runTierTwo) return false;
        if (gameTotal != that.gameTotal) return false;
        if (gameWins != that.gameWins) return false;
        return name.equals(that.name);

    }

    @Override
    public int hashCode() {
        int result = name.hashCode();
        result = 31 * result + safetyAttempts;
        result = 31 * result + safetySuccesses;
        result = 31 * result + safetyScratches;
        result = 31 * result + safetyReturns;
        result = 31 * result + safetyEscapes;
        result = 31 * result + safetyForcedErrors;
        result = 31 * result + breakSuccesses;
        result = 31 * result + breakAttempts;
        result = 31 * result + breakContinuations;
        result = 31 * result + breakScratches;
        result = 31 * result + breakBallsMade;
        result = 31 * result + shootingBallsMade;
        result = 31 * result + shootingTurns;
        result = 31 * result + shootingMisses;
        result = 31 * result + shootingScratches;
        result = 31 * result + runOuts;
        result = 31 * result + runTierOne;
        result = 31 * result + runTierTwo;
        result = 31 * result + gameTotal;
        result = 31 * result + gameWins;
        return result;
    }

    @Override
    public String toString() {
        return "AbstractPlayer{" +
                "name='" + name + '\'' +
                "\n safetyAttempts=" + safetyAttempts +
                "\n safetySuccesses=" + safetySuccesses +
                "\n safetyScratches=" + safetyScratches +
                "\n safetyReturns=" + safetyReturns +
                "\n safetyEscapes=" + safetyEscapes +
                "\n safetyForcedErrors=" + safetyForcedErrors +
                "\n breakSuccesses=" + breakSuccesses +
                "\n breakAttempts=" + breakAttempts +
                "\n breakContinuations=" + breakContinuations +
                "\n breakScratches=" + breakScratches +
                "\n breakBallsMade=" + breakBallsMade +
                "\n shootingBallsMade=" + shootingBallsMade +
                "\n shootingTurns=" + shootingTurns +
                "\n shootingMisses=" + shootingMisses +
                "\n shootingScratches=" + shootingScratches +
                "\n runOuts=" + runOuts +
                "\n runTierOne=" + runTierOne +
                "\n runTierTwo=" + runTierTwo +
                "\n gameTotal=" + gameTotal +
                "\n gameWins=" + gameWins +
                '}';
    }
}
