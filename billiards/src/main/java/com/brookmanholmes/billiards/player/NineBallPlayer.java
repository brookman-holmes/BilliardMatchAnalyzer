package com.brookmanholmes.billiards.player;

/**
 * Created by Brookman Holmes on 1/12/2016.
 */
public class NineBallPlayer extends AbstractPlayer implements IWinsOnBreak {
    private int earlyWins = 0;
    private int winsOnBreak = 0;

    public NineBallPlayer(String name, int rank) {
        super(name, rank);
    }

    public NineBallPlayer(String name) {
        super(name);
    }

    @Override
    public void addPlayerStats(AbstractPlayer player) {
        super.addPlayerStats(player);

        if (player instanceof IWinsOnBreak) {
            winsOnBreak += ((IWinsOnBreak) player).getWinsOnBreak();
            earlyWins += ((IWinsOnBreak) player).getEarlyWins();
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
    public void addWinOnBreak() {
        winsOnBreak++;
    }

    @Override
    public int getWinsOnBreak() {
        return winsOnBreak;
    }

    @Override
    public void addWinsOnBreak(int wins) {
        if (wins < 0)
            throw new IllegalArgumentException("Wins must be greater than or equal to 0");
        winsOnBreak += wins;
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

        NineBallPlayer that = (NineBallPlayer) o;

        if (earlyWins != that.earlyWins) return false;
        return winsOnBreak == that.winsOnBreak;

    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + earlyWins;
        result = 31 * result + winsOnBreak;
        return result;
    }

    @Override
    public String toString() {
        return "NineBallPlayer{" +
                "winsOnBreak=" + winsOnBreak +
                "earlyWins=" + earlyWins +
                "} " + super.toString();
    }
}
