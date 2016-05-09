package com.brookmanholmes.billiards.player;

/**
 * Created by Brookman Holmes on 1/12/2016.
 */
public interface IWinsOnBreak extends IEarlyWins {
    void addWinOnBreak();

    int getWinsOnBreak();

    void addWinsOnBreak(int wins);
}
