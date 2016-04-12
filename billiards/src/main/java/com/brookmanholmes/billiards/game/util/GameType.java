package com.brookmanholmes.billiards.game.util;

/**
 * Created by Brookman Holmes on 10/26/2015.
 */
public enum GameType {
    BCA_EIGHT_BALL("BCA 8 Ball"),
    BCA_TEN_BALL("BCA 10 Ball"),
    BCA_NINE_BALL("BCA 9 Ball"),
    STRAIGHT_POOL("Straight Pool"),
    AMERICAN_ROTATION("American Rotation"),
    APA_NINE_BALL("APA 9 Ball"),
    APA_EIGHT_BALL("APA 8 Ball");

    final String name;

    GameType(String name) {
        this.name = name;
    }

    @Override public String toString() {
        return name;
    }
}
