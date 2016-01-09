package com.brookmanholmes.billiards.game.util;

import java.awt.Point;

/**
 * Created by helios on 1/9/2016.
 */
public class RaceTo {
    private int playerRaceTo;
    private int opponentRaceTo;

    public RaceTo(int playerRank, int opponentRank) {
        Point raceTo = ApaRaceToHelper.apa8BallRaceTo(playerRank, opponentRank);
        playerRaceTo = raceTo.x;
        opponentRaceTo = raceTo.y;
    }

    public int getPlayerRaceTo() {
        return playerRaceTo;
    }

    public int getOpponentRaceTo() {
        return opponentRaceTo;
    }
}
