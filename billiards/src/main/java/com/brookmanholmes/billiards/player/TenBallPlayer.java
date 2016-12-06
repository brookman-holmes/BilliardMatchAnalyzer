package com.brookmanholmes.billiards.player;

/**
 * Created by Brookman Holmes on 1/12/2016.
 */
public class TenBallPlayer extends AbstractPlayer implements IEarlyWins {
    private int earlyWins = 0;

    public TenBallPlayer(String name, int rank) {
        super(name, rank);
    }

    public TenBallPlayer(String name) {
        super(name);
    }

    @Override
    public void addPlayerStats(AbstractPlayer player) {
        super.addPlayerStats(player);

        if (player instanceof IEarlyWins) {
            earlyWins += ((IEarlyWins) player).getEarlyWins();
        }
    }

    @Override
    public void addEarlyWin() {
        earlyWins++;
    }

    @Override
    public int getEarlyWins() {
        return earlyWins;
    }

    @Override
    public void addEarlyWins(int wins) {
        if (wins < 0)
            throw new IllegalArgumentException("Wins must be greater than or equal to 0");
        earlyWins += wins;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;

        TenBallPlayer that = (TenBallPlayer) o;

        return earlyWins == that.earlyWins;

    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + earlyWins;
        return result;
    }

    @Override
    public String toString() {
        return "TenBallPlayer{" +
                "earlyWins=" + earlyWins +
                "\n " + super.toString() +
                '}';
    }
}
