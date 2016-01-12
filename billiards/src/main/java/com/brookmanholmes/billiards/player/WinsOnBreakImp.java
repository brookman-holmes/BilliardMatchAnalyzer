package com.brookmanholmes.billiards.player;

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
}
