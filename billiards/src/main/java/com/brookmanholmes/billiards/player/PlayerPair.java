package com.brookmanholmes.billiards.player;

/**
 * Created by Brookman Holmes on 11/4/2015.
 */
public class PlayerPair<T extends AbstractPlayer> {
    final public T player;
    final public T opponent;

    public PlayerPair(T player, T opponent) {
        this.player = player;
        this.opponent = opponent;
    }
}
