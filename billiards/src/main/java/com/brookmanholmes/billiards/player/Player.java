package com.brookmanholmes.billiards.player;

import com.brookmanholmes.billiards.game.GameType;
import com.brookmanholmes.billiards.turn.ITurn;

import org.apache.commons.math3.stat.StatUtils;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
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
    List<ITurn> turns = new ArrayList<>();
    String id = "";
    String name = "";

    public Player(Player player) {
        this(player.id, player.name, player.gameType, player.rank, player.opponentRank);
        addPlayerStats(player);
    }

    public Player(String id, String name, GameType gameType, int rank, int opponentRank) {
        this(id, name, gameType, rank);
        this.opponentRank = opponentRank;
    }

    /**
     * Creates a new player with the specified arguments
     * @param id The id of the player
     * @param rank The rank of the player
     */
    public Player(String id, String name, GameType gameType, int rank) {
        this.name = name;
        this.id = id;
        this.rank = rank;
        this.gameType = gameType;
    }

    /**
     * Creates a new player with the specified arguments, defaulting the rank to 0
     * @param id The id of the player
     */
    public Player(String id, String name, GameType gameType) {
        this(id, name, gameType, 0);
    }

    public void addPlayerStats(Collection<Player> players) {
        for (Player player : players) {
            addPlayerStats(player);
        }
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

        runLengths.addAll(player.runLengths);

        if (gameType.isStraightPool()) {
            if (player.points > player.rank)
                gameWins++;
        }

        turns.addAll(player.turns);
    }

    public void addTurn(ITurn turn) {
        turns.add(turn);
    }

    public void addTurns(Collection<ITurn> turns) {
        this.turns.addAll(turns);
    }

    public List<ITurn> getTurns() {
        return new ArrayList<>(turns);
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
        } else if (gameType == GameType.ALL) {
            return gameTotal;
        } else
            return rank;
    }

    public int getShootingTurns() {
        return shootingTurns;
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
     * Getter for the player id
     * @return The id of the player
     */
    public String getId() {
        return id;
    }

    /**
     * Setter for the player id
     * @param id The new id of the player
     */
    public void setId(String id) {
        this.id = id;
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
        runLengths.add(ballsMade);
        if (ballsMade > highRun)
            highRun = ballsMade;

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
        return this.id.compareTo(o.id);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Player player = (Player) o;

        if (rank != player.rank) return false;
        if (safetyAttempts != player.safetyAttempts) return false;
        if (safetySuccesses != player.safetySuccesses) return false;
        if (safetyFouls != player.safetyFouls) return false;
        if (safetyReturns != player.safetyReturns) return false;
        if (breakSuccesses != player.breakSuccesses) return false;
        if (breakAttempts != player.breakAttempts) return false;
        if (breakContinuations != player.breakContinuations) return false;
        if (breakFouls != player.breakFouls) return false;
        if (breakBallsMade != player.breakBallsMade) return false;
        if (shootingBallsMade != player.shootingBallsMade) return false;
        if (shootingTurns != player.shootingTurns) return false;
        if (shootingMisses != player.shootingMisses) return false;
        if (shootingFouls != player.shootingFouls) return false;
        if (gameTotal != player.gameTotal) return false;
        if (gameWins != player.gameWins) return false;
        if (safetyEscapes != player.safetyEscapes) return false;
        if (safetyForcedErrors != player.safetyForcedErrors) return false;
        if (breakAndRuns != player.breakAndRuns) return false;
        if (tableRuns != player.tableRuns) return false;
        if (fiveBallRun != player.fiveBallRun) return false;
        if (opponentRank != player.opponentRank) return false;
        if (winsOnBreak != player.winsOnBreak) return false;
        if (earlyWins != player.earlyWins) return false;
        if (points != player.points) return false;
        if (deadBalls != player.deadBalls) return false;
        if (highRun != player.highRun) return false;
        if (seriousFouls != player.seriousFouls) return false;
        if (gameType != player.gameType) return false;
        if (date != null ? !date.equals(player.date) : player.date != null) return false;
        if (!runLengths.equals(player.runLengths)) return false;
        if (!id.equals(player.id)) return false;
        return name.equals(player.name);

    }

    @Override
    public int hashCode() {
        int result = gameType.hashCode();
        result = 31 * result + (date != null ? date.hashCode() : 0);
        result = 31 * result + rank;
        result = 31 * result + safetyAttempts;
        result = 31 * result + safetySuccesses;
        result = 31 * result + safetyFouls;
        result = 31 * result + safetyReturns;
        result = 31 * result + breakSuccesses;
        result = 31 * result + breakAttempts;
        result = 31 * result + breakContinuations;
        result = 31 * result + breakFouls;
        result = 31 * result + breakBallsMade;
        result = 31 * result + shootingBallsMade;
        result = 31 * result + shootingTurns;
        result = 31 * result + shootingMisses;
        result = 31 * result + shootingFouls;
        result = 31 * result + gameTotal;
        result = 31 * result + gameWins;
        result = 31 * result + safetyEscapes;
        result = 31 * result + safetyForcedErrors;
        result = 31 * result + breakAndRuns;
        result = 31 * result + tableRuns;
        result = 31 * result + fiveBallRun;
        result = 31 * result + opponentRank;
        result = 31 * result + winsOnBreak;
        result = 31 * result + earlyWins;
        result = 31 * result + points;
        result = 31 * result + deadBalls;
        result = 31 * result + highRun;
        result = 31 * result + seriousFouls;
        result = 31 * result + runLengths.hashCode();
        result = 31 * result + id.hashCode();
        result = 31 * result + name.hashCode();
        return result;
    }

    @Override
    public String toString() {
        return "Player{" +
                "gameType=" + gameType +
                "\n date=" + date +
                "\n rank=" + rank +
                "\n safetyAttempts=" + safetyAttempts +
                "\n safetySuccesses=" + safetySuccesses +
                "\n safetyFouls=" + safetyFouls +
                "\n safetyReturns=" + safetyReturns +
                "\n breakSuccesses=" + breakSuccesses +
                "\n breakAttempts=" + breakAttempts +
                "\n breakContinuations=" + breakContinuations +
                "\n breakFouls=" + breakFouls +
                "\n breakBallsMade=" + breakBallsMade +
                "\n shootingBallsMade=" + shootingBallsMade +
                "\n shootingTurns=" + shootingTurns +
                "\n shootingMisses=" + shootingMisses +
                "\n shootingFouls=" + shootingFouls +
                "\n gameTotal=" + gameTotal +
                "\n gameWins=" + gameWins +
                "\n safetyEscapes=" + safetyEscapes +
                "\n safetyForcedErrors=" + safetyForcedErrors +
                "\n breakAndRuns=" + breakAndRuns +
                "\n tableRuns=" + tableRuns +
                "\n fiveBallRun=" + fiveBallRun +
                "\n opponentRank=" + opponentRank +
                "\n winsOnBreak=" + winsOnBreak +
                "\n earlyWins=" + earlyWins +
                "\n points=" + points +
                "\n deadBalls=" + deadBalls +
                "\n highRun=" + highRun +
                "\n seriousFouls=" + seriousFouls +
                "\n runLengths=" + runLengths +
                "\n id='" + id + '\'' +
                "\n name='" + name + '\'' +
                '}';
    }

    public void addPoints(int points) {
        this.points += points;
    }

    public int getSeriousFouls() {
        return seriousFouls;
    }

    public String getName() {
        return name;
    }
}
