package com.brookmanholmes.billiards.game;

/**
 * Created by Brookman Holmes on 10/26/2015.
 */
public enum PlayerTurn {
    PLAYER() {
        @Override public PlayerTurn nextPlayer() {
            return OPPONENT;
        }
    },
    OPPONENT() {
        @Override public PlayerTurn nextPlayer() {
            return PLAYER;
        }
    };

    /**
     * Convenience method for easily determining which player goes after this one
     * @return returns the opposite of the enum (PLAYER returns OPPONENT, OPPONENT returns PLAYER)
     */
    public abstract PlayerTurn nextPlayer();
}
