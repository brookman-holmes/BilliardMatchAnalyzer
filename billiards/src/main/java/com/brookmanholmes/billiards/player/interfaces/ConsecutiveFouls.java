package com.brookmanholmes.billiards.player.interfaces;

/**
 * Created by Brookman Holmes on 11/4/2015.
 */
public interface ConsecutiveFouls {
    void addFoul();

    void removeFouls();

    int getFouls();

    void addFouls(int fouls);
}
