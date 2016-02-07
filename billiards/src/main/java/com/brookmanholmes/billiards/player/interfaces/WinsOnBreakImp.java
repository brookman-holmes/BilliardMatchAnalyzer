package com.brookmanholmes.billiards.player.interfaces;

/**
 * Created by Brookman Holmes on 1/12/2016.
 */
public class WinsOnBreakImp extends EarlyWinsImp implements WinsOnBreak {
    int winsOnBreak = 0;

    public WinsOnBreakImp() {
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
        winsOnBreak += wins;
    }

    @Override
    public void addEarlyWins(int wins) {
        earlyWins += wins;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;

        WinsOnBreakImp that = (WinsOnBreakImp) o;

        return winsOnBreak == that.winsOnBreak;

    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + winsOnBreak;
        return result;
    }
}
