package com.brookmanholmes.billiards.player;

import com.brookmanholmes.billiards.game.util.ApaRaceToHelper;
import com.brookmanholmes.billiards.player.interfaces.Apa;
import com.brookmanholmes.billiards.player.interfaces.WinsOnBreak;
import com.brookmanholmes.billiards.player.interfaces.WinsOnBreakImp;

/**
 * Created by Brookman Holmes on 1/12/2016.
 */
public class ApaNineBallPlayer extends AbstractPlayer implements Apa {
    WinsOnBreak winsOnBreak;
    int rank;
    int points = 0;
    int deadBalls;

    public ApaNineBallPlayer(String name, int rank) {
        super(name);
        this.rank = rank;
        winsOnBreak = new WinsOnBreakImp();
    }

    @Override public void addPlayerStats(AbstractPlayer player) {
        super.addPlayerStats(player);

        if (player instanceof Apa) {
            winsOnBreak.addWinsOnBreak(((Apa) player).getWinsOnBreak());
            winsOnBreak.addEarlyWins(((Apa) player).getEarlyWins());
        }

        if (player instanceof ApaNineBallPlayer)
            points += ((ApaNineBallPlayer) player).points;
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
        return ApaRaceToHelper.apa9BallRaceTo(rank);
    }

    public int getDeadBalls() {
        return deadBalls;
    }

    public void addDeadBalls(int deadBalls) {
        this.deadBalls += deadBalls;
    }

    @Override public void addEarlyWin() {
        winsOnBreak.addEarlyWin();
    }

    @Override public int getEarlyWins() {
        return winsOnBreak.getEarlyWins();
    }

    @Override public void addWinOnBreak() {
        winsOnBreak.addWinOnBreak();
    }

    @Override public int getWinsOnBreak() {
        return winsOnBreak.getWinsOnBreak();
    }

    @Override public void addWinsOnBreak(int wins) {
        winsOnBreak.addWinsOnBreak(wins);
    }

    @Override public void addEarlyWins(int wins) {
        winsOnBreak.addEarlyWins(wins);
    }

    @Override public int getMatchPoints(int opponentScore, int opponentRank) {
        if (points == ApaRaceToHelper.apa9BallRaceTo(rank))
            return 20 - ApaRaceToHelper.getMinimumMatchPointsEarned(opponentRank, opponentScore);
        else return ApaRaceToHelper.getMinimumMatchPointsEarned(rank, points);
    }

    @Override public void addGameWon() {
        super.addGameWon();
        // the player had to have made the 9 ball to get to this point... I think...
        points++;
    }

    @Override public int getRank() {
        return rank;
    }

    @Override public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;

        ApaNineBallPlayer that = (ApaNineBallPlayer) o;

        if (rank != that.rank) return false;
        if (points != that.points) return false;
        return winsOnBreak.equals(that.winsOnBreak);

    }

    @Override public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + winsOnBreak.hashCode();
        result = 31 * result + rank;
        result = 31 * result + points;
        return result;
    }

    @Override public String toString() {
        return "ApaNineBallPlayer{" +
                "winsOnBreak=" + winsOnBreak +
                ", rank=" + rank +
                ", points=" + points +
                "} " + super.toString();
    }
}
