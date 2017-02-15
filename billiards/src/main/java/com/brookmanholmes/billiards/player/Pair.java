package com.brookmanholmes.billiards.player;

import java.io.Serializable;

/**
 * Data class for holding a pair of players
 * Created by Brookman Holmes on 2/3/2016.
 */
public class Pair<T extends Player> implements Serializable {
    private final T player;
    private final T opponent;

    /**
     * Creates a new pair of players
     * @param player The player in this pair
     * @param opponent The opponent in this pair
     */
    public Pair(T player, T opponent) {
        this.player = player;
        this.opponent = opponent;
    }

    /**
     * Getter for player
     * @return the player in this pair of players
     */
    public T getPlayer() {
        return player;
    }

    /**
     * Getter for opponent
     * @return the opponent in this pair of players
     */
    public T getOpponent() {
        return opponent;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Pair<?> pair = (Pair<?>) o;

        if (!player.equals(pair.player)) return false;
        return opponent.equals(pair.opponent);

    }

    @Override
    public int hashCode() {
        int result = player.hashCode();
        result = 31 * result + opponent.hashCode();
        return result;
    }
}
