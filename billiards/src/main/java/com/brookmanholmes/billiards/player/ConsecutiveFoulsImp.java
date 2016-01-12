package com.brookmanholmes.billiards.player;

/**
 * Created by Brookman Holmes on 1/12/2016.
 */
public class ConsecutiveFoulsImp implements ConsecutiveFouls {
    int consecutiveFouls = 0;

    public ConsecutiveFoulsImp() {
    }

    @Override
    public void addFoul() {
        consecutiveFouls++;
    }

    @Override
    public void removeFouls() {
        consecutiveFouls = 0;
    }

    @Override
    public int getFouls() {
        return consecutiveFouls;
    }
}
