package com.brookmanholmes.billiards.game.util;

/**
 * Created by Brookman Holmes on 1/11/2016.
 */
public class RaceTo {
    int playerRaceTo;
    int opponentRaceTo;

    public RaceTo(int playerRaceTo, int opponentRaceTo) {
        this.playerRaceTo = playerRaceTo;
        this.opponentRaceTo = opponentRaceTo;
    }

    public int getPlayerRaceTo() {
        return playerRaceTo;
    }

    public int getOpponentRaceTo() {
        return opponentRaceTo;
    }
}
