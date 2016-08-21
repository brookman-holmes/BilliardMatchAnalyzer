package com.brookmanholmes.billiards.player;

/**
 * Created by Brookman Holmes on 1/11/2016.
 */
public class RaceTo {
    private final int playerRaceTo;
    private final int opponentRaceTo;

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

    @Override public String toString() {
        return "RaceTo{" +
                "playerRaceTo=" + playerRaceTo +
                "\n opponentRaceTo=" + opponentRaceTo +
                '}';
    }

    @Override public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        RaceTo raceTo = (RaceTo) o;

        if (playerRaceTo != raceTo.playerRaceTo) return false;
        return opponentRaceTo == raceTo.opponentRaceTo;

    }

    @Override public int hashCode() {
        int result = playerRaceTo;
        result = 31 * result + opponentRaceTo;
        return result;
    }
}
