package com.brookmanholmes.billiards.player;

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
}
