package com.brookmanholmes.billiards.player;

/**
 * Created by Brookman Holmes on 1/12/2016.
 */
public interface IApa extends IWinsOnBreak {
    int getMatchPoints(int opponentScore, int opponentRank);

    int getRank();

    int getPoints();

    int getPointsNeeded(int opponentRank);
}
