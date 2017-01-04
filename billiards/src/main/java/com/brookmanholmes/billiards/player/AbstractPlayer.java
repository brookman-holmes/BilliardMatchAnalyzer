package com.brookmanholmes.billiards.player;

import java.text.DecimalFormat;
import java.util.Date;

/**
 * Data class for storing information about player stats
 * Created by Brookman Holmes on 10/28/2015.
 */
public abstract class AbstractPlayer implements Comparable<AbstractPlayer> {
    // formatter for percentages (e.g. .875)
    final static DecimalFormat pctf = new DecimalFormat("#.000");
    // formatter for average number of balls made per turn (e.g. 5.33)
    final static DecimalFormat avgf = new DecimalFormat("##.##");
    final static String ZERO_PERCENT = ".000";
    final static String ZERO = "0";
    Date date;
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
    int breakAndRuns = 0;
    int tableRuns = 0;
    int fiveBallRun = 0;
    private String name = "";

    /**
     * Creates a new player with the specified arguments
     * @param name The name of the player
     * @param rank The rank of the player
     */
    public AbstractPlayer(String name, int rank) {
        this.name = name;
        this.rank = rank;
    }

    /**
     * Creates a new player with the specified arguments, defaulting the rank to 0
     * @param name The name of the player
     */
    public AbstractPlayer(String name) {
        this(name, 0);
    }

    /**
     * Takes stats from another player and adds them to the this player
     * @param player The player whose stats you want to add
     */
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

        breakAndRuns += player.breakAndRuns;
        tableRuns += player.tableRuns;
        fiveBallRun += player.fiveBallRun;

        gameTotal += player.gameTotal;
        gameWins += player.gameWins;
    }

    /**
     * Getter for the player name
     * @return The name of the player
     */
    public String getName() {
        return name;
    }

    /**
     * Setter for the player name
     * @param name The new name of the player
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Getter for the rank of the player
     * @return The rank of the player
     */
    public int getRank() {
        return rank;
    }

    /**
     * Getter for the date of the match that this player played in
     * @return A copy of the date, unless the date is null then it returns the current date
     */
    public Date getMatchDate() {
        return date == null ? new Date() : new Date(date.getTime());
    }

    /**
     * Sets the date of the match that this player played in
     * @param date The date this player played in a match
     */
    public void setMatchDate(Date date) {
        this.date = new Date(date.getTime());
    }

    /**
     * Adds a safety attempt for the player and a safety foul (if they fouled)
     * @param foul Whether or not the player fouled on their attempted safety
     */
    public void addSafetyAttempt(boolean foul) {
        safetyAttempts++;

        if (foul)
            safetyFouls++;
    }

    /**
     * Adds a safety for the player and a safety return (if their opponent played a safe on them
     * in the last turn)
     * @param opponentPlayedSuccessfulSafe Whether or not they came to the table after their opponent
     *                                     played a safe
     * @param shootingBallsMade
     */
    public void addSafety(boolean opponentPlayedSuccessfulSafe, int shootingBallsMade) {
        addSafetyAttempt(false);
        safetySuccesses++;
        if (opponentPlayedSuccessfulSafe && shootingBallsMade == 0)
            safetyReturns++;
    }

    /**
     * Adds balls made for the player and whether or not they fouled, this will also automatically
     * add in a shooting turn for the player
     * @param ballsMade The number of balls made
     * @param foul Whether or not the player fouled
     */
    public void addShootingBallsMade(int ballsMade, boolean foul) {
        shootingBallsMade += ballsMade;
        shootingTurns++;

        if (foul)
            shootingFouls++;
    }

    /**
     * Adds in a shooting miss for the player
     */
    public void addShootingMiss() {
        shootingMisses++;
    }

    /**
     * Adds in a break shot, the number of balls made, whether or not they got continuation and
     * if they fouled
     * @param ballsMade The number of balls made
     * @param continuation True if they continued their play after the break, false otherwise
     * @param foul True if the player fouled on the break, false otherwise
     */
    public void addBreakShot(int ballsMade, boolean continuation, boolean foul) {
        breakAttempts++;

        if (foul)
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

    /**
     * Increments both {@link com.brookmanholmes.billiards.player.AbstractPlayer#gameTotal} and
     * {@link com.brookmanholmes.billiards.player.AbstractPlayer#gameWins} by 1
     */
    public void addGameWon() {
        gameTotal++;
        gameWins++;
    }

    /**
     * Increments both {@link com.brookmanholmes.billiards.player.AbstractPlayer#gameTotal} by 1
     */
    public void addGameLost() {
        gameTotal++;
    }

    /**
     * Getter for gameWins
     * @return The number of games won
     */
    public int getWins() {
        return gameWins;
    }

    /**
     * Getter for gameTotal
     * @return The number of games played
     */
    public int getGameTotal() {
        return gameTotal;
    }

    /**
     * Getter for the total number of fouls, combines
     * {@link com.brookmanholmes.billiards.player.AbstractPlayer#shootingFouls},
     * {@link com.brookmanholmes.billiards.player.AbstractPlayer#safetyFouls} and
     * {@link com.brookmanholmes.billiards.player.AbstractPlayer#breakFouls}
     * @return The total number of times the player has fouled
     */
    public int getTotalFouls() {
        return shootingFouls + safetyFouls + breakFouls;
    }

    /**
     * Getter for the number of shots made
     * @return The total number of shooting balls made
     */
    public int getShootingBallsMade() {
        return shootingBallsMade;
    }

    /**
     * Getter for the total number of shooting attempts, combines
     * {@link com.brookmanholmes.billiards.player.AbstractPlayer#shootingMisses} and
     * {@link com.brookmanholmes.billiards.player.AbstractPlayer#shootingBallsMade}
     * @return The total number of shooting attempts
     */
    public int getShootingAttempts() {
        return shootingMisses + shootingBallsMade;
    }

    /**
     * Getter for shooting fouls
     * @return Returns the total number of shooting fouls made
     */
    public int getShootingFouls() {
        return shootingFouls;
    }

    /**
     * Returns the number of safety attempts made
     * @return The number of safety attempts made
     */
    public int getSafetyAttempts() {
        return safetyAttempts;
    }

    /**
     * Returns the number of successful safeties made
     * @return The number of successful safeties made
     */
    public int getSafetySuccesses() {
        return safetySuccesses;
    }

    /**
     * Returns the number of fouls made during safety attempts
     * @return The number of fouls made during safety attempts
     */
    public int getSafetyFouls() {
        return safetyFouls;
    }

    /**
     * Returns the number of safeties that were made immediately following their opponent playing
     * a safety
     * @return The number of safeties returned
     */
    public int getSafetyReturns() {
        return safetyReturns;
    }

    /**
     * Returns the number of shots that were made immediately following their opponent playing
     * a safety
     * @return The number of safeties escaped
     */
    public int getSafetyEscapes() {
        return safetyEscapes;
    }

    /**
     * The number of errors made because the opponent played a safety
     * @return The number of errors made following an opponent playing a safety
     */
    public int getSafetyForcedErrors() {
        return safetyForcedErrors;
    }

    /**
     * The number of break and runs made
     * @return The number of break and runs
     */
    public int getBreakAndRuns() {
        return breakAndRuns;
    }

    /**
     * The number of table runs made (making all the balls on the table in 9/10 ball, or all 8 of
     * your balls in 8 ball)
     * @return The number of table runs
     */
    public int getTableRuns() {
        return tableRuns;
    }

    /**
     * The number of games won that involved making 5 or fewer balls
     * @return The number of games won that involved making 5 or fewer balls
     */
    public int getFiveBallRun() {
        return fiveBallRun;
    }

    /**
     * The number of breaks that made a ball
     * @return The number of breaks that made a ball
     */
    public int getBreakSuccesses() {
        return breakSuccesses;
    }

    /**
     * The number of break shots attempted
     * @return The number of break shots attempted
     */
    public int getBreakAttempts() {
        return breakAttempts;
    }

    /**
     * The number of breaks that the player made a ball immediately after the break
     * @return The number of breaks that the player 'continued' shooting afterwards
     */
    public int getBreakContinuations() {
        return breakContinuations;
    }

    /**
     * The number of breaks that were fouls
     * @return The number of fouls on break shots
     */
    public int getBreakFouls() {
        return breakFouls;
    }

    /**
     * The number of balls made in all break shots combined
     * @return The total number of balls made during all break shots
     */
    public int getBreakBallsMade() {
        return breakBallsMade;
    }

    /**
     * Adds a break and run to the player
     */
    public void addBreakAndRun() {
        breakAndRuns++;
    }

    /**
     * Adds a table run to the player
     */
    public void addTableRun() {
        tableRuns++;
    }

    /**
     * Adds a five (or fewer) ball run to the player
     */
    public void addFiveBallRun() {
        fiveBallRun++;
    }

    /**
     * Adds a safety escape to the player (opponent played a safe and the player makes a ball)
     */
    public void addSafetyEscape() {
        safetyEscapes++;
    }

    /**
     * Adds a forced error to the player (opponent played a safe and the player fouls)
     */
    public void addSafetyForcedError() {
        safetyForcedErrors++;
    }

    /**
     * Retrieves the win percentage for the player, formatted as #.000
     * @return The win percentage for the player
     */
    public String getWinPct() {
        if (gameTotal > 0) {
            return pctf.format((double) gameWins / (double) gameTotal);
        } else return ZERO_PERCENT;
    }

    /**
     * The average number of balls made per turn, formatted as ##.##
     * @return The average number of balls made per turn
     */
    public String getAvgBallsTurn() {
        if (shootingTurns > 0) {
            return avgf.format(((double) shootingBallsMade) / (double) shootingTurns);
        } else return ZERO;
    }

    /**
     * Retrieves the safety percentage for the player, formatted as #.000
     * @return The safety percentage for the player
     */
    public String getSafetyPct() {
        if (safetyAttempts > 0) {
            return pctf.format((double) safetySuccesses / (double) safetyAttempts);
        } else return ZERO_PERCENT;
    }

    /**
     * Retrieves the shooting percentage for the player, formatted as #.000
     * @return The shooting percentage for the player
     */
    public String getShootingPct() {
        if (getShootingAttempts() > 0) {
            return pctf.format((double) shootingBallsMade / ((double) getShootingAttempts()));
        } else return ZERO_PERCENT;
    }

    /**
     * Retrieves the successful percentage for the player, formatted as #.000
     * @return The successful breaking percentage for the player
     */
    public String getBreakPct() {
        if (getBreakAttempts() > 0) {
            return pctf.format((double) breakSuccesses / (double) breakAttempts);
        } else return ZERO_PERCENT;
    }

    public float getMatchCompletionPct() {
        return (float) gameWins / (float) rank;
    }

    /**
     * The total number of shots attempted (shooting, breaking and safeties)
     * @return The total number of shots attempted
     */
    public int getShotAttemptsOfAllTypes() {
        return getShootingAttempts() + getBreakAttempts() + getSafetyAttempts();
    }

    /**
     * The total number of successful shots (shooting, breaking, safeties)
     * @return The total number of successful shots
     */
    public int getShotsSucceededOfAllTypes() {
        return getShootingBallsMade() + getBreakSuccesses() + getSafetySuccesses();
    }

    /**
     * The average number of balls made on the break, formatted as ##.##
     * @return The average number of balls made on the break
     */
    public String getAvgBallsBreak() {
        if (breakAttempts > 0) {
            return avgf.format(((double) breakBallsMade / (double) breakAttempts));
        } else return ZERO;
    }

    /**
     * The aggressiveness of the player
     * {@link AbstractPlayer#getShootingAttempts()} / (
     * {@link AbstractPlayer#getShootingAttempts()} + {@link AbstractPlayer#getSafetyAttempts()})
     * formatted as #.000
     * @return The aggressiveness of the player
     */
    public String getAggressivenessRating() {
        if (shootingBallsMade + shootingMisses + safetyAttempts > 0) {
            return pctf.format((((double) getShootingAttempts()) / ((double) getShootingAttempts() + (double) safetyAttempts)));
        } else return ZERO_PERCENT;

    }

    /**
     * The player's true shooting percentage, which is determined by
     * {@link AbstractPlayer#getShotsSucceededOfAllTypes()} /
     * {@link AbstractPlayer#getShotAttemptsOfAllTypes()} formatted as #.000
     * @return The player's true shooting percentage
     */
    public String getTrueShootingPct() {
        if (getShotAttemptsOfAllTypes() > 0) {
            return pctf.format((double) getShotsSucceededOfAllTypes() / (double) getShotAttemptsOfAllTypes());
        } else return ZERO_PERCENT;
    }

    @Override
    public int compareTo(AbstractPlayer o) {
        return this.name.compareTo(o.name);
    }

    @Override
    public boolean equals(Object o) {
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
        if (breakAndRuns != that.breakAndRuns) return false;
        if (tableRuns != that.tableRuns) return false;
        if (fiveBallRun != that.fiveBallRun) return false;
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
        result = 31 * result + breakAndRuns;
        result = 31 * result + tableRuns;
        result = 31 * result + fiveBallRun;
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
                "\n breakAndRuns=" + breakAndRuns +
                "\n tableRuns=" + tableRuns +
                "\n fiveBallRun=" + fiveBallRun +
                "\n gameTotal=" + gameTotal +
                "\n gameWins=" + gameWins +
                '}';
    }
}
