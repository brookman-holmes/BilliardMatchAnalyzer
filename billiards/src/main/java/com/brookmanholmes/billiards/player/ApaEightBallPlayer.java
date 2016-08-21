package com.brookmanholmes.billiards.player;

/**
 * Created by Brookman Holmes on 1/12/2016.
 */
public class ApaEightBallPlayer extends AbstractPlayer implements IApa {
    private final int rank;
    private int winsOnBreak = 0;
    private int earlyWins = 0;

    public ApaEightBallPlayer(String name, int rank) {
        super(name);
        this.rank = rank;
    }

    @Override public void addPlayerStats(AbstractPlayer player) {
        super.addPlayerStats(player);

        if (player instanceof IApa) {
            winsOnBreak += ((IApa) player).getWinsOnBreak();
            earlyWins += ((IApa) player).getEarlyWins();
        }
    }

    @Override public int getPoints() {
        return gameWins;
    }

    @Override public int getPointsNeeded(int opponentRank) {
        RaceTo raceTo = Players.apa8BallRaceTo(rank, opponentRank);
        return raceTo.getPlayerRaceTo();
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
        RaceTo raceTo = Players.apa8BallRaceTo(rank, opponentRank);

        if (getWins() == raceTo.getPlayerRaceTo()) {
            if (opponentScore == 0)
                return 3;
            else return 2;
        } else if (getWins() + 1 == raceTo.getPlayerRaceTo()) {
            return 1;
        } else return 0;
    }

    @Override public int getRank() {
        return rank;
    }

    @Override public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;

        ApaEightBallPlayer that = (ApaEightBallPlayer) o;

        if (winsOnBreak != that.winsOnBreak) return false;
        if (earlyWins != that.earlyWins) return false;
        return rank == that.rank;

    }

    @Override public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + winsOnBreak;
        result = 31 * result + earlyWins;
        result = 31 * result + rank;
        return result;
    }

    @Override public String toString() {
        return "ApaEightBallPlayer{" +
                "winsOnBreak=" + winsOnBreak +
                ", rank=" + rank +
                "} " + super.toString();
    }
}
