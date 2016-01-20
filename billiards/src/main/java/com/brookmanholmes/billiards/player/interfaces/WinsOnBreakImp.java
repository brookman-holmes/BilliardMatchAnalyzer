package com.brookmanholmes.billiards.player.interfaces;

/**
 * Created by Brookman Holmes on 1/12/2016.
 */
public class WinsOnBreakImp extends EarlyWinsImp implements WinsOnBreak {
    EarlyWinsImp earlyWins;
    int winsOnBreak = 0;

    public WinsOnBreakImp() {
        earlyWins = new EarlyWinsImp();
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
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        WinsOnBreakImp that = (WinsOnBreakImp) o;

        if (winsOnBreak != that.winsOnBreak) return false;
        return earlyWins.equals(that.earlyWins);

    }

    @Override
    public int hashCode() {
        int result = earlyWins.hashCode();
        result = 31 * result + winsOnBreak;
        return result;
    }

    @Override
    public String toString() {
        return "WinsOnBreakImp{" +
                "earlyWins=" + earlyWins.toString() +
                "\n winsOnBreak=" + winsOnBreak +
                '}';
    }
}
