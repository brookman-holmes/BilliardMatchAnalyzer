package com.brookmanholmes.billiards.player;

/**
 * Data class for keeping track of what two players need for their race in a match
 * Created by Brookman Holmes on 1/11/2016.
 */
public class RaceTo {
    private final int playerRaceTo;
    private final int opponentRaceTo;

    /**
     * Creates a new RaceTo object with the corresponding arguments as it's values
     * @param playerRaceTo The number of games/points needed for the player to win the match
     * @param opponentRaceTo The number of games/points needed for the opponent to win the match
     */
    public RaceTo(int playerRaceTo, int opponentRaceTo) {
        this.playerRaceTo = playerRaceTo;
        this.opponentRaceTo = opponentRaceTo;
    }

    /**
     * Getter for the points/games that the player needs in their race
     * @return The number of points/games needed to win the match
     */
    public int getPlayerRaceTo() {
        return playerRaceTo;
    }

    /**
     * Getter for the points/games that the opponent needs in their race
     * @return The number of points/games needed to win the match
     */
    public int getOpponentRaceTo() {
        return opponentRaceTo;
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
