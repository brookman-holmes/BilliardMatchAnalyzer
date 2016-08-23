package com.brookmanholmes.billiards.player;

/**
 * Created by Brookman Holmes on 1/12/2016.
 */
public class ApaNineBallPlayer extends AbstractPlayer implements IApa {
    int points = 0;
    private int winsOnBreak = 0;
    private int earlyWins = 0;
    private int deadBalls = 0;

    public ApaNineBallPlayer(String name, int rank) {
        super(name, rank);
    }

    @Override public void addPlayerStats(AbstractPlayer player) {
        super.addPlayerStats(player);

        if (player instanceof IApa) {
            winsOnBreak += ((IApa) player).getWinsOnBreak();
            earlyWins += ((IApa) player).getEarlyWins();
        }

        if (player instanceof ApaNineBallPlayer) {
            points += ((ApaNineBallPlayer) player).points;
            deadBalls += ((ApaNineBallPlayer) player).getDeadBalls();
        }
    }

    @Override public void addBreakShot(int ballsMade, boolean continuation, boolean scratch) {
        super.addBreakShot(ballsMade, continuation, scratch);

        if (!scratch)
            points += ballsMade;
    }

    @Override public void addShootingBallsMade(int ballsMade, boolean scratch) {
        super.addShootingBallsMade(ballsMade, scratch);
        points += ballsMade;
    }

    public void addPoints(int points) {
        this.points += points;
    }

    @Override public int getPoints() {
        return points;
    }

    @Override public int getPointsNeeded(int opponentRank) {
        return Players.apa9BallRaceTo(rank);
    }

    public int getDeadBalls() {
        return deadBalls;
    }

    public void addDeadBalls(int deadBalls) {
        this.deadBalls += deadBalls;
    }

    @Override public void addEarlyWin() {
        earlyWins++;
    }

    @Override public int getEarlyWins() {
        return earlyWins;
    }

    @Override public void addWinOnBreak() {
        winsOnBreak++;
    }

    @Override public int getWinsOnBreak() {
        return winsOnBreak;
    }

    @Override public void addWinsOnBreak(int wins) {
        winsOnBreak += wins;
    }

    @Override public void addEarlyWins(int wins) {
        earlyWins += wins;
    }

    @Override public int getMatchPoints(int opponentScore, int opponentRank) {
        if (points == Players.apa9BallRaceTo(rank))
            return 20 - Players.getMinimumMatchPointsEarned(opponentRank, opponentScore);
        else return Players.getMinimumMatchPointsEarned(rank, points);
    }

    @Override public void addGameWon() {
        super.addGameWon();
        // the player had to have made the 9 ball to get to this point... I think...
        points++;
    }

    @Override public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;

        ApaNineBallPlayer that = (ApaNineBallPlayer) o;

        if (winsOnBreak != that.winsOnBreak) return false;
        if (earlyWins != that.earlyWins) return false;
        if (rank != that.rank) return false;
        if (points != that.points) return false;
        return deadBalls == that.deadBalls;

    }

    @Override public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + winsOnBreak;
        result = 31 * result + earlyWins;
        result = 31 * result + rank;
        result = 31 * result + points;
        result = 31 * result + deadBalls;
        return result;
    }

    @Override public String toString() {
        return "ApaNineBallPlayer{" +
                "winsOnBreak=" + winsOnBreak +
                ", rank=" + rank +
                ", points=" + points +
                ", deadBalls=" + deadBalls +
                "} " + super.toString();
    }
}
