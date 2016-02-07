package com.brookmanholmes.billiards.game.util;

/**
 * Created by Brookman Holmes on 10/26/2015.
 */
public enum PlayerColor {
    SOLIDS() {
        @Override
        public PlayerColor opposite() {
            return STRIPES;
        }
    },
    STRIPES() {
        @Override
        public PlayerColor opposite() {
            return SOLIDS;
        }
    },
    OPEN() {
        @Override
        public PlayerColor opposite() {
            return OPEN;
        }
    };

    public abstract PlayerColor opposite();
}
