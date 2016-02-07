package com.brookmanholmes.billiards.player.interfaces;

/**
 * Created by Brookman Holmes on 1/12/2016.
 */
public class EarlyWinsImp implements EarlyWins {
    int earlyWins = 0;

    public EarlyWinsImp() {

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
        earlyWins += wins;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        EarlyWinsImp that = (EarlyWinsImp) o;

        return earlyWins == that.earlyWins;

    }

    @Override
    public int hashCode() {
        return earlyWins;
    }

    @Override
    public String toString() {
        return "EarlyWinsImp{" +
                "earlyWins=" + earlyWins +
                '}';
    }
}
