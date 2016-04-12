package com.brookmanholmes.billiards.game.util;

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

    public abstract PlayerTurn nextPlayer();
}
