package com.brookmanholmes.billiards.player;

/**
 * Created by Brookman Holmes on 2/3/2016.
 */
public class Pair<T> {
    private final T player;
    private final T opponent;

    public Pair(T player, T opponent) {
        this.player = player;
        this.opponent = opponent;
    }

    public T getPlayer() {
        return player;
    }

    public T getOpponent() {
        return opponent;
    }

    @Override public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Pair<?> pair = (Pair<?>) o;

        if (!player.equals(pair.player)) return false;
        return opponent.equals(pair.opponent);

    }

    @Override public int hashCode() {
        int result = player.hashCode();
        result = 31 * result + opponent.hashCode();
        return result;
    }
}
