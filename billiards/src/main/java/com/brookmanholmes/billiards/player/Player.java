package com.brookmanholmes.billiards.player;

import com.brookmanholmes.billiards.game.GameType;

import org.apache.commons.math3.stat.StatUtils;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Data class for storing information about player stats
 * Created by Brookman Holmes on 10/28/2015.
 */
public class Player implements Comparable<Player>, Serializable {

    private final GameType gameType;

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
    int opponentRank;
    int winsOnBreak = 0;
    int earlyWins = 0;
    int points;
    int deadBalls;
    int highRun;
    int seriousFouls;
    List<Integer> runLengths = new ArrayList<>();
    String name = "";

    public Player(String name, GameType gameType, List<Player> players) {
        this(name, gameType, players.size() > 0 ? players.get(0).rank : 5);

        for (Player player : players)
            addPlayerStats(player);
    }

    public Player(String name, GameType gameType, int rank, int opponentRank) {
        this.opponentRank = opponentRank;
        this.name = name;
        this.gameType = gameType;
        this.rank = rank;
    }

    /**
     * Creates a new player with the specified arguments
     * @param name The name of the player
     * @param rank The rank of the player
     */
    public Player(String name, GameType gameType, int rank) {
        this.name = name;
        this.rank = rank;
        this.gameType = gameType;
    }

    /**
     * Creates a new player with the specified arguments, defaulting the rank to 0
     * @param name The name of the player
     */
    public Player(String name, GameType gameType) {
        this(name, gameType, 0);
    }

    /**
     * Takes stats from another player and adds them to the this player
     * @param player The player whose stats you want to add
     */
    public void addPlayerStats(Player player) {
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

        earlyWins += player.earlyWins;
        winsOnBreak += player.winsOnBreak;
        points += player.points;
        deadBalls += player.deadBalls;

        if (highRun < player.highRun)
            highRun = player.highRun;

        seriousFouls += player.seriousFouls;

        if (gameType.isStraightPool()) {
            if (player.shootingTurns > 0)
                runLengths.add(player.shootingBallsMade);

            if (player.points > player.rank)
                gameWins++;
        }
    }

    public int getMatchPoints(int opponentScore) {
        if (gameType.isApa8Ball()) {
            if (getWins() == Players.apa8BallRaceTo(rank, opponentRank).getPlayerRaceTo()) {
                if (opponentScore == 0)
                    return 3;
                else return 2;
            } else if (getWins() + 1 == Players.apa8BallRaceTo(rank, opponentRank).getPlayerRaceTo()) {
                return 1;
            } else return 0;
        } else if (gameType.isApa9Ball()) {
            if (points >= Players.apa9BallRaceTo(rank))
                return 20 - Players.getMinimumMatchPointsEarned(opponentRank, opponentScore);
            else return Players.getMinimumMatchPointsEarned(rank, points);
        } else return 0;
    }

    public int getPoints() {
        if (gameType.isApa8Ball()) {
            return gameWins;
        } else if (gameType.isApa9Ball()) {
            return shootingBallsMade + breakBallsMade + gameWins;
        } else if (gameType.isStraightPool()) {
            return shootingBallsMade - shootingFouls - breakFouls - (seriousFouls * 15);
        } else return gameWins;
    }

    public int getPointsNeeded() {
        if (gameType.isApa8Ball()) {
            return Players.apa8BallRaceTo(rank, opponentRank).getPlayerRaceTo();
        } else if (gameType.isApa9Ball()) {
            return Players.apa9BallRaceTo(rank);
        } else return rank;
    }

    public int getDeadBalls() {
        return deadBalls;
    }

    public void addDeadBalls(int deadBalls) {
        this.deadBalls += deadBalls;
    }

    public void addWinOnBreak() {
        winsOnBreak++;
    }

    public int getWinsOnBreak() {
        return winsOnBreak;
    }

    public void addWinsOnBreak(int wins) {
        winsOnBreak += wins;
    }

    public void addEarlyWin() {
        earlyWins++;
    }

    public int getEarlyWins() {
        return earlyWins;
    }

    public void addEarlyWins(int wins) {
        earlyWins += wins;
    }

    public int getHighRun() {
        return highRun;
    }

    public List<Integer> getRunLengths() {
        return new ArrayList<>(runLengths);
    }

    public void addSeriousFoul() {
        seriousFouls++;
    }

    public GameType getGameType() {
        return gameType;
    }

    public double getAverageRunLength() {
        if (runLengths.size() > 0) {
            double[] runLengths = new double[this.runLengths.size()];
            for (int i = 0; i < this.runLengths.size(); i++) {
                runLengths[i] = this.runLengths.get(i);
            }

            return StatUtils.mean(runLengths);
        } else return 0;
    }

    public double getMedianRunLength() {
        if (runLengths.size() > 0) {
            double[] runLengths = new double[this.runLengths.size()];
            for (int i = 0; i < this.runLengths.size(); i++) {
                runLengths[i] = this.runLengths.get(i);
            }

            return StatUtils.percentile(runLengths, 50);
        } else return 0;
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
     * Increments both {@link Player#gameTotal} and
     * {@link Player#gameWins} by 1
     */
    public void addGameWon() {
        gameTotal++;
        gameWins++;
    }

    /**
     * Increments both {@link Player#gameTotal} by 1
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
     * {@link Player#shootingFouls},
     * {@link Player#safetyFouls} and
     * {@link Player#breakFouls}
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
     * {@link Player#shootingMisses} and
     * {@link Player#shootingBallsMade}
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
     * Retrieves the win percentage for the player
     * @return The win percentage for the player
     */
    public double getWinPct() {
        if (gameTotal > 0) {
            return (double) gameWins / (double) gameTotal;
        } else return 0;
    }

    /**
     * The average number of balls made per turn
     * @return The average number of balls made per turn
     */
    public double getAvgBallsTurn() {
        if (shootingTurns > 0) {
            return ((double) shootingBallsMade) / (double) shootingTurns;
        } else return 0;
    }

    /**
     * Retrieves the safety percentage for the player
     * @return The safety percentage for the player
     */
    public double getSafetyPct() {
        if (safetyAttempts > 0) {
            return (double) safetySuccesses / (double) safetyAttempts;
        } else return 0;
    }

    /**
     * Retrieves the shooting percentage for the player
     * @return The shooting percentage for the player
     */
    public double getShootingPct() {
        if (getShootingAttempts() > 0) {
            return (double) shootingBallsMade / (double) getShootingAttempts();
        } else return 0;
    }

    /**
     * Retrieves the successful percentage for the player
     * @return The successful breaking percentage for the player
     */
    public double getBreakPct() {
        if (getBreakAttempts() > 0) {
            return (double) breakSuccesses / (double) breakAttempts;
        } else return 0;
    }

    public float getMatchCompletionPct() {
        if (getPointsNeeded() == 0)
            return 0;
        else
            return (float) getPoints() / (float) getPointsNeeded();
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
     * The average number of balls made on the break
     * @return The average number of balls made on the break
     */
    public double getAvgBallsBreak() {
        if (breakAttempts > 0) {
            return (double) breakBallsMade / (double) breakAttempts;
        } else return 0;
    }

    /**
     * The aggressiveness of the player
     * {@link Player#getShootingAttempts()} / (
     * {@link Player#getShootingAttempts()} + {@link Player#getSafetyAttempts()})
     * @return The aggressiveness of the player
     */
    public double getAggressivenessRating() {
        if (shootingBallsMade + shootingMisses + safetyAttempts > 0) {
            return (((double) getShootingAttempts()) / ((double) getShootingAttempts() + (double) safetyAttempts));
        } else return 0;

    }

    /**
     * The player's true shooting percentage, which is determined by
     * {@link Player#getShotsSucceededOfAllTypes()} /
     * {@link Player#getShotAttemptsOfAllTypes()}
     * @return The player's true shooting percentage
     */
    public double getTrueShootingPct() {
        if (getShotAttemptsOfAllTypes() > 0) {
            return (double) getShotsSucceededOfAllTypes() / (double) getShotAttemptsOfAllTypes();
        } else return 0;
    }

    @Override
    public int compareTo(Player o) {
        return this.name.compareTo(o.name);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Player that = (Player) o;

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
        return "Player{" +
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

    public void addPoints(int points) {
        this.points += points;
    }

    public int getSeriousFouls() {
        return seriousFouls;
    }
}