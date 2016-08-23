package com.brookmanholmes.billiards.player;

import java.text.DecimalFormat;
import java.util.Date;

/**
 * Created by Brookman Holmes on 10/28/2015.
 */
public abstract class AbstractPlayer implements Comparable<AbstractPlayer> {
    // formatter for percentages (e.g. .875)
    private final static DecimalFormat pctf = new DecimalFormat("#.000");
    // formatter for average number of balls made per turn (e.g. 5.33)
    private final static DecimalFormat avgf = new DecimalFormat("##.##");
    private final static String ZERO_PERCENT = ".000";
    private final static String ZERO = "0";
    private String name = "";
    private Date date;
    int rank;
    int safetyAttempts = 0;
    int safetySuccesses = 0;
    int safetyFouls = 0;
    int safetyReturns = 0;
    int breakSuccesses = 0;
    int breakAttempts = 0;
    int breakContinuations = 0;
    int breakFouls = 0;
    int breakBallsMade = 0;
    int shootingBallsMade = 0;
    int shootingTurns = 0;
    int shootingMisses = 0;
    int shootingFouls = 0;
    int gameTotal = 0;
    int gameWins = 0;
    int safetyEscapes = 0;
    int safetyForcedErrors = 0;
    int runOuts = 0;
    int runTierOne = 0;
    int runTierTwo = 0;

    public AbstractPlayer(String name, int rank) {
        this.name = name;
        this.rank = rank;
    }

    public AbstractPlayer(String name) {
        this.name = name;
        this.rank = Integer.MAX_VALUE;
    }

    public void addPlayerStats(AbstractPlayer player) {
        this.safetyAttempts += player.safetyAttempts;
        this.safetyReturns += player.safetyReturns;
        this.safetySuccesses += player.safetySuccesses;
        this.safetyFouls += player.safetyFouls;
        this.safetyEscapes += player.safetyEscapes;
        this.safetyForcedErrors += player.safetyForcedErrors;

        this.breakSuccesses += player.breakSuccesses;
        this.breakAttempts += player.breakAttempts;
        this.breakContinuations += player.breakContinuations;
        this.breakFouls += player.breakFouls;
        this.breakBallsMade += player.breakBallsMade;

        shootingBallsMade += player.shootingBallsMade;
        shootingTurns += player.shootingTurns;
        shootingMisses += player.shootingMisses;
        shootingFouls += player.shootingFouls;

        runOuts += player.runOuts;
        runTierOne += player.runTierOne;
        runTierTwo += player.runTierTwo;

        gameTotal += player.gameTotal;
        gameWins += player.gameWins;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getRank() {
        return rank;
    }

    public Date getMatchDate() {
        return date == null ? new Date() : new Date(date.getTime());
    }

    public void setMatchDate(Date date) {
        this.date = date;
    }

    public void addSafetyAttempt(boolean scratch) {
        safetyAttempts++;

        if (scratch)
            safetyFouls++;
    }

    public void addSafety(boolean opponentPlayedSuccessfulSafe) {
        addSafetyAttempt(false);
        safetySuccesses++;
        if (opponentPlayedSuccessfulSafe) safetyReturns++;
    }

    public void addShootingBallsMade(int ballsMade, boolean scratch) {
        shootingBallsMade += ballsMade;
        shootingTurns++;

        if (scratch)
            shootingFouls++;
    }

    public void addShootingMiss() {
        shootingMisses++;
    }

    public void addBreakShot(int ballsMade, boolean continuation, boolean scratch) {
        breakAttempts++;

        if (scratch)
            breakFouls++;
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

    public int getTotalFouls() {
        return shootingFouls + safetyFouls + breakFouls;
    }

    public int getShootingBallsMade() {
        return shootingBallsMade;
    }

    public int getShootingAttempts() {
        return shootingMisses + shootingBallsMade;
    }

    public int getShootingFouls() {
        return shootingFouls;
    }

    public int getSafetyAttempts() {
        return safetyAttempts;
    }

    public int getSafetySuccesses() {
        return safetySuccesses;
    }

    public int getSafetyFouls() {
        return safetyFouls;
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

    public int getBreakFouls() {
        return breakFouls;
    }

    public int getBreakBallsMade() {
        return breakBallsMade;
    }

    public void addBreakAndRun() {
        runOuts++;
    }

    public void addTableRun() {
        runTierOne++;
    }

    public void addFourBallRun() {
        runTierTwo++;
    }

    public void addSafetyEscape() {
        safetyEscapes++;
    }

    public void addSafetyForcedError() {
        safetyForcedErrors++;
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

    public String getBreakPct() {
        if (getBreakAttempts() > 0) {
            return pctf.format((double) breakSuccesses / (double) breakAttempts);
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

    @Override public int compareTo(AbstractPlayer o) {
        return this.name.compareTo(o.name);
    }

    @Override public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        AbstractPlayer that = (AbstractPlayer) o;

        if (safetyAttempts != that.safetyAttempts) return false;
        if (safetySuccesses != that.safetySuccesses) return false;
        if (safetyFouls != that.safetyFouls) return false;
        if (safetyReturns != that.safetyReturns) return false;
        if (safetyEscapes != that.safetyEscapes) return false;
        if (safetyForcedErrors != that.safetyForcedErrors) return false;
        if (breakSuccesses != that.breakSuccesses) return false;
        if (breakAttempts != that.breakAttempts) return false;
        if (breakContinuations != that.breakContinuations) return false;
        if (breakFouls != that.breakFouls) return false;
        if (breakBallsMade != that.breakBallsMade) return false;
        if (shootingBallsMade != that.shootingBallsMade) return false;
        if (shootingTurns != that.shootingTurns) return false;
        if (shootingMisses != that.shootingMisses) return false;
        if (shootingFouls != that.shootingFouls) return false;
        if (runOuts != that.runOuts) return false;
        if (runTierOne != that.runTierOne) return false;
        if (runTierTwo != that.runTierTwo) return false;
        if (gameTotal != that.gameTotal) return false;
        if (gameWins != that.gameWins) return false;
        return name.equals(that.name);

    }

    @Override public int hashCode() {
        int result = name.hashCode();
        result = 31 * result + safetyAttempts;
        result = 31 * result + safetySuccesses;
        result = 31 * result + safetyFouls;
        result = 31 * result + safetyReturns;
        result = 31 * result + safetyEscapes;
        result = 31 * result + safetyForcedErrors;
        result = 31 * result + breakSuccesses;
        result = 31 * result + breakAttempts;
        result = 31 * result + breakContinuations;
        result = 31 * result + breakFouls;
        result = 31 * result + breakBallsMade;
        result = 31 * result + shootingBallsMade;
        result = 31 * result + shootingTurns;
        result = 31 * result + shootingMisses;
        result = 31 * result + shootingFouls;
        result = 31 * result + runOuts;
        result = 31 * result + runTierOne;
        result = 31 * result + runTierTwo;
        result = 31 * result + gameTotal;
        result = 31 * result + gameWins;
        return result;
    }

    @Override public String toString() {
        return "AbstractPlayer{" +
                "name='" + name + '\'' +
                "\n safetyAttempts=" + safetyAttempts +
                "\n safetySuccesses=" + safetySuccesses +
                "\n safetyFouls=" + safetyFouls +
                "\n safetyReturns=" + safetyReturns +
                "\n safetyEscapes=" + safetyEscapes +
                "\n safetyForcedErrors=" + safetyForcedErrors +
                "\n breakSuccesses=" + breakSuccesses +
                "\n breakAttempts=" + breakAttempts +
                "\n breakContinuations=" + breakContinuations +
                "\n breakFouls=" + breakFouls +
                "\n breakBallsMade=" + breakBallsMade +
                "\n shootingBallsMade=" + shootingBallsMade +
                "\n shootingTurns=" + shootingTurns +
                "\n shootingMisses=" + shootingMisses +
                "\n shootingFouls=" + shootingFouls +
                "\n runOuts=" + runOuts +
                "\n runTierOne=" + runTierOne +
                "\n runTierTwo=" + runTierTwo +
                "\n gameTotal=" + gameTotal +
                "\n gameWins=" + gameWins +
                '}';
    }
}
