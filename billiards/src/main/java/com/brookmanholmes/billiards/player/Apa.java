package com.brookmanholmes.billiards.player;

/**
 * Created by Brookman Holmes on 1/12/2016.
 */
public interface Apa extends WinsOnBreak {
    int getMatchPoints(int opponentScore, int opponentRaceTo);

    int getRank();
}
