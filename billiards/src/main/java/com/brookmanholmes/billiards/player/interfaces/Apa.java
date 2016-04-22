package com.brookmanholmes.billiards.player.interfaces;

/**
 * Created by Brookman Holmes on 1/12/2016.
 */
public interface Apa extends WinsOnBreak {
    int getMatchPoints(int opponentScore, int opponentRank);

    int getRank();

    int getPoints();

    int getPointsNeeded(int opponentRank);
}
