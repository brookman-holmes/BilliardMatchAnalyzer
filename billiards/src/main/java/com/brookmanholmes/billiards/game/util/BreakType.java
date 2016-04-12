package com.brookmanholmes.billiards.game.util;

/**
 * Created by Brookman Holmes on 10/26/2015.
 */
public enum BreakType {
    LOSER("Loser"),
    ALTERNATE("Alternating"),
    WINNER("Winner"),
    PLAYER("Player"),
    OPPONENT("Opponent");

    String name;

    BreakType(String name) {
        this.name = name;
    }

    @Override public String toString() {
        return name;
    }
}
